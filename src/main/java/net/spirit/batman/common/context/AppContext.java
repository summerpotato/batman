package net.spirit.batman.common.context;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 应用上下文入口类，提供获取Spring ApplicationContext对象的静态获取方法，供Spring容器外的对象调用。
 * 如果对象已经由Spring容器进行管理，请不要调用此类中的api，直接使用Spring的依赖注入即可。
 * 
 * @author SummerPotato
 */
@Component("appContext")
public class AppContext implements ApplicationContextAware {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AppContext.class);

	static class AppContextImpl {

		static ApplicationContext ctx = getApplicationContext();

		synchronized static ApplicationContext getApplicationContext() {
			if (AppContext.ctx != null) {
				ctx = AppContext.ctx;
			} else if (ctx == null) {
				logger.warn("Spring容器尚末通过Web容器进行初始化，将以独立应用的形式初始化Spring容器。如果您正在Web容器中运行应用，这可能是一个错误，" +
						"\n请检查配置文件以修正此错误；如果您确认这不是错误（如正在运行测试、正在EJB容器中运行应用、正在运行独立应用等），请忽略本提示。");
				ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
				AppContext.ctx = ctx;
			}
			return ctx;
		}

	}
	static ApplicationContext ctx;

	/**
	 * 获取Spring容器对象
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		if (ctx == null) {
			ctx = AppContextImpl.ctx;
		}
		return ctx;
	}

	/**
	 * 从Spring容器中获取服务对象
	 * @param clz
	 * @return
	 */
	public static <T> T getBean(Class<T> clz) {
		return getApplicationContext().getBean(clz);
	}

	public static <T> T getBean(String name, Class<T> clz) {
		return getApplicationContext().getBean(name, clz);
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		logger.info("--- 初始化应用上下文入口类 ---");
		AppContext.ctx = ctx;
		if (logger.isInfoEnabled()) {
			String[] names = ctx.getBeanDefinitionNames();
			StringBuilder sb = new StringBuilder("以下为Spring容器中的服务列表（").append(names.length).append("):");
			for (int i=0; i<names.length; i++) {
				String[] aliases = ctx.getAliases(names[i]);
				sb.append("\r\n\t[").append(i).append("]").append(names[i]);
				if (aliases.length>0) {
					sb.append("[").append(aliases[0]);
					for (int j=1;j<aliases.length;j++) {
						sb.append(",").append(aliases[j]);
					}
					sb.append("]");
				}
				sb.append("\t:\t").append(ctx.getType(names[i]));
			}
			logger.info(sb);
		}
	}
	
}
