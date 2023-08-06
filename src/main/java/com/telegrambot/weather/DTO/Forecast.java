package com.telegrambot.weather.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.Optional;

@Data
@SuppressWarnings("unused")
@JsonRootName(value = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {
    @JsonProperty(value = "now")
    long now;
    @JsonProperty(value = "fact")
    Fact fact;
    @JsonProperty(value = "geo_object")
    GeoObject geoObject;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fact {
        @JsonProperty(value = "temp")
        int temp;
        @JsonProperty(value = "feels_like")
        int feelsLike;
        @JsonProperty(value = "condition")
        String condition;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoObject {
        @JsonProperty(value = "country")
        Country country;
        @JsonProperty(value = "province")
        Province province;
        @JsonProperty(value = "locality")
        Locality locality;
        @JsonProperty(value = "district")
        District district;

        public String getFullGeo() {
            return Optional.ofNullable(country.name).orElse("") + ", " +
                    Optional.ofNullable(province.name).orElse("") + ", " +
                    Optional.ofNullable(locality.name).orElse("") + ", " +
                    Optional.ofNullable(district.name).orElse("");
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Country {
            @JsonProperty(value = "name")
            String name;
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Province {
            @JsonProperty(value = "name")
            String name;
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public static class Locality {
            @JsonProperty(value = "name")
            String name;
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class District {
            @JsonProperty(value = "name")
            String name;
        }
    }
}
