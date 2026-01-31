package data;

import com.github.javafaker.Faker;
import lombok.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public static String getSymbol() {
        List<String> sym = Stream.of("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", "`", "~", ",", ".", "<", ">", "{", "}", "[", "]")
                .collect(Collectors.toList());

        return sym.get(faker.number().numberBetween(0, sym.size()));
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

    public static CardInfo getPaymentDataWithAllEmptyFields() {
        return new CardInfo("", "", "", "", "");
    }

    public static CardInfo getValidCardMonthYearAndEmptyOthers() {
        return new CardInfo(validCard, getMonth(), getYear(1), "", "");
    }

    public static CardInfo getPaymentDataWithShortCardNumber() {
        return new CardInfo(shortCardNumber, getMonth(), getYear(4), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithOneZeroInYear() {
        return new CardInfo(declinedCard, getMonth(), "0", getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithTwoZeroesInMonth() {
        return new CardInfo(validCard, "00", getYear(3), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithCyrillicInCardHolder() {
        return new CardInfo(validCard, getMonth(), getYear(2), getCardHolderInCyrillic(), getCvv());
    }

    public static CardInfo getPaymentDataWithMonthMoreThanValid() {
        return new CardInfo(validCard, getInvalidMonth(), getYear(5), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithSymbolInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(4), getSymbol(), getCvv());
    }

    public static CardInfo getPaymentDataWithCardValidityPeriodEightYears() {
        return new CardInfo(validCard, getMonth(), getYear(8), getCardHolder(), getCvv());
    }

    public static CardInfo getPaymentDataWithNumberInCardHolderField() {
        return new CardInfo(validCard, getMonth(), getYear(1), faker.numerify("#### #####"), getCvv());
    }
}

