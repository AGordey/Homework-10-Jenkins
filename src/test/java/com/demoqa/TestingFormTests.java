package com.demoqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class TestingFormTests {
    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }


    @Test
    @DisplayName("Registration form")
    void fillFormTest() {
        step("Open form on site", () -> {
            open("/automation-practice-form");
            executeJavaScript("$('footer').remove()");
            executeJavaScript("$('#fixedban').remove()");
        });
        step("Fill form ", () -> {
            $("#firstName").setValue("OurFirstName");
            $("#lastName").setValue("OurLastName");
            $("#userEmail").setValue("OurEmail@email.com");
            $("#userNumber").setValue("1234567890");
            $("#currentAddress").setValue("OurCurrentAddress");

            //Поле с радиобаттоном - выбор пола
            $("#genterWrapper").$(byText("Male")).click();

            //Дата рождения
            $("#dateOfBirthInput").click();
            $(".react-datepicker__year-select").selectOption("2000");
            $(".react-datepicker__month-select").selectOption("January");
            $(".react-datepicker__day.react-datepicker__day--019").click();

            //Поле предметов - выпадающее меню
            $("#subjectsInput").setValue("Hindi").pressEnter();

            //Выбор хобби
            $("#hobbiesWrapper").$(byText("Reading")).click();

            //Выбор города
            $("#stateCity-wrapper").click();
            $(byText("Haryana")).click();
            $("#city").click();
            $(byText("Karnal")).click();

            //Зaгрузка файла
            $("#uploadPicture").uploadFromClasspath("1.png");

            //Кнопка Submit
            $("#submit").click();
        });
        step("Verify form ", () -> {
            //Проверка введенных данных
            $(".modal-body").shouldHave
                    (text("OurFirstName"),
                            text("OurLastName"),
                            text("OurEmail@email.com"),
                            text("1234567890"),
                            text("Male"),
                            text("Hindi"),
                            text("Reading"), text("Haryana"),
                            text("Karnal"),
                            text("1.png"),
                            text("19 January,2000"),
                            text("OurCurrentAddress"));
        });
    }
    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}
