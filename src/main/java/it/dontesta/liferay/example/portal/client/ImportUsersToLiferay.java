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
package it.dontesta.liferay.example.portal.client;

import it.dontesta.liferay.example.portal.client.model.UserToImport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.client.soap.portal.model.CompanySoap;
import com.liferay.client.soap.portal.model.GroupSoap;
import com.liferay.client.soap.portal.model.RoleSoap;
import com.liferay.client.soap.portal.model.UserSoap;
import com.liferay.client.soap.portal.service.ServiceContext;
import com.liferay.client.soap.portal.service.http.CompanyServiceSoap;
import com.liferay.client.soap.portal.service.http.CompanyServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.GroupServiceSoap;
import com.liferay.client.soap.portal.service.http.GroupServiceSoapService;
import com.liferay.client.soap.portal.service.http.GroupServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.Portal_UserServiceSoapBindingStub;
import com.liferay.client.soap.portal.service.http.RoleServiceSoap;
import com.liferay.client.soap.portal.service.http.RoleServiceSoapService;
import com.liferay.client.soap.portal.service.http.RoleServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.UserServiceSoap;
import com.liferay.client.soap.portal.service.http.UserServiceSoapServiceLocator;

public class ImportUsersToLiferay {

	static final Logger LOGGER = LoggerFactory
			.getLogger(ImportUsersToLiferay.class);

	static final String LIFERAY_USER_NAME = (System.getProperty("username") != null) ? System
			.getProperty("username") : "test";
	static final String LIFERAY_USER_PASSWORD = (System.getProperty("password") != null) ? System
			.getProperty("password") : "test";

	static final String USER_SERVICE = "Portal_UserService";
	static final String COMPANY_SERVICE = "Portal_CompanyService";
	static final String GROUP_SERVICE = "Portal_GroupService";
	static final String ROLES_SERVICE = "Portal_RoleService";

	static final String FILE_TO_IMPORT_USERS = "/Users/amusarra/Documents/users_for_import_liferay.xlsx";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			long userId = 0;
			long companyId = 0;
			String virtualHost = (System.getProperty("liferayCompanyVirtualHost") != null) ? System
					.getProperty("liferayCompanyVirtualHost") : "localhost";
			
			URL userServiceEndPoint = _getURL(LIFERAY_USER_NAME,
					LIFERAY_USER_PASSWORD, USER_SERVICE);
			URL companyServiceEndPoint = _getURL(LIFERAY_USER_NAME,
					LIFERAY_USER_PASSWORD, COMPANY_SERVICE);

			LOGGER.info("Try lookup User Service by End Point: "
					+ userServiceEndPoint + "...");
			UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
			UserServiceSoap userService = locatorUser
					.getPortal_UserService(userServiceEndPoint);

			((Portal_UserServiceSoapBindingStub) userService)
					.setUsername(LIFERAY_USER_NAME);
			((Portal_UserServiceSoapBindingStub) userService)
					.setPassword(LIFERAY_USER_PASSWORD);

			LOGGER.info("Try lookup Company Service by End Point: "
					+ companyServiceEndPoint + "...");
			CompanyServiceSoapServiceLocator locatorCompany = new CompanyServiceSoapServiceLocator();
			CompanyServiceSoap companyService = locatorCompany
					.getPortal_CompanyService(companyServiceEndPoint);
			CompanySoap companySoap = companyService
					.getCompanyByVirtualHost(virtualHost);
			companyId = companySoap.getCompanyId();
			LOGGER.info("Company ID " + companyId);

			LOGGER.info("Get UserID" + "...");
			userId = userService.getUserIdByScreenName(
					companySoap.getCompanyId(), LIFERAY_USER_PASSWORD);
			LOGGER.info("UserId for user named " + LIFERAY_USER_PASSWORD
					+ " is " + userId);
			
