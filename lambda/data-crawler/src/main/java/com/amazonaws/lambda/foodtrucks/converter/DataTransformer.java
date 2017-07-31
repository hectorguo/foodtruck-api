package com.amazonaws.lambda.foodtrucks.converter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.amazonaws.lambda.foodtrucks.datasetinit.Coordinate;
import com.amazonaws.lambda.foodtrucks.datasetinit.FoodTruckDetail;
import com.amazonaws.lambda.foodtrucks.datasetinit.Location;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public abstract class DataTransformer {

	public abstract FoodTruckDetail getTransformedItem(JSONObject jsonObj);
	
    public void transform(JSONObject jsonObj, DynamoDBMapper mapper) {
	    	FoodTruckDetail item = getTransformedItem(jsonObj);
	    	
		if(item == null) {
			return;
		}
			
	    mapper.save(item);
	    System.out.println(item.getId());
    };
    
    
    /**
	 * Transforms a game score from the Scores table into stats aggregated by player in the
	 * PlayerStats table.
	 */
    public static final DataTransformer YELP_FOOD_TRUCK_DETAIL = new DataTransformer( ) {
			@Override
			public FoodTruckDetail getTransformedItem(JSONObject jsonObj) {
				Coordinate coord = new Coordinate();
				JSONObject coordinates = jsonObj.getJSONObject("coordinates");
				coord.setLatitude(coordinates.optDouble("latitude"));
				coord.setLongitude(coordinates.optDouble("longitude"));
				
				Location location = new Location();
				location.init(jsonObj.getJSONObject("location"));
	//            Table viewTable = dynamodb.getTable(FOOD_TRUCK_LIST_TABLE_NAME);
	            
	            FoodTruckDetail item = new FoodTruckDetail();
	            item.setId(jsonObj.getString("id"));
	            item.setName(jsonObj.getString("name"));
	            item.setImageUrl(jsonObj.optString("image_url"));
	            item.setUrl(jsonObj.optString("url"));
	            item.setPhone(jsonObj.optString("phone"));
	            item.setDisplayPhone(jsonObj.optString("display_phone"));
	            item.setRating(jsonObj.getDouble("rating"));
	            item.setCoordinates(coord);
	            item.setLocation(location);
	            
				return item;
			}
    };
    
    /**
	 * Transforms a game score from the Scores table into stats aggregated by player in the
	 * PlayerStats table.
	 */
    public static final DataTransformer STREET_FOOD_DETAIL = new DataTransformer( ) {
    		@Override
    		public FoodTruckDetail getTransformedItem(JSONObject jsonObj) {
    			String TRIM_PHONE_REGEX = "\\(|\\)|\\-";
			List<String> displayAddr = new ArrayList<String>();
		
			Coordinate coord = new Coordinate();
			JSONObject lastLocation = jsonObj.optJSONObject("last");
			if(lastLocation == null) {
				return null;
			}
			
			coord.setLatitude(lastLocation.getDouble("latitude"));
			coord.setLongitude(lastLocation.getDouble("longitude"));
			
			Location location = new Location();
			location.setAddress1(lastLocation.getString("display"));
			location.setCity(jsonObj.getString("region"));
			location.setCountry("US");
			
			displayAddr.add(lastLocation.getString("display"));
			location.setDisplayAddress(displayAddr);
        
            FoodTruckDetail item = new FoodTruckDetail();
            item.setId(jsonObj.getString("identifier"));
            item.setName(jsonObj.getString("name"));
//            item.setImageUrl(jsonObj.optString("image_url"));
            item.setUrl(jsonObj.optString("url"));
            item.setPhone(jsonObj.optString("phone").replaceAll(TRIM_PHONE_REGEX, "").trim());
            item.setDisplayPhone(jsonObj.optString("phone"));
            item.setRating(jsonObj.getDouble("rating"));
            item.setCoordinates(coord);
            item.setLocation(location);
//            item.setLocation(new Location(jsonObj.getJSONObject("location")));
            return item;
    		}
    	};
}