package com.amazonaws.lambda.foodtrucks.datasetinit;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class Coordinate {
	private double latitude;
	private double longitude;
	
	@DynamoDBAttribute(attributeName = "latitude")
	public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    @DynamoDBAttribute(attributeName = "longitude")
	public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
