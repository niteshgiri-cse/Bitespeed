# Bitespeed Identity Reconciliation

## Hosted API

POST https://your-app.onrender.com/identify

## Request Body

{
"email": "mcfly@hillvalley.edu",
"phoneNumber": "123456"
}

## Response

{
"contact": {
"primaryContatctId": 1,
"emails": [],
"phoneNumbers": [],
"secondaryContactIds": []
}
}

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- H2 Database
- Render (Deployment)

## How to run locally

mvn spring-boot:run