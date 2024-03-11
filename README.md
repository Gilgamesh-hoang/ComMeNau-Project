# ComMeNau-Project
The ComMeNau Web site is a Java-based web application that allows users to discover, purchase, and manage their favorite foods. This project is built using JSP and Servlet technology, providing a secure and feature-rich experience for ordering rice and food online.
## Table of Contents
- [Technology](#technology)
- [Database](#database)
- [Library to perform data retrieval.](#library-to-perform-data-retrieval)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Test Account](#test-account)
- [Table Of Functions](#table-of-functions)
- [Security](#security)

### Technology
- JSP (JavaServer Pages)
- Servlet
- JDBC (Java Database Connectivity)
### Database
- MySQL
### Library to perform data retrieval.
- JDBI

### Getting Started

To get started with the Web ComMeNau project, follow these steps:

1. **Clone the Repository:**
    ```
    https://github.com/Gilgamesh-hoang/ComMeNau-Project.git
    ```
2. **Navigate to the Project Directory:**
    ```
    cd ComMeNau-Project
    ```
3. **Execute SQL File:**

   Excute file sql in /Database/DB.sql to create table of project
4. **Edit Configuration:**
   Open the src/main/resources/DB.properties and Mail.properties files, edit them with your specific configuration details.
   ```
    sudo vi /src/main/resources/DB.properties 
    sudo vi /src/main/resources/Mail.properties 
   ```
5. **Run Project:**
   Using tomcat to run project on local server.
### Usage

**Login and Register:**
Navigate to the login and registration pages to access the full functionality of the web application.

**Email Communication:**
Experience email functionality for registration confirmation, contact, and other communication purposes.

**Real-time Chat:**
Send and receive messages in real time using Web Socket

**Shopping Cart:**
Manage your shopping cart with the ability to store items in both cookie and database.

**Payment Integration:**
Complete the payment process using integrated payment gateways like VNPay.

**CRUD Operations:**
Perform CRUD operations for orders, products, users and other entities.

### Test Account
#### Administrator
- Username: admin
- Password: 123456
#### User
- Username: john
- Password: 123456

### Table Of Functions
|    	| User Functions                         	| Administrator Functions                                  	|
|----	|----------------------------------------	|----------------------------------------------------------	|
| 1  	| Sign in, sign up.                      	| View basic statistics.                                   	|
| 2  	| Remember me.                           	| Category management.                                     	|
| 3  	| Forgot password.                       	| Product management.                                      	|
| 4  	| Log in with google, facebook.          	| Article management.                                      	|
| 5  	| Authenticate users by email.           	| Manage feedback.                                         	|
| 6  	| Authorization.                         	| Chat with users (Chatbox)                                	|
| 7  	| Personal account management.           	| Order management.                                        	|
| 8  	| Shopping cart.                         	| Lock and unlock accounts.                                	|
| 9  	| Payment (VNPAY, COD).                  	| Automatically destroy remaining products during the day. 	|
| 10 	| Order management.                      	|                                                          	|
| 11 	| View product details and reviews.      	|                                                          	|
| 12 	| Search and filter products.            	|                                                          	|
| 13 	| Contact.                               	|                                                          	|
| 14 	| Favorites list.                        	|                                                          	|
| 15 	| Blog                                   	|                                                          	|
| 16 	| Chat with the administrator (Chatbox). 	|                                                          	|

### Security

The Web ComMeNau project prioritizes security with the following measures:

* Bcrypt for password hashing
* Properties file
* Protection against SQL injection attacks