package net.spirit.batman.util.misc;

/**
 * Description:Twitter唯一ID生成器
 * 
 * 唯一ID生成的主要目的是：为一个分布式系统的数据object产生一个唯一的标识。
 * 一般对于唯一ID生成的要求主要这么几点：
 * 毫秒级的快速响应
 * 可用性强
 * prefix有连续性方便DB顺序存储
 * 体积小，8字节为佳
 * 
 * [UUID]
 * 优：java自带，方便
 * 劣：占用空间大，16字节
 
 * [Snowflake]:timestamp + work number + seq number
 * 优：可用性强，速度快，8字节
 * 劣：需要引入zookeeper 和独立的snowflake专用服务器
 * 
 * 
 * snowflake的结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 
 * 第1位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)，然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点），最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * 一共加起来刚好64位，为一个Long型。(转换成字符串长度为18)
 * snowflake生成的ID整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和workerId作区分），并且效率较高。
 * 据说：snowflake每秒能够产生26万个ID。
 * 
 * @author SummerPotato
 *
 */
public class TwitterIdWorker {

    /**
     * Snowflake
     * 
     * 0               41              51          64
     * +---------------+----------------+-----------+
     * |timestamp(ms)  | worker node id | sequence  |
     * +---------------+----------------+-----------+
     * 
     * id  = timestamp | workerid | sequence (eg. 1451063443347648410)
	 */

	private final long twepoch = 1288834974657L;
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private final long sequenceBits = 12L;
	private final long workerIdShift = sequenceBits;
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	public TwitterIdWorker(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("--- worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("--- datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}
		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

	
	
	public static void main(String[] args) {
		TwitterIdWorker idWorker = new TwitterIdWorker(0, 0);
		for (int i = 0; i < 1000; i++) {
			long id = idWorker.nextId();
			System.out.println(id);
		}
	}
	  
}
