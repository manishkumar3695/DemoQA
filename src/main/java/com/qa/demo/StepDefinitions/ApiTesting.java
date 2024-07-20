package com.qa.demo.StepDefinitions;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.qa.demo.Utils.FrameworkUtil.*;

public class ApiTesting {
    //    static RestAssured.given()
    static HashMap<String, String> projectIdMap = new HashMap<>();
    private static JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);

    @Given("user calls the api")
    public void userCallsTheApi() {
        Response res = RestAssured.given()
                .auth()
                .preemptive()
                .basic(getConfig("JiraUserName"), getConfig("JiraToken"))
                .pathParam("issueIdOrKey", "SCRUM-9")
                .header("Accept", "application/json")
                .when()
                .get("https://bankexamclear1.atlassian.net/rest/api/3/issue/{issueIdOrKey}")
                .then()
                .extract()
                .response();
        String body = res.getBody().asString();
        Map<String, Object> mp = new HashMap<>();
        mp.put("newJson", body);
        System.out.println("new map: " + mp);
    }

    @Given("user stores the project id")
    public void userStoresTheProjectId() {
        Response res = RestAssured.given()
                .auth()
                .preemptive()
                .basic(getConfig("JiraUserName"), getConfig("JiraToken"))
                .header("Accept", "application/json")
                .when()
                .get("https://bankexamclear1.atlassian.net/rest/api/3/project")
                .then()
                .extract()
                .response();
        String body = res.getBody().asString();
        ArrayList<String> arr = JsonPath.read(body, "$..[1].id");

        projectIdMap.put("projectID", arr.get(0));
        System.out.println("projectID: " + arr.get(0));

    }

    @And("user stores issuetype id")
    public void userStoresIssuetypeId() {
        Response res = RestAssured.given()
                .auth()
                .preemptive()
                .basic(getConfig("JiraUserName"), getConfig("JiraToken"))
                .header("Accept", "application/json")
                .queryParam("projectId", projectIdMap.get("projectID"))
                .when()
                .get("https://bankexamclear1.atlassian.net/rest/api/3/project")
                .then()
                .extract()
                .response();
        String body = res.getBody().asString();
        ArrayList<String> arr = JsonPath.read(body, "$..[0].id");
        projectIdMap.put("issueID", arr.get(0));
        System.out.println("issueID: " + arr.get(0));
    }

    @And("user stores the user id")
    public void userStoresTheUserId() {
        Response res = RestAssured.given()
                .auth()
                .preemptive()
                .basic(getConfig("JiraUserName"), getConfig("JiraToken"))
                .header("Accept", "application/json")
                .queryParam("projectKeys", "SCRUM")
                .when()
                .get("https://bankexamclear1.atlassian.net/rest/api/3/user/assignable/multiProjectSearch")
                .then()
                .extract()
                .response();
        String body = res.getBody().asString();
        ArrayList<String> arr = JsonPath.read(body, "$..[0].accountId");
        projectIdMap.put("accountID", arr.get(0));
        System.out.println("accountID: " + arr.get(0));
        System.out.println("accountID: " + projectIdMap.get("accountID"));
    }

    @And("user creates the issue")
    public void userCreatesTheIssue() throws ParseException {
        String body = getConfig("JsonBody1").replace("issuetypeID", projectIdMap.get("issueID"))
                .replace("projectID", projectIdMap.get("projectID"));
        body = body.replace("accountID1", projectIdMap.get("accountID"));
        System.out.println("requestPayload: " + body);
        JSONObject ob = (JSONObject) parser.parse(body);
        System.out.println("requestPayloadAfterParse: " + ob.toJSONString());

        Response res = RestAssured.given()
                .auth()
                .preemptive()
                .basic(getConfig("JiraUserName"), getConfig("JiraToken"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(ob.toJSONString())
                .when()
                .post("https://bankexamclear1.atlassian.net/rest/api/3/issue")
                .then()
                .extract()
                .response();

        String body1 = res.getBody().asString();
        System.out.println("issuebody: " + body1);
    }
}
