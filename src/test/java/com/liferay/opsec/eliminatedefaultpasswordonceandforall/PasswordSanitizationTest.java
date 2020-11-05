package com.liferay.opsec.eliminatedefaultpasswordonceandforall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.liferay.portal.kernel.util.DigesterUtil;

import org.junit.jupiter.api.Test;

class PasswordSanitizationTest {

	PasswordSanitizerImpl testSubjectDefault = new PasswordSanitizerImpl("test");
	PasswordSanitizerImpl testSubjectCustom  = new PasswordSanitizerImpl("non-test");
	
	public PasswordSanitizationTest() {
		new DigesterUtil().setDigester(new DigesterImpl());
	}
	
	
	@Test
	void testSinglePasswordSanitization() {
		delegatedTestSinglePassword(testSubjectDefault);
		delegatedTestSinglePassword(testSubjectCustom);
	}

	@Test
	void testDualPasswordSanitization() {
		delegatedTestDualPasswords(testSubjectDefault);
		delegatedTestDualPasswords(testSubjectCustom);
	}
	
	@Test
	void testThatAnExplicitlyConfiguredPasswordIsAlwaysUsed() {
		assertEquals("non-test", testSubjectCustom.sanitizePassword("test"));
	}
	
	@Test 
	void testThatTestAlwaysGetsRandomizedIfNotConfigured() {
		assertTrue(testSubjectDefault.sanitizePassword("test").length() > 10);
	}

	@Test
	void testNeverAllowedValue() {
		// test is never allowed
		assertNotEquals("test", testSubjectDefault.sanitizePassword("test"));
		assertNotEquals("test", testSubjectDefault.sanitizePasswords("test", "test"));
		
		assertNotEquals("test", testSubjectCustom.sanitizePassword("test"));
		assertNotEquals("test", testSubjectCustom.sanitizePasswords("test", "test"));
		
	}
	
	@Test
	void testConfiguredValueAlwaysAllowed() {
		assertEquals("non-test", testSubjectDefault.sanitizePassword("non-test"));
		assertEquals("non-test", testSubjectDefault.sanitizePasswords("non-test", "non-test"));

		assertEquals("non-test", testSubjectCustom.sanitizePassword("non-test"));
		assertEquals("non-test", testSubjectCustom.sanitizePasswords("non-test", "non-test"));
	}
	
	@Test 
	void testNonequalPasswordsGenerateNonequalResults() {
		assertNotEquals(testSubjectDefault.sanitizePasswords("one", "two"), testSubjectDefault.sanitizePasswords("one", "two"));
		assertNotEquals(testSubjectCustom.sanitizePasswords("one", "two"), testSubjectCustom.sanitizePasswords("one", "two"));
	}
	
	
	
	void delegatedTestDualPasswords(PasswordSanitizerImpl psi) {
		assertNull(psi.sanitizePasswords(null, null));
		assertNull(psi.sanitizePasswords(null, "something"));
		assertNull(psi.sanitizePasswords("something", null));
		assertNull(psi.sanitizePasswords(null, "test"));
		assertNull(psi.sanitizePasswords("test", null));
		
		// equal (unconfigured) passwords generate equal results
		assertEquals(psi.sanitizePasswords("one", "one"), psi.sanitizePasswords("one", "one"));
	}
	
	void delegatedTestSinglePassword(PasswordSanitizerImpl psi) {
		assertNull(psi.sanitizePassword(null));
		assertNotNull(psi.sanitizePassword("password"));
		assertNotNull(psi.sanitizePassword("test"));
		assertNotNull(psi.sanitizePassword("non-test"));
	}
}
