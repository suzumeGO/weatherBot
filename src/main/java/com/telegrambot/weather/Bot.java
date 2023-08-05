package com.telegrambot.weather;

import com.telegrambot.weather.DTO.Forecast;
import com.telegrambot.weather.annotations.BotCommandHandler;
import com.telegrambot.weather.components.BotCommands;
import com.telegrambot.weather.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("deprecation, unused")
@Slf4j
public class Bot extends TelegramLongPollingBot implements BotCommands {
    final BotConfig config;
    Map<Long, UserLocation> location = new HashMap<>();
    @Autowired
    WeatherService service;

    @Autowired
    public Bot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId;
        String receivedMessage;
        if (update.hasMessage() && update.getMessage().hasText()) {
            receivedMessage = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            answerUtils(receivedMessage, chatId);
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            receivedMessage = update.getCallbackQuery().getData();
            answerUtils(receivedMessage, chatId);
        } else if (update.hasMessage() && update.getMessage().hasLocation()) {
            log.info(update.getMessage().getLocation().getLatitude() + " " +
                    update.getMessage().getLocation().getLongitude());
            chatId = update.getMessage().getChatId();
            setLocation(update.getMessage(), chatId);
        }
    }

    private void answerUtils(String messageText, long chatId) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(BotCommandHandler.class)) {
                BotCommandHandler commandHandler = method.getAnnotation(BotCommandHandler.class);
                if (commandHandler.value().equals(messageText)) {
                    try {
                        method.invoke(this, chatId);
                        return;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        //throw UnknownCommandException();
        sendErrorMessage(messageText, chatId);
    }

    private void sendErrorMessage(String messageText, long chatId) {
        SendMessage errMsg = new SendMessage();
        errMsg.setText("Ошибка. Неизвестная команда");
        errMsg.setChatId(chatId);
        try {
            execute(errMsg);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        log.info("Unexpected message " + messageText);
    }

    private void setLocation(Message message, long chatId) {
        location.put(chatId, new UserLocation(message.getLocation().getLatitude(),
                message.getLocation().getLongitude()));
        log.info("Location saved");
    }

    @BotCommandHandler(value = "/help")
    private void helpCommandHandler(long chatId) {
        log.info("help command was used");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(BotCommands.HELP_TEXT);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @BotCommandHandler(value = "/start")
    private void startCommandHandler(long chatId) {
        log.info("start command was used");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if (!location.containsKey(chatId)) {
            message.setText("Укажите вашу геолокацию");
        } else {
            formWeatherMessage(chatId, message);
        }
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void formWeatherMessage(long chatId, SendMessage message) {
        Forecast forecast = null;
        try {
            for (Map.Entry<Long, UserLocation> entry : location.entrySet()) {
                if (entry.getKey() == chatId) {
                    forecast = service.getCurrentWeather(
                            entry.getValue().lat,
                            entry.getValue().lon,
                            config.getApiToken());
                } else {
                    log.info("Location not set");
                    return;
                }
            }
            printForecast(message, forecast);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void printForecast(SendMessage message, Forecast forecast) {
        message.setText("Текущая погода в " + forecast.getGeoObject().getFullGeo() + " на " +
                String.valueOf(LocalDateTime.ofEpochSecond(forecast.getNow(), 0, ZonedDateTime.now().getOffset())).replace('T', ' ') + "\n" +
                "Температура воздуха: " + forecast.getFact().getTemp() +
                "(Ощущается как " + forecast.getFact().getFeelsLike() + ")" + "\n" +
                castCondition(forecast.getFact().getCondition()));
    }

    private static String castCondition(String condition){
        String res = "";
        Conditions[] conditions = Conditions.values();
        for (Conditions cond : conditions) {
            if (cond.getValue().equals(condition)){
                res =  cond.getTrans();
            }
        }
        return res;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    public static class UserLocation {
        public UserLocation(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
        double lat;
        double lon;
    }
}
