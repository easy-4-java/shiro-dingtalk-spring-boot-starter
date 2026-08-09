package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.boot.dingtalk.authc.DingTalkAuthenticationSuccessHandler;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore( name = {
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration",
	"org.apache.shiro.spring.boot.ShiroBizWebAutoConfiguration"
})
/** Auto-configuration for Shiro Ding Talk Web.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = ShiroDingTalkProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroDingTalkProperties.class })
public class ShiroDingTalkWebAutoConfiguration extends AbstractShiroWebConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	protected DingTalkAuthenticationSuccessHandler wxAuthenticationSuccessHandler(
			JwtPayloadRepository jwtPayloadRepository,
			ShiroJwtProperties jwtProperties) {
		return new DingTalkAuthenticationSuccessHandler(jwtPayloadRepository, jwtProperties.isCheckExpiry());
	}

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
