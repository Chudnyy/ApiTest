package cucumberTests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by oleksandrchudnyi on 11/25/16.
 */

@RunWith(Cucumber.class)
@CucumberOptions(
//        format = {"Pretty"},
        features = {"src/cucumberTests/feature/apiTests.feature"}
)
public class CucumberRunner {

}
