
## Brief description and instructions to run project

Project is built on top of Spring (Boot). In the development process I have used this JSK setup:
```
openjdk version "17.0.14" 2025-01-21
OpenJDK Runtime Environment Homebrew (build 17.0.14+0)
OpenJDK 64-Bit Server VM Homebrew (build 17.0.14+0, mixed mode, sharing)
```
Database (Postgres) and the application can be started using Docker. 
To run the project, you need to have Docker installed on your machine.


### Some useful commands and instructions to get started:

- Install all dependencies from the pom.xml file

- `./mvnw package -DskipTests` to make JAR file 

- `docker-compose up --build`

- access web app at http://localhost:8080

- Please make sure that 4 (four) DB tables are present at `postgresql://localhost:5432` and `action_type` table is populated from `data.sql` file in the project. This should happen automatically if all goes well.


### Tests
There are some automated tests present in the project.

  
### Endpoints
#### POST /api/track/action

Valid JSON body content example:
```
{
    "action_type_slug": "product_viewed",
    "customer_id": "1",
    "product_id": "1",
    "session_id": "1"
}
```
`action_type_slug` - (Mandatory) Needs to have value set to one of following: `product_viewed`, `product_added_cart`, `checkout_started`, `purchase_completed`

For `action_type_slug` with values `product_viewed` and `product_added_cart` it is mandatory to have `product_id` and `session_id` fields in the body.

⚠️ Please note that there is a global rate limit for this endpoint, set to 10 requests per minute. This of course would be increased in future. 


#### POST /api/track/sale
Valid JSON body content example:
```
{
    "total_amount": 100,
    "transaction_identifier": "trid",
    "transaction_products": 
        [
            {
                "product_identifier": "pdid2",
                "quantity": 1,
                "unit_price": 50.50
            },
            {
                "product_identifier": "pdid2",
                "quantity": 1,
                "unit_price": 49.50
            }
        ]
}
```
⚠️ Please note that there is a global rate limit for this endpoint, set to 10 requests per minute. This of course would be increased in future. 


#### GET  /api/metrics
Accepted parameters: `startDate` and `endDate`. 
Format for both parameters: `YYYY-MM-DD`
Example: /api/metrics?startDate=2025-01-01&endDate=2025-02-28
JSON response content example
```
{
    "conversionRate": 50,
    "totalSales": 1
}
```
The returned conversion rate is session aware. Meaning that two product view actions within the same session is counted as one action. If any product is purchased via this session
it would be counted as 1 conversion.
If this needs to be different we can change this. For more information of the logic seek for https://github.com/martinsluters/sales-conversion-api/blob/107f6fa4e8c8f4a20e912f6113e09b375258b1b7/src/main/java/com/am/SalesConversionAPI/repository/tracker/useraction/ActionRepository.java

⚠️ Please note that the response for this endpoint is cached for 1 minute and the endpoint requires the HTTP header to contain valid basic auth details to be passed. 
For demoing purposes the user name is `user` and password is `password`.


### Improvements
There are many improvements to be made - more tests, containerize project properly, apply coding standard (still figuring out how to apply it), DRY code, more sophisticated security and many more. 


### EER Diagram
![er](https://github.com/user-attachments/assets/93d759de-d874-4f38-bad3-4687e3a50ee5)


