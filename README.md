# To-Do Application
M.K's Simple To Do Application services with Java 8 + Spring Boot

# Service Details
APIs to register and login for a user
A user can create a list which will contain tasks.
Only user who has created the list can modify or delete the list and add/update/delete tasks in the list.


# API Endpoints Examples
A postman collection is included in the repository for easyily accessing all the endpoints along with examples.


## Test Results
![Alt text](test-result.gif?raw=true "Test Results")


## Test Coverage Details
![Alt text](test-coverage-report.jpeg?raw=true "Test Coverage Details")


## Running JUnit and Coverage Test

1.  Backend API - Java
  
  i.  run mvn clean install, maven will create a jar at /target location along with test reports under /targer/surefire-reports.
  ii. Run maven jar with java -jar <jar-name>.jar command and it will start the backend application.


## Running JUnit and Coverage Test

1.  Using Eclipse / IntelliJ
      
	i.	Right click on your project in the Project Explorer then select "Coverage As" > "JUnit Test" / Run 'All Test' with Coverage. IDE will run the test and generate a report about the Junit execution as well as the coverage result. 

2.  Using Maven 
      
	i. Install Maven. 
      
	ii. Go to the project directory, then run mvn test. Maven will run the test and generate the Junit execution report. Coverage report will be generated at PROJECT_DIRECTORY\target\surefire-reports\
