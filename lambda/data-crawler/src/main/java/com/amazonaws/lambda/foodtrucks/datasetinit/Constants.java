package com.amazonaws.lambda.foodtrucks.datasetinit;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class Constants {

    public static final String LOCAL_CRED_PROFILE_NAME = "default";

    // Source table
    public static final String YELP_SEARCH_API = "https://jirv8u2ell.execute-api.us-west-1.amazonaws.com/dev/foodtrucks/search";
    public static final String CONTENT_TYPE = "application/json";
    public static final String AUTH = "Bearer UOG4x25kRFF6bWtb-Sq8wP2J3mD9NZfSKbKdweHWO0nC7C-A5-ROuVH30RQ7_2tQrYpIAvOuIjI9OBtON8BtUb49la3UGXmc0B_tgTddC14pp0ceMTSHY_xxnyhtWXYx";
    public static final String CACHE_CONTROL = "no-cache";
    
    public static final String YELP_CITY_RESOURCE = "src/test/resources/cities_for_yelp.json";
    public static final String STREETFOOD_CITY_RESOURCE = "src/test/resources/cities_for_streetfood.json";
    
    public static final String STREET_FOOD_API = "http://data.streetfoodapp.com/1.1/schedule/";
    public static final String TRUCK_ID = "id";
    public static final String COORDINATES = "coordinates";
    public static final String TRUCK_LAT = "latitude";
    public static final String TRUCK_LNG = "longitude";

    // PlayerStats table
    public static final String GEO_POINTS_TABLE_NAME = "GeoPoints";
    public static final String FOOD_TRUCK_LIST_TABLE_NAME = "FoodTruckList";
    public static final String TOTAL_GAMEPLAY = "totalGameplay";
    public static final String TOTAL_GAMES = "totalGames";
    public static final String MAX_SCORE = "maxScore";

    // Second Materialized View
    public static final String HIGH_SCORES_BY_DATE_TABLE_NAME = "HighScoresByDate";

    // Lambda function tracking table
    public static final String FUNCTION_TRACKER_TABLE_NAME = "FunctionTracker";
    public static final String SEGMENT = "segment";
    public static final String STATUS = "status";
    public static final String LAST_SCORE_ID = "lastScoreId";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_INCOMPLETE = "INCOMPLETE";
    public static final String STATUS_DONE = "DONE";

    public static final AmazonDynamoDB dynamodb;
	public static final DynamoDB documentAPI;
	public static final DynamoDBMapper mapper;

    static {
    		dynamodb = AmazonDynamoDBClientBuilder.standard()
			    					.withRegion(Regions.US_WEST_1)
			                    .withCredentials(new ProfileCredentialsProvider(LOCAL_CRED_PROFILE_NAME))
			                    .build();

    		mapper = new DynamoDBMapper(dynamodb);
        documentAPI = new DynamoDB(dynamodb);
    }

}