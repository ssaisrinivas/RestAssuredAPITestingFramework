
package com.sai.restAssured.Tests.DataDrivenTesting;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sai.restAssured.Tests.Listener.RestAssureListener;
import com.sai.restAssured.Tests.POJO.Booking;
import com.sai.restAssured.Tests.POJO.BookingDates;
import com.sai.restAssured.Tests.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class DataDrivenTestingUsingJsonFile {

    @Test(dataProvider = "getTestData")
    public void DataDrivenTestingUsingJson(LinkedHashMap<String, Object> testData) throws JsonProcessingException {
	System.out.println(testData);

	String firstname = (String) testData.get("firstname");
	String lastname = (String) testData.get("lastname");
	String additionalneeds = (String) testData.get("additionalneeds");
	int totalprice = (int) testData.get("totalprice");
	boolean depositpaid = (boolean) testData.get("depositpaid");
	String checkin = (String) testData.get("checkin");
	String checkout = (String) testData.get("checkout");

	BookingDates bookingDates = new BookingDates(checkin, checkout);
	Booking booking = new Booking(firstname, lastname, additionalneeds, totalprice, depositpaid, bookingDates);

	ObjectMapper objectMapper = new ObjectMapper();
	String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);

	RestAssured.given().filter(new RestAssureListener()).contentType(ContentType.JSON)
		.body(requestBody).baseUri("https://restful-booker.herokuapp.com/booking").when().post().then()
		.assertThat().statusCode(200).extract().response();

    }

    @DataProvider(name = "getTestData")
    public Object[] getTestDataUsingJson() {

	Object[] obj = null;

	try {
	    String jsonTestData = FileUtils.readFileToString(new File(FileNameConstants.JSON_TEST_DATA), "UTF-8");

	    JSONArray jsonArray = JsonPath.read(jsonTestData, "$");

	    obj = new Object[jsonArray.size()];

	    for (int i = 0; i < jsonArray.size(); i++) {
		obj[i] = jsonArray.get(i);
	    }

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return obj;
    }

}
