package com.letv.woodpecker.auth;

import com.letv.auth.core.impl.AbstractAuthProxy;

public class AuthProxyImpl extends AbstractAuthProxy {

	@Override
	public String getSsoUrl(int type) {
		return getSsoUrl();
	}
}

