package com.amazonaws.lambda.foodtrucks;

import com.amazonaws.lambda.foodtrucks.datasetinit.DataCrawler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaDataCrawlHandler implements RequestHandler<Request, Response> {

    @Override
    public Response handleRequest(Request request, Context context) {
        context.getLogger().log("Received event: " + request.toString());
        
        context.getLogger().log("Storing data from street food api...");
        DataCrawler.CrawlStreetFoodData();
        
        context.getLogger().log("Storing data from yelp api...");
        DataCrawler.CrawlYelpData();
        
//        List<String> cities = request.getCities();
//        int counter = 0;
        
//        for(String city : cities) {
//        		context.getLogger().log("Received cities: " + city);
//	        	try {
//	    			HttpResponse<JsonNode> res = fetch.getYelpData(city);
//	    			JSONArray businesses = res.getBody().getObject().getJSONObject("body").getJSONArray("businesses");
//	    			
//	    			for(int i = 0; i < businesses.length(); i++) {
//	    				JSONObject item = businesses.getJSONObject(i);
//	    				DataTransformer.FOOD_TRUCK_DETAIL.transform(item, mapper);
//	    				counter++;
//	    			}
//	    		
//	    		} catch (UnirestException e) {
//	    			throw new RuntimeException(e);
//	    		}
//        }
        String statusStr = String.format("Data Crawling finished");
        return new Response(statusStr);
        
        
        
//        for (DynamodbStreamRecord record : event.getRecords()) {
//            Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
//            if (newData == null) continue;  // ignore deletes
//
//            Item item = Item.fromMap(InternalCalls.toSimpleMapValue(newData));
//            DataTransformer.PLAYER_STATS_TRANSFORMER.transform(item, dynamodb);
//        }
//        for (DynamodbStreamRecord record : event.getRecords()) {
//            context.getLogger().log(record.getEventID());
//            context.getLogger().log(record.getEventName());
//            context.getLogger().log(record.getDynamodb().toString());
//        }
//        return event.getRecords().size();
    }
    
    
}