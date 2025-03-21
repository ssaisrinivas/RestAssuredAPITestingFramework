package com.sai.restAssured.Tests;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.sai.restAssured.Tests.utils.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import junit.framework.Assert;

public class PostAPIRequest extends BaseTest{

    @Test
    public void createBooking() {
	
	
	// prepare request body
	
	JSONObject booking = new JSONObject();
	JSONObject bookingDate = new JSONObject();

	booking.put("firstname", "api testing");
	booking.put("lastname", "tutorial");
	booking.put("totalprice", 1000);
	booking.put("depositpaid", true);
	booking.put("additionalneeds", "breakfast");
	booking.put("bookingdates", bookingDate);
	bookingDate.put("checkin", "2024-01-01");
	bookingDate.put("checkout", "2025-02-02");
	
	Response response =
		
		RestAssured
		 	.given()
		 	   .contentType(ContentType.JSON)
		 	   .baseUri("https://restful-booker.herokuapp.com/booking")
		 	   .body(booking.toString())
		 	   //.log().body()
		 	  // .log().headers()
		 	   //.log().all()
		 	 .when()
		 	    .post()
		 	 .then()
		 	    .assertThat()
		 	    .statusCode(200)
		 	    .statusLine("HTTP/1.1 200 OK")
		 	    .body("booking.firstname", Matchers.equalTo("api testing"))
		 	    .body("booking.depositpaid", Matchers.equalTo(true))
		 	    .body("booking.bookingdates.checkin", Matchers.equalTo("2024-01-01"))
	        	    .header("Content-Type", "application/json; charset=utf-8")
	        	    //.log().body()
	        	    //.log().headers()
			    //.log().cookies()
	        	    //.log().all()
	        	    //.log().ifValidationFails()
		 	    .extract()
		 	    .response();
	
	Assert.assertTrue(response.getBody().asString().contains("depositpaid"));
	
	int bookingId = response.path("bookingid");
	RestAssured
	  .given()
        	  .contentType(ContentType.JSON)
        	  .pathParam("bookingId", bookingId)
        	  .baseUri("https://restful-booker.herokuapp.com/booking")
         .when()
         	  .get("{bookingId}")
	  .then()
	  	  .assertThat()
	  	  .statusCode(200)
	  	  .statusLine("HTTP/1.1 200 OK")
	  	  .body("firstname", Matchers.equalTo("api testing"))
	 	  .body("depositpaid", Matchers.equalTo(true))
	 	  .body("bookingdates.checkin", Matchers.equalTo("2024-01-01"))
	 	  .header("Content-Type", "application/json; charset=utf-8")
	          .log()
	          .all();
	
	}
}
