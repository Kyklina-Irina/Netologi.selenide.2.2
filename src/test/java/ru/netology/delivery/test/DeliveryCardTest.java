package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.timeout = 15000; // 15 секунд на ожидание элементов
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully deliver card with valid data")
    void shouldSuccessfullyDeliverCard() {
        // 1. Подготовка данных
        String city = "Москва";
        String name = "Иванов Иван";
        String phone = "+79001234567";
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String expectedNotificationText = "Встреча успешно забронирована на " + date;

        // 2. Заполнение формы
        $("[data-test-id='city'] input").setValue(city);

        // Очистка и ввод даты
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();

        // 3. Отправка формы
        $$("button").find(Condition.text("Забронировать")).click();

        // 4. Проверка результата
        $(".notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText(expectedNotificationText));
    }
}