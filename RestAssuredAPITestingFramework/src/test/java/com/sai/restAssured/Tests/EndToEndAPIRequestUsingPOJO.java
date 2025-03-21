package com.sai.restAssured.Tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sai.restAssured.Tests.Listener.RestAssureListener;
import com.sai.restAssured.Tests.Listener.RestAssureSuccessListener;
import com.sai.restAssured.Tests.POJO.Booking;
import com.sai.restAssured.Tests.POJO.BookingDates;
import com.sai.restAssured.Tests.utils.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EndToEndAPIRequestUsingPOJO extends BaseTest {
    private static final Logger logger = LogManager.getLogger(EndToEndAPIRequestUsingPOJO.class);

    @Test
    public void postAPIRequest() {

	logger.info("End to End test execution started");
	// Serialization

	// POST
	Response response;
	int bookingId = 0;

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

	    response = RestAssured.given().contentType(ContentType.JSON)
		    .filter(new RestAssureListener())
		    .filter(new RestAssureSuccessListener())
		    
		    .baseUri("https://restful-booker.herokuapp.com/booking").body(requestBody)

		    .when().post()

		    .then().assertThat().statusCode(200).statusLine("HTTP/1.1 200 OK")
		    .body("booking.firstname", Matchers.equalTo("api testing"))
		    .body("booking.depositpaid", Matchers.equalTo(true))
		    .body("booking.bookingdates.checkin", Matchers.equalTo("2024-01-01"))
		    .header("Content-Type", "application/json; charset=utf-8").extract().response();

	    bookingId = response.path("bookingid");

	    // Get POST
	    System.out.println("==========================This is POST request Get method =================");

	    RestAssured.given().filter(new RestAssureListener())
	    .filter(new RestAssureSuccessListener()).contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId).when()
		    .get("{bookingid}").then().statusCode(200).log().body().extract().response();

	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}

	// POST Token

	JSONObject token = new JSONObject();
	token.put("username", "admin");
	token.put("password", "password123");

	Response response1 =

		RestAssured.given()
		.filter(new RestAssureListener())
		.filter(new RestAssureSuccessListener())
		.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/auth").body(token.toString()).when().post()
			.then().assertThat().statusCode(200).extract().response();
	String tokens = response1.path("token");
	System.out.println(tokens);

	// PUT

	BookingDates bookingDatesput = new BookingDates("2023-01-01", "2024-01-01");
	Booking bookingput = new Booking("Specflow", "Selenium C#", "biriyani specials", 4665, true, bookingDatesput);

	ObjectMapper omput = new ObjectMapper();
	try {
	    String requestBodyput = om.writerWithDefaultPrettyPrinter().writeValueAsString(bookingput);
	    // System.out.println(requestBodyput);

	    response = RestAssured.given().filter(new RestAssureSuccessListener()).filter(new RestAssureListener()).contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId)
		    .cookie("token", tokens).body(requestBodyput)
		    .when().put("{bookingid}")
		    .then().assertThat().log().body().statusCode(200).statusLine("HTTP/1.1 200 OK").extract()
		    .response();

	    System.out.println("==========================This is PUT request Get method =================");

	    // Get PUT

	    Response responsePut = RestAssured.given().filter(new RestAssureSuccessListener()).filter(new RestAssureListener()).contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId).when()
		    .get("{bookingid}").then().statusCode(200).log().body().extract().response();

	    System.out.println("==========================This is PATCH request Get method =================");

	    response = RestAssured.given().filter(new RestAssureSuccessListener()).filter(new RestAssureListener()).contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId)
		    .cookie("token", tokens).body("{\r\n" + "    \"firstname\": \"Testers Talk\"\r\n" + "}")

		    .when().patch("{bookingid}")

		    .then().assertThat().log().body().statusCode(200).statusLine("HTTP/1.1 200 OK").extract()
		    .response();
	    System.out.println("==========================This is DELETE request Get method =================");

	    response = RestAssured.given().filter(new RestAssureSuccessListener()).filter(new RestAssureListener()).contentType(ContentType.JSON)
		    .baseUri("https://restful-booker.herokuapp.com/booking/").pathParam("bookingid", bookingId)
		    .cookie("token", tokens).when().delete("{bookingid}")

		    .then().assertThat().log().body().statusCode(201
			    
			    ).statusLine("HTTP/1.1 201 Created").extract()
		    .response();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}
	logger.info("End to End test execution Ended");

    }

}
