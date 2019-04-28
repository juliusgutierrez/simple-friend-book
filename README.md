## simple-friend-book 
A simple project made in spring framework and mysql that do the following : 
* can create, login, search a friend, add a friend, remove a friend

### Requirements
* Java 8
* Maven 3
* Mysql 


### Build
* mvn clean install 

### Run 
* create a database name test or you can choose your own db, but you have to update the spring.datasource.url
* add username and password of mysql to application.properties
* mvn spring-boot:run

### Testing
* Either run the test class
* for manual testing, import phonebook.postman_collection.json to postman


