package com.hendisantika.springbootmysqlreport.controller;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
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
public class PdfController {

    @Autowired
    ApplicationContext context;

    @GetMapping(path = "pdf/{jrxml}")
    @ResponseBody
    public void getPdf(@PathVariable String jrxml, HttpServletResponse response) throws Exception {
        //Get JRXML template from resources folder
        Resource resource = context.getResource("classpath:jasperreports/" + jrxml + ".jrxml");
        //Compile to jasperReport
        InputStream inputStream = resource.getInputStream();
        JasperReport report = JasperCompileManager.compileReport(inputStream);
        //Parameters Set
        Map<String, Object> params = new HashMap<>();

        //Data source Set
        JRDataSource dataSource = new JREmptyDataSource();
        //Make jasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        //Media Type
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        //Export PDF Stream
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
