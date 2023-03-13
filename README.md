# myAuction
myAuction is my test Spring Boot application using Quartz for job scheduling, Thymeleaf for client side implementation, JUnit 5 and MockMVC for testing. It uses a PostgreSQL database to store data.

There are 3 users roles:
- _user_ can create an account, log in, make a bid, cancel the bid, view information about himself (lots won, lots he participated in, etc.).
- _auctioneer_ has the ability to create and delete lots.
- _admin_ can create and delete any bids and lots, view information about users and give them rights

How do I run the application?
1) Compile the project
2) Use docker with docker-compose.yml

Or:
1) install PostgreSQL
2) create DB "my_auction" with default Quartz schema for postgre
