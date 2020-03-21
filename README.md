# HeyCar

## About Application

This is a REST Web Service with Spring and  H2 in memory database

### You can

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
*GET - http://localhost:8080/car-listing/search?year={value}
```

### Spec -
* Java 8
* Build with Maven
* InMemory database

### Running -
Run as a Spring Boot App


