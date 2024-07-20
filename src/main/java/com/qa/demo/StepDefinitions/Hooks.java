package com.qa.demo.StepDefinitions;

import com.qa.demo.Utils.DriverUtil;

import static com.qa.demo.Utils.DriverUtil.getDriver;
import static com.qa.demo.Utils.FrameworkUtil.*;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.*;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class Hooks {
    private ThreadLocal<Integer> cucumberStepIndex = new ThreadLocal<>();

    @BeforeStep
    public void setUpStep(Scenario scenario) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("Hooks BeforeStep");
        Field f = scenario.getClass().getDeclaredField("delegate");
        f.setAccessible(true);
        TestCaseState sc = (TestCaseState) f.get(scenario);
        Field f1 = sc.getClass().getDeclaredField("testCase");
        f1.setAccessible(true);
        TestCase testCase = (TestCase) f1.get(sc);

        List<PickleStepTestStep> testSteps = testCase.getTestSteps()
                .stream().filter(x -> x instanceof PickleStepTestStep)
                .map(x -> (PickleStepTestStep) x).collect(Collectors.toList());


        System.out.println(testSteps.get(cucumberStepIndex.get()).getStep().getKeyword() + testSteps.get(cucumberStepIndex.get()).getStep().getText());


    }

    @AfterStep
    public void tearDownStep() {
        cucumberStepIndex.set(cucumberStepIndex.get() + 1);
    }

    @Before(order = 1)
    public void setup(Scenario scenario) {
        logger.info(scenario.getName());

        DriverUtil.getInstance();
        getDriver().get(getConfig("URL"));

        cucumberStepIndex.set(0);
    }
    @After
    public void quit(Scenario scenario) {
        getDriver().quit();
    }
}
