Feature: first feature

  Scenario: Get all countries info
    When user do request for all countries
    Then server return response with code 200
    And response contains list of all countries

  Scenario: Get all Countries info - use Uppercase parameter ALL
    When user do request for all countries using uppercase ALL
    Then server return response with code 404

  Scenario: Get country info by iso2 code
    Given user has all countries info and pick some 2 character API call
    When user do country info request by 2 character API call
    Then server return response with code 200
    And response contains correct country info

  Scenario: Get Country info using not existed iso2code
    When user do country info request using 2 character API call with not existed code
    Then server return response with code 200
    And response message like "No matching country found ..."

  Scenario: Get country info by iso3 code
    Given user has all countries info and pick some 3 character API call
    When user do country info request by 3 character API call
    Then server return response with code 200
    And response contains correct country info

  Scenario: Get country info by iso3 code without specifying code value
    When user do country info request by 3 character API call without code value
    Then server return response with code 404

  Scenario: Get countries info using search
    When user do search request with search parameter
    Then server return response with code 200
    And list of search results

  Scenario: Get country info using search - without required key
    When user do search request without required "text" key
    Then server return response with code 400