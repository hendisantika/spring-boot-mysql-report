# Spring Boot MySQL Report - Setup Notes

## Summary

Successfully configured the application with the latest compatible libraries and Java 17.

## Final Configuration

### Dependencies

- **Spring Boot**: 3.5.7 (Latest)
- **JasperReports**: 6.17.0 (Stable with Java 17)
- **Java**: 17 (LTS version, recommended for JasperReports)
- **MySQL Connector**: Latest from Spring Boot
- **Apache POI**: 5.5.0 (Latest)
- **iText**: 2.1.7 (Required for JasperReports 6.x PDF export)

### Key Configuration Files

#### jasperreports.properties

Located in `src/main/resources/jasperreports.properties`:

```properties
# Disable all XML validation to bypass SAX parser issues
org.xml.sax.driver=
net.sf.jasperreports.jrxml.schema.resource=
net.sf.jasperreports.jrxml.validation.enabled=false
```

This configuration is **critical** - it disables XML schema validation which causes SAX parser errors with Java 17+.

### JRXML Compilation

To compile JRXML files to .jasper format, use Java 17:

```bash
JAVA_HOME=/path/to/java-17 mvn compile
```

The .jasper file is already compiled and included in `src/main/resources/reports/car_list.jasper`.

### Running the Application

With Java 17:

```bash
JAVA_HOME=/path/to/java-17 mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Verified Endpoints

All endpoints tested and working:

- ✓ GET `http://localhost:8080/report/pdf` - PDF report (HTTP 200)
- ✓ GET `http://localhost:8080/report/excel` - Excel inline (HTTP 200)
- ✓ GET `http://localhost:8080/report/excel2` - Excel download (HTTP 200)
- ✓ GET `http://localhost:8080/report/excel3` - Excel alt format (HTTP 200)

## Important Notes

### Why Java 17 instead of Java 21/25?

Java 21 and 25 have stricter SAX parser property ordering requirements that cause
JasperReports to fail with "SAXNotSupportedException" during JRXML compilation.
Java 17 LTS provides the best compatibility with JasperReports while still supporting
all Spring Boot 3.x features.

### Why JasperReports 6.17.0 instead of 7.0.3?

JasperReports 7.0.3 has fundamental XML parsing incompatibilities with modern Java's
security restrictions. Version 6.17.0 is stable, well-tested, and fully functional
with Java 17 when XML validation is disabled via jasperreports.properties.

### iText Version

JasperReports 6.x requires iText 2.1.7 (com.lowagie:itext) for PDF export.
Later versions (4.x+) have different package structures and licensing (AGPL).

## Troubleshooting

If you encounter PDF generation errors:

- Ensure iText version is 2.1.7
- Verify jasperreports.properties is in src/main/resources

If JRXML compilation fails:

- Use Java 17 (not 21 or 25)
- Ensure jasperreports.properties exists with validation disabled
- Pre-compiled .jasper file is already included if needed
