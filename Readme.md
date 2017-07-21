# MobileEats API

A RESTful Service for querying food trucks info based on location.

----------

<!-- TOC -->

- [MobileEats API](#mobileeats-api)
    - [Architecture](#architecture)
    - [APIs](#apis)
        - [Authentication](#authentication)
            - [Sample Request](#sample-request)
        - [Search Food Trucks](#search-food-trucks)
            - [Sample Request](#sample-request-1)
    - [Online Test](#online-test)

<!-- /TOC -->

## Architecture

![Architecture](https://ws1.sinaimg.cn/large/6d0af205ly1fhrdkmmjoej219l0ihn1p.jpg)

## APIs

The APIs are consistent with [Yelp's Fusion API](https://www.yelp.com/developers/documentation/v2/search_api), but it focuses on searching food trucks.

### Authentication

By posting this request, you will get a `access_token` and `token_type`.
Please attach the two parameters into HTTP Headers when you send a `GET` request for searching food trucks.

```
POST https://jirv8u2ell.execute-api.us-west-1.amazonaws.com/dev/foodtrucks/auth
```

#### Sample Request

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

```
GET https://jirv8u2ell.execute-api.us-west-1.amazonaws.com/dev/foodtrucks/search?location={:location}
```

**Search Parameters**

Name | Data Type | Required / Optional | Description
---------|----------|---------|---------
limit | number | optional | Number of business results to return
offset | number | optional | Offset the list of returned business results by this amount
sort | number | optional | Sort mode: 0=Best matched (default), 1=Distance, 2=Highest Rated. If the mode is 1 or 2 a search may retrieve an additional 20 businesses past the initial limit of the first 20 results. This is done by specifying an offset and limit of 20. Sort by distance is only supported for a location or geographic search. The rating sort is not strictly sorted by the rating value, but by an adjusted rating value that takes into account the number of ratings, similar to a bayesian average. This is so a business with 1 rating of 5 stars doesn’t immediately jump to the top.
category_filter | string | optional | Category to filter search results with. See the list of supported categories. The category filter can be a list of comma delimited categories. For example, 'bars,french' will filter by Bars and French. The category identifier should be used (for example 'discgolf', not 'Disc Golf').
radius_filter | number | optional | Search radius in meters. If the value is too large, a AREA_TOO_LARGE error may be returned. The max value is 40000 meters (25 miles).
deals_filter | bool | optional | Whether to exclusively search for businesses with deals

#### Sample Request

**Request Headers**

Authorization === ${`token_type`} ${`access_token`}

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
                "categories": [
                    {
                        "alias": "indpak",
                        "title": "Indian"
                    },
                    {
                        "alias": "streetvendors",
                        "title": "Street Vendors"
                    }
                ],
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

## Online Test

You can test the APIs through POSTMAN

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/a5d66337d334e29a0c87)