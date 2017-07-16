# Foodtruck API

A RESTful Service for querying food trucks info based on location.

## Architecture

## APIs

### Query food trucks

```
GET /api/trucks?location={:latitude,longitude}&distance={:mile}
```

Response Body:

```json
{
  "trucks": [
    {
      "distance": 0,
      "_id": "583b6c108741bd262f27fb18",
      "name": "aaa",
      ...
    },
    {
      "distance": 288,
      "_id": "581e85ebd77536215ec8b5d7",
      "name": "bbb",
      ...
    }
  ]
}
```