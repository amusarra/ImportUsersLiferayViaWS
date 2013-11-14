/*
 * #%L
 * Import users from excel into Liferay Portal by Web Services SOAP
 * %%
 * Copyright (C) 2013 Antonio Musarra
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package it.dontesta.liferay.example.portal.client.model;

import java.util.Arrays;
import java.util.Calendar;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author amusarra
 *
 */
public class UserToImport {
	
	private String accountId;
	
	private Calendar birthDate;
	
	@NotNull(message="The email attribute can not be null")
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="The email address specified is not valid")
	private String email;
	
	@NotNull(message="The First Name attribute can not be null")
	private String firstName;
	
	@NotNull
	@Pattern(regexp="M|F", message="Il valore deve essere M o F")
	private String gender;
	
	private String jobTitle;
	
	@NotNull
	@Pattern(regexp="[a-z]{2}_[A-Z]{2}", message="The value of the language id is not correct.")
	private String languageId;
	
	@NotNull(message="The Last Name attribute can not be null")
	private String lastName;
	
	private String middleName;
	
	@NotNull
	private String password;

	@NotNull(message="The Role Name attribute can not be null")
	private String[] roleName;

	@NotNull(message="The Screen Name attribute can not be null")
	private String screenName;
	
	@NotNull(message="The Site Name attribute can not be null")
	private String[] siteName;
	
	@NotNull(message="The Time ZoneId attribute can not be null")
	private String timeZoneId;
	
	private String title;
		
	/**
	 * 
	 */
	public UserToImport() {
		this.password = RandomStringUtils.randomAlphabetic(8);
	}

	/**
	 * @param title
	 * @param screenName
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @param gender
	 * @param birthDate
	 * @param jobTitle
	 * @param siteName
	 * @param roleName
	 * @param languageId
	 * @param timeZoneId
	 * @param accountId
	 */
	public UserToImport(String title, String screenName, String email,
			String firstName, String lastName, String middleName,
			String gender, Calendar birthDate, String jobTitle, String[] siteName,
			String[] roleName, String languageId, String timeZoneId,
			String accountId) {
		super();
		this.title = title;
		this.screenName = screenName;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.jobTitle = jobTitle;
		this.siteName = siteName;
		this.roleName = roleName;
		this.languageId = languageId;
		this.timeZoneId = timeZoneId;
		this.accountId = accountId;
		this.password = RandomStringUtils.randomAlphabetic(8);
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @return the birthDate
	 */
	public Calendar getBirthDate() {
		return birthDate;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the roleName
	 */
	public String[] getRoleName() {
		return roleName;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @return the siteName
	 */
	public String[] getSiteName() {
		return siteName;
	}

	/**
	 * @return the timeZoneId
	 */
	public String getTimeZoneId() {
		return timeZoneId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Calendar birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String[] roleName) {
		this.roleName = roleName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String[] siteName) {
		this.siteName = siteName;
	}

	/**
	 * @param timeZoneId the timeZoneId to set
	 */
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserToImport [accountId=" + accountId + ", birthDate="
				+ birthDate + ", email=" + email + ", firstName=" + firstName
				+ ", gender=" + gender + ", jobTitle=" + jobTitle
				+ ", languageId=" + languageId + ", lastName=" + lastName
				+ ", middleName=" + middleName + ", password=" + password
				+ ", roleName=" + Arrays.toString(roleName) + ", screenName="
				+ screenName + ", siteName=" + Arrays.toString(siteName)
				+ ", timeZoneId=" + timeZoneId + ", title=" + title + "]";
	}

}
