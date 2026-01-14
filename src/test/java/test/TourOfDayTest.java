package test;

import data.DataHelper;
import data.SQLHelper;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.HomePage;


import static com.codeborne.selenide.Selenide.open;

public class TourOfDayTest {
    HomePage homePage;

//    @BeforeAll
//    static void setUpAll() {
//        WebDriverManager.chromedriver().setup();
//    }

    @BeforeEach
    void setUp() {
        homePage = open("http://localhost:8080", HomePage.class);

    }

    @AfterAll
    static void clearDB() {
        SQLHelper.clearTables();
    }

    @Test
    void shouldSuccessPaymentWithDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentData();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }


    @Test
    void shouldMatchPaymentAmountOnPageWithAmountInDB() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentData();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = SQLHelper.getAmountFromDb();
        val actual = debetCardPayment.getAmountFromPage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldSuccessPaymentInCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidPaymentData();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }
}
