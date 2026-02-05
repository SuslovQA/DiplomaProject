package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {
    private SelenideElement header = $x("//h2");
    private SelenideElement debetCardPaymentButton = $(byText("Купить"));
    private SelenideElement creditPaymentButton = $(byText("Купить в кредит"));
    private SelenideElement cardImage = $x("//img");
    private SelenideElement cardHeader = $x("//h3[@class='heading heading_size_m heading_theme_alfa-on-white Order_cardHeading__2PyrG']");

    public HomePage() {
        header.shouldBe(visible);
        cardImage.shouldBe(exist);
        cardHeader.isDisplayed();
        debetCardPaymentButton.isEnabled();
        creditPaymentButton.isEnabled();
    }

    public DebetCardPayment debetCardPayment() {
        debetCardPaymentButton.click();
        return new DebetCardPayment();
    }

    public CreditPayment creditPayment() {
        creditPaymentButton.click();
        return new CreditPayment();
    }
}