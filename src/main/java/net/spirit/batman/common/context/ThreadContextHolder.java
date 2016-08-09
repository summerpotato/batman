package net.spirit.batman.common.context;

/**
 * 使用线程变量进行隐式传参
 * 
 * @author SummerPotato
 * 
 */
public class ThreadContextHolder {

	static ThreadLocal<DefaultThreadAttributes> holder = new ThreadLocal<DefaultThreadAttributes>();

	public static ThreadAttributes getThreadContext() {
		DefaultThreadAttributes attr = holder.get();
		if (attr == null) {
			attr = new DefaultThreadAttributes();
			holder.set(attr);
		}
		return attr;
	}

	public static void setThreadContext(DefaultThreadAttributes context) {
		holder.set(context);
	}

	public static void resetThreadContext() {
		holder.remove();
	}
	
}
