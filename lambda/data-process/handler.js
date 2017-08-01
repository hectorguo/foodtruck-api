'use strict';
// Thanks to https://github.com/rh389/dynamodb-geo.js/tree/master/example

const ddbGeo = require('dynamodb-geo');
const AWS = require('aws-sdk');
const uuid = require('uuid');
const unmarshalItem = require('dynamodb-marshaler').unmarshalItem;

const CREDENTIAL_PROFILE = 'silver-lining';
const REGION = 'us-west-1';

// Set up AWS
// const credentials = new AWS.SharedIniFileCredentials({profile: CREDENTIAL_PROFILE});
// AWS.config.credentials = credentials;
// AWS.config.update({
//     // accessKeyId: '',
//     // secretAccessKey: '',
//     region: REGION
// });

// Set up Database 
const TABLE_NAME = 'Summary';
const END_POINT = 'https://dynamodb.us-west-1.amazonaws.com';

const ddb = new AWS.DynamoDB({ endpoint: new AWS.Endpoint(END_POINT) });

// Configuration for a new instance of a GeoDataManager. Each GeoDataManager instance represents a table
const config = new ddbGeo.GeoDataManagerConfiguration(ddb, TABLE_NAME);

// Instantiate the table manager
const capitalsManager = new ddbGeo.GeoDataManager(config);

// Use GeoTableUtil to help construct a CreateTableInput.
const createTableInput = ddbGeo.GeoTableUtil.getCreateTableRequest(config);


function createTable() {
  // Tweak the schema as desired
  createTableInput.ProvisionedThroughput.ReadCapacityUnits = 2;
  createTableInput.ProvisionedThroughput.WriteCapacityUnits = 2;
  console.log('Creating table with schema:');
  console.dir(createTableInput, { depth: null });

  // Create the table
  return ddb.createTable(createTableInput).promise()
    // Wait for it to become ready
    .then(function () { return ddb.waitFor('tableExists', { TableName: config.tableName }).promise() });
}

function mappingToGeo(data) {
  console.log('Loading sample data from capitals.json');
  // const data = require('./capitals.json');
  return data.map((capital) => {
      const location = capital.location.M;
      return {
          RangeKeyValue: capital.id, // Use this to ensure uniqueness of the hash/range pairs.
          GeoPoint: {
              latitude: capital.coordinates.M.latitude.N * 1,
              longitude: capital.coordinates.M.longitude.N * 1
          },
          PutItemInput: {
              Item: {
                  country: location.country,
                  state: location.state,
                  city: location.city,
                  address1: location.address1,
                  zip_code: location.zip_code
              }
          }
      }
  });
}

function mappingToSummary(data) {
  console.log('Loading sample data from capitals.json');
  // const data = require('./capitals.json');
  return data.map((capital) => {
      return {
          RangeKeyValue: capital.id, // Use this to ensure uniqueness of the hash/range pairs.
          GeoPoint: {
              latitude: capital.coordinates.M.latitude.N * 1,
              longitude: capital.coordinates.M.longitude.N * 1
          },
          PutItemInput: {
              Item: {
                  location: capital.location,
                  name: capital.name,
                  rating: capital.rating,
                  url: capital.url,
                  image_url: capital.image_url,
                  display_phone: capital.display_phone,
                  phone: capital.phone
              }
          }
      }
  });
}

function putItems(items) {
  const BATCH_SIZE = 25;
  const WAIT_BETWEEN_BATCHES_MS = 1000;
  var currentBatch = 1;
  const ITEMS_COUNT = items.length;

  function resumeWriting() {
      if (items.length === 0) {
          return Promise.resolve();
      }
      const thisBatch = [];
      for (var i = 0, itemToAdd = null; i < BATCH_SIZE && (itemToAdd = items.shift()); i++) {
          thisBatch.push(itemToAdd);
      }
      console.log('Writing batch ' + (currentBatch++) + '/' + Math.ceil(ITEMS_COUNT / BATCH_SIZE));
      return capitalsManager.batchWritePoints(thisBatch).promise()
          .then(function () {
              return new Promise(function (resolve) {
                  setInterval(resolve,WAIT_BETWEEN_BATCHES_MS);
              });
          })
          .then(function () {
              return resumeWriting()
          });
  }

  return resumeWriting().then(() => ITEMS_COUNT).catch(function (error) {
      console.warn(error);
  });
}

