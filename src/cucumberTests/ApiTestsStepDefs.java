package cucumberTests;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonArray;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonParser;
import gherkin.deps.com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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


    final static String NAME = "name";
    final static String ALPHA2CODE = "alpha2_code";
    final static String ALPHA3CODE = "alpha3_code";
    final static String ALL = "all";
    final static String GET = "GET";
    final static String baseURL = "http://services.groupkt.com/country/get/";
    final static String RESTRESPONSE = "RestResponse";
    final static String RESULT = "result";
    final static String MESSAGES = "messages";

    protected void request() throws IOException {



        HttpURLConnection connection = initHttpURLConnection(ALL, GET, baseURL);

        System.out.println(connection.getResponseCode() + connection.getResponseMessage());

        JsonElement responseBody = getResponseBody(connection);
        System.out.println(responseBody.toString());

        JsonArray resultsList = getResultsList(responseBody);
        System.out.println(resultsList.toString());

        JsonArray responseMessage = getMessagessList(responseBody);
        System.out.println(responseMessage.toString());

        JsonElement countryInfo = getCountryInfo(resultsList, 1);
        System.out.println(getCountryInfo(resultsList, 1).toString());


        System.out.println(getCountryParameterValue(countryInfo, NAME));
        System.out.println(getCountryParameterValue(countryInfo, ALPHA2CODE));
        System.out.println(getCountryParameterValue(countryInfo, ALPHA3CODE));

        System.out.println(5);

    }

    private JsonArray getResultsList(JsonElement responseBody) {
        return responseBody.getAsJsonObject().get(RESTRESPONSE).getAsJsonObject().get(RESULT).getAsJsonArray();
    }

    private JsonArray getMessagessList(JsonElement responseBody) {
        return responseBody.getAsJsonObject().get(RESTRESPONSE).getAsJsonObject().get(MESSAGES).getAsJsonArray();
    }

    private JsonElement getResponseBody(HttpURLConnection connection) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return new JsonParser().parse(new JsonReader(rd));
    }

    private HttpURLConnection initHttpURLConnection(String urlSearchParameter, String GET, String baseURL) throws IOException {
        URL url = new URL(baseURL + urlSearchParameter);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(GET);
        return connection;
    }

    private JsonElement getCountryInfo(JsonArray resultsList, int element) {
        JsonElement countryInfo = resultsList.get(element);
        return countryInfo;
    }

    private String getCountryParameterValue(JsonElement countryInfo, String parameterName) {
        return getCountryParameter(countryInfo, parameterName).toString().replace("\"", "");
    }

    private JsonElement getCountryParameter(JsonElement countryInfo, String parameterName) {
        return countryInfo.getAsJsonObject().get(parameterName);
    }
}

