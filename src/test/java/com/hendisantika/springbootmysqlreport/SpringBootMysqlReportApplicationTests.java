package com.hendisantika.springbootmysqlreport;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootMysqlReportApplicationTests {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    public void contextLoads() {
    }

    private void assertReportGenerates(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isNotEmpty();
    }

    @Test
    public void pdfReportGenerates() throws Exception {
        assertReportGenerates("/report/pdf");
    }

    @Test
    public void excelReportGenerates() throws Exception {
        assertReportGenerates("/report/excel");
    }

    @Test
    public void excel2ReportGenerates() throws Exception {
        assertReportGenerates("/report/excel2");
    }

    @Test
    public void excel3ReportGenerates() throws Exception {
        assertReportGenerates("/report/excel3");
    }

}
