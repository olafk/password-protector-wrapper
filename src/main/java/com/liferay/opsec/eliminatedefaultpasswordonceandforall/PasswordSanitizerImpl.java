package com.liferay.opsec.eliminatedefaultpasswordonceandforall;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.StringUtil;

public class PasswordSanitizerImpl {

	private String configuredDefaultPassword;

	public PasswordSanitizerImpl(String configuredDefaultPassword) {
		this.configuredDefaultPassword = configuredDefaultPassword;
	}

	public String sanitizePassword(String password) {
		if(password == null) {
			return null;
		}
		if(password.equals("test")) {
			if(configuredDefaultPassword.equals("test")) {
				password = DigesterUtil.digest(seed+password);
				_log.info("secretly changing password 'test' to random value '" + password + "'");
			} else {
				_log.info("using manually configured default admin password instead of 'test'");
				password = configuredDefaultPassword;
			}
		}
		return password;
	}

	public String sanitizePasswords(String password1, String password2) {
		if(password1 != null && password2 != null) {
			if(password1.equals(password2)) {
				return sanitizePassword(password1);
			} else {
				_log.fatal("received non-matching passwords - generating random value");
				return StringUtil.randomString(17);
			}
		}
		return null;
	}

	Log _log = LogFactoryUtil.getLog(PasswordSanitizerImpl.class);
	String seed = StringUtil.randomString(16);
}
