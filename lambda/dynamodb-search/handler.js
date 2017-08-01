'use strict';
const request = require('request-promise-native');
const YELP_AUTH_API = 'https://api.yelp.com/oauth2/token';
const YELP_SEARCH_API = 'https://api.yelp.com/v3/businesses/search?term=foodtrucks';
const YELP_BUSINESS_API = 'https://api.yelp.com/v3/businesses';

const YELP_CLIENT_ID = 'XlnTMhO5pJ8whMNEegSKig';
const YELP_CLIENT_SECRET = 'iV7Cd8axrxbGPTV2jtDX60tM99MCTW3kkEswBQ4JQDMfr3MsyDUyCkcxcxsz5I2w';
const YELP_GRANT_TYPE = 'client_credentials';

/**
 * Include AWS SDK
 */
const AWS = require('aws-sdk');
const docClient = new AWS.DynamoDB.DocumentClient({region:'eu-west-1'});


/**
 * GET /search
 */
module.exports.search = (event, context, callback) => {

  let searchResults = {
    TableName: 'Summary',
    Limit: 100
  };

  docClient.scan(searchResults, function(err,data){
    if(err){
      callback(err,null);
    }else{
      callback(null,data);
    }
  });

  //can use query instead of scan. can only query on primary key, and sort on secondary key.
/*
  var params = {
    TableName: 'Summary',
    Key:{
      "truckname": 2131
    }
  }

  docClient.get(params, function(err,data){
    if(err){
      callback(err,null);
    }else{
      callback(null,data);
    }
  });
*/

  //console.log('query:',event.query);
  // console.log('headers:', event.headers);
  // request.get({
  //   uri: YELP_SEARCH_API,
  //   qs: event.query,
  //   headers: {Authorization: event.token}
  // })
  // .then((res) => {
  //   const response = {
  //     statusCode: 200,
  //     body: JSON.parse(res)
  //   };

  //   callback(null, response);
  // })
  // .catch((err) => {
  //     console.error(err.message);
  //     callback(null, err.message);
  // });
};

/**
 * GET /{id}?locale=en_US
 */
module.exports.business = (event, context, callback) => {
  console.log('query:',event.query);
  // console.log('headers:', event.headers);
  // if(!token) {
  //   token = event.token;
  // }
  request.get({
    uri: `${YELP_BUSINESS_API}/${event.id}`,
    qs: event.query,
    headers: {Authorization: event.token}
  })
  .then((res) => {
    const response = {
      statusCode: 200,
      body: JSON.parse(res)
    };

    callback(null, response);
  })
  .catch((err) => {
      console.error(err.message);
      callback(null, err.message);
  });
  // Use this code if you don't use the http event with the LAMBDA-PROXY integration
  // callback(null, { message: 'Go Serverless v1.0! Your function executed successfully!', event });
};

module.exports.auth = (event, context, callback) => {
  const options = {
    url: YELP_AUTH_API,
    headers:
    {
      'cache-control': 'no-cache',
      'content-type': 'application/x-www-form-urlencoded'
    },
    form:
    {
      client_id: YELP_CLIENT_ID,
      client_secret: YELP_CLIENT_SECRET,
      grant_type: YELP_GRANT_TYPE
    }
  };

  request.post(options)
    .then((res) => {
      const response = {
        statusCode: 200,
        body: res
      };
      
      callback(null, response);
      return JSON.parse(response);
    })
    .then((resJson) => {
      token = `${token_type} ${access_token}`;
    })
    .catch((err) => {
      console.error(err.message);
      callback(null, err.message);
    });
}

// module.exports.hello = (event, context, callback) => {
//   const response = {
//     statusCode: 200,
//     body: JSON.stringify({
//       message: 'Go Serverless v1.0! Your function executed successfully!',
//       input: event,
//     }),
//   };

//   callback(null, response);

//   // Use this code if you don't use the http event with the LAMBDA-PROXY integration
//   // callback(null, { message: 'Go Serverless v1.0! Your function executed successfully!', event });
// };