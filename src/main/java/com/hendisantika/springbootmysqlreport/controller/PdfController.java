package com.hendisantika.springbootmysqlreport.controller;

import com.hendisantika.springbootmysqlreport.domain.Car;
import com.hendisantika.springbootmysqlreport.repository.CarRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-mysql-report
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 25/02/18
 * Time: 19.17
 * To change this template use File | Settings | File Templates.
 */

@RestController
@RequestMapping("/report")
public class PdfController {

//    private Logger logger = LogManager.getLogManager(PdfController.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CarRepository carRepository;

    //    @GetMapping(path = "pdf/{jrxml}")
    @GetMapping(path = "/pdf")
    @ResponseBody
//    public void getPdf(@PathVariable String jrxml, HttpServletResponse response) throws Exception {
    public void getPdf(HttpServletResponse response) throws Exception {
        //Load pre-compiled jasper report
        Resource resource = context.getResource("classpath:reports/car_list.jasper");
        InputStream inputStream = resource.getInputStream();
        JasperReport report = (JasperReport) JRLoader.loadObject(inputStream);

        //Parameters Set
        Map<String, Object> params = new HashMap<>();

        List<Car> cars = (List<Car>) carRepository.findAll();

        //Data source Set
        JRDataSource dataSource = new JRBeanCollectionDataSource(cars);
        params.put("datasource", dataSource);

        //Make jasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        //Media Type
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        //Export PDF Stream
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    @GetMapping(path = "/excel")
    @ResponseBody
    public void getExcel(HttpServletResponse response) throws Exception {
        //Load pre-compiled jasper report
        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/car_list.jasper");
        JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);

        Map<String, Object> params = new HashMap<>();

        List<Car> cars = (List<Car>) carRepository.findAll();

        //Data source Set
        JRDataSource dataSource = new JRBeanCollectionDataSource(cars);
        params.put("datasource", dataSource);

        //Make jasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "inline; filename=car_list.xlsx");

        final OutputStream ops = response.getOutputStream();

        //Export Excel Stream using modern API
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ops));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setWhitePageBackground(false);
        xlsxExporter.setConfiguration(configuration);

        xlsxExporter.exportReport();
    }

    @GetMapping(path = "/excel2")
    @ResponseBody
    private void getDownloadReportXlsx(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();

            List<Car> cars = (List<Car>) carRepository.findAll();

            //Data source Set
            JRDataSource dataSource = new JRBeanCollectionDataSource(cars);
            params.put("datasource", dataSource);

            //Load pre-compiled jasper report
            InputStream jasperStream = this.getClass().getResourceAsStream("/reports/car_list.jasper");
            JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);

            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true);
            configuration.setWhitePageBackground(false);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            xlsxExporter.setConfiguration(configuration);

            xlsxExporter.exportReport();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=car_list.xlsx");

            response.getOutputStream().write(os.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            response.flushBuffer();
        } catch (JRException ex) {
            System.out.println("Error : " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        }
    }

    @GetMapping(path = "/excel3")
    @ResponseBody
    private void getDownloadReportXls(HttpServletResponse response) {
        try {
            //Load pre-compiled jasper report
            InputStream jasperStream = this.getClass().getResourceAsStream("/reports/car_list.jasper");
            JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);

            Map<String, Object> params = new HashMap<>();
            List<Car> cars = (List<Car>) carRepository.findAll();

            //Data source Set
            JRDataSource dataSource = new JRBeanCollectionDataSource(cars);
            params.put("datasource", dataSource);

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=car_list.xlsx");

            final OutputStream ops = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setWhitePageBackground(false);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setDetectCellType(true);
            xlsxExporter.setConfiguration(configuration);

            xlsxExporter.exportReport();
            ops.write(byteArrayOutputStream.toByteArray());
            ops.flush();
            ops.close();

        } catch (JRException ex) {
            System.out.println("Error : " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        }
    }
}
