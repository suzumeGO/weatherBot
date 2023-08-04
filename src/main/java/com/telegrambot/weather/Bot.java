package com.telegrambot.weather;

import com.telegrambot.weather.DTO.Forecast;
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
import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("deprecation, unused")
@Slf4j
public class Bot extends TelegramLongPollingBot implements BotCommands {
    final BotConfig config;
    Map<Double, Double> location = new HashMap<>();
    @Autowired
    WeatherService service;
    @Autowired
    public Bot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
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
        } else if (update.hasMessage() && update.getMessage().hasLocation()){
            log.info(update.getMessage().getLocation().getLatitude() + " " +
                    update.getMessage().getLocation().getLongitude());
            setLocation(update.getMessage());
        }
    }

    private void answerUtils(String messageText, long chatId) {
        switch (messageText) {
            case "/start" -> startCommandHandler(chatId);
            case "/help" -> helpCommandHandler(chatId);

            default -> log.info("Unexpected message");
        }
    }

    private void helpCommandHandler(long chatId){
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

    private void setLocation(Message message) {
        location.put(message.getLocation().getLatitude(),
                message.getLocation().getLongitude());
    }

    private void startCommandHandler(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if (location == null) {
            message.setText("Укажите вашу геолокацию");
        } else {
            message.setText("Прогноз погоды:");
            try {
                Forecast forecast = service.getCurrentWeather(location.keySet().stream().findFirst().get(),
                        location.get(location.keySet().stream().findFirst().get()), config.getApiToken());
                message.setText("Дата и время: " + forecast.getDateAndTime() +
                        "Температура воздуха: "+ forecast.getTemp() + "Погодные условия: "+
                        forecast.getCondition());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
