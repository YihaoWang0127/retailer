#Implementation

For this assignment, we apply Springboot and H2 database, integrated with Gradle 6.7

Run the main class of this project

Connect to the H2 database. In you browser, use http://localhost:8080/h2-console to login. (Using the credentials from application.properties file)

Run the script.sql in H2 console to create data in db

Use Postman or other tool to grab the outcome http://localhost:8080/customers/{customerId}/rewards

#Logic

we calculate each user's rewards based on transaction and time period. So we need two entities customer and transaction, we also need one more entity rewards to store the value For time period, we can use timestamp to point to the duration.

We use rest api to handle request, in the controller layer, we also apply error message for the possible error In this project, we catch the user not found exception.

In service layer, we do the calculation and bussiness logic there

In repository layer, we extends CrudRepository to handle the basic query for us in H2 DB.

#Unit Test

Changing the IntelliJ settings for 'Gradle -> Run Tests Using' from 'Gradle (default)' to 'IntelliJ IDEA'.

#To Do

Add JWT token interceptor to secure proj

Enrich requests

Convert to more db

Connect to AWS

..