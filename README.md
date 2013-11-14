Import users from excel into Liferay Portal by Web Services SOAP
=====================================
Welcome dear readers!

A few days ago, I made a simple example of using the Liferay Client Library. The project is based on Maven. Below are shown the steps needed to perform a test. I remember that the operations performed by the sample program are:

* Performs login on Liferay;
* Retrieves the CompanyID based on the virtualhost;
* Reading excel file that contains the list of users to import
* Running the import of users
	* Checking the roles that associate the user
	* Checking the sites or groups with which to associate the user
	* Add user on Liferay

	$ git clone git://github.com/amusarra/import-users-liferay-via-ws.git
	$ cd import-users-liferay-via-ws/
	$ mvn package
	
List 1. Clone repository and build the package

	$ cd target/
	$ java -Dusername=admin -Dpassword=admin -jar -DfileToImport=src/main/resources/users_for_import_liferay.xlsx target/import-users-liferay-via-ws-0.0.1-SNAPSHOT-jar-with-dependencies.jar
List 2. Run the portal client example

	
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
	
List 3. Show the performed tasks.

![Figure 1 – List of new imported users](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-20.png)
![Figure 2 – User details view](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-45.png)
![Figure 3 – User sites view](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-45.png)
![Figure 4 – User roles view](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-45.png)
![Figure 5 – User categorization view](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-45.png)
![Figure 6 – User display settings view](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-45.png)
![Figure 7 – User custom fields view](http://musarra.files.wordpress.com/2013/05/screen-shot-2013-05-23-at-23-59-45.png)