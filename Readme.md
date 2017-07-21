# MobileEats API

A RESTful Service for querying food trucks info based on location.

----------

<!-- TOC -->

- [MobileEats API](#mobileeats-api)
    - [Architecture](#architecture)
    - [APIs](#apis)
        - [Authentication](#authentication)
            - [Request](#request)
            - [Sample](#sample)
        - [Search Food Trucks](#search-food-trucks)
            - [Request](#request-1)
            - [Sample](#sample-1)
    - [Online Test](#online-test)

<!-- /TOC -->

## Architecture

![Architecture](https://ws1.sinaimg.cn/large/6d0af205ly1fhrdkmmjoej219l0ihn1p.jpg)

## APIs

The APIs are consistent with [Yelp's Fusion API](https://www.yelp.com/developers/documentation/v2/search_api), but it focuses on searching food trucks.

### Authentication

By posting this request, you will get a `access_token` and `token_type`.
Please attach the two parameters into HTTP Headers when you send a `GET` request for searching food trucks.

#### Request

```
POST /foodtrucks/auth
```

#### Sample

```
POST https://jirv8u2ell.execute-api.us-west-1.amazonaws.com/dev/foodtrucks/auth
```

**Response Body**

```json
{
    "access_token": "UOG4x25kRFF6bWtb-Sq8wP2J3mD9NZfSKbKdweHWO0nC7C-A5-ROuVH30RQ7_2tQrYpIAvOuIjI9OBtON8BtUb49la3UGXmc0B_tgTddC14pp0ceMTSHY_xxnyhtWXYx",
    "expires_in": 15270710,
    "token_type": "Bearer"
}
```

### Search Food Trucks

Search food trucks by city or geolocation.

#### Request

```
GET /foodtrucks/search
```

**Search Parameters**

Name | Data Type | Required / Optional | Description
---------|----------|---------|---------
location | string	| Required | if either latitude or longitude is not provided. Specifies the combination of "address, neighborhood, city, state or zip, optional country" to be used when searching for businesses.
latitude | decimal	| Required | if location is not provided. Latitude of the location you want to search nearby.
longitude | decimal	| Required | if location is not provided. Longitude of the location you want to search nearby.
radius | int	| Optional |  Search radius in meters. If the value is too large, a AREA_TOO_LARGE error may be returned. The max value is 40000 meters (25 miles).
categories | string	| Optional |  Categories to filter the search results with. See the list of supported categories. The category filter can be a list of comma delimited categories. For example, "bars,french" will filter by Bars and French. The category identifier should be used (for example "discgolf", not "Disc Golf").
locale | string	| Optional |  Specify the locale to return the business information in. See the list of supported locales.
limit | int	| Optional |  Number of business results to return. By default, it will return 20. Maximum is 50.
offset | int	| Optional |  Offset the list of returned business results by this amount.
sort_by | string	| Optional |  Sort the results by one of the these modes: best_match, rating, review_count or distance. By default it's best_match. The rating sort is not strictly sorted by the rating value, but by an adjusted rating value that takes into account the number of ratings, similar to a bayesian average. This is so a business with 1 rating of 5 stars doesn’t immediately jump to the top.
price | string	| Optional |  Pricing levels to filter the search result with: 1 = $, 2 = $$, 3 = $$$, 4 = $$$$. The price filter can be a list of comma delimited pricing levels. For example, "1, 2, 3" will filter the results to show the ones that are $, $$, or $$$.
open_now | boolean	| Optional |  Default to false. When set to true, only return the businesses open now. Notice that open_at and open_now cannot be used together.
open_at | int	| Optional |  An integer represending the Unix time in the same timezone of the search location. If specified, it will return business open at the given time. Notice that open_at and open_now cannot be used together.
attributes | string	| Optional |  Additional filters to restrict search results. Possible values are: <ul><li>**hot_and_new** - Hot and New businesses</li> <li>**request_a_quote** - Businesses that have the Request a Quote feature</li> <li>**waitlist_reservation** - Businesses that have an online waitlist</li> <li>**cashback** - Businesses that offer Cash Back</li> <li>**deals** - Businesses that offer Deals</li> <li>**gender_neutral_restrooms** - Businesses that provide gender neutral restrooms.</li></ul>  You can combine multiple attributes by providing a comma separated like "attribute1,attribute2". If multiple attributes are used, only businesses that satisfy ALL attributes will be returned in search results. For example, the attributes "hot_and_new,cashback" will return businesses that are Hot and New AND offer Cash Back.


#### Sample

```
GET https://jirv8u2ell.execute-api.us-west-1.amazonaws.com/dev/foodtrucks/search?latitude=37.3533530886479&longitude=-122.013784749846&radius_filter=500
```

**Request Headers**

`Authorization` === ${`token_type`} ${`access_token`}

```
Content-Type: application/json
Authorization: Bearer UOG4x25kRFF6bWtb-Sq8wP2J3mD9NZfSKbKdweHWO0nC7C-A5-ROuVH30RQ7_2tQrYpIAvOuIjI9OBtON8BtUb49la3UGXmc0B_tgTddC14pp0ceMTSHY_xxnyhtWXYx
```

**Response Body**

```json
{
    "statusCode": 200,
    "body": {
        "businesses": [
            {
                "id": "delhi-chaat-sunnyvale-2",
                "name": "Delhi Chaat",
                "image_url": "https://s3-media4.fl.yelpcdn.com/bphoto/g7ZZOXWzR5dAAu3OhJ-9hw/o.jpg",
                "is_closed": false,
                "url": "https://www.yelp.com/biz/delhi-chaat-sunnyvale-2?adjust_creative=XlnTMhO5pJ8whMNEegSKig&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=XlnTMhO5pJ8whMNEegSKig",
                "review_count": 170,
                "rating": 4,
                "coordinates": {
                    "latitude": 37.3676143,
                    "longitude": -122.0225156
                },
                "transactions": [],
                "price": "$",
                "location": {
                    "address1": "603 Old San Francisco Rd",
                    "address2": "",
                    "address3": "",
                    "city": "Sunnyvale",
                    "zip_code": "94086",
                    "country": "US",
                    "state": "CA",
                    "display_address": [
                        "603 Old San Francisco Rd",
                        "Sunnyvale, CA 94086"
                    ]
                },
                "phone": "",
                "display_phone": "",
                "distance": 726.874826268
            },
            ...
        ]
    }
}
```

Name | Data Type | Description
---------|----------|---------
businesses | object[] | A list of business Yelp finds based on the search criteria.
businesses[x].coordinates | object | The coordinates of this business.
businesses[x].coordinates.latitude | decimal | The latitude of this business.
businesses[x].coordinates.longitude | decimal | The longitude of this business.
businesses[x].display_phone | string | Phone number of the business formatted nicely to be displayed to users. The format is the standard phone number format for the business's country.
businesses[x].distance | decimal | The distance in meters from the search location. This returns meters regardless of the locale.
businesses[x].id | string | Yelp id of this business.
businesses[x].image_url | string | URL of photo for this business.
businesses[x].is_closed | bool | Whether business has been (permanently) closed
businesses[x].location | object | The location of this business, including address, city, state, zip code and country.
businesses[x].location.address1 | string | Street address of this business.
businesses[x].location.address2 | string | Street address of this business, continued.
businesses[x].location.address3 | string | Street address of this business, continued.
businesses[x].location.city | string | City of this business.
businesses[x].location.country | string | ISO 3166-1 alpha-2 country code of this business.
businesses[x].location.display_address | string[] | Array of strings that if organized vertically give an address that is in the standard address format for the business's country.
businesses[x].location.state | string | ISO 3166-2 (with a few exceptions) state code of this business.
businesses[x].location.zip_code | string | Zip code of this business.
businesses[x].name | string | Name of this business.
businesses[x].phone | string | Phone number of the business.
businesses[x].price | string | Price level of the business. Value is one of $, $$, $$$ and $$$$.
businesses[x].rating | decimal | Rating for this business (value ranges from 1, 1.5, ... 4.5, 5).
businesses[x].review_count | int | Number of reviews for this business.
businesses[x].url | string | URL for business page on Yelp.
businesses[x].transactions | string[] | A list of Yelp transactions that the business is registered for. Current supported values are "pickup", "delivery", and "restaurant_reservation".

## Online Test

You can test the APIs through POSTMAN

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/a5d66337d334e29a0c87)