package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import page.HomePage;


import static com.codeborne.selenide.Selenide.open;

public class TourOfDayTest {
    HomePage homePage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        homePage = open("http://localhost:8080", HomePage.class);
        // SQLHelper.clearTables();
    }

    //Payment Gate

    @Test
    @DisplayName("1.1 Заполнение валидными данными, Payment Gate (позитивный сценарий)")
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
    @DisplayName("1.2 Сравнение суммы оплаты на странице (в UI) и списанной суммы с карты (в DB), Payment Gate (позитивный сценарий)")
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
    @DisplayName("1.3 Заполнение валидными данными с текущей датой, Payment Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithCurrentDatePayInfoDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithCurrentDate();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.4 Заполнение валидными данными (срок действия карты '8' лет от текущей даты),  Payment Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithCardValidityPeriodEightYearsDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEightYearsCardValidityPeriod();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.5 Отображение отклонения платежа с картой 'DECLINED',  Payment Gate (позитивный сценарий)")
    void shouldDisplayDeclinedMassageWithDeclinedCardDebetCard() {
        val debetCardpayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentDataWithDeclinedCard();

        debetCardpayment.CardInfo(paymentData);
        debetCardpayment.debetCardDeclineMassage("Ошибка! Банк отказал в проведении операции.");

        val expected = "DECLINED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.6 Ввод данных с заполнением поля 'Владелец' строчными (маленькими) буквами,  Payment Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithLowerCaseValueInCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithLowerCaseValueInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.7 Ввод данных с заполнением поля 'Владелец' прописными (большими) буквами,  Payment Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithUpperCaseValueInCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithUpperCaseValueInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("2.1 Ввод данных с пустым полем 'Номер карты', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithEmptyCardNumberFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCard();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("2.2 Ввод данных с коротким номером карты (11 символов) в поле 'Номер карты', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithShortNumberCardDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithShortCardNumber();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("3.1 Ввод данных с пустым полем 'Месяц', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithEmptyMonthFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyMonth();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("3.2 Ввод данных с заполнением поля 'Месяц' значением '00', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithTwoZeroesInMonthFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentDaya = DataHelper.getPaymentDataWithTwoZeroesInMonthField();

        debetCardPayment.CardInfo(paymentDaya);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("3.3 Ввод данных с заполнением поля 'Месяц' значением от 13 до 99, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithInvalidMonthDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithMonthMoreThanValidValue();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidMonth();
    }

    @Test
    @DisplayName("4.1 Ввод данных с пустым полем 'Год', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithEmptyYearFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyYear();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("4.2 Ввод данных с заполнением поля 'Год' значением 'прошлый год от текущего', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithInvalidDateDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentDataWithInvalidDate();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidDate();
    }

    @Test
    @DisplayName("4.3 Ввод данных с заполнением поля 'Год' значением '0', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithTwoZeroesInYearFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithOneZeroInYearField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("4.4 Ввод данных с заполнением поля 'Год' случайным значением от '55' до '99', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithInvalidValueInYearFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithInvalidValueInYearField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("5.1 Ввод данных с пустым полем 'Владелец', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithEmptyCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCardHolder();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("5.2 Ввод данных с заполнением поля 'Владелец' значением на кириллице, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithCyrillicInCardHolderDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithCyrillicInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("5.3 Ввод данных с заполнением поля 'Владелец' значением с символами, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithSymbolsInCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithSymbolsInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("5.4 Ввод данных с заполнением поля 'Владелец' числовым значением, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithNumberInCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithNumberInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("6.1 Ввод данных с пустым полем 'CVC/CVV', Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithEmptyCvvFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCvv();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("6.2 Ввод данных с заполнениме поля 'CVC/CVV' значением '0', Payment Gate (негативный сценарий)")
    void shouldDisplayErrorWithZeroInCvvFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithZeroInCvv();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("6.3 Ввод данных с заполнением поля 'CVC/CVV' занчением с из двух чисел, Payment Gate (негативный сценарий)")
    void shouldDisplayErrorWithInvalidValueInCvvFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithInvalidValueInCvvField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("7.1 Отправка формы со всеми пустыми полями, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithAllEmptyFieldsDebetCard() {
        val countOfEmptyFields = 5;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithAllEmptyFields();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeEmptyFields(countOfEmptyFields);
    }

    @Test
    @DisplayName("7.2 Ввод значения '0' во все поля, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithAllInvalidParametersDebetCard() {
        val countOfFields = 5;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithAllInvalidParameters();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeInvalidParameters(countOfFields);
    }

    @Test
    @DisplayName("8.1 Ввод валидных данных в поля 'Номер Карты', 'Месяц', 'Год', остальные поля не заполнены, Payment Gate (негативынй сценарий)")
    void shouldDisplayErrorWithEmptyFieldsDebetCard() {
        val countOfEmptyFields = 3;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidCardMonthYearAndEmptyOthers();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeEmptyFields(countOfEmptyFields);
    }

    //Credit

    @Test
    @DisplayName("1.1 Заполнение валидными данными, Credit Gate (позитивный сценарий)")
    void shouldSuccessCreditRequest() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidPaymentData();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.2 Заполнение валидными данными с текущей датой, Credit Gate (позитивный сценарий)")
    void shouldSuccessWithCurrentDatePayInfoCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithCurrentDate();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.3 Заполнение валидными данными (срок действия карты '8' лет от текущей даты), Credit Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithCardValidityPeriodEightYearsCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEightYearsCardValidityPeriod();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.4 Отображение отклонения платежа с картой 'DECLINED', Credit Gate (позитивный сценарий)")
    void shouldDisplayErrorWithDeclinedCardCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidPaymentDataWithDeclinedCard();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditDeclinedMassage("Ошибка! Банк отказал в проведении операции.");

        val expected = "DECLINED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.5 Ввод данных с заполнением поля 'Владелец' строчными (маленькими) буквами,  Credit Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithLowerCaseValueInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithLowerCaseValueInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("1.6 Ввод данных с заполнением поля 'Владелец' рпрописными (большими) буквами,  Credit Gate (позитивный сценарий)")
    void shouldSuccessPaymentWithUpperCaseValueInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithUpperCaseValueInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("2.1 Ввод данных с пустым полем 'Номер карты', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithEmptyCardFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCard();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("2.2 Ввод данных с коротким номером карты (11 символов) в поле 'Номер карты', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithShortCardNumberCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithShortCardNumber();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("3.1 Ввод данных с пустым полем 'Месяц', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithEmptyMonthFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyMonth();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("3.2 Ввод данных с заполнением поля 'Месяц' значением '00', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithInvalidParameterMontCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithTwoZeroesInMonthField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("3.2 Ввод данных с заполнением поля 'Месяц' значением от 13 до 99, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithInvalidMonthCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithMonthMoreThanValidValue();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidMonth();
    }

    @Test
    @DisplayName("4.1 Ввод данных с пустым полем  'Год', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithEmptyYearFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyYear();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("4.2 Ввод данных с заполнением поля 'Год' значением 'прошлый год от текущего', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithInvalidDateCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidPaymentDataWithInvalidDate();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidDate();
    }

    @Test
    @DisplayName("4.3 Ввод данных с заполнением поля 'Год' значением '0', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithZeroInYearFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithOneZeroInYearField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("4.4 Ввод данных с заполнением поля 'Год' случайным значением от '55' до '99', Credit Gate (негативынй сценарий)")
    void shouldDisplayErrorWithInvalidValueInYearFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithInvalidValueInYearField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("5.1 Ввод данных с пустым полем 'Владелец', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithEmptyCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCardHolder();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("5.2 Ввод данных с заполнением поля 'Владелец' значением на кириллице, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithCyrillicInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithCyrillicInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("5.3 Ввод данных в поле 'Владелец' значением с символами, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithSymbolInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithSymbolsInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("5.4 Ввод данных в поле 'Владелец' числовым значением, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithNumberInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithNumberInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("6.1 Ввод данных с пустым полем 'CVC/CVV'")
    void shouldDisplayErrorWithEmptyCvvFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCvv();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("6.2 Ввод данных с заполнениме поля 'CVC/CVV' значением '0', Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithZeroInCvvFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithZeroInCvv();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("6.3 Ввод данных с заполнением поля 'CVC/CVV' занчением с из двух чисел, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithInvalidValueInCvvFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithInvalidValueInCvvField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("7.1 Отправка формы со всеми пустыми полями, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithAllEmptyFieldsCredit() {
        val countEmptyFields = 5;
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithAllEmptyFields();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithSomeEmptyFields(countEmptyFields);
    }

    @Test
    @DisplayName("7.2 Ввод значения '0' во все поля, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithAllInvalidParametersCredit() {
        val countOfFields = 5;
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithAllInvalidParameters();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithSomeInvalidParameters(countOfFields);
    }

    @Test
    @DisplayName("8.1 Ввод валидных данных в поля 'Номер Карты', 'Месяц', 'Год', остальные поля не заполнены, Credit Gate (негативный сценарий)")
    void shouldDisplayErrorWithEmptyFieldsCredit() {
        val countOfEmptyFields = 2;
        val creditPaymentCardPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidCardMonthYearAndEmptyOthers();

        creditPaymentCardPayment.CardInfo(paymentData);
        creditPaymentCardPayment.creditErrorMassageWithSomeInvalidParameters(countOfEmptyFields);
    }
}