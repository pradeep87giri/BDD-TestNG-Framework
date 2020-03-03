package testRunners;

import baseClass.AppTest;
import cucumber.api.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/DemoBlaze.feature",
tags = {"@RegressionTest"},
glue={"stepDefinitions"},
format = { "pretty",
"html:target/site/DemoBlaze",
"rerun:target/rerun.txt",
"json:target/DemoBlaze.json" },dryRun=false,monochrome = true)

public class DemoBlaze extends AppTest {
	

}
