package noraui.application.steps.demo;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import noraui.application.page.Page;
import noraui.application.page.demo.DemoPage;
import noraui.application.steps.Step;
import noraui.cucumber.annotation.Conditioned;
import noraui.exception.FailureException;
import noraui.exception.Result;
import noraui.exception.TechnicalException;
import noraui.gherkin.GherkinStepCondition;
import noraui.utils.Messages;

public class DemoSteps extends Step {

    private DemoPage demoPage;

    public DemoSteps() throws TechnicalException {
        super();
        this.demoPage = (DemoPage) Page.getInstance(DemoPage.class);
    }

    @Then("I update checkboxes and check radio list 'DEMO_HOME(.*)' with '(.*)':")
    public void selectCheckbox(String elementKey, String valueKey, Map<String, Boolean> values) throws TechnicalException, FailureException {
        selectCheckbox(demoPage.getPageElementByKey(elementKey), true);
        selectCheckbox(demoPage.getPageElementByKey(elementKey), valueKey, values);
        checkRadioList(demoPage.getPageElementByKey(elementKey), valueKey);
    }

    @And("I check message '(.*)' on alert")
    public void check(String message) throws TechnicalException, FailureException {
        ((JavascriptExecutor) getDriver()).executeScript("console.log('test');");
        String msg = getLastConsoleAlertMessage();
        if (msg == null || !msg.equals(message)) {
            new Result.Failure<>(message, Messages.format(Messages.FAIL_MESSAGE_NOT_FOUND_ON_ALERT, message), false, demoPage.getCallBack());
        }
    }

    /**
     * Update a html input text with a text in a DEMO page.
     *
     * @param elementName
     *            Is target element
     * @param text
     *            Is the new data (text)
     * @throws TechnicalException
     *             is thrown if you have a technical error (format, configuration, datas, ...) in NoraUi.
     *             Exception with {@value noraui.utils.Messages#FAIL_MESSAGE_ERROR_ON_INPUT} message (with screenshot, no exception)
     * @throws FailureException
     */
    @When("I update text 'DEMO_HOME(.*)' with '(.*)'")
    public void updateText(String elementName, String text) throws TechnicalException, FailureException {
        clearText(demoPage.getPageElementByKey(elementName));
        updateText(demoPage.getPageElementByKey(elementName), text);
        checkMandatoryTextField(demoPage.getPageElementByKey(elementName));
        String value = readValueTextField(demoPage.getPageElementByKey(elementName));
        if (!text.equals(value)) {
            new Result.Failure<>(value, Messages.format(Messages.FAIL_MESSAGE_WRONG_EXPECTED_VALUE, demoPage.getPageElementByKey(elementName), text, demoPage.getApplication()), true,
                    demoPage.getCallBack());
        }
        saveElementValue(elementName, demoPage);
    }

    /**
     * Update a html select input text with a text data (if exist in html "option" list).
     *
     * @param elementName
     *            Is target element
     * @param text
     *            Is the new data
     * @throws TechnicalException
     *             is thrown if you have a technical error (format, configuration, datas, ...) in NoraUi.
     *             Exception with {@value noraui.utils.Messages#FAIL_MESSAGE_ERROR_ON_INPUT} message (with screenshot, no exception)
     * @throws FailureException
     */
    @When("I update select list 'DEMO_HOME(.*)' with '(.*)'")
    public void updateList(String elementName, String text) throws TechnicalException, FailureException {
        updateList(demoPage.getPageElementByKey(elementName), text);
        checkTextSelectedInList(demoPage.getPageElementByKey(elementName), text);
    }

    /**
     * Click on html element using Javascript if all 'expected' parameters equals 'actual' parameters in conditions.
     *
     * @param page
     *            The concerned page of toClick
     * @param toClick
     *            html element
     * @param conditions
     *            list of 'expected' values condition and 'actual' values ({@link noraui.gherkin.GherkinStepCondition}).
     * @throws TechnicalException
     *             is thrown if you have a technical error (format, configuration, datas, ...) in NoraUi.
     *             Exception with {@value noraui.utils.Messages#FAIL_MESSAGE_UNABLE_TO_OPEN_ON_CLICK} message (with screenshot, with exception)
     * @throws FailureException
     *             if the scenario encounters a functional error
     */
    @Conditioned
    @When("I click by js on xpath '(.*)' from '(.*)' page[\\.|\\?]")
    public void clickOnByJs(String xpath, String page, List<GherkinStepCondition> conditions) throws TechnicalException, FailureException {
        loggerStep.debug("clickOnByJs with xpath " + xpath + " on " + page + " page");
        clickOnByJs(Page.getInstance(page), xpath);
    }

}
