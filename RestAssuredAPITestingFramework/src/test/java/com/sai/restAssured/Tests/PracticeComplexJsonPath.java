package com.sai.restAssured.Tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.sai.restAssured.Tests.utils.FileNameConstants;

import net.minidev.json.JSONArray;

public class PracticeComplexJsonPath {

    @Test
    public void parsingComplexJson() {

	try {

	    String jsonFile = FileUtils.readFileToString(new File(FileNameConstants.COMPLEX_JSON), "UTF-8");
	    JSONArray toppingsType = JsonPath.read(jsonFile, "$.items.item..topping..type");
	    System.out.println(toppingsType.get(1));
	    JSONArray battersType = JsonPath.read(jsonFile, "$.items.item..batters..batter..type");
	    System.out.println(battersType.get(3));

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
