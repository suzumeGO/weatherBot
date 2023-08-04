package com.telegrambot.weather.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
@JsonRootName(value = "")
public class Forecast {
    @JsonProperty(value = "now")
    String now;
    @JsonProperty(value = "now_dt")
    String dateAndTime;
    @JsonProperty(value = "temp")
    String temp;
    @JsonProperty(value = "condition")
    String condition;
    @JsonProperty(value = "info")
    Info info;
    @JsonProperty(value = "fact")
    Fact fact;
    @JsonProperty(value = "forecasts")
    List<Forecasts> forecasts;

    public static class Info {
        @JsonProperty(value = "n")
        boolean n;
        @JsonProperty(value = "geoid")
        int geoid;
        @JsonProperty(value = "lan")
        double lat;
        @JsonProperty(value = "lon")
        double lon;
        @JsonProperty(value = "tzinfo")
        Tzinfo tzinfo;
        @JsonProperty(value = "def_pressure_mm")
        int defPressureMm;
        @JsonProperty(value = "def_pressure_pa")
        int defPressurePa;
        @JsonProperty(value = "url")
        String url;
        public static class Tzinfo {
            @JsonProperty(value = "offset")
            long offset;
            @JsonProperty(value = "name")
            String name;
            @JsonProperty(value = "abbr")
            String abbr;
            @JsonProperty(value = "dst")
            boolean dst;
        }
    }
    public static class Fact {
        @JsonProperty(value = "temp")
        short temp;
        @JsonProperty(value = "feels_like")
        short feelsLike;
        @JsonProperty(value = "icon")
        String icon;
        @JsonProperty(value = "condition")
        String condition;
        @JsonProperty(value = "wind_speed")
        double windSpeed;
        @JsonProperty(value = "wind_gust")
        double windGust;
        @JsonProperty(value = "wind_dir")
        String windDir;
        @JsonProperty(value = "pressure_mm")
        int pressureMm;
        @JsonProperty(value = "pressure_pa")
        int pressurePa;
        @JsonProperty(value = "humidity")
        int humidity;
        @JsonProperty(value = "daytime")
        char daytime;
        @JsonProperty(value = "polar")
        boolean polar;
        @JsonProperty(value = "season")
        String season;
        @JsonProperty(value = "prec_type")
        int precType;
        @JsonProperty(value = "prec_strength")
        double precStrength;
        @JsonProperty(value = "is_thunder")
        boolean isThunder;
        @JsonProperty(value = "cloudness")
        double cloudness;
        @JsonProperty(value = "obs_time")
        String obsTime;
        @JsonProperty(value = "phenom_icon")
        String phenomIcon;
        @JsonProperty(value = "phenom_condition")
        String phenomCondition;
    }

    public static class Forecasts {
        String date;
        String dateTs;
        int week;
        String sunrise;
        String sunset;
        short moonCode;
        String moonText;
        Parts parts;
        List<Hours> hours;
        public static class Hours {
            int hour;
            String hourTs;
            int temp;
            int feelsLike;
            String icon;
            String condition;
            double windSpeed;
            int windGust;
            String windDir;
            int pressureMm;
            int pressurePa;
            int humidity;
            int precMm;
            int precPeriod;
            double precType;
            double precStrength;
            boolean isThunder;
            double cloudness;
        }

        public static class Parts {
            Night night;
            Day day;
            Evening evening;
            Morning morning;
            DayShort dayShort;
            NightShort nightShort;

            public static class Night {
                int tempMin;
                int tempMax;
                int tempAvg;
                int feelsLike;
                String icon;
                String condition;
                char daytime;
                boolean polar;
                double windSpeed;
                double windGust;
                String windDir;
                int pressureMm;
                int pressurePa;
                int humidity;
                int precMm;
                int precPeriod;
                double precStrength;
                double cloudness;
            }

            public static class Evening {
                int tempMin;
                int tempMax;
                int tempAvg;
                int feelsLike;
                String icon;
                String condition;
                char daytime;
                boolean polar;
                double windSpeed;
                double windGust;
                String windDir;
                int pressureMm;
                int pressurePa;
                int humidity;
                int precMm;
                int precPeriod;
                double precStrength;
                double cloudness;
            }

            public static class Day {
                int tempMin;
                int tempMax;
                int tempAvg;
                int feelsLike;
                String icon;
                String condition;
                char daytime;
                boolean polar;
                double windSpeed;
                double windGust;
                String windDir;
                int pressureMm;
                int pressurePa;
                int humidity;
                int precMm;
                int precPeriod;
                double precStrength;
                double cloudness;
            }

            public static class Morning {
                int tempMin;
                int tempMax;
                int tempAvg;
                int feelsLike;
                String icon;
                String condition;
                char daytime;
                boolean polar;
                double windSpeed;
                double windGust;
                String windDir;
                int pressureMm;
                int pressurePa;
                int humidity;
                int precMm;
                int precPeriod;
                double precStrength;
                double cloudness;
            }

            public static class DayShort {
                int tempMin;
                int tempMax;
                int tempAvg;
                int feelsLike;
                String icon;
                String condition;
                char daytime;
                boolean polar;
                double windSpeed;
                double windGust;
                String windDir;
                int pressureMm;
                int pressurePa;
                int humidity;
                int precMm;
                int precPeriod;
                double precStrength;
                double cloudness;
            }

            public static class NightShort {
                int tempMin;
                int tempMax;
                int tempAvg;
                int feelsLike;
                String icon;
                String condition;
                char daytime;
                boolean polar;
                double windSpeed;
                double windGust;
                String windDir;
                int pressureMm;
                int pressurePa;
                int humidity;
                int precMm;
                int precPeriod;
                double precStrength;
                double cloudness;
            }
        }
    }
}
