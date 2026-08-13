package com.games.lhv;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Basic LHV Connect HTTP + XML utility using Hutool.
 */
public class LhvClient {
    private final String baseUrl;
    private final String clientCode;
    private final String clientCountry;

    public LhvClient(String baseUrl, String clientCode, String clientCountry) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.clientCode = clientCode;
        this.clientCountry = clientCountry;
    }

    public String postXml(String path, Object requestObj) throws Exception {
        String xml = toXml(requestObj);

        HttpRequest req = HttpRequest.post(baseUrl + path)
                .header("Content-Type", "application/xml; charset=utf-8")
                .header("Client-Code", clientCode)
                .header("Client-Country", clientCountry)
                .body(xml, "UTF-8");

        HttpResponse resp = req.execute();
        int status = resp.getStatus();
        String body = resp.body();
        if (status != 200 && status != 202) {
            throw new RuntimeException("HTTP Error: " + status + " body=" + body);
        }
        return body;
    }

    public String get(String path) {
        HttpRequest req = HttpRequest.get(baseUrl + path)
                .header("Client-Code", clientCode)
                .header("Client-Country", clientCountry);
        HttpResponse resp = req.execute();
        if (resp.getStatus() != 200) {
            throw new RuntimeException("HTTP GET Error: " + resp.getStatus() + " body=" + resp.body());
        }
        return resp.body();
    }

    public String toXml(Object obj) throws Exception {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> T fromXml(String xml, Class<T> clazz) throws Exception {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller um = context.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        return (T) um.unmarshal(sr);
    }
}
