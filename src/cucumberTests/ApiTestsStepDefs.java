package cucumberTests;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonArray;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonObject;
import gherkin.deps.com.google.gson.JsonParser;
import gherkin.deps.com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by oleksandrchudnyi on 11/28/16.
 */
public class ApiTestsStepDefs {
    @Given("^precondition step$")
    public void preconditionStep() throws Throwable {
        System.out.println(1);
//        throw new PendingException();
    }

    @When("^execution step$")
    public void executionStep() throws Throwable {
        System.out.println(2);
        request();
//        throw new PendingException();
    }

    @Then("^validation step$")
    public void validationStep() throws Throwable {
        System.out.println(3);
//        throw new PendingException();
    }

    protected void request() throws IOException {

        String alpha2Code = "alpha2_code";
        String alpha3Code = "alpha3_code";
        String URL = "http://services.groupkt.com/country/get/all";

        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println(connection.getResponseCode() + connection.getResponseMessage());

        JsonElement jsonElement = new JsonParser().parse(new JsonReader(rd));
        System.out.println(jsonElement.toString());

        JsonArray jsonArray = jsonElement.getAsJsonObject().get("RestResponse").getAsJsonObject().get("result").getAsJsonArray();
        System.out.println(jsonArray.toString());

        JsonElement countryInfo = jsonArray.get(1);
        System.out.println(countryInfo.toString());

        System.out.println(countryInfo.getAsJsonObject().get("name").toString().replace("\"", "") + " " + countryInfo.getAsJsonObject().get(alpha2Code) + " " + countryInfo.getAsJsonObject().get(alpha3Code));

        System.out.println(5);

    }
}

