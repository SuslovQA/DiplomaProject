package data;

import com.github.javafaker.Faker;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));

    public static String validCard = "4444 4444 4444 4441";
    public static String declinedCard = "4444 4444 4444 4442";
    public static String shortCardNumber = "4444 4444 4444 444";

   @Value
    public static class CardInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String owner;
        private String cvv;
    }

    public static String getMonth() {
       return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int plusYears) {
       return LocalDate.now().plusYears(plusYears).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidPatternOfYear() {
       return "0";
    }

    public static String getOwner() {
       return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getCvv() {
       return faker.numerify("###");
    }

    public static String getCurrentMonth() {
       return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getLasMonth() {
       return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static CardInfo getValidPaymentData() {
       return new CardInfo(validCard, getMonth(), getYear(1), getOwner(), getCvv());
    }

    public static CardInfo getValidPaymentDataWithDeclinedCard() {
       return new CardInfo(declinedCard, getMonth(), getYear(2), getOwner(), getCvv());
    }

    public static CardInfo getValidPaymentDataWithInvalidDate() {
       return new CardInfo(validCard, getMonth(), getYear(-1), getOwner(), getCvv());
    }

    public static CardInfo getPaymentDataWithCurrentDate() {
       return new CardInfo(validCard, getCurrentMonth(), getYear(0), getOwner(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyCard() {
           return new CardInfo("", getMonth(), getYear(5), getOwner(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyMonth() {
        return new CardInfo(validCard, "", getYear(4), getOwner(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyYear() {
        return new CardInfo(validCard, getMonth(), "", getOwner(), getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyOwner() {
        return new CardInfo(validCard, getMonth(), getYear(3),"", getCvv());
    }

    public static CardInfo getPaymentDataWithEmptyCvv() {
        return new CardInfo(validCard, getMonth(), getYear(5), getOwner(), "");
    }

    public static CardInfo getPaymentDataWithAllEmptyFields() {
        return new CardInfo("", "", "", "", "");
    }

    public static CardInfo getValidCardAndMonthAndEmptyOthers() {
       return new CardInfo(validCard, getMonth(), "", "", "");
    }

    public static CardInfo getPaymentDataWithShortCardNumber() {
        return new CardInfo(shortCardNumber, getMonth(), getYear(4), getOwner(), getCvv());
    }

    public static CardInfo getPaymentDataWithInvalidPatternOfYear() {
        return new CardInfo(declinedCard, getMonth(), getInvalidPatternOfYear(), getOwner(), getCvv());
    }
}

