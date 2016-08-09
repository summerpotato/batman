package net.spirit.batman.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import net.spirit.batman.exception.AppException;
import net.spirit.batman.exception.BizException;
import net.spirit.batman.util.json.JsonUtil;

/**
 * @Description: 所有基于Spring MVC的Web控制器类（Action）的统一父类，提供一些便利的请求处理方法，如返回Json、文本数据等
 * <p>
 * 相见欢
 * 林花谢了春红,
 * 太匆匆。
 * 无奈朝来寒雨晚来风。
 * 胭脂泪,
 * 相留醉,
 * 几时重。
 * 自是人生长恨水长东。
 * </p>
 * @version: v1.0.0
 * @author SummerPotato
 * @date 2016-8-8
 * 
 * Modification History:
 * Date			Author			Version			Description
 * --------------------------------------------------------*
 * 2016-8-8		SummerPotato	v1.0.0
 */
public class BizController {

	private static final String MIME_JSON = "text/html;charset=UTF-8";	//"application/json;charset=UTF-8";

	/**
	 * 返回JSon格式的数据
	 *
	 * @param response
	 * @param text
	 * @throws Exception
	 */
	public String returnJson(HttpServletResponse response, CharSequence text) {
		return returnText(response, text, MIME_JSON);
	}

	/**
	 * 返回JSon格式的数据
	 *
	 * @param response
	 * @param data
	 * @throws Exception
	 */
	public String returnJson(HttpServletResponse response, Object data) {
		return returnText(response, JsonUtil.toJsonString(data), MIME_JSON);
	}

	/**
	 * 返回xml格式的数据
	 *
	 * @param response
	 * @param text
	 * @throws Exception
	 */
	public String returnXml(HttpServletResponse response, CharSequence text) {
		return returnText(response, text, "text/xml;charset=UTF-8");
	}

	/**
	 * 返回文本数据
	 * @param response
	 * @param text
	 * @throws Exception
	 */
	public String returnText(HttpServletResponse response, CharSequence text) {
		return returnText(response, text, "text/plain;charset=UTF-8");
	}

	/**
	 * 返回文本数据
	 * @param response
	 * @param text
	 * @param contenttype 内容类型，如：text/plain、text/xml、application/json、text/json、text/javascript、application/javascript（不支持旧浏览器）
	 * @param encoding 字符集编码，如：GB18030、UTF-8，不建议使用GB2312和GBK
	 * @throws Exception
	 */
	public String returnText(HttpServletResponse response, CharSequence text, final String contenttype, final String encoding) {
		return returnText(response, text, contenttype + ";charset=" + encoding);
	}

	public String returnHtml(HttpServletResponse response, CharSequence text) {
		return returnText(response, text, "text/html;charset=UTF-8");
	}

	/**
	 * 返回文本数据
	 *
	 * @param response
	 * @param text
	 * @param contenttype
	 * @throws IOException
	 */
	public String returnText(HttpServletResponse response, CharSequence text,
			final String contenttype) {
		response.setContentType(contenttype);
		if (text != null) {
			try {
				response.getWriter().write(text.toString());
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
		return null;
	}


	/**
	 * 设置文件头格式
	 * @param response
	 * @param mimeType
	 * @param fileName
	 * @param size
	 */
	public void setFileHeader(HttpServletResponse response, CharSequence mimeType,
			final CharSequence fileName, int size) {
		response.reset();
		// 设置response的Header
		if(mimeType != null){
			response.setContentType(mimeType.toString()) ;
		}
		try {
			response.addHeader("Content-Disposition","attachment;filename=" + new String(fileName.toString().getBytes("GB18030"),"ISO-8859-1")) ;
		} catch (UnsupportedEncodingException e) {
			throw new BizException("文件名编码转换失败") ;
		}
		if(size > 0){
			response.addIntHeader("Content-Length", size) ;
		}
	}

	/**
	 * 文件下载
	 * @param response
	 * @param mimeType
	 * @param fileName
	 * @param file
	 * @param size
	 */
	public void downloadFile(HttpServletResponse response, CharSequence mimeType,
			final CharSequence fileName, InputStream file, int size) {
		setFileHeader(response, mimeType, fileName, size) ;
		try {
			OutputStream out = response.getOutputStream() ;
			int l ;
			while((l=file.read()) >= 0){
				out.write(l) ;
			}
			out.flush() ;
		} catch (IOException e) {
			throw new BizException("下载文件失败") ;
		}finally {
			try {
				file.close();
			} catch (IOException e) {
				throw new BizException("下载文件失败") ;
			}
		}
	}
	
}
