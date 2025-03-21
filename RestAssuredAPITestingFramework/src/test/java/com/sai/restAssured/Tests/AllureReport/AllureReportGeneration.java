package com.sai.restAssured.Tests.AllureReport;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sai.restAssured.Tests.Listener.RestAssureListener;
import com.sai.restAssured.Tests.Listener.RestAssureSuccessListener;
import com.sai.restAssured.Tests.utils.BaseTest;
import com.sai.restAssured.Tests.utils.FileNameConstants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("Epic-01")
@Feature("Create Update Delete Booking")
public class AllureReportGeneration extends BaseTest {

    private static final Logger logger = LogManager.getLogger(AllureReportGeneration.class);
    private Response response;
    private int bookingId = 0;
    private static String tokens;

    @BeforeTest
    public void accessToken() {
	// POST Token
	logger.info("End to End test execution started for Token");
	JSONObject token = new JSONObject();
	token.put("username", "admin");
	token.put("password", "password123");
	Response response1 = RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssureListener())
		.filter(new RestAssureSuccessListener()).contentType(ContentType.JSON)
		.baseUri("https://restful-booker.herokuapp.com/auth").body(token.toString()).when().post().then()
		.assertThat().statusCode(200).extract().response();
	tokens = response1.path("token");
	System.out.println(tokens);
	logger.info("End to End test execution Ended Token");
    }

    @Story("Story 1")
    @Test(priority = 0, description = "end to end api testing POST")
    @Description("end to end testing POST")
    @Severity(SeverityLevel.CRITICAL)
    public void postAPIRequest() throws IOException {

	logger.info("End to End test execution started");
	// POST Request

	String postApiRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),
		"UTF-8");
	System.out.println(postApiRequestBody);
	response = RestAssured.given().contentType(ContentType.JSON).filter(new RestAssureListener())
		.filter(new RestAssureSuccessListener()).filter(new AllureRestAssured())
		.baseUri("https://restful-booker.herokuapp.com/booking").body(postApiRequestBody).when().post().then()
		.assertThat().statusCode(200).statusLine("HTTP/1.1 200 OK")
		.body("booking.firstname", Matchers.equalTo("api testing"))
		.body("booking.depositpaid", Matchers.equalTo(true))
		.header("Content-Type", "application/json; charset=utf-8").extract().response();
	bookingId = response.path("bookingid");

	// Get POST Request
	System.out.println("==========================This is POST request Get method =================");

	RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssureListener())
		.filter(new RestAssureSuccessListener()).contentType(ContentType.JSON)
		.baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId).when()
		.get("{bookingid}").then().statusCode(200).log().body().extract().response();
	logger.info("End to End test execution Ended");
    }

    @Story("Story 2")
    @Test(priority = 1, description = "end to end api testing PUT")
    @Description("end to end testing PUt")
    @Severity(SeverityLevel.BLOCKER)
    public void putAPIRequest() throws IOException {
	// PUT Request
	logger.info("End to End test execution started for PUT");

	try {
	    String putApiRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),
		    "UTF-8");
	    System.out.println(putApiRequestBody);

	    response = RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssureSuccessListener())
		    .filter(new RestAssureListener()).contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId)
		    .cookie("token", tokens).body(putApiRequestBody).when().put("{bookingid}").then().assertThat().log()
		    .body().statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();

	    System.out.println("==========================This is PUT request Get method =================");

	    // Get PUT Request
	    RestAssured.given().filter(new AllureRestAssured())
		    .filter(new RestAssureSuccessListener()).filter(new RestAssureListener())
		    .contentType(ContentType.JSON).baseUri("https://restful-booker.herokuapp.com/booking/")
		    .pathParam("bookingid", bookingId).when().get("{bookingid}").then().statusCode(200).log().body()
		    .extract().response();
	    logger.info("End to End test execution Ended PUT");
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}
    }

    @Story("Story 3")
    @Test(priority = 2, description = "end to end api testing PATCH")
    @Description("end to end testing PATCH")
    @Severity(SeverityLevel.MINOR)
    public void patchAPIRequest() throws IOException {

	// PATCH Request

	logger.info("End to End test execution Staeted PATCH");
	System.out.println("==========================This is PATCH request Get method =================");

	String patchApiRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),
		"UTF-8");
	System.out.println(patchApiRequestBody);

	response = RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssureSuccessListener())
		.filter(new RestAssureListener()).contentType(ContentType.JSON)
		.baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId)
		.cookie("token", tokens).body(patchApiRequestBody).when().patch("{bookingid}").then().assertThat().log()
		.body().statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();
	logger.info("End to End test execution Ended PATCH");
    }

    @Story("Story 4")
    @Test(priority = 3, description = "end to end api testing DELETE")
    @Description("end to end testing DELETE")
    @Severity(SeverityLevel.NORMAL)
    public void deleteAPIRequest() {
	// Delete Request
	logger.info("End to End test execution started for DELETE");
	System.out.println("==========================This is DELETE request Get method =================");

	response = RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssureSuccessListener())
		.filter(new RestAssureListener()).contentType(ContentType.JSON)
		.baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId)
		.cookie("token", tokens).when().delete("{bookingid}").then().assertThat().log().body().statusCode(201)
		.statusLine("HTTP/1.1 201 Created").extract().response();
	logger.info("End to End test execution Ended DELETE");
    }

}
