package org.apache.shiro.spring.boot.dingtalk.exception;

import org.apache.shiro.authc.AuthenticationException;
/** Exception thrown when a Ding Talk Code Expired error occurs.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class DingTalkCodeExpiredException extends AuthenticationException {

	public DingTalkCodeExpiredException(String msg) {
		super(msg);
	}
	
	public DingTalkCodeExpiredException(String msg, Throwable t) {
		super(msg, t);
	}

}
