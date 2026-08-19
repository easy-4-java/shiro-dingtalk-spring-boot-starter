package org.apache.shiro.spring.boot.dingtalk.exception;

import org.apache.shiro.authc.AuthenticationException;
/** Exception thrown when a Ding Talk Code Incorrect error occurs.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class DingTalkCodeIncorrectException extends AuthenticationException {

	/**
	 * Constructs a new ding talk code incorrect exception instance.
	 *
	 * @param msg the msg
	 */
	public DingTalkCodeIncorrectException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new ding talk code incorrect exception instance.
	 *
	 * @param msg the msg
	 * @param t the t
	 */
	public DingTalkCodeIncorrectException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
