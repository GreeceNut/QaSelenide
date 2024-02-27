package ru.netology.service;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CallBackTest {
    //зараннее прописываем логику для даты
    private String date(int addDays, String pattern) {
     /* LocalDate.now — возврат даты системы
        plusDays — добавляет к дате н-ое количество дней
        format — выставляет определенный формат даты, в данном случае это dd.MM.yyyy
      */
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldPost() {
        //загружаем страницу
        open("http://localhost:9999");

        //ищем эелементы
        SelenideElement form = $("form");

        //взаимодействуем с элементами
        form.$("[data-test-id=city] input").setValue("Москва");
        //Удаляем текст в поле Дата
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        //Создаем переменную даты не раннее трех дней с текущей даты
        String dateMeet = date(3, "dd.MM.yyyy");
        form.$("[data-test-id=date] input").setValue(dateMeet);

        form.$("[data-test-id=name] input").setValue("Василий Мамин-Сибиряк");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();

        $("[data-test-id=notification].notification_visible")
                .shouldBe(exist, Duration.ofSeconds(15));

    }

    @Test
    void shouldPostWithCititesList() {
        //загружаем страницу
        open("http://localhost:9999");

        //ищем эелементы
        SelenideElement form = $("form");

        //взаимодействуем с элементами

        //Вводим в поле Город Мо
        form.$("[data-test-id=city] input").setValue("Мо");
        //Ищем коллекцию города, а затем ищем нужный город, в данном случаем Москва, и кликаем
        $$(".menu-item").findBy(Condition.text("Москва")).click();

        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String dateMeet = date(3, "dd.MM.yyyy");
        form.$("[data-test-id=date] input").setValue(dateMeet);
        form.$("[data-test-id=name] input").setValue("Василий Мамин-Сибиряк");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();

        $("[data-test-id=notification].notification_visible")
                .shouldBe(exist, Duration.ofSeconds(15));

    }

    @Test
    void shouldPostWithCalendar() {
        //загружаем страницу
        open("http://localhost:9999");

        //ищем эелементы
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");


        String dateMeet = date(3, "dd.MM.yyyy");
        //Кликаем на календарь
        form.$("[data-test-id=date] input").click();
        //сравниваем первую доступную дату с датой данной по условию
        if (!dateMeet.equals(date(7, "dd.MM.yyyy"))) {
            //если месяцы расходятся, то нажимаем на стрелку месяца
            $("[data-step='1']").click();
        }
        $$("[data-day]").findBy(Condition.text(date(7, "dd"))).click();

        form.$("[data-test-id=name] input").setValue("Василий Мамин-Сибиряк");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();

        $("[data-test-id=notification].notification_visible")
                .shouldBe(exist, Duration.ofSeconds(15));

    }
}