function queryByGeo(lat, lon, radiusInMeter = 2500) {
  console.log(`Querying by radius, looking 2.5km from ${lat}, ${lon}`);
  return capitalsManager.queryRadius({
      RadiusInMeter: radiusInMeter,
      CenterPoint: {
          latitude: lat,
          longitude: lon
      }
  })
}


function deleteTable() {
  return ddb.deleteTable({ TableName: config.tableName }).promise()
}


function query() {
  const params = {
    TableName : "FoodTruckList",
    KeyConditionExpression: "rating < :val",
    // ExpressionAttributeNames:{
    //     "#rt": "rating"
    // },
    ExpressionAttributeValues: {
        ":val": {"N": "6"}
    }
  }
  return ddb.query(params).promise()
  .then(console.log)
  .catch(console.error)
}

/**
 * Scan
 * 
 * @param {Object} params 
 * {
 *  TableName: "Movies",
 *  ProjectionExpression: "#yr, title, info.rating",
 *  FilterExpression: "#yr between :start_yr and :end_yr",
 *  ExpressionAttributeNames: {
 *      "#yr": "year",
 *  },
 *  ExpressionAttributeValues: {
 *       ":start_yr": 1950,
 *       ":end_yr": 1959 
 *  }
 * }
 * @returns 
 */
function scan(params) {
  return ddb.scan(params).promise();
}

// scan({
//     TableName: "FoodTruckList",
//     ProjectionExpression: "id, coordinates, #loc",
//     // Limit: 6,
//     // FilterExpression: "#yr between :start_yr and :end_yr",
//     ExpressionAttributeNames: {
//         "#loc": "location",
//     },
//     // ExpressionAttributeValues: {
//     //      ":start_yr": 1950,
//     //      ":end_yr": 1959 
//     // }
// })
// .then((res) => {
//   const items = mapping(res.Items);
//   return putItems(items);
// })
// .then(console.log)
// .catch(console.error)

module.exports.processData = (event, context, callback) => {
  scan({
    TableName: "FoodTruckList",
    ProjectionExpression: "id, #na, coordinates, #loc, rating, #url, image_url, display_phone, phone",
    // Limit: 6,
    // FilterExpression: "#yr between :start_yr and :end_yr",
    ExpressionAttributeNames: {
        "#loc": "location",
        "#na": "name",
        "#url": "url"
    }
    // ExpressionAttributeValues: {
    //      ":start_yr": 1950,
    //      ":end_yr": 1959 
    // }
  })
  .then((res) => {
    const items = mappingToSummary(res.Items);
    return putItems(items);
  })
  .then((itemsCount) => {
    const response = {
      statusCode: 201,
      body: {
        message: `${itemsCount} items have been put in TABLE: ${config.tableName}`,
        input: event,
      }
    };
    callback(null, response);
  })
  .catch(console.error);
  
};


module.exports.queryByLatLon = (event, context, callback) => {
  if(!event.latitude || !event.longitude) {
    callback(null, {
      statusCode: 400,
      body: {
        message: 'Params Error: latitude or longitude is empty'
      }
    });
    return;
  }

  queryByGeo(event.latitude, event.longitude, event.radiusInMeter)
  .then((res) => {
    const response = {
      statusCode: 200,
      body: {
        businesses: res.map(unmarshalItem)  // transfer dynamoDB data to JSON
      }
    };
    callback(null, response);
  })
  .catch(console.error);
  
};
