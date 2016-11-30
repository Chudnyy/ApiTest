package cucumberTests;

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
 * Created by achudnyy on 11/29/2016.
 */
public class BaseFunctions {

    final static String BASE_URL = "http://services.groupkt.com/country/get/";
    final static String REST_RESPONSE = "RestResponse";
    final static String RESULT = "result";
    final static String MESSAGES = "messages";


    JsonElement responseBody;
    JsonArray resultList;


    public JsonArray getResultsList(HttpURLConnection connection) {
        responseBody = getResponseBody(connection);
        return responseBody
                .getAsJsonObject()
                .get(REST_RESPONSE)
                .getAsJsonObject()
                .get(RESULT)
                .getAsJsonArray();
    }

    public JsonElement getSingleResult(HttpURLConnection connection) {
        responseBody = getResponseBody(connection);
        return responseBody
                .getAsJsonObject()
                .get(REST_RESPONSE)
                .getAsJsonObject()
                .get(RESULT);
    }

    public JsonArray getMessagesList(HttpURLConnection connection) {
        responseBody = getResponseBody(connection);
        return responseBody
                .getAsJsonObject()
                .get(REST_RESPONSE)
                .getAsJsonObject()
                .get(MESSAGES)
                .getAsJsonArray();
    }

    private JsonElement getResponseBody(HttpURLConnection connection) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonParser()
                .parse(new JsonReader(bufferedReader));
    }

    public HttpURLConnection initHttpConnection(String urlSearchParameter, String searchValue) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL + urlSearchParameter + searchValue);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public HttpURLConnection initHttpConnectionForSearch(String searchURL, String searchValue) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(searchURL + searchValue);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //TODO: change method to get random country info from list
    public JsonElement getFirstCountryInfo(HttpURLConnection connection) {
        resultList = getResultsList(connection);
        return resultList.get(0);
    }

    public String getCountryParameterValueFromCountryInfo(JsonElement countryInfo, String parameterName) {
        return countryInfo
                .getAsJsonObject()
                .get(parameterName)
                .toString()
                .replace("\"", "");
    }
}