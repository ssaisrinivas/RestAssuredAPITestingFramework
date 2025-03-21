package com.sai.restAssured.Tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sai.restAssured.Tests.POJO.Booking;
import com.sai.restAssured.Tests.POJO.BookingDates;
import com.sai.restAssured.Tests.utils.BaseTest;
import com.sai.restAssured.Tests.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PostAPIRequestUsingPOJO extends BaseTest {

    @Test
    public void postAPIRequest() throws IOException {

	String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA), "UTF-8");

	// Serialization

	BookingDates bookingDates = new BookingDates("2024-01-01", "2025-01-01");
	Booking booking = new Booking("api testing", "tutorial", "super bowls", 3523, true, bookingDates);

	ObjectMapper om = new ObjectMapper();
	try {
	    String requestBody = om.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
	    System.out.println(requestBody);

	    // De-Serialization

	    Booking bookingDetails = om.readValue(requestBody, Booking.class);
	    System.out.println(bookingDetails.toString());

	    System.out.println(bookingDetails.getFirstname());
	    System.out.println(bookingDetails.getTotalprice());
	    System.out.println(bookingDetails.getBookingdates().getCheckin());

	    Response response = RestAssured.given().contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking").body(requestBody)

		    .when().post()

		    .then().assertThat().statusCode(200).statusLine("HTTP/1.1 200 OK")
		    .body("booking.firstname", Matchers.equalTo("api testing"))
		    .body("booking.depositpaid", Matchers.equalTo(true))
		    .body("booking.bookingdates.checkin", Matchers.equalTo("2024-01-01"))
		    .header("Content-Type", "application/json; charset=utf-8").extract().response();

	    int bookingId = response.path("bookingid");

	    System.out.println(jsonSchema);

	    RestAssured.given().contentType(ContentType.JSON).baseUri("https://restful-booker.herokuapp.com/booking/")
		    .pathParam("bookingid", bookingId).when().get("{bookingid}").then().statusCode(200)
		    .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().body();

	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}

    }
}
