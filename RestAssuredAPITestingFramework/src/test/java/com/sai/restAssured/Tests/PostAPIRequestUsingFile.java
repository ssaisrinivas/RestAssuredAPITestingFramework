package com.sai.restAssured.Tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import com.jayway.jsonpath.JsonPath;
import com.sai.restAssured.Tests.utils.BaseTest;
import com.sai.restAssured.Tests.utils.FileNameConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import junit.framework.Assert;
import net.minidev.json.JSONArray;


public class PostAPIRequestUsingFile extends BaseTest {

    @Test
    public void postAPIRequest() {
	try {
	    String postApiRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),"UTF-8");
	    //System.out.println(postApiRequestBody);
	    
	Response response =    RestAssured
	    
	         .given()
	         	.contentType(ContentType.JSON)
	     		.body(postApiRequestBody)
	     		.baseUri("https://restful-booker.herokuapp.com/booking")
	     	.when()
	     		.post()
	     	.then()
	     		.assertThat()
	     		.statusCode(200)
	     		.extract()
	     		.response();
	
	JSONArray jsonArrayFirstname = JsonPath.read(response.body().asString(),"$.booking..firstname");
	String firstname = (String) jsonArrayFirstname.get(0);
	Assert.assertEquals(firstname, "api testing");
	
	JSONArray jsonArrayLastname = JsonPath.read(response.body().asString(),"$.booking..lastname");
	String lastName = (String) jsonArrayLastname.get(0);
	Assert.assertEquals(lastName, "tutorial");
	
	JSONArray jsonArrayCheckin = JsonPath.read(response.body().asString(),"$.booking..bookingdates.checkin");
	String checkin = (String) jsonArrayCheckin.get(0);
	Assert.assertEquals(checkin, "2018-01-01");
	
	JSONArray bookingIdarray = JsonPath.read(response.body().asString(),"$..bookingid");
	int bookingId = (int) bookingIdarray.get(0);
	
	
		RestAssured
			.given()
			    .contentType(ContentType.JSON)
			    .baseUri("https://restful-booker.herokuapp.com/booking/")
			    .pathParam("bookingid", bookingId)
			.when()
			     .get("{bookingid}")
			.then()
			     .statusCode(200)
			     .body("firstname", Matchers.equalTo("api testing"))
			     .body("depositpaid", Matchers.equalTo(true))
			     .body("bookingdates.checkin", Matchers.equalTo("2018-01-01"))
			     .header("Content-Type", "application/json; charset=utf-8")
			     .log().body();
	
	
	
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	
    }
}
