# HeyCar

## About Application

This is a REST Web Service with Spring and  H2 in memory database

### Endpoints

You can see the Api definitions here or in swagger (http://localhost:8080/swagger-ui.html)

* Create a new Listing
 
```
POST - http://localhost:8080/car-listing/vehicle_listings/{dealer_id}
```
```JSON
[
 {
 "code": "b",
 "make": "renault",
 "model": "megane",
 "kW": 132,
 "year": 2014,
 "color": "red",
 "price": 13990
 }
]

```

* Upload Listings using CSV file
```
*POST - http://localhost:8080/car-listing/upload_csv/{dealerId}
 ```
 Sample CSV file data

 code,make/model,power-in-ps,year,color,price
 1,mercedes/a 180,123,2014,black,15950
 2,audi/a3,111,2016,white,17210
 3,vw/golf,86,2018,green,14980
 4,skoda/octavia,86,2018,,16990

* Get Listings
```
*GET - http://localhost:8080/car-listing/search
```

* Get Listings by different parameters as year/make/model/color
```
*GET - http://localhost:8080/car-listing/search?year={value}&make={value}&color={value}
```

 Get Listings endpoint is paginated , use page and pageSize request params to control the response.

### Technologies/Libraries used: -
* Java 8
* Build with Maven
* InMemory database

### Running -
This service is using maven wrapper, it is not necessary to have maven in the execution environment.
<ul>
  <li><b>./mvnw clean verify</b> - to run tests</li>
  <li><b>./mvnw clean package</b> - creates executable jar</li>
  <li><b>java -jar ./target/hey-car-assignment-1.0-SNAPSHOT.jar</b> - Runs the executable jar on default port(8080)</li>
  <li><b>java -Dserver.port=8090 -jar ./target/hey-car-assignment-1.0-SNAPSHOT.jar</b> - If default port, 8080 already in use then it can be changed with server.port</li>
 </ul>
 
To Build and run using docker then follow the below steps - 
Prerequisites - Docker installation
<ul>
  <li><b>Clone the repository</b<</li>
  <li><b>Go to this repo in the termainal</b></li>
  <li><b>./mvnw clean package</b></li>
  <li><b>docker build -t hey-car-assignment:latest .</b></li>
  <li><b>docker run -p 8080:8080 -t hey-car-assignment:latest</b></li>
 </ul>
 
## Testing the application -

You can test using swagger - http://localhost:8080/swagger-ui.html

## Future Improvements -

<ul>
    <li>Implement Authorization</li>
    <li>Implement Dealer service/controller to create dealers. So that we can validate the dealer information</li>
    <li>Improve JPA auditing by setting loggedIn user to creaatedBy.</li>
    <li>Implement Changelog to the CAR listing table to check what changes has made to a car by the dealer</li>
    <li>Improve docker file to run shell script which can accepts environment variables</li>
    <li>Implement CI pieline</li>
</ul>


