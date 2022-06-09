# User Registration Service


## REST API for a basic, generic user registration service 
Scope - only registration service.
### Requirements:
1. A user will register using the name, address, email Id ,mobile number and password
2. Only one registration is allowed using for a single emailId or a mobile number
3. Email Id must be verified by sending a URL at the email id and then marking the email id as
verified when the url is clicked by the user
4. Mobile number must be verified by sending an OTP (one time password) via SMS to the
mobile number and then marking the mobile number as verified once the user enters the
unique OTP sent to the user's mobile number
## Mail settings
In **application.properties** file, change following settings,
Use your username and password for authentication on smtp server. 
- spring.mail.username=rrpassignment@gmail.com
- spring.mail.password=abcd@1234

If you are using other than gmail.smtp server then also change following fields in application.properties file:
- spring.mail.host=smtp.gmail.com
- spring.mail.port=587
### The given implementation is providing REST API end only, as for testing API , use POSTMAN like API testing tool.
### To see API Documentation run http://localhost:8080/swagger-ui.html, after executing application.
#### Implementation Details
- Framework: Spring Boot framework- as it is providing in built MVC, JPA, and Tomcat server . 
- Database: In-memory database for easement of application to run locally.

