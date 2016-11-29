package cucumberTests;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonArray;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonParser;
import gherkin.deps.com.google.gson.stream.JsonReader;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by oleksandrchudnyi on 11/28/16.
 */
public class ApiTestsStepDefs {

    @When("^user do request for all countries$")
    public void userDoRequestForAllCountries() throws IOException {

        connection = initHttpConnection(ALL, searchValue);
    }

    @Then("^server return response with code (\\d+)$")
    public void serverReturnResponseWithCode(int arg0) throws IOException {

        Assert.assertEquals("Incorrect response code", arg0, connection.getResponseCode());
    }

    @And("^response contains list of all countries$")
    public void responseContainsListOfAllCountries() throws IOException {

        resultList = getResultsList(connection);
        //TODO: rewrite check(not size only)
        Assert.assertEquals("", COUNTRIES_TOTAL_COUNT, resultList.size());
    }

    @When("^user do request for all countries using uppercase ALL$")
    public void userDoRequestForAllCountriesUsingUppercaseALL() {

        connection = initHttpConnection(ALL.toUpperCase(), searchValue);
    }

    @Given("^user has all countries info and pick some (\\d+) character API call$")
    public void userHasAllCountriesInfoAndPickSomeCharacterApiCall(int isoCodeType) {

        connection = initHttpConnection(ALL, searchValue);
        String searchParameter = (isoCodeType == 2 ? ALPHA2CODE : ALPHA3CODE);
        countryInfo = getFirstCountryInfo(connection);
        searchValue = getCountryParameterValue(connection, searchParameter);

    }

    @When("^user do country info request by (\\d+) character API call$")
    public void userDoCountryInfoRequestByCharacterApiCall(int isoCodeType)  {
        String urlParameter = (isoCodeType == 2 ? ISO2CODE : ISO3CODE);
        connection = initHttpConnection(urlParameter, searchValue);

    }

    @And("^response contains correct country info$")
    public void responseContainsCorrectCountryInfo() {

        Assert.assertEquals("", countryInfo, getSingleResult(connection));

    }

    @When("^user do country info request using (\\d+) character API call with not existed code$")
    public void userDoCountryInfoRequestUsingCharacterAPICallWithNotExistedCode(int isoCodeType) {

        String urlParameter = (isoCodeType == 2 ? ISO2CODE : ISO3CODE);
        String notExistedCode = (isoCodeType == 2 ? "WW" : "WWW");
        connection = initHttpConnection(urlParameter, notExistedCode);
    }

    @And("^response message like \"([^\"]*)\"$")
    public void responseMessageLike(String message) {
        System.out.println(message);
        Assert.assertTrue(getMessagesList(connection).toString().contains(message.replace("...", "")));
    }

    @When("^user do country info request by (\\d+) character API call without code value$")
    public void userDoCountryInfoRequestByCharacterAPICallWithoutCodeValue(int isoCodeType) {

        String urlParameter = (isoCodeType == 2 ? ISO2CODE : ISO3CODE);
        String notExistedCode = "";
        connection = initHttpConnection(urlParameter, notExistedCode);
    }

    @When("^user do search request with search parameter$")
    public void userDoSearchRequestWithSearchParameter() {
        searchValue = "UA";
        connection = initHttpConnectionForSearch(BASE_SEARCH_URL, searchValue);
    }

    @And("^list of search results$")
    public void listOfSearchResults() {
        //TODO: replace assert with more detailed
        Assert.assertTrue("Search result should not be empty.", getResultsList(connection).size() > 0);
    }

    @When("^user do search request without required \"([^\"]*)\" key$")
    public void userDoSearchRequestWithoutRequiredKey(String propertyKey) throws Throwable {

        searchValue = "UA";
        String newUrl = BASE_SEARCH_URL.replace(propertyKey, "");
        connection = initHttpConnectionForSearch(newUrl, searchValue);

    }



    @Before
    public void beforeScenario() {
        HttpURLConnection connection = null;
        JsonElement responseBody = null;
        JsonArray resultList = null;
        JsonElement countryInfo = null;
        String countryParameterValue = null;
    }


    final static String NAME = "name";
    final static String ALPHA2CODE = "alpha2_code";
    final static String ALPHA3CODE = "alpha3_code";
    final static String ISO2CODE = "iso2code/";
    final static String ISO3CODE = "iso3code/";
    final static String ALL = "all/";
    final static String GET = "get";
    final static String BASE_URL = "http://services.groupkt.com/country/get/";
    final static String BASE_SEARCH_URL = "http://services.groupkt.com/country/search?text=";
    final static String REST_RESPONSE = "RestResponse";
    final static String RESULT = "result";
    final static String MESSAGES = "messages";
    final static int COUNTRIES_TOTAL_COUNT = 249;
    static String searchValue = "";

    HttpURLConnection connection;
    JsonElement responseBody;
    JsonArray resultList;
    JsonElement countryInfo;
    String countryParameterValue;

    protected void request() throws IOException {



//        connection = initHttpConnection(ALL, searchValue);

//        System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());

//        JsonElement responseBody = getResponseBody(connection);
//        System.out.println(responseBody.toString());

//        JsonArray resultsList = getResultsList(connection);
//        System.out.println(resultsList.toString());

//        JsonArray responseMessage = getMessagesList(connection);
//        System.out.println(responseMessage.toString());

//        JsonElement countryInfo = getFirstCountryInfo(connection);
//        System.out.println(getFirstCountryInfo(resultsList, 1).toString());


//        System.out.println(getCountryParameterValue(countryInfo, NAME));
//        System.out.println(getCountryParameterValue(countryInfo, ALPHA2CODE));
//        System.out.println(getCountryParameterValue(countryInfo, ALPHA3CODE));

        System.out.println(5);

    }

    private JsonArray getResultsList(HttpURLConnection connection) {
        responseBody = getResponseBody(connection);
        return responseBody.getAsJsonObject().get(REST_RESPONSE).getAsJsonObject().get(RESULT).getAsJsonArray();
    }

    private JsonElement getSingleResult(HttpURLConnection connection) {
        responseBody = getResponseBody(connection);
        return responseBody.getAsJsonObject().get(REST_RESPONSE).getAsJsonObject().get(RESULT);
    }

    private JsonArray getMessagesList(HttpURLConnection connection) {
        responseBody = getResponseBody(connection);
        return responseBody.getAsJsonObject().get(REST_RESPONSE).getAsJsonObject().get(MESSAGES).getAsJsonArray();
    }

    private JsonElement getResponseBody(HttpURLConnection connection) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement responseBody = new JsonParser().parse(new JsonReader(bufferedReader));
        return responseBody;
    }

    private HttpURLConnection initHttpConnection(String urlSearchParameter, String searchValue) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL + urlSearchParameter + searchValue);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private HttpURLConnection initHttpConnectionForSearch(String searchURL, String searchValue) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(searchURL + searchValue);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private JsonElement getFirstCountryInfo(HttpURLConnection connection) {
        resultList = getResultsList(connection);
        JsonElement countryInfo = resultList.get(0);
        return countryInfo;
    }

    private String getCountryParameterValue(HttpURLConnection connection, String parameterName) {
        return getFirstCountryInfo(connection).getAsJsonObject().get(parameterName).toString().replace("\"", "");
    }


}

