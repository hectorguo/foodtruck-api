package com.amazonaws.lambda.foodtrucks.datasetinit;

//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Future;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.AUTH;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.YELP_CITY_RESOURCE;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.STREETFOOD_CITY_RESOURCE;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.CONTENT_TYPE;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.YELP_SEARCH_API;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.STREET_FOOD_API;
import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.mapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.lambda.foodtrucks.converter.DataTransformer;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DataCrawler {
	
	public static void main(String[] args) {
		CrawlStreetFoodData();
		CrawlYelpData();
	}
	
	public static void CrawlStreetFoodData() {
//		DataCrawler fetch = new DataCrawler();
        List<String> cities = DataCrawler.getCitiesFromJsonFile(STREETFOOD_CITY_RESOURCE);
        System.out.println(cities.toString());
        List<FoodTruckDetail> items = new ArrayList<FoodTruckDetail>();
		try {
			for(String city : cities) {
				HttpResponse<JsonNode> res = DataCrawler.getStreetfoodData(city);
				JSONObject vendors = res.getBody().getObject().getJSONObject("vendors");
				
				Iterator<?> keys = vendors.keys();
				
				
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if ( vendors.get(key) instanceof JSONObject ) {
				    		FoodTruckDetail item = DataTransformer.STREET_FOOD_DETAIL.getTransformedItem((JSONObject) vendors.get(key));
//				    		DataTransformer.STREET_FOOD_DETAIL.transform((JSONObject) vendors.get(key), mapper);
				    		if(item != null) {
				    			items.add(item);
					    		System.out.println(item.getId());
				    		}
				    }
				}
				
			}
			mapper.batchSave(items);
		} catch (UnirestException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static void CrawlYelpData() {
//		DataCrawler fetch = new DataCrawler();
        List<String> cities = DataCrawler.getCitiesFromJsonFile(YELP_CITY_RESOURCE);
        System.out.println(cities.toString());
        List<FoodTruckDetail> items = new ArrayList<FoodTruckDetail>();
		try {
			for(String city : cities) {
				HttpResponse<JsonNode> res = DataCrawler.getYelpData(city);
				JSONArray businesses = res.getBody().getObject().getJSONObject("body").getJSONArray("businesses");
				
				items.clear();

				for(int i = 0; i < businesses.length(); i++) {
					FoodTruckDetail item = DataTransformer.YELP_FOOD_TRUCK_DETAIL.getTransformedItem(businesses.getJSONObject(i));
//					DataTransformer.FOOD_TRUCK_DETAIL.transform(item, mapper);
					if(item != null) {
			    			items.add(item);
				    		
			    		}
				}
				mapper.batchSave(items);
				System.out.println("Data Saved ---- City:" + city + ", Food trucks count: " + items.size());
			}
			
		} catch (UnirestException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static HttpResponse<JsonNode> getYelpData(String city) throws UnirestException {
		HttpResponse<JsonNode> future = Unirest.get(YELP_SEARCH_API)
				  .header("content-type", CONTENT_TYPE)
				  .header("Authorization", AUTH)
				  .queryString("location", city + ",ca")
				  .asJson();
		return future;
	}
	
	public static HttpResponse<JsonNode> getStreetfoodData(String city) throws UnirestException {
		HttpResponse<JsonNode> future = Unirest.get(STREET_FOOD_API + city)
				  .header("content-type", CONTENT_TYPE)
				  .asJson();
		return future;
	}
	
	public static List<String> getCitiesFromJsonFile(String filePath) {
		List<String> cities = new ArrayList<String>();
		
		try{
			String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONArray a = jsonObj.getJSONArray("cities");
	
			for (Object o : a)
			{
				cities.add(o.toString());
			}
			  
			  
		}
        catch(Exception ex){
            System.out.println(ex.toString());
        }
		return cities;
	}
	
}
