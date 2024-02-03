# TMBD Backend Project

<img width="700" alt="Porject Architecture" src="https://github.com/HabibaGamil/TMBD-backend-Microservices/assets/75835933/f4318abb-be6f-4bd5-9cd9-d336b5de3458">

## Movie Microservice
Endpoints are secured using Auth module
### Endpoints
- Get Movie -> returns movie object based on ID
- Get Movie Page -> return list of movies based on specified page

## User Microservice
This microservices is responsible for creating and managing users in TMBD
### Endpoints
- Signup -> creates new user in its database ans returns token pair
- Login -> validates request details and returns token pair
- Refresh -> enables login using refresh token and login by email, returns new access token.

## Auth Module
This module abstract all JWT and authentication logic. It's also connected to a separate user_status database that tracks the status of users across the application.

## How to run
### Required technologies
- Java 17
- Maven
- Docker
### Steps:
1. Run "docker compose up" in project directory to get databases up and running
2. Run microservices
3. Test using postman or run frontend web application here: https://github.com/HabibaGamil/frontend-project
   



