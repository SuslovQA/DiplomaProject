package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CreditPayment {
    private SelenideElement heading = $(byText("Кредит по данным карты"));
    private SelenideElement cardNumberField = $x("//input[@placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $x("//input[@placeholder='08']");
    private SelenideElement yearField = $x("//input[@placeholder='22']");
    private SelenideElement cardOwnerField = $x("//div[3]//input[not(@placeholder='999')]");
    private SelenideElement cvvField = $x("//input[@placeholder='999']");
    private SelenideElement continueButton = $x("//form//div[4]//button");
    private SelenideElement successMassage = $x("//div[contains(@class, 'notification_status_ok')]//div[@class='notification__content']");
    private SelenideElement declineMassage = $x("//div[contains(@class, 'notification_status_error')]//div[@class='notification__content']");
    private SelenideElement invalidDateError = $x("//span[contains(text(), 'Истёк срок действия карты')]");
    private SelenideElement invalidParameterError = $x("//span[contains(text(), 'Неверный формат')]");
    private SelenideElement emptyFieldError = $x("//span[contains(text(), 'Поле обязательно для заполнения')]");
    private SelenideElement invalidMonthError = $x("//span[contains(text(), 'Неверно указан срок действия карты')]");
    private ElementsCollection someEmptyFieldsError = $$x("//span[@class='input__sub']");


    public CreditPayment() {
        heading.shouldBe(visible);
        cardNumberField.isDisplayed();
        monthField.isDisplayed();
        yearField.isDisplayed();
        cardOwnerField.isDisplayed();
        cvvField.isDisplayed();
        continueButton.isEnabled();
    }

    public void CardInfo(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        cardOwnerField.setValue(cardInfo.getCardHolder());
        cvvField.setValue(cardInfo.getCvv());
        continueButton.click();
    }

    public void creditSuccessMassage(String expectedText) {
        successMassage.shouldHave(text(expectedText), Duration.ofSeconds(10)).shouldBe(visible);
    }

    public void creditDeclinedMassage(String expectedText) {
        declineMassage.shouldHave(text(expectedText), Duration.ofSeconds(10)).shouldBe(visible);
    }

    public void creditErrorMassageWithInvalidParameter() {
        invalidParameterError.shouldBe(visible);
        emptyFieldError.shouldNotBe();
    }

    public void creditErrorMassageWithSomeInvalidParameters(int expectedSize) {
        invalidParameterError.shouldBe(visible);
        emptyFieldError.shouldNotBe(visible);
    }

    public void creditErrorMassageWithEmptyField() {
        emptyFieldError.shouldBe(visible);
        invalidParameterError.shouldNotBe(visible);
    }

    public void creditErrorMassageWithSomeEmptyFields(int expectedSize) {
        someEmptyFieldsError.filterBy(text(emptyFieldError.getText())).shouldHave(size(expectedSize));
        emptyFieldError.shouldNotBe(visible);
    }

    public void creditErrorMassageWithInvalidDate() {
        invalidDateError.shouldBe(visible);
        emptyFieldError.shouldNotBe(visible);
    }

    public void creditErrorMassageWithInvalidMonth() {
        invalidMonthError.shouldBe(visible);
        emptyFieldError.shouldNotBe(visible);
    }
}
