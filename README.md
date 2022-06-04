# Martrust Exam

## Overview
 This is a project api with relation to exchange rate of all the currencies in the world. I used the <https://exchangeratesapi.io/> for the rates provider.

## Endpoints
 * GET /currencies - list all available currency code.
 * GET /exchange-rate - returns the exchange rate of provided buy and sell currency.
 * GET /convert - returns the converted buy amount to sell amount.

## Prerequisite
 * Java 11
 * Maven
 * Git

## How to Run Application
 1. Clone repo
 ```git
 git clone https://github.com/levigratico/martrust-exam.git
 ```
 2. Run maven install
 ```maven
 mvn clean install
 ```

## Test API via Swagger UI
 1. Make sure that application is running.
 2. Open this url <http://localhost:8080/swagger-ui/#/exchange-rate-controller> to any browser.
 3. Collapse one endpoint and click *"Try it out"* button.
 4. If parameters is present, then fill it up first and click *"Execute"* button.
 5. Result should be shown below *"Response"* section.

## How to Run Test
 1. Run maven test
 ```maven
 mvn clean test
 ```

