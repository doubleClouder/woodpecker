/**
 * 
 */
package com.letv.woodpecker.auth;

import com.letv.auth.core.Authentication;
import com.letv.auth.core.impl.AbstractAuthenticationFactory;
import com.letv.auth.pojo.Permission;
import com.letv.auth.pojo.User;

import java.util.List;


public class AuthenticationFactoryImpl extends AbstractAuthenticationFactory {

		@Override
		protected Authentication buildAuthentication(User user, List<Permission> perms)
				throws com.letv.auth.exception.AuthenticationException {
			return new AuthenticationImpl(user, perms);
		}

}
