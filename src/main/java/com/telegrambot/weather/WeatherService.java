package com.telegrambot.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegrambot.weather.DTO.Forecast;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class WeatherService {

    private String getData(double lat, double lon, String key) throws IOException {
        return Request.Get("https://api.weather.yandex.ru/v2/forecast?lat=" + lat + "&" + "lon=" + lon + "&" +
                        "lang=" + "ru_RU")
                .setHeader("X-Yandex-API-Key", key)
                .connectTimeout(60000)
                .execute().returnContent().asString(StandardCharsets.UTF_8);
    }

    public Forecast getCurrentWeather(double lat, double lon, String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(getData(lat, lon, key), Forecast.class);
    }

}
