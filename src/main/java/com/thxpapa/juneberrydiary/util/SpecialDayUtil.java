package com.thxpapa.juneberrydiary.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thxpapa.juneberrydiary.dto.cal.CalRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SpecialDayUtil {
    @Value("${special.api.url}")
    private String specialUrl;
    @Value("${datago.api.ekey}")
    private String eKey;

    public List<CalRequestDto.SpecialDayDto> getHoliDeInfo(String solYear) {
        try {
            HttpURLConnection conn = null;
            JsonParser jsonParser = new JsonParser();
            ObjectMapper objectMapper = new ObjectMapper();

            StringBuilder urlBuilder = new StringBuilder(specialUrl + "/openapi/service/SpcdeInfoService/getHoliDeInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + eKey);
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(solYear, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("200", "UTF-8"));

            conn = getConnection(urlBuilder.toString());
            if (conn == null) return null;

            String line;
            BufferedReader br = null;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                while((line = br.readLine())!=null) {
                    sb.append(line);
                }

                XmlMapper xmlMapper = new XmlMapper();
                JsonNode jsonNode = xmlMapper.readTree(sb.toString());

                String jsonString = objectMapper.writeValueAsString(jsonNode);

                JsonObject json = (JsonObject) jsonParser.parse(jsonString);
                JsonArray list = ((JsonObject) json.get("body")).getAsJsonObject("items").getAsJsonArray("item");

                return Arrays.asList(new ObjectMapper().readValue(list.toString(), CalRequestDto.SpecialDayDto[].class));
            }
            return null;
        } catch(Exception e) {
            log.error("getHoliDeInfo error occurred!");
            return null;
        }
    }

    public List<CalRequestDto.SpecialDayDto> getAnniversaryInfo(String solYear) {
        try {
            HttpURLConnection conn = null;
            JsonParser jsonParser = new JsonParser();
            ObjectMapper objectMapper = new ObjectMapper();

            StringBuilder urlBuilder = new StringBuilder(specialUrl + "/openapi/service/SpcdeInfoService/getAnniversaryInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + eKey);
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(solYear, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("200", "UTF-8"));

            conn = getConnection(urlBuilder.toString());
            if (conn == null) return null;

            String line;
            BufferedReader br = null;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                while((line = br.readLine())!=null) {
                    sb.append(line);
                }

                XmlMapper xmlMapper = new XmlMapper();
                JsonNode jsonNode = xmlMapper.readTree(sb.toString());

                String jsonString = objectMapper.writeValueAsString(jsonNode);

                JsonObject json = (JsonObject) jsonParser.parse(jsonString);
                JsonArray list = ((JsonObject) json.get("body")).getAsJsonObject("items").getAsJsonArray("item");

                return Arrays.asList(new ObjectMapper().readValue(list.toString(), CalRequestDto.SpecialDayDto[].class));
            }
            return null;
        } catch(Exception e) {
            log.error("getAnniversaryInfo error occurred!");
            return null;
        }
    }

    public List<CalRequestDto.SpecialDayDto> get24DivisionsInfo(String solYear) {
        try {
            HttpURLConnection conn = null;
            JsonParser jsonParser = new JsonParser();
            ObjectMapper objectMapper = new ObjectMapper();

            StringBuilder urlBuilder = new StringBuilder(specialUrl + "/openapi/service/SpcdeInfoService/get24DivisionsInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + eKey);
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(solYear, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("200", "UTF-8"));

            conn = getConnection(urlBuilder.toString());
            if (conn == null) return null;

            String line;
            BufferedReader br = null;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                while((line = br.readLine())!=null) {
                    sb.append(line);
                }

                XmlMapper xmlMapper = new XmlMapper();
                JsonNode jsonNode = xmlMapper.readTree(sb.toString());

                String jsonString = objectMapper.writeValueAsString(jsonNode);

                JsonObject json = (JsonObject) jsonParser.parse(jsonString);
                JsonArray list = ((JsonObject) json.get("body")).getAsJsonObject("items").getAsJsonArray("item");

                return Arrays.asList(new ObjectMapper().readValue(list.toString(), CalRequestDto.SpecialDayDto[].class));
            }
            return null;
        } catch(Exception e) {
            log.error("get24DivisionInfo error occurred!");
            return null;
        }
    }

    private HttpURLConnection getConnection(String link) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (Exception e) {
            return null;
        }
    }
}
