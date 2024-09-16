package com.example.support.service;


import com.example.support.config.BotConfig;
import com.example.support.models.Messages;
import com.example.support.repository.MessagesRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private final BotConfig config;
    @Autowired
    private MessagesRepository repository;

    private ArrayList<String> texts = new ArrayList<>(Arrays.asList(
            "Когда волнения исказят твои мысли, помни, что я здесь, чтобы поддержать тебя.",
            "Не бойся показывать свою уязвимость - в твоей силе искренность.",
            "Ты сильная и умная, и ты способна преодолеть любые трудности.",
            "Не забывай, что после каждого дождя вырастает радуга. Ты преодолеешь это!",
            "Позволь себе быть уязвимой - это не слабость, а проявление твоей силы.",
            "Трудные моменты не определяют тебя, они лишь показывают, как сильна ты на самом деле.",
            "Вместе мы сможем пройти через любые бури, потому что мы - команда.",
            "Ты - мое вдохновение своей силой и настойчивостью. Верь в себя так же, как я верю в тебя.",
            "Помни, что после каждой ночи наступает утро. Ты преодолеешь это и снова увидишь свет.",
            "Трудные времена приходят и уходят, а ты останешься стойкой и сильной как всегда.",
            "Не опускай руки, потому что в тебе есть неиссякаемая сила и мудрость.",
            "Ты не одна в этой борьбе - я здесь, чтобы поддержать тебя и помочь преодолеть все сложности.",
            "Позволь себе чувствовать все эмоции, это нормально. Я здесь, чтобы выслушать и поддержать тебя.",
            "Ты - сильная женщина, и я горжусь тем, как ты справляешься с жизненными испытаниями.",
            "Трудности - это просто временные испытания, которые сделают тебя еще сильнее.",
            "Ты способна на многое, и я верю, что ты преодолеешь это с достоинством.",
            "Не сомневайся в себе - ты обладаешь невероятной силой, чтобы пройти через все.",
            "Помни, что после каждого подъема следует спуск. Ты преодолеешь это и снова пойдешь вверх.",
            "Моя поддержка тебе безгранична, как и моя любовь. Мы в этом вместе.",
            "НАХЕР ЭТОГО БОТА, ИДИ СВОЕГО МУЖИКА ПЫТАЙ НА ПОДДЕРЖКУ",
            "Ты не одинока в своих борьбах. Я здесь, чтобы держать тебя за руку и идти вперед вместе.",
            "Не бойся падений - они лишь учат нас стоять еще крепче. Ты справишься!",
            "Трудности - это лишь временные испытания, которые сделают нас только сильнее в итоге.",
            "Ты - моя сила и мое вдохновение. Верь в себя так же сильно, как я верю в тебя.",
            "Ты - цветок, который может выжить в самой суровой зиме. Ты такая сильная и красивая.",
            "Не забывай, что каждый день я здесь, чтобы напомнить тебе, насколько ты удивительна.",
            "Даже в самый серый день ты светишь ярче всех звезд на небе. Ты моя лучезарная радуга.",
            "Твоя улыбка - мой самый любимый вид цветов. Серьезно, ты просто сияешь.",
            "Если бы тебя можно было вырастить, я бы посадил сад из твоей красоты. Ты мое самое драгоценное растение.",
            "Ты - мое солнце в холодном мире, моя звезда в бесконечной вселенной.",
            "Ты такая удивительная, что даже самые красивые цветы завидуют твоей красоте.",
            "Ты - мой источник вдохновения, моя муза, мое волшебство.",
            "Твоя улыбка - как чарующая мелодия, которая наполняет мою жизнь радостью и счастьем.",
            "Ты - мое сокровище, которое я так ценю. Берегу тебя как самое драгоценное.",
            "Твоя доброта и забота о других делают тебя еще более прекрасной. Ты - настоящий ангел на земле.",
            "С тобой я чувствую себя самым счастливым человеком на свете. Ты делаешь мою жизнь сказочной.",
            "Твоя улыбка - мой наркотик. Я просто не могу получить её достаточно.",
            "Когда я смотрю на тебя, я вижу не только красоту, но и доброту, мудрость и силу.",
            "Твоя нежность и забота о людях вокруг тебя - это просто волшебство. Ты - настоящее чудо.",
            "Ты - не только моя любовь, но и моя поддержка, мое утешение и мое всё.",
            "С тобой я чувствую, что могу преодолеть все преграды и покорить мир. Ты мой источник силы.",
            "Ты - мое счастье, мое солнце, моя радость. Благодарю судьбу за то, что ты в моей жизни.",
            "Твоя улыбка - это магия, которая превращает серые будни в праздник. Ты - мое волшебство.",
            "Ты - мой квантовый скачок счастья. С тобой я чувствую, что мир превращается в сказку.",
            "Твоя улыбка - луч света в темном мире. Ты - мой свет во мраке.",
            "Ты - мое утешение в горе, моя радость в печали, моя опора в трудные времена.",
            "Ты - мой вдохновитель, моя муза, мой пламя страсти. Ты разгоняешь темноту своим сиянием.",
            "Твоя красота - это не только внешнее очарование, но и внутреннее сияние. Ты - полна света и тепла.",
            "Ты - моя звезда путеводная, мой компас в жизни, мое руководство в темноте.",
            "С тобой я чувствую, что даже самые невозможные мечты становятся реальностью. Ты - мой волшебный ключ.",
            "Ты - мое сердце, моя душа, мое все. Ты - моя половинка, без которой я не могу быть целым.",
            "Твоя улыбка - мой антидепрессант, который лечит все мои раны и поднимает настроение в любой момент.",
            "Ты - моя звезда удачи, мое счастье, мой талисман. С тобой я верю в чудеса.",
            "Ты - мое утешение в печали, моя радость в радости, моя поддержка в бурю. Ты - мой опорный креп.",
            "Твоя любовь - это нечто большее, чем просто слова. Это океан нежности, который наполняет мое сердце.",
            "Ты - моя муза, мое вдохновение, мое творческое пламя. Ты делаешь мою жизнь яркой и интересной.",
            "С каждым днем я все больше и больше влюбляюсь в тебя. Ты - мой ангел-хранитель на земле.",
            "Твоя красота - это не только внешность, но и внутренний свет. Ты - искренняя, добрая и удивительная."));

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start","начать работу с ботом"));
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(),null));
        }catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText(); 
            long chatID = update.getMessage().getChatId();
            System.out.println(update.getMessage().getChat());
            log.info("Новое сообщение от " + update.getMessage().getChat().getFirstName() + ": " + messageText);
            if(update.getMessage().getChat().getUserName()==null){
                sendMessage(chatID, "Ваш @userName недоступен, добавьте его для дальнейшей работы с ботом\n" +
                        "Для этого идете в Настройки->Выбрать имя пользователя, здесь его и создаете.");
            }
            else if(update.getMessage().getChat().getUserName().equals("mogiloverr") || update.getMessage().getChat().getUserName().equals("boshiro_123") ) {
                switch (messageText) {
                    //БАЗОВЫЙ КЕЙСЫ
                    case "/start":
                            startCommandReceived(chatID);
                        break;
                    case "Поддержка!":
                        sendMessage(chatID,texts.get(new Random().nextInt(texts.size()-1)));
                        break;
                    default:
                        Messages newMessage = new Messages();
                        newMessage.setUserName(update.getMessage().getChat().getUserName());
                        newMessage.setMessage(update.getMessage().getText());
                        repository.save(newMessage);
                        sendMessage(chatID,"Запомнил :D");
                }
            }
        }
    }

    private void startCommandReceived(long chatID){
        String answer = EmojiParser.parseToUnicode("Привет, Любимая!," + " добро пожаловать в этого бота!"
                 +"\nЛичность успешно подтверждена" + ":white_check_mark:\n" +
                "Это такой бот милый поддержки и тд, иногда будут проскакивать шутки комплименты и всякого такого рода штуки.\n" +
                "Так же, этот бот помнит всё, что ты ему отправишь, любые сообщения, так что можешь писать всё что угодно: мечты пожелания хотелки)))\n\n" +
                "P.S. Все текста адресованы тебе от меня, твоего медвежонка, а бот лишь посыльный\n\n" +
                "Для получения поддержки напиши 'Поддержка!' или нажми на такую же кнопочку");
        sendMessage(chatID, answer);
    }


    public void sendMessage(long chatID, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(textToSend);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyBoardsList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Поддержка!");
        keyBoardsList.add(row);
        replyKeyboardMarkup.setKeyboard(keyBoardsList);
        message.setReplyMarkup(replyKeyboardMarkup);
        //Добавление кнопок к клавиатуре(виртуальная клавиатура)
        try{
            execute(message);
        }catch (TelegramApiException e){
            System.out.println(e);
        }
    }


}
