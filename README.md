# :parking:arking Lot 

### Basic Parking Lot

Create a REST service to manage sales for a paid parking lot. Use Java 11+ and any framework or 3rd party libs of your
choice (Spring, JavaEE, etc). Use a relational database of your choice (and provide the schema) – i.e MySQL, PostGreSql,
H2. Include a Maven or Gradle build for the project.

### Business requirements:

- The parking lot has 50 car spots and 10 bus spots; :heavy_check_mark:
- For cars hourly price is EUR 1 while the 24hr price is EUR 10; :heavy_check_mark:
- For buses hourly price is EUR 5 while 24hr price is EUR 40; :heavy_check_mark:
- Sales are generated when a vehicle leaves the parking lot. :heavy_check_mark:

### The REST service should have all necessary methods to:

- Record occupancy; :heavy_check_mark:
- Record sales. :heavy_check_mark:

### Optional, for bonus points:

- Provide REST method generating an itemized report of sales given a date range; :heavy_check_mark:
- Protect the REST service using basic authentication; :heavy_check_mark:
- Provide some unit tests. :heavy_check_mark:

Deliver project via a GitHub shared repository or a Zip file.

### Еxtra of my choice:

- Global REST exception hanler; :heavy_check_mark:
- Reset payment reports and write current state into CSV file (log files); :heavy_check_mark:
- Custom request with query parameters. :heavy_check_mark:

**NB**: Postman collections and DB structure are added in the project repo.

**Project structure**:
```
src
├─ main
│   └── java
│       ├── com.pros.parkinglot
│       │   ├── configuration
│       │   │   ├── role
│       │   │   │   └── Role
│       │   │   ├── DataPopulator
│       │   │   ├── GlobalRestExceptionHandler
│       │   │   ├── ParkingLotConfiguration
│       │   │   └── SecurityConfiguration
│       │   ├── controller
│       │   │   ├── ParkingController
│       │   │   └── ReportsController
│       │   ├── dto        
│       │   │   ├── ReportDto
│       │   │   ├── TicketDto
│       │   │   ├── TimeRange
│       │   │   └── VehicleDto
│       │   ├── exception
│       │   │   ├── DuplicateRegistrationNumberException
│       │   │   ├── NoAvailableSlotsException
│       │   │   └── NotAvailableVehicleInTheParkingException
│       │   ├── mapper
│       │   │   ├── ReportDtoMapper
│       │   │   └── SlotDtoMapper
│       │   ├── model   
│       │   │   ├── report   
│       │   │   │   └── Report                                        
│       │   │   └── slot 
│       │   │       ├── type       
│       │   │       │   └── VehicleType
│       │   │       ├── Bus
│       │   │       ├── Car
│       │   │       └── Vehicle
│       │   ├── repository 
│       │   │   ├── ReportRepository
│       │   │   └── VehicleRepository
│       │   ├── service 
│       │   │   ├── ParkingService
│       │   │   └── ReportService
│       │   └── ParkingLotApplication
│       └── resources
│           ├── logs
│           │   └── ...
│           ├── ...
│           └── application.properties
└─ test
    └── ...
```
