package com.qa.demo.Runners;

import io.cucumber.testng.*;
import org.testng.annotations.*;
import static com.qa.demo.Utils.FrameworkUtil.logger;

@CucumberOptions(
        features = {"src/main/resources/featureFiles"},
        glue = {"com.qa.demo.StepDefinitions"},
        tags = "@Manish",
        dryRun = false,
        monochrome = true
)

public class CucumberRunner extends AbstractTestNGCucumberTests {
    private TestNGCucumberRunner testngCucumber;

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Test BeforeSuite");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        System.out.println("Test BeforeClass");
        this.testngCucumber = new TestNGCucumberRunner(this.getClass());
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        System.out.println("Test BeforeMethod");
    }

    @Test(groups = {"cucumber"},
            dataProvider = "sce",
            description = "Runs Cucumber Scenarios"
    )
    public void run(PickleWrapper pickleWrapper,FeatureWrapper featureWrapper) {
        System.out.println(pickleWrapper.getPickle().getName() + "::" + pickleWrapper.getPickle().getScenarioLine());
        this.testngCucumber.runScenario(pickleWrapper.getPickle());
    }

    @DataProvider
    public Object[][] sce() {
        logger.info("test senarios count : "+this.testngCucumber.provideScenarios().length);
        return testngCucumber == null ? new Object[0][0] : this.testngCucumber.provideScenarios();

    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Test AfterMethod");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("Test AfterClass");
        if (testngCucumber != null) {
            testngCucumber.finish();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("Test AfterSuite");
    }
}
