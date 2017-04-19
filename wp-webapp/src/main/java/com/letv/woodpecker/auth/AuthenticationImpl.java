/**
 * 
 */
package com.letv.woodpecker.auth;

import com.letv.auth.core.impl.AbstractAuthentication;
import com.letv.auth.exception.AuthenticationException;
import com.letv.auth.pojo.Permission;
import com.letv.auth.pojo.User;

import java.util.List;


public class AuthenticationImpl extends AbstractAuthentication {
	public AuthenticationImpl(User user, List<Permission> perms)
			throws AuthenticationException {
		super(user, perms);
	}
}
