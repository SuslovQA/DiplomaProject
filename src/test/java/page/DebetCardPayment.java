package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper.CardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;


public class DebetCardPayment {
    private SelenideElement heading = $(byText("Оплата по карте"));
    private SelenideElement cardNumberField = $x("//input[@placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $x("//input[@placeholder='08']");
    private SelenideElement yearField = $x("//input[@placeholder='22']");
    private SelenideElement cardOwnerField = $x("//div[3]//input[not(@placeholder='999')]");
    private SelenideElement cvvField = $x("//input[@placeholder='999']");
    private SelenideElement continueButton = $x("//form//div[4]//button");
    private SelenideElement amount = $x("//li[4]");
    private SelenideElement successMassage = $x("//div[contains(@class, 'notification_status_ok')]//div[@class='notification__content']");


    public DebetCardPayment() {
        heading.shouldBe(visible);
        cardNumberField.isDisplayed();
        monthField.isDisplayed();
        yearField.isDisplayed();
        cardOwnerField.isDisplayed();
        cvvField.isDisplayed();
        continueButton.isEnabled();
    }

    public void CardInfo(CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        cardOwnerField.setValue(cardInfo.getOwner());
        cvvField.setValue(cardInfo.getCvv());
        continueButton.click();
    }

    public void debetCardSuccessMassage(String expectedText) {
        successMassage.shouldHave(text(expectedText), Duration.ofSeconds(10)).shouldBe(visible);
    }

    public String getAmountFromPage() {
      String amountText = amount.getText();
        String[] amountArray = amountText.split(" ");
        return amountArray[1] + amountArray[2];
    }
}
