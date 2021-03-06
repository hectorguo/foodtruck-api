'use strict';
const request = require('request-promise-native');
const YELP_AUTH_API = 'https://api.yelp.com/oauth2/token';
const YELP_SEARCH_API = 'https://api.yelp.com/v3/businesses/search?term=foodtrucks';
const YELP_BUSINESS_API = 'https://api.yelp.com/v3/businesses';
const YELP_GRAPHQL_API = 'https://api.yelp.com/v3/graphql';

const YELP_CLIENT_ID = 'XlnTMhO5pJ8whMNEegSKig';
const YELP_CLIENT_SECRET = 'iV7Cd8axrxbGPTV2jtDX60tM99MCTW3kkEswBQ4JQDMfr3MsyDUyCkcxcxsz5I2w';
const YELP_GRANT_TYPE = 'client_credentials';

/**
 * GET /search
 */
module.exports.search = (event, context, callback) => {
  console.log('query:',event.query);
  // console.log('headers:', event.headers);
  // if(!token) {
  //   token = event.token;
  // }
  request.get({
    uri: YELP_SEARCH_API,
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
      const response = JSON.parse(res);
      
      callback(null, response);
      return response;
    })
    .catch((err) => {
      console.error(err.message);
      callback(null, err.message);
    });
}

module.exports.graphql = (event, context, callback) => {
  request.post({
    url: YELP_GRAPHQL_API,
    headers: {
      Authorization: event.token || "Bearer UOG4x25kRFF6bWtb-Sq8wP2J3mD9NZfSKbKdweHWO0nC7C-A5-ROuVH30RQ7_2tQrYpIAvOuIjI9OBtON8BtUb49la3UGXmc0B_tgTddC14pp0ceMTSHY_xxnyhtWXYx",
      "Content-Type": event.contentType || "application/graphql"
    },
    body: event.query
  })
  .then((res) => {
    console.log("Response =", res);
    return JSON.parse(res);
  })
  .then((res) => {
    callback(null, res);
  })
  .catch((err) => {
    console.error(err.message);
    callback(null, err.message);
  });
}