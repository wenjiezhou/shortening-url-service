shortening-url-service
===============

A lightweight microservice built on Spring using postgresql as a backing datastore meant to handle all url shortening relate APIs.

Part I
===============
### Prereqs

* Java 1.8 or higher
* PostgreSQL 9.5 
* 
Note: For this quick demo purpose, i choose to use PostgreSQL rds. The Nosql (ex: DynamoDB) should be a better choose for this service in teams of scalability and durability

#### Database setup 

You do not need to setup database. The well configured AWS RDS instance is up running here:rlshorter.cdr2w6zgugdk.us-east-1.rds.amazonaws.com:5432.
The application will connect to this RDS instance automatically.

#### Starting Shortening Url Service Locally
Go to top level of the project, and run follwoing 3 commands in sequential order:

1. Run `chmod +x gradlew` to avoid gradlew permission deny.
 
2. Run `./gradlew clean build` to build with Gradle.
 
3. Run `./gradlew bootRun` to start the embedded Tomcat server

### Api Testing Locally
Step 1:You can run following curl command to Post shortening url api.

Sample Curl command:
curl -H "Content-type: application/json" -X POST -d '{"url": "https://help.netflix.com/legal/termsofuse?locale=en&docType=termsofuse"}'  http://localhost:9024/shortener

Sample response:
{"shortUrl":"http://localhost:9024/shortener/Cd"}

Step 2: copy shotUrl value from above response "http://localhost:9024/shortener/Cd", and paste it to browser, you will be redirect to original long url "https://help.netflix.com/legal/termsofuse?locale=en&docType=termsofuse".

### Api Testing against the service deployed in cloud
In case of if the service can not be run in your local machine successfully, i also deploy the same code on to cloud.
Following is how to test it:

Step 1: Run curl command.

Sample curl command:
curl -H "Content-type: application/json" -X POST -d '{"url": "https://help.netflix.com/legal/termsofuse?locale=en&docType=termsofuse"}
'  http://shortening-url.us-west-2.elasticbeanstalk.com/shortener

Sample response:
{"shortUrl":"http://shortening-url.us-west-2.elasticbeanstalk.com/shortener/Fr"}

Step 2:copy shotUrl value from above response "http://shortening-url.us-west-2.elasticbeanstalk.com/shortener/Fr", and paste it to browser, you will be redirect to original long url "https://help.netflix.com/legal/termsofuse?locale=en&docType=termsofuse".

### Design
-The service creates new db record for each shortening url Post API call. And genered a unique short Url base on the corresponding db record ID.
-ID, long_url and short_url are all persisted into data base during Post call.
-The service is able to find db id from short_url, and then find corresponding long url.

Part II
===============
In order to improve the Scalability, Availability and Durability of the service, following updates need to be made:

#1, In data store layer, instead of RDMS RDS, key value store as NoSql data base is a much better choice. I m going to use AWS DynamoDB as NoSql option here. Scalability: Base on aws doc, the DynamoDB's IOPS can be increase as high as possible with no limit, thus 100,000 request per second won't be a problem. (This result is accomplished by partitioning NoSql table). Availability and Durability: DynamoDB service replicates data across three facilities in an AWS Region to provide fault tolerance in the event of a server failure or Availability Zone outage.To achieve high uptime and durability, Amazon DynamoDB synchronously replicates data across three facilities within an AWS Region. (Cross region replication is the option as well if needed. We can increase scalability and availability further in this way).

#2, In application server layer, instead of one app server, multiple deployed app servers is must. We should deploy shortening-url-service app on to multiple AWS EC2 instances and these instances should be cross multiple availability zone(at least 3). These deployed app server EC2 instance should be put behind ELB (elastic load balance) group for load balance purpose. These ELBs should be allocated in more than one availability zone as well to avoid single point failure. One more thing need be mentioned is, Autoscaling group should be used, so that the app server EC2 instances can be scale out and scale in automatically base on traffic load increase and decrease. Furthermore, to scale up the system further, we can deploy above architecture to multiple different regions. The Route53 should be used to route request to closest regional servers, as well as regional fail over in case of certain region is down.

#3: We can create a distributed cache layer (Mamcached or Redis) to reduce the db read load if needed.

Potential problem and solution: with high traffic, the race condition result in duplicate primary key is possible. In my design, the uniqueness of the db primary key is the key, since the short url is derived from db primary key. In case of duplicated primary key is created by race condition, the db would throw duplicated primary exception, the service layer should capture this exception and try to generate another unique key again (retry machnism is needed in order to resolve the race condition problem).

### Following are References
https://goo.gl/

https://en.wikipedia.org/wiki/Bijection

https://aws.amazon.com/dynamodb/faqs/

