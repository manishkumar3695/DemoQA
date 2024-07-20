package com.qa.demo.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import static com.qa.demo.Utils.ElementUtil.getElement;

public class MyPageStepDef {
    @Given("user is landing on home page")
    public void userIsLandingOnHomePage() {
        //getElement("new").click();
        System.out.println("Hello World");
    }
}
