package org.apache.shiro.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;


/**
 * Filtervia@Bean，Spring Boot Filter chain， ，URLFilter，Shiroconfiguration URL。
 * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter
 * https://gitee.com/baomidou/sms
 */
@Configuration
@AutoConfigureBefore( name = {
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration",  // shiro-spring-boot-web-starter
	"org.apache.shiro.spring.boot.ShiroBizWebFilterConfiguration" // spring-boot-starter-shiro-biz
})
/** Configuration for Shiro Ding Talk Web authentication filter chain.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = ShiroDingTalkProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroDingTalkProperties.class })
public class ShiroDingTalkWebFilterConfiguration implements ApplicationContextAware {

	protected static final Logger LOG = LoggerFactory.getLogger(ShiroDingTalkWebFilterConfiguration.class);
	private ApplicationContext applicationContext;
	
	
	
	@Override
	/** Sets the application context.
	 * @param applicationContext the applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/** Returns the application context.
	 * @return the result
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
