Import users from excel into Liferay Portal by Web Services SOAP
=====================================
Welcome dear readers!

A few days ago, I made a simple example of using the Liferay Client Library. The reference version of Liferay is the 6.1 and 6.2 Community Edition. The program should also work with the Enterprise edition. The project is based on Maven. Below are shown the steps needed to perform a test. I remember that the operations performed by the sample program are:

1. Performs login on Liferay;
2. Retrieves the CompanyID based on the virtualhost;
3. Reading excel file that contains the list of users to import
4. Running the import of users
	1. Checking the roles that associate the user
	2. Checking the sites or groups with which to associate the user
	3. Add user on Liferay

**Attention**. 
The master branch contains the code working with version 6.2 of Liferay. The 6.1 branch contains code instead working with the 6.1 version of Liferay.

**Note**: Prior to Liferay 6.2, there were two different URLs for accessing remote Liferay services. 
* http://[host]:[port]/api/secure/axis was for services requiring authentication 
* http://[host]:[port]/api/axis was for services that didn’t require authentication. 
As of Liferay 6.2, all remote Liferay services require authentication and the http://[host]:[port]/api/axis URL is used to access them.

```	 
    $ git clone git://github.com/amusarra/ImportUsersLiferayViaWS.git
    $ cd ImportUsersLiferayViaWS.git/
    $ mvn package
```
List 1. Clone repository and build the package
```
    $ java -Dusername=admin -Dpassword=admin -jar -DfileToImport=src/main/resources/users_for_import_liferay.xlsx target/import-users-liferay-via-ws-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
List 2. Run the portal client example

The program will by default try to connect to the services of your local installation of Liferay.You can specify connection parameters using the following parameters:

* -DliferayAddressProtocol: Specify the protocol: http or https (default http)
* -DliferayAddressPort: Specify the tcp port (default 8080)
* -DliferayAddressFQDN: Specify the hostname or fqdn (default localhost)
* -DliferayCompanyVirtualHost: Specify the hostname or fqdn of the company (default localhost)
* -Dusername: Specify the username (default test)
* -Dpassword: Specify the password (default test)
* -DfileToImport: Specify the valid path of the excel file

```
    $ java -DliferayAddressProtocol=http -DliferayAddressPort=8080 -DliferayAddressFQDN=localhost -Dusername=admin -Dpassword=admin -DfileToImport=/Users/amusarra/Documents/workspace-myBlog/import-users-liferay-via-ws/src/main/resources/users_for_import_liferay.xlsx -jar target/import-users-liferay-via-ws-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
List 3. A complete example with all parameters

```
	2013-11-14 13:48:13,892 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Try lookup User Service by End Point: http://admin:admin@localhost:8080/api/secure/axis/Portal_UserService...
	2013-11-14 13:48:14,343 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Try lookup Company Service by End Point: http://admin:admin@localhost:8080/api/secure/axis/Portal_CompanyService...
	2013-11-14 13:48:14,581 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Company ID 1
	2013-11-14 13:48:14,582 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Get UserID...
	2013-11-14 13:48:14,619 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - UserId for user named admin is 10382
	2013-11-14 13:48:15,517 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Add user object UserToImport [accountId=7477234f-34f7-76bf-b
	2013-11-14 13:48:15,533 [main] WARN  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - The username attribute can not be null for rowId 5
	2013-11-14 13:48:15,534 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - User ready for import to liferay is 4
	2013-11-14 13:48:15,534 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Starting import users...
	2013-11-14 13:48:15,557 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - User cmontagno added on liferay.
	2013-11-14 13:48:15,558 [main] INFO  it.dontesta.liferay.example.portal.client.ImportUsersToLiferay - Import users to liferay completed successfully
```	
List 3. Show the performed tasks.


![Figure 1 – Excel file format](https://www.dontesta.it/wp-content/uploads/2013/11/excel_file_format.png)

Figure 1 – Excel file format

![Figure 2 – List of new imported users](https://www.dontesta.it/wp-content/uploads/2013/11/new_imported_users.png)

Figure 2 – List of new imported users

![Figure 3 – User details view](https://www.dontesta.it/wp-content/uploads/2013/11/user_details.png)

Figure 3 – User details view

![Figure 4 – User sites view](https://www.dontesta.it/wp-content/uploads/2013/11/user_sites.png)

Figure 4 – User sites view

![Figure 5 – User roles view](https://www.dontesta.it/wp-content/uploads/2013/11/user_roles.png)

Figure 5 – User roles view

![Figure 6 – User categorization view](https://www.dontesta.it/wp-content/uploads/2013/11/user_categorization.png)

Figure 6 – User categorization view

![Figure 7 – User display settings view](https://www.dontesta.it/wp-content/uploads/2013/11/user_display_settings.png)

Figure 7 – User display settings view

![Figure 8 – User custom fields view](https://www.dontesta.it/wp-content/uploads/2013/11/user_custom_fields.png)

Figure 8 – User custom fields view

![Figure 9 – Search user by tag name](https://www.dontesta.it/wp-content/uploads/2013/11/user_search_by_tag.png)

Figure 9 – Search user by tag name
