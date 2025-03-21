package com.sai.restAssured.Tests.DataDrivenTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sai.restAssured.Tests.Listener.RestAssureListener;
import com.sai.restAssured.Tests.POJO.Booking;
import com.sai.restAssured.Tests.POJO.BookingDates;
import com.sai.restAssured.Tests.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class DataDrivenTestingUsingXmlFile {

    //@Test(dataProvider = "getExcelTestData")
    public void dataDrivenTestingUsingXmlFile(Map<String, String> testData) throws JsonProcessingException {

	System.out.println(testData);
	


	String firstname = (String) testData.get("firstname");
	String lastname = (String) testData.get("lastname");
	String additionalneeds = (String) testData.get("additionalneeds");
	int totalprice = Integer.parseInt(testData.get("totalprice"));
	boolean depositpaid = Boolean.parseBoolean(testData.get("depositpaid"));
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

    @DataProvider(name = "getExcelTestData")
    public Object[][] getExcelTestData() throws FilloException {
	String querry = "select * from Sheet1 where Run='Yes'";

	Object[][] objArray = null;
	Map<String, String> testData = null;
	List<Map<String, String>> testDataList = null;

	Fillo fillo = new Fillo();
	Connection connection = null;
	Recordset recordSet = null;
	connection = fillo.getConnection(FileNameConstants.EXCEL_TEST_DATA);
	recordSet = connection.executeQuery(querry);
	testDataList = new ArrayList<Map<String, String>>();
	while (recordSet.next()) {
	    testData = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	    for (String field : recordSet.getFieldNames()) {
		testData.put(field, recordSet.getField(field));
	    }
	    testDataList.add(testData);
	}
	objArray = new Object[testData.size()][];
	for (int i = 0; i < testDataList.size(); i++) {
	    objArray[i][0] = testDataList.get(i);
	}
	return objArray;
    }
}
