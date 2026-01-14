package data;

import com.github.javafaker.Faker;
import lombok.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));

    public static String validCard = "4444 4444 4444 4441";
    public static String invalidCard = "4444 4444 4444 4442";

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

    public static String getOwner() {
       return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getCvv() {
       return faker.numerify("###");
    }

    public static CardInfo getValidPaymentData() {
       return new CardInfo(validCard, getMonth(), getYear(1), getOwner(), getCvv());
    }

    public static CardInfo getValidDataWithDeclinedCard() {
       return new CardInfo(invalidCard, getMonth(), getYear(2), getOwner(), getCvv());
    }
}

