## CerQlar - Interest Matching API
Matching Buyer’s interests to Seller's Certificate Bundles in buying certain quantity of Certificates.

## Requirements Details
### Buyer
- Has list of his Interests
- Interest defines what Certificates and how many of them Buyer wants to buy -
{Certificate Energy Source, Quantity}. E.g. 1000 Solar certificates
### Seller
- Has access to all open Interests from all Buyers
- Has list of Certificate Bundles
- Each Certificate Bundle represents certain quantity of Certificates
- Certificate Bundle is - {Certificate Energy Source, Quantity, Issuer, Issue date}.
E.g. 500 Solar certificates issued on 2021-01-01 by “First Dutch Solar Power Plant”
- Has a function that:
  - For a given Interest Id
  - Tries to pick required amount of Certificates that match specified Interest
  - If it is possible to achieve required quantity - assigns matching Certificate
Bundles to specified Interest and closes the Interest
  - If not - throws an error
### Certificate Energy Sources
- Solar
- Wind
- Water

## Technical Implemntation
### Tech Stack
 - Kotlin
 - Spring
 - REST API interface
 - In Memory H2 Database (SQL DB) as preferred
### REST API documentation
 - Swagger UI -> http://localhost:8080/swagger-ui/
 - Authentication -> Basic Authentication (request uname and password)

### DB Access
 - DB UI -> http://localhost:8080/intmatch-console
 - Authentication -> Request for username and password

### Test coverage
 - Unit Tests
 - Integration Tests
