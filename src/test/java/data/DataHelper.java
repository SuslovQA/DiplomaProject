package data;

import com.github.javafaker.Faker;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRu = new Faker(new Locale("ru"));

    public static String validCard = "4444 4444 4444 4441";
    public static String declinedCard = "4444 4444 4444 4442";
    public static String shortCardNumber = "4444 4444 4444 444";

    @Value
    public static class CardInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String cardHolder;
        private String cvv;
    }

    public static String getMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int plusYears) {
        return LocalDate.now().plusYears(plusYears).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getCardHolder() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getCardHolderInCyrillic() {
        return fakerRu.name().lastName() + " " + fakerRu.name().firstName();
    }

    public static String getCvv() {
        return faker.numerify("###");
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getInvalidMonth() {
        return String.valueOf(faker.number().numberBetween(13, 99));
    }

    public static String getInvalidYear() {
        return String.valueOf(faker.number().numberBetween(55, 99));
    }

    public static String getInvalidCvv() {
        return faker.numerify("##");
    }

    public static String getZero() {
        return "0";
    }

    public static String getSymbols() {
        List<String> symbols = Stream.of("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+",
                        "`", "~", ",", ".", "<", ">", "{", "}", "[", "]")
                .collect(Collectors.toList());

        int randomValue = faker.number().numberBetween(0, symbols.size());

        return symbols.get(randomValue) + symbols.get(randomValue) + symbols.get(randomValue) + symbols.get(randomValue);
    }

    public static CardInfo getValidPaymentData() {
        return new CardInfo(validCard, getMonth(), getYear(1), getCardHolder(), getCvv());
    }

    public static CardInfo getValidPaymentDataWithDeclinedCard() {
        return new CardInfo(declinedCard, getMonth(), getYear(2), getCardHolder(), getCvv());
    }

    public static CardInfo getValidPaymentDataWithInvalidDate() {
        return new CardInfo(validCard, getMonth(), getYear(-1), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithCurrentDate() {
        return new CardInfo(validCard, getCurrentMonth(), getYear(0), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyCard() {
        return new CardInfo("", getMonth(), getYear(5), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyMonth() {
        return new CardInfo(validCard, "", getYear(4), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyYear() {
        return new CardInfo(validCard, getMonth(), "", getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyCardHolder() {
        return new CardInfo(validCard, getMonth(), getYear(3), "", getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyCvv() {
        return new CardInfo(validCard, getMonth(), getYear(5), getCardHolder(), "");
    }

    public static CardInfo getPaymentDataWithZeroInCvv() {
        return new CardInfo(validCard, getMonth(), getYear(4), getCardHolder(), getZero());
    }

    public static CardInfo getPaymentDataWithInvalidValueInCvvField() {
        return new CardInfo(declinedCard, getMonth(), getYear(2), getCardHolder(), getInvalidCvv());
    }

    public static CardInfo getPaymentDataWithAllEmptyFields() {
        return new CardInfo("", "", "", "", "");
    }

    public static CardInfo getValidCardMonthYearAndEmptyOthers() {
        return new CardInfo(validCard, getMonth(), getYear(1), "", "");
    }

    public static CardInfo getPaymentDataWithShortCardNumber() {
        return new CardInfo(shortCardNumber, getMonth(), getYear(4), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithAllInvalidParameters() {
        return new CardInfo(getZero(), getZero(), getZero(), getZero(), getZero());
    }

    public static CardInfo getPaymentDataWithOneZeroInYearField() {
        return new CardInfo(validCard, getMonth(), getZero(), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithInvalidValueInYearField() {
        return new CardInfo(declinedCard, getMonth(), getInvalidYear(), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithTwoZeroesInMonthField() {
        return new CardInfo(validCard, "00", getYear(3), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithCyrillicInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(2), getCardHolderInCyrillic(), getCvv());
    }

    public static CardInfo getPaymentDataWithMonthMoreThanValidValue() {
        return new CardInfo(validCard, getInvalidMonth(), getYear(5), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithSymbolsInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(4), getSymbols(), getCvv());
    }

    public static CardInfo getPaymentDataWithEightYearsCardValidityPeriod() {
        return new CardInfo(validCard, getMonth(), getYear(8), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithNumberInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(1), faker.numerify("#### #####"), getCvv());
    }

    public static CardInfo getPaymentDataWithLowerCaseValueInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(2), getCardHolder().toLowerCase(), getCvv());
    }

    public static CardInfo getPaymentDataWithUpperCaseValueInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(2), getCardHolder().toUpperCase(), getCvv());
    }
}