package net.spirit.batman.util.misc;

import java.util.UUID;

/**
 * @Description: UUID生成器
 *
 * @version: v1.0.0
 * @author: SummerPotato
 * @date: 2016-8-8
 *
 * Modification History:
 * Date			Author			Version			Description
 *---------------------------------------------------------*
 * 2016-8-8		SummerPotato	v1.0.0
 */
public class UUIDGenerator {
	
	/**
	 * 获得一个新的uuid
	 * return
	 */
	public static String getNewId(){
		 String s = UUID.randomUUID().toString(); 
		 StringBuffer sb = new StringBuffer();
		 sb.append(s.substring(0,8)).append(s.substring(9,13)).append(s.substring(14,18)).append(s.substring(19,23)).append(s.substring(24));
	     return sb.toString(); 
	}

}
