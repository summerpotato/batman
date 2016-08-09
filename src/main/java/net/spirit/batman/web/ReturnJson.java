package net.spirit.batman.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.spirit.batman.exception.BizException;
import net.spirit.batman.util.json.JsonMapper;

/**
 * @Description 为 Web-Controller 提供方便的请求处理方法
 * @author SummerPotato
 * @date 2016-8-8
 */
public class ReturnJson {

	private static final String MIME_JSON = "text/html;charset=UTF-8";

	public static String returnJson(HttpServletResponse response, Object data) {
		return returnText(response, JsonMapper.nonEmptyMapper().toJson(data), MIME_JSON);
	}

	public static String returnText(HttpServletResponse response, CharSequence text, String contenttype) {
		response.setContentType(contenttype);
		if (text != null) {
			try {
				PrintWriter out = response.getWriter();
				out.write(text.toString());
				out.close();
			} catch (IOException e) {
				throw new BizException(e);
			}
		}
		return null;
	}
}
