package cucumberTests;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonElement;
import org.junit.Assert;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by oleksandrchudnyi on 11/28/16.
 */
public class ApiTestsStepDefs extends BaseFunctions{

    final static String ALPHA2CODE = "alpha2_code";
    final static String ALPHA3CODE = "alpha3_code";
    final static String ISO2CODE = "iso2code/";
    final static String ISO3CODE = "iso3code/";
    final static String ALL = "all/";
    final static String BASE_SEARCH_URL = "http://services.groupkt.com/country/search?text=";
    final static int COUNTRIES_TOTAL_COUNT = 249;

    String searchValue = "";
    HttpURLConnection connection;
    JsonElement countryInfo;

    @Before
    public void beforeScenario() {
        connection = null;
        responseBody = null;
        resultList = null;
        countryInfo = null;
        searchValue = "";
    }

    @After
    public void afterScenario(){
        connection.disconnect();
    }

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
        searchValue = getCountryParameterValueFromCountryInfo(countryInfo, searchParameter);
    }

    @When("^user do country info request by (\\d+) character API call$")
    public void userDoCountryInfoRequestByCharacterApiCall(int isoCodeType) {
        String urlParameter = (isoCodeType == 2 ? ISO2CODE : ISO3CODE);
        connection = initHttpConnection(urlParameter, searchValue);

    }

    @And("^response contains correct country info$")
    public void responseContainsCorrectCountryInfo() {

        Assert.assertEquals("Response contains incorrect country info", countryInfo, getSingleResult(connection));

    }

    @When("^user do country info request using (\\d+) character API call with not existed code$")
    public void userDoCountryInfoRequestUsingCharacterAPICallWithNotExistedCode(int isoCodeType) {

        String urlParameter = (isoCodeType == 2 ? ISO2CODE : ISO3CODE);
        String notExistedCode = (isoCodeType == 2 ? "WW" : "WWW");
        connection = initHttpConnection(urlParameter, notExistedCode);
    }

    @And("^response message like \"([^\"]*)\"$")
    public void responseMessageLike(String message) {
        Assert.assertTrue("Incorrect message", getMessagesList(connection).toString().contains(message.replace("...", "")));
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
}