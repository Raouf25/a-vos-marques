## Project setup
 So let´s have a quick look into the project´s structure. The project uses Maven´s Multi Module capabilities to achieve a clean setup:
```
a-vos-marques
├─┬ backend     → backend module with Spring Boot code
│ ├── src
│ └── pom.xml
├─┬ frontend    → frontend module with Vue.js code
│ ├── src
│ └── pom.xml
└── pom.xml     → Maven parent pom managing both modules
```
The pom.xml in the project´s root folder contains the two modules backend and frontend:

```xml
    <modules>
            <module>frontend</module>
            <module>backend</module>
    </modules>
```   
.... //TODO 
source: https://blog.codecentric.de/en/2018/04/spring-boot-vuejs/
 
    
#### First app run
Now we already have everything in place to fire up our Spring Boot powered Vue.js application! 
Just enter the project’s repository and let Maven do its job inside the project root directory:
```
mvn clean install
```
This will build our whole app and we can simply do a:
```
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
```
Now let´s open our browser and hit http://localhost:8088/. 
That´s it, our app should now look like this: