package org.apache.shiro.spring.boot.dingtalk.realm;

import com.dingtalk.spring.boot.DingTalkTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.taobao.api.ApiException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.biz.realm.AuthorizingRealmListener;
import org.apache.shiro.spring.boot.dingtalk.authc.DingTalkMaLoginRequest;
import org.apache.shiro.spring.boot.dingtalk.exception.DingTalkAuthenticationServiceException;
import org.apache.shiro.spring.boot.dingtalk.exception.DingTalkCodeNotFoundException;
import org.apache.shiro.spring.boot.dingtalk.token.DingTalkMaAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DingTalk AuthorizingRealm
 * @author [@Loong Wan](https://github.com/loong10k)
 */

public class DingTalkMaAuthorizingRealm extends AbstractAuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(DingTalkMaAuthorizingRealm.class);


    private final DingTalkTemplate dingTalkTemplate;
    private Map<String, String> appKeySecret = new ConcurrentHashMap<>();

    public DingTalkMaAuthorizingRealm( DingTalkTemplate dingTalkTemplate) {
        this.dingTalkTemplate = dingTalkTemplate;
    }

	@Override
	/** Returns the authentication token class.
	 * @return the result
	 */
	public Class<?> getAuthenticationTokenClass() {
		return DingTalkMaAuthenticationToken.class;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		log.info("Handle authentication token {}.", new Object[] { token });

    	AuthenticationException ex = null;
    	AuthenticationInfo info = null;

    	try {

			DingTalkMaAuthenticationToken dingTalkToken = (DingTalkMaAuthenticationToken) token;
			DingTalkMaLoginRequest loginRequest = (DingTalkMaLoginRequest) dingTalkToken.getPrincipal();

			if (!StringUtils.hasText(loginRequest.getAuthCode())) {
				log.debug("No authCode found in request.");
				throw new DingTalkCodeNotFoundException("No authCode found in request.");
			}

			if(!dingTalkTemplate.hasAppKey(loginRequest.getKey())) {
				log.debug("Invalid App Key {} .", loginRequest.getKey());
				throw new DingTalkCodeNotFoundException("Invalid App Key.");
			}

			try {
				if (StringUtils.hasText(loginRequest.getAuthCode())) {
					String appKey = loginRequest.getKey();
					String corpId = dingTalkTemplate.getCorpId(appKey);
					String appSecret = dingTalkTemplate.getAppSecret(corpId, appKey);
					// 获取access_token
					String accessToken = dingTalkTemplate.getAccessToken(appKey, appSecret);
					loginRequest.setAccessToken(accessToken);
				}
			} catch (ApiException e) {
				throw new DingTalkAuthenticationServiceException(e.getErrMsg(), e);
			}

			info = getRepository().getAuthenticationInfo(dingTalkToken);

		} catch (AuthenticationException e) {
			ex = e;
		}

		//调用事件监听器
		if(getRealmsListeners() != null && getRealmsListeners().size() > 0){
			for (AuthorizingRealmListener realmListener : getRealmsListeners()) {
				if(ex != null || null == info){
					realmListener.onFailure(this, token, ex);
				}else{
					realmListener.onSuccess(this, info);
				}
			}
		}

		if(ex != null){
			throw ex;
		}

		return info;
	}

}
