package com.liferay.opsec.eliminatedefaultpasswordonceandforall;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.UserLocalServiceWrapper;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Olaf Kock
 */
@Component(immediate = true, property = {}, service = ServiceWrapper.class)
public class PasswordProtectionUserLocalServiceWrapper extends UserLocalServiceWrapper {

	public PasswordProtectionUserLocalServiceWrapper() {
		super(null);
	}

	public PasswordProtectionUserLocalServiceWrapper(UserLocalServiceWrapper wrappee) {
		super(wrappee);
	}

	@Override
	public User addUser(long creatorUserId, long companyId, boolean autoPassword, String password1, String password2,
			boolean autoScreenName, String screenName, String emailAddress, long facebookId, String openId,
			Locale locale, String firstName, String middleName, String lastName, long prefixId, long suffixId,
			boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, String jobTitle, long[] groupIds,
			long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail,
			ServiceContext serviceContext) throws PortalException {

		return super.addUser(creatorUserId, companyId, autoPassword,
				getSanitizer().sanitizePasswords(password1, password2),
				getSanitizer().sanitizePasswords(password1, password2), autoScreenName, screenName, emailAddress,
				facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth,
				birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail,
				serviceContext);
	}

	@Override
	public User addUser(User user) {
		if (!user.isPasswordEncrypted() && user.getPassword() != null) {
			user.setPassword(getSanitizer().sanitizePassword(user.getPassword()));
		}
		return super.addUser(user);
	}

	@Override
	public User addUserWithWorkflow(long creatorUserId, long companyId, boolean autoPassword, String password1,
			String password2, boolean autoScreenName, String screenName, String emailAddress, long facebookId,
			String openId, Locale locale, String firstName, String middleName, String lastName, long prefixId,
			long suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, String jobTitle,
			long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail,
			ServiceContext serviceContext) throws PortalException {

		return super.addUserWithWorkflow(creatorUserId, companyId, autoPassword,
				getSanitizer().sanitizePasswords(password1, password2),
				getSanitizer().sanitizePasswords(password1, password2), autoScreenName, screenName, emailAddress,
				facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth,
				birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail,
				serviceContext);
	}

	@Override
	public User updateUser(long userId, java.lang.String oldPassword, java.lang.String newPassword1,
			java.lang.String newPassword2, boolean passwordReset, java.lang.String reminderQueryQuestion,
			java.lang.String reminderQueryAnswer, java.lang.String screenName, java.lang.String emailAddress,
			long facebookId, java.lang.String openId, boolean hasPortrait, byte[] portraitBytes,
			java.lang.String languageId, java.lang.String timeZoneId, java.lang.String greeting,
			java.lang.String comments, java.lang.String firstName, java.lang.String middleName,
			java.lang.String lastName, long prefixId, long suffixId, boolean male, int birthdayMonth, int birthdayDay,
			int birthdayYear, java.lang.String smsSn, java.lang.String facebookSn, java.lang.String jabberSn,
			java.lang.String skypeSn, java.lang.String twitterSn, java.lang.String jobTitle, long[] groupIds,
			long[] organizationIds, long[] roleIds, List<UserGroupRole> userGroupRoles, long[] userGroupIds,
			ServiceContext serviceContext) throws PortalException {

		return super.updateUser(userId, oldPassword, getSanitizer().sanitizePasswords(newPassword1, newPassword2),
				getSanitizer().sanitizePasswords(newPassword1, newPassword2), passwordReset, reminderQueryQuestion,
				reminderQueryAnswer, screenName, emailAddress, facebookId, openId, hasPortrait, portraitBytes,
				languageId, timeZoneId, greeting, comments, firstName, middleName, lastName, prefixId, suffixId, male,
				birthdayMonth, birthdayDay, birthdayYear, smsSn, facebookSn, jabberSn, skypeSn, twitterSn, jobTitle,
				groupIds, organizationIds, roleIds, userGroupRoles, userGroupIds, serviceContext);
	}

	@Override
	public User updateUser(User user) {
		if (!user.isPasswordEncrypted() && user.getPassword() != null) {
			user.setPassword(getSanitizer().sanitizePassword(user.getPassword()));
		}
		return super.updateUser(user);
	}

	@Override
	public User updatePassword(long userId, String password1, String password2, boolean passwordReset)
			throws PortalException {
		return super.updatePassword(userId, getSanitizer().sanitizePasswords(password1, password2),
				getSanitizer().sanitizePasswords(password1, password2), passwordReset);
	}

	@Override
	public User updatePassword(long userId, java.lang.String password1, java.lang.String password2,
			boolean passwordReset, boolean silentUpdate) throws PortalException {

		return super.updatePassword(userId, getSanitizer().sanitizePasswords(password1, password2),
				getSanitizer().sanitizePasswords(password1, password2), passwordReset, silentUpdate);
	}

	@Override
	public User updatePasswordManually(long userId, java.lang.String password, boolean passwordEncrypted,
			boolean passwordReset, Date passwordModifiedDate) throws PortalException {

		return super.updatePasswordManually(userId, getSanitizer().sanitizePassword(password), passwordEncrypted,
				passwordReset, passwordModifiedDate);
	}

	private PasswordSanitizerImpl getSanitizer() {
		if (sanitizer == null) {
			String defaultPassword = PropsUtil.get(PropsKeys.DEFAULT_ADMIN_PASSWORD);
			sanitizer = new PasswordSanitizerImpl(defaultPassword);
		}
		return sanitizer;
	}

	private PasswordSanitizerImpl sanitizer = null;
}