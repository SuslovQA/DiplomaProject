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
    @DisplayName("Дебетовая карта успешная оплата")
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
    @DisplayName("Дебетовая карта - сравнение списанной суммы с карты и суммы на странице оплаты в UI")
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
    @DisplayName("Дебетовая карта - успешная оплата с текущей датой")
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
    @DisplayName("Баг Дебетовая карта - срок действия карты 8 лет")
    void shouldSuccessPaymentWithCardValidityPeriodEightYearsDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithCardValidityPeriodEightYears();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getPaymentStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Баг Дебетовая карта - Отображение отклонения платеже с картой DECLINED")
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
    @DisplayName("Баг Дебетовая карта - с пустым полем 'Номер карты'")
    void shouldDisplayErrorWithEmptyCardNumberFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCard();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - с пустым полем 'Месяц'")
    void shouldDisplayErrorWithEmptyMonthFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyMonth();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - с пустым полем 'Год'")
    void shouldDisplayErrorWithEmptyYearFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyYear();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Дебетовая карта - с пустым полем 'Держатель'")
    void shouldDisplayErrorWithEmptyCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCardHolder();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("2 бага Дебетовая карта - с пустым полем 'CVV'")
    void shouldDisplayErrorWithEmptyCvvFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCvv();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - со всеми пустыми полями")
    void shouldDisplayErrorWithAllEmptyFieldsDebetCard() {
        val countOfEmptyFields = 5;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithAllEmptyFields();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeEmptyFields(countOfEmptyFields);
    }

    @Test
    @DisplayName("Дебетовая карта - невалидная дата (прошлый год от текущего)")
    void shouldDisplayErrorWithInvalidDateDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidPaymentDataWithInvalidDate();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidDate();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - Карта, месяц, год валиные, остальные пустые")
    void shouldDisplayErrorWithEmptyFieldsDebetCard() {
        val countOfEmptyFields = 3;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getValidCardMonthYearAndEmptyOthers();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeEmptyFields(countOfEmptyFields);
    }

    @Test
    @DisplayName("Дебетовая ката - короткий нмер карты (11 символов)")
    void shouldDisplayErrorWithShortNumberCardDebetCard() {
        val countOfFields = 1;
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithShortCardNumber();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithSomeInvalidParameters(countOfFields);
    }

    @Test
    @DisplayName("Дебетовая карта - Declined карта и невалтдный год")
    void shouldDisplayErrorWithDeclinedCardAndInvalidYearDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithOneZeroInYear();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - 2 нуля в месяце")
    void shouldDisplayErrorWithTwoZeroesInMonthFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentDaya = DataHelper.getPaymentDataWithTwoZeroesInMonth();

        debetCardPayment.CardInfo(paymentDaya);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - кириллица в cardHolder")
    void shouldDisplayErrorWithCyrillicInCardHolderDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithCyrillicInCardHolder();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Деетовая карта - в поле 'Месяц' значение от 13 до 99")
    void shouldDisplayErrorWithInvalidMonthDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithMonthMoreThanValid();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidMonth();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - сивол в поле 'Держатель карты'")
    void shouldDisplayErrorWithSymbolInCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithSymbolInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг Дебетовая карта - цифры в поле 'Держатель карты'")
    void shouldDisplayErrorWithNumberInCardHolderFieldDebetCard() {
        val debetCardPayment = homePage.debetCardPayment();
        val paymentData = DataHelper.getPaymentDataWithNumberInCardHolderField();

        debetCardPayment.CardInfo(paymentData);
        debetCardPayment.debetCardErrorMassageWithInvalidParameter();
    }


    //Credit
    @Test
    @DisplayName("Кредит - успешная оплата")
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
    @DisplayName("Кредит - успешная оплата с текущей датой")
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
    @DisplayName("Баг Кредит - карта Declined")
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
    @DisplayName("Баг Кредит - срок действия карты 8 лет")
    void shouldSuccessPaymentWithCardValidityPeriodEightYearsCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithCardValidityPeriodEightYears();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditSuccessMassage("Операция одобрена Банком.");

        val expected = "APPROVED";
        val actual = SQLHelper.getCreditStatus();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Баг Кредит - пустая карта")
    void shouldDisplayErrorWithEmptyCardFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCard();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг Кредит - пустой месяц")
    void shouldDisplayErrorWithEmptyMonthFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyMonth();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг Кредит - пустой год")
    void shouldDisplayErrorWithEmptyYearFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyYear();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Кредит - с пустым полем 'Держатель'")
    void shouldDisplayErrorWithEmptyCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCardHolder();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("2 Бага Кредит - пустое поле 'CVV'")
    void shouldDisplayErrorWithEmptyCvvFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithEmptyCvv();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithEmptyField();
    }

    @Test
    @DisplayName("Баг Кредит - все поля пустые")
    void shouldDisplayErrorWithAllEmptyFields() {
        val countEmptyFields = 5;
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithAllEmptyFields();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithSomeEmptyFields(countEmptyFields);
    }

    @Test
    @DisplayName("Кредит - короткий номер карты (11 символов)")
    void shouldDisplayErrorWithShortCardNumberCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithShortCardNumber();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Кредит - истекший год")
    void shouldDisplayErrorWithInvalidDateCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getValidPaymentDataWithInvalidDate();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidDate();
    }

    @Test
    @DisplayName("Кредит - один ноль в поле 'Год'")
    void shouldDisplayErrorWithZeroInYearFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithOneZeroInYear();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг Кредит - два ноля в поле 'Месяц'")
    void shouldDisplayErrorWithInvalidParameterMontCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithTwoZeroesInMonth();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг Кредит - кириллица в поле 'Держатель'")
    void shouldDisplayErrorWithCyrillicInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithCyrillicInCardHolder();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Кредит - в поле 'Месяц' значение от 13 до 99")
    void shouldDisplayErrorWithInvalidMonthCredit() {
        val creitPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithMonthMoreThanValid();

        creitPayment.CardInfo(paymentData);
        creitPayment.creditErrorMassageWithInvalidMonth();
    }

    @Test
    @DisplayName("Баг Кредит - сивол в поле 'Держатель карты'")
    void shouldDisplayErrorWithSymbolInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithSymbolInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }

    @Test
    @DisplayName("Баг Кредит - цифры в поле 'Держатель карты'")
    void shouldDisplayErrorWithNumberInCardHolderFieldCredit() {
        val creditPayment = homePage.creditPayment();
        val paymentData = DataHelper.getPaymentDataWithNumberInCardHolderField();

        creditPayment.CardInfo(paymentData);
        creditPayment.creditErrorMassageWithInvalidParameter();
    }
}
