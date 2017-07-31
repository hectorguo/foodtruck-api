package com.amazonaws.lambda.foodtrucks.datasetinit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;


@DynamoDBDocument
public class Location {
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String zip_code;
	private String country;
	private String state;
	private List<String> display_address;
	
	@DynamoDBAttribute(attributeName = "address1")
	public String getAddress1() { return address1; }
	public void setAddress1(String address1) { this.address1 = address1; }
	
	@DynamoDBAttribute(attributeName = "address2")
	public String getAddress2() { return address2; }
	public void setAddress2(String address2) { this.address2 = address2; }
	
	@DynamoDBAttribute(attributeName = "address3")
	public String getAddress3() { return address3; }
	public void setAddress3(String address3) { this.address3 = address3; }

	@DynamoDBAttribute(attributeName = "city")
	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }

	@DynamoDBAttribute(attributeName = "zip_code")
	public String getZipCode() { return zip_code; }
	public void setZipCode(String zip_code) { this.zip_code = zip_code; }
	
	@DynamoDBAttribute(attributeName = "country")
	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }
	
	@DynamoDBAttribute(attributeName = "state")
	public String getState() { return state; }
	public void setState(String state) { this.state = state; }
	
	@DynamoDBAttribute(attributeName = "display_address")
	public List<String> getDisplayAddress() { return display_address; }
	public void setDisplayAddress(List<String> display_address) { this.display_address = display_address; }
	
	public void init(JSONObject locationJson) {
		this.setAddress1(locationJson.optString("address1"));
		this.setAddress2(locationJson.optString("address2"));
		this.setAddress3(locationJson.optString("address3"));
		this.setCity(locationJson.getString("city"));
		this.setZipCode(locationJson.getString("zip_code"));
		this.setCountry(locationJson.getString("country"));
		this.setState(locationJson.getString("state"));
		
		JSONArray addrList = locationJson.getJSONArray("display_address");
		List<String> addrStrList = new ArrayList<String>();
		for(Object addr : addrList) {
			addrStrList.add(addr.toString());
		}
		this.setDisplayAddress(addrStrList);
	}
}
