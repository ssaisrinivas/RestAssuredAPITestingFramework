package com.sai.restAssured.Tests.DataDrivenTesting;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sai.restAssured.Tests.Listener.RestAssureListener;
import com.sai.restAssured.Tests.POJO.Booking;
import com.sai.restAssured.Tests.POJO.BookingDates;
import com.sai.restAssured.Tests.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class DataDrivenTestingUsingCsvFile {

    @Test(dataProvider="getCsvTestData")
    public void dataDrivenTestingUsingCsvFile(Map<String , String> testData) throws JsonProcessingException {
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
    
    
    @DataProvider(name="getCsvTestData")
    public Object [][] getCsvTestData() throws CsvValidationException, IOException{
	
	Object [][] obj = null;
	Map<String,String > map = null;
	List<Map<String,String >> testDataList = null;
	CSVReader csvReader = new CSVReader(new FileReader(FileNameConstants.CSV_TEST_DATA));
	
	testDataList = new ArrayList<Map<String,String>>();
	String[] line = null;
	int count=0;
	while((line= csvReader.readNext())!=null) {
	    
	    if(count ==0) {
		count++;
		continue;
	    }
	    
	    map = new TreeMap<String , String>(String.CASE_INSENSITIVE_ORDER);
	    map.put("firstname", line[0]);
	    map.put("lastname", line[1]);
	    map.put("totalprice", line[2]);
	    map.put("depositpaid", line[3]);
	    map.put("checkin", line[4]);
	    map.put("checkout", line[5]);
	    map.put("additionalneeds", line[6]);
	    testDataList.add(map);
	}
	obj = new Object[testDataList.size()][1];
	
	for(int i=0;i< testDataList.size();i++) {
	    
	   obj[i][0] = testDataList.get(i);
	  // System.out.println(obj[i][0]);
	}
	
	return obj;
    }
    
}