			List<UserToImport> userToImportList = getUsersToImportFromExcel();
			if (userToImportList.size() > 0) {
				LOGGER.info("User ready for import to liferay is {}", userToImportList.size());
				LOGGER.info("Starting import users...");
				if (executeImport(companyId, userToImportList)) {
					LOGGER.info("Import users to liferay completed successfully");
				} else {
					LOGGER.warn("Import users to liferay completed with warning!");
				}
			} else {
				LOGGER.warn("User for import to liferay is {}", userToImportList.size());
			}
		} catch (RemoteException re) {
			LOGGER.error(re.getClass().getCanonicalName()
					+ re.getMessage());
		} catch (ServiceException se) {
			LOGGER.error(se.getClass().getCanonicalName()
					+ se.getMessage());
		}
	}
	
	/**
	 * Get the URL Liferay SOAP Service
	 * 
	 * @param remoteUser
	 * @param password
	 * @param serviceName
	 * @return
	 */
	private static URL _getURL(String remoteUser, String password,
			String serviceName) {

		final String LIFERAY_PROTOCOL = "http://";
		final String LIFERAY_TCP_PORT = "8080";
		final String LIFERAY_FQDN = "localhost";
		final String LIFERAY_AXIS_PATH = "/api/axis/";
		
		String liferayAddressProtocol = System.getProperty("liferayAddressProtocol");
		String liferayAddressPort = System.getProperty("liferayAddressPort");
		String liferayAddressFQDN = System.getProperty("liferayAddressFQDN");
		
		try {
			if (liferayAddressProtocol != null && liferayAddressPort != null
					&& liferayAddressFQDN != null) {
				return new URL(liferayAddressProtocol + "://"
						+ URLEncoder.encode(remoteUser, "UTF-8") + ":"
						+ URLEncoder.encode(password, "UTF-8") + "@" + liferayAddressFQDN
						+ ":" + liferayAddressPort + LIFERAY_AXIS_PATH + serviceName);
			} else {
				return new URL(LIFERAY_PROTOCOL
						+ URLEncoder.encode(remoteUser, "UTF-8") + ":"
						+ URLEncoder.encode(password, "UTF-8") + "@" + LIFERAY_FQDN
						+ ":" + LIFERAY_TCP_PORT + LIFERAY_AXIS_PATH + serviceName);
			}
		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	/**
	 * Read users to import from excel file.
	 * 
	 * @return Returns a list of users who are ready for import.
	 */
	private static List<UserToImport> getUsersToImportFromExcel() {
		String fileName = (System.getProperty("fileToImport") != null) ? System
				.getProperty("fileToImport") : FILE_TO_IMPORT_USERS;
		InputStream inp = null;
		List<UserToImport> usersList = new ArrayList<UserToImport>();
		UserToImport user = null;
		boolean readyForImport = true;
		
		try {
			inp = new FileInputStream(fileName);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			
			for (Row row : sheet) {
				LOGGER.debug("Processing row index {}...", row.getRowNum());
				if (row.getRowNum() == 0) {
					LOGGER.debug("First row is the header. Skip this row");
					continue;
				} else {
					user = new UserToImport();
				}
				for (Cell cell : row) {
					LOGGER.debug("Processing cell index {}...",
							cell.getColumnIndex());
					switch (cell.getColumnIndex()) {
					case 0:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setTitle(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 1:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setScreenName(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						} else {
							LOGGER.warn("The username attribute is not null. Row skipped");
							LOGGER.warn("Processing cell index {}...[FAILED]",
									cell.getColumnIndex());
							break;
							
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 2:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setEmail(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						} else {
							LOGGER.warn("The email attribute is not null. Row skipped");
							LOGGER.warn("Processing cell index {}...[FAILED]",
									cell.getColumnIndex());
							break;
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 3:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setFirstName(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 4:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setMiddleName(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 5:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setLastName(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 6:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setGender(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 7:
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (DateUtil.isCellDateFormatted(cell)) {
								Calendar calBirthDate = Calendar.getInstance();
								calBirthDate.setTime(cell.getDateCellValue());
								
								user.setBirthDate(calBirthDate);
								LOGGER.debug("Value cell index {} is {}",
										cell.getColumnIndex(), cell.getDateCellValue());
							} else {
								LOGGER.warn("Value cell index {} {}", cell.getColumnIndex(), cell.getNumericCellValue());
								LOGGER.warn("Value cell index {} not contain a date type format", cell.getColumnIndex());
							}
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 8:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setJobTitle(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						break;
					case 9:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setSiteName(cell.getStringCellValue().split(
									","));
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), Arrays.toString(cell.getStringCellValue().split(",")));
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 10:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setRoleName(cell.getStringCellValue().split(
									","));
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), Arrays.toString(cell.getStringCellValue().split(",")));
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 11:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setLanguageId(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 12:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setTimeZoneId(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());
						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					case 13:
						if (!cell.getStringCellValue().isEmpty()) {
							user.setAccountId(cell.getStringCellValue());
							LOGGER.debug("Value cell index {} is {}",
									cell.getColumnIndex(), cell.getStringCellValue());

						}
						LOGGER.debug("Processing cell index {}...[OK]",
								cell.getColumnIndex());
						break;
					default:
						break;
					}
				}
				if (user.getScreenName() == null) {
					LOGGER.warn("The username attribute can not be null for rowId {}", row.getRowNum());
					readyForImport = false;
				}
				if (user.getEmail() == null) {
					LOGGER.warn("The email attribute can not be null for rowId {}", row.getRowNum());
					readyForImport = false;
				}
				if (readyForImport) {
					LOGGER.info("Add user object {} to user list...", user.toString());
					usersList.add(user);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
		} catch (IllegalStateException e) {
			LOGGER.error(e.getMessage());
		} catch (InvalidFormatException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		return usersList;
	}
	
	/**
	 * Performs user import. The users are imported and made ​​active Tagged as imported users
	 * 
	 * @param usersList
	 * @return Returns true if all users are imported successfully, false if any errors have occurred.
	 */
	private static boolean executeImport(long companyId, List<UserToImport> usersList) {
		URL userServiceEndPoint = _getURL(LIFERAY_USER_NAME,
				LIFERAY_USER_PASSWORD, USER_SERVICE);
		boolean errorOnImport = false;

		LOGGER.info("Try lookup User Service by End Point: "
				+ userServiceEndPoint + "...");
		
		UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
		try {
			UserServiceSoap userService = locatorUser
					.getPortal_UserService(userServiceEndPoint);
			
			for (UserToImport userToImport : usersList) {
				List<GroupSoap> companySite = checkSiteName(companyId, userToImport.getSiteName());
				List<RoleSoap> roles = checkRoleName(companyId, userToImport.getRoleName());
				Map<String, Serializable> expandoBridgeAttributes = new HashMap<String, Serializable>();

				long[] groupIds = new long[companySite.size()];
				long[] roleIds = new long[roles.size()];
				long[] organizationIds = {};
				long[] userGroupIds = {};
				
				for (int i = 0; i < companySite.size(); i++) {
					groupIds[i] = companySite.get(i).getGroupId();
				}

				for (int i = 0; i < roles.size(); i++) {
					roleIds[i] = roles.get(i).getRoleId();
				}
				
				boolean male = (userToImport.getGender().equals("M")) ? true : false;
				expandoBridgeAttributes.put("AccountId", userToImport.getAccountId());
				
				ServiceContext serviceContext = new ServiceContext();
				serviceContext.setAddGuestPermissions(true);
				serviceContext.setAssetTagNames(new String[]{"IMPORTED USERS"});
				serviceContext.setExpandoBridgeAttributes((HashMap<String, Serializable>) expandoBridgeAttributes);
				serviceContext.setWorkflowAction(1);
				
				try {
					UserSoap newUserLiferay = userService.addUser(companyId, true, userToImport.getPassword(),
							userToImport.getPassword(), false, userToImport.getScreenName(), userToImport.getEmail(),
							0, new String(), userToImport.getLanguageId(), userToImport.getFirstName(), 
							userToImport.getMiddleName(),
							userToImport.getLastName(), 0, 0, male, 
							userToImport.getBirthDate().get(Calendar.MONTH),
							userToImport.getBirthDate().get(Calendar.DAY_OF_MONTH), 
							userToImport.getBirthDate().get(Calendar.YEAR), 
							userToImport.getJobTitle(), 
							groupIds, organizationIds, roleIds, userGroupIds, true, serviceContext);
					LOGGER.info("User {} added on liferay.", newUserLiferay.getScreenName());
				} catch (RemoteException e) {
					errorOnImport = true;
					LOGGER.warn("Error on adding user {} on liferay", userToImport.toString());
					LOGGER.warn(e.getMessage());
				}
			}
		} catch (ServiceException e) {
			errorOnImport = true;
			LOGGER.warn(e.getMessage());
		}
		return !errorOnImport;
	}
	
	/**
	 * Returns a list of roles found
	 * 
	 * @param companyId
	 * @param roleName
	 * @return
	 */
	private static List<RoleSoap> checkRoleName(long companyId, String[] roleName) {
		List<RoleSoap> roles = new ArrayList<RoleSoap>();
		URL roleServiceEndPoint = _getURL(LIFERAY_USER_NAME,
				LIFERAY_USER_PASSWORD, ROLES_SERVICE);

		LOGGER.info("Try lookup Role Service by End Point: "
				+ roleServiceEndPoint + "...");

		RoleServiceSoapService locatorRole = new RoleServiceSoapServiceLocator();
		RoleServiceSoap roleService = null;
		try {
			roleService = locatorRole
					.getPortal_RoleService(roleServiceEndPoint);
			for (String role : roleName) {
				RoleSoap roleObject = null;
				try {
					roleObject = roleService.getRole(companyId, role);
					roles.add(roleObject);
				} catch (RemoteException e) {
					LOGGER.warn(e.getMessage());
				}
			}
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
		}
		return roles;
	}

	/**
	 * Returns a list of groups or site found
	 * 
	 * @param companyId
	 * @param siteName
	 * @return
	 */
	private static List<GroupSoap> checkSiteName(long companyId, String[] siteName) {
		List<GroupSoap> sites = new ArrayList<GroupSoap>();
		URL groupServiceEndPoint = _getURL(LIFERAY_USER_NAME,
				LIFERAY_USER_PASSWORD, GROUP_SERVICE);

		LOGGER.info("Try lookup Group Service by End Point: "
				+ groupServiceEndPoint + "...");

		GroupServiceSoapService locatorGroup = new GroupServiceSoapServiceLocator();
		GroupServiceSoap groupService = null;
		try {
			groupService = locatorGroup
					.getPortal_GroupService(groupServiceEndPoint);
			for (String name : siteName) {
				GroupSoap site = null;
				try {
					site = groupService.getGroup(companyId, name);
					sites.add(site);
				} catch (RemoteException e) {
					LOGGER.warn(e.getMessage());
				}
			}
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
		}
		return sites;
	}
}
