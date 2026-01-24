package test;

import data.DataHelper;
import data.SQLHelper;
import lombok.val;
import org.junit.jupiter.api.*;
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
    void shouldSuccessPaymentWithCurrentDate() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithCurrentDate();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Баг ")
    void shouldDisplayErrorWithDeclinedCard() {
        val debetCardpayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentDataWithDeclinedCard();

        debetCardpayment.CardInfo(paymentData);
        debetCardpayment.debetCardErrorMassage("Ошибка! Банк отказал в проведении операции.");

        val expected = "DECLINED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Баг ")
    void shouldDisplayErrorWithEmptyCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCard();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг ")
    void shouldDisplayErrorWithEmptyMonth() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyMonth();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг ")
    void shouldDisplayErrorWithEmptyYear() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyYear();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    void shouldDisplayErrorWithEmptyOwner() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyOwner();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг")
    void shouldDisplayErrorWithEmptyCvv() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCvv();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг")
    void shouldDisplayErrorWithAllEmptyFields() {
        val countOfEmptyFields = 5;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithAllEmptyFields();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeEmptyFields(countOfEmptyFields);
    }

    @Test
    void shouldDisplayErrorWithInvalidDate() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentDataWithInvalidDate();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidDate();
    }

    @Test
    @DisplayName("Баг Pairwise 1")
    void shouldDisplayErrorWithEmptyFields() {
        val countOfEmptyFields = 3;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidCardAndMonthAndEmptyOthers();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeEmptyFields(countOfEmptyFields);
    }

    @Test
    @DisplayName("Pairwise 2")
    void shouldDisplayErrorWithShortNumberCard() {
        val countOfFields = 1;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithShortCardNumber();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeInvalidParameters(countOfFields);
    }

    @Test
    @DisplayName("Pairwise 3")
    void shouldDisplayErrorWithDeclinedCardAndInvalidYear() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithOneZeroInYear();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг 2 нуля в месяце")
    void shouldDisplayErrorWithTwoZeroesInMont() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentDaya = DataHelper.getPaymentDataWithTwoZeroesInMonth();

        debetCardPayment.CardInfo(paymentDaya);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг с кириллицей в cardHolder")
    void shouldDisplayErrorWithCyrillicInCardHolder() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithCyrillicInCardHolder();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Кредит Success")
    void shouldSuccessCreditRequest() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidPaymentData();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }





//    Номер	Год	Месяц	Вдалелец	Cvv
////    Валид	 	01
////    Валид	29	12	Rand	000
////    Невалид	 	12	Rand Rand	777
////    Невалид	29	05	Rand Rand
//    Невалид	22	05	 	000
//    Невалид	22	01	Rand	777
//            29	05	Rand	777
//            22	01	Rand Rand
// 	22	12	Rand Rand	000
//            05	 	777
//    Валид	22	12	 	777
//    Валид	22	05	Rand
//    Валид	 	05	Rand Rand	000
//    Валид	29	01	Rand Rand	777


//    Приложение в собственной СУБД должно сохранять информацию о том, успешно ли был совершён платёж и каким способом.
//    Данные карт при этом сохранять не допускается.
//    Заявлена поддержка двух СУБД. Вы должны это проверить:
//MySQL;
//PostgreSQL.

}
