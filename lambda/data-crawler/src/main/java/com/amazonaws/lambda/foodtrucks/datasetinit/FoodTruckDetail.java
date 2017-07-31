package com.amazonaws.lambda.foodtrucks.datasetinit;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import static com.amazonaws.lambda.foodtrucks.datasetinit.Constants.FOOD_TRUCK_LIST_TABLE_NAME;

@DynamoDBTable(tableName = FOOD_TRUCK_LIST_TABLE_NAME)
public class FoodTruckDetail {
	private String id;
	private String name;
	private Location location;
	private Coordinate coordinates;
	private String image_url;
	private String phone;
	private String url;
	private String display_phone;
	private double rating;
	
	@DynamoDBHashKey(attributeName = "id")
	public String getId() { return id; }
    public void setId(String id) { this.id = id; }
	
    @DynamoDBAttribute(attributeName = "name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @DynamoDBAttribute(attributeName = "image_url")
    public String getImageUrl() { return image_url; }
    public void setImageUrl(String image_url) { this.image_url = image_url; }
    
    @DynamoDBAttribute(attributeName = "url")
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    @DynamoDBAttribute(attributeName = "phone")
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    @DynamoDBAttribute(attributeName = "display_phone")
    public String getDisplayPhone() { return display_phone; }
    public void setDisplayPhone(String display_phone) { this.display_phone = display_phone; }
    
    @DynamoDBAttribute(attributeName = "rating")
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    @DynamoDBAttribute(attributeName = "location")
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    
    @DynamoDBAttribute(attributeName = "coordinates")
    public Coordinate getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinate coordinates) { this.coordinates = coordinates; }
    
}
