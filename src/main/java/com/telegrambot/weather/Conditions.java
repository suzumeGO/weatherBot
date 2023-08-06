package com.telegrambot.weather;

public enum Conditions {
    CLEAR("clear", "ясно"), PARTLY_CLOUDY("partly_cloudy","малооблачно"), CLOUDY("cloudy","олачно с прояснениями"),
    OVERCAST("overcast","пасмурно"), LIGHT_RAIN("light-rain","небольшой дождь"),
    RAIN("rain","дождь"), HEAVY_RAIN("heavy-rain","сильный дождь"),
    SHOWERS("showers","ливень"), WET_SNOW("wet-snow","дождь со снегом"), LIGHT_SNOW("light-snow","небольшой снег"),
    SNOW("snow","снег"), SNOW_SHOWERS("snow-showers","снегопад"), HAIL("hail","град"),
    THUNDERSTORM("thunderstorm","гроза"), THUNDERSTORM_WITH_RAIN("thunderstorm-with-rain","дождь с грозой"),
    THUNDERSTORM_WITH_HAUL("thunderstorm-with-hail","гроза с градом");
    private final String value;
    private final String trans;
    Conditions(String value, String trans) {
        this.value = value;
        this.trans = trans;
    }
    public String getValue() {
        return value;
    }
    public String getTrans() {
        return trans;
    }
}
