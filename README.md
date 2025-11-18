# Spring Boot MySQL Report

A Spring Boot application that demonstrates generating PDF and Excel reports from MySQL database using JasperReports.

## Features

- Generate PDF reports from database data
- Export data to Excel (XLSX) format
- Multiple Excel export endpoints with different configurations
- Pre-compiled JasperReports templates for better performance
- REST API endpoints for report generation

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 25 (Jakarta EE)
- **JasperReports**: 6.20.6
- **MySQL**: 9.4
- **Apache POI**: 5.5.0
- **Maven**: Build tool

## Prerequisites

- Java 21+ (tested with Java 25)
- MySQL Server 5.7+
- Maven 3.6+

## Database Configuration

The application connects to MySQL with the following default settings (configured in
`src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/carDB?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=create-drop
```

The database and tables will be created automatically on startup, and sample data will be inserted.

## Build & Run

### Build the project

```bash
mvn clean install
```

This will:

1. Clean the target directory
2. Compile JasperReports templates (.jrxml → .jasper)
3. Compile Java source code
4. Package the application

### Run the application

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/spring-boot-mysql-report-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Basic Endpoint

- **GET** `http://localhost:8080/` - Hello World endpoint

### Report Endpoints

#### PDF Report

- **GET** `http://localhost:8080/report/pdf`
- Generates a PDF report of all cars in the database
- Content-Type: `application/pdf`

#### Excel Reports

1. **GET** `http://localhost:8080/report/excel`
    - Inline Excel export
    - Content-Disposition: `inline; filename=car_list.xlsx`

2. **GET** `http://localhost:8080/report/excel2`
    - Downloadable Excel export with enhanced formatting
    - Content-Disposition: `attachment; filename=car_list.xlsx`
    - Includes: empty space removal, cell type detection

3. **GET** `http://localhost:8080/report/excel3`
    - Alternative Excel export configuration
    - Content-Disposition: `attachment; filename=car_list.xlsx`

## Sample Data

The application automatically populates the database with sample car data on startup:

- Ford Modeo (Red, Diesel, 2013)
- Alfa Romeo Spider (Black, B98, 2016)
- Mercedes-Benz 180 (Silver, Diesel, 2011)
- Mercedes-Benz A (Red, A95, 2017)
- Audi A3 (Black, A95, 2014)
- Toyota Auris (Black, A95, 2013)
- Toyota Avensis (White, Diesel, 2015)
- Nissan Micra (Silver, A95, 2015)
- Nissan X-Trail (Blue, Diesel, 2016)

## Project Structure

```
spring-boot-mysql-report/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hendisantika/springbootmysqlreport/
│   │   │       ├── SpringBootMysqlReportApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── IndexController.java
│   │   │       │   └── PdfController.java
│   │   │       ├── domain/
│   │   │       │   └── Car.java
│   │   │       └── repository/
│   │   │           └── CarRepository.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── reports/
│   │           ├── car_list.jrxml (source template)
│   │           └── car_list.jasper (compiled template)
│   └── test/
│       └── java/
│           └── com/hendisantika/springbootmysqlreport/
│               └── SpringBootMysqlReportApplicationTests.java
└── pom.xml
```

## Key Updates & Fixes

### Spring Boot 3.x Migration

- Migrated from `javax.*` to `jakarta.*` packages
- Updated JPA annotations to Jakarta Persistence
- Updated Servlet API to Jakarta Servlet
- Migrated from JUnit 4 to JUnit 5

### JasperReports Optimization

- Added Maven plugin to pre-compile JRXML templates at build time
- Using JasperReports 6.20.6 for better Java 25 compatibility
- Controller methods now load pre-compiled `.jasper` files instead of runtime compilation
- Modernized JasperReports API:
    - Replaced deprecated `JRXlsExporter` with `JRXlsxExporter`
    - Using `SimpleExporterInput` and `SimpleOutputStreamExporterOutput`
    - Using `SimpleXlsxReportConfiguration` for export configuration

## Screenshots

### PDF Report

![PDF Report](img/pdf.png "PDF Report")

### Excel Report

![XLS Report](img/xls.png "XLS report")

## Testing the Application

### Using curl

```bash
# Test the basic endpoint
curl http://localhost:8080/

# Download PDF report
curl http://localhost:8080/report/pdf -o car_report.pdf

# Download Excel report
curl http://localhost:8080/report/excel -o car_report.xlsx
curl http://localhost:8080/report/excel2 -o car_report2.xlsx
curl http://localhost:8080/report/excel3 -o car_report3.xlsx
```

### Using a browser

Simply navigate to any of the endpoints listed above in your web browser.

## Troubleshooting

### Port 8080 already in use

If you get an error that port 8080 is already in use, either:

- Stop the process using port 8080
- Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

### MySQL Connection Issues

Ensure MySQL is running and accessible with the credentials specified in `application.properties`. You can test the
connection:

```bash
mysql -u root -p
```

### Build Issues

If you encounter build issues, try:

```bash
mvn clean install -U
```

The `-U` flag forces Maven to update dependencies.

## License

This project is for demonstration purposes.

## Author

- **Hendi Santika**
- Email: hendisantika@gmail.com
- Telegram: @hendisantika34
