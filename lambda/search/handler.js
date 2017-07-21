'use strict';
const request = require('request-promise-native');
const YELP_AUTH_API = 'https://api.yelp.com/oauth2/token';
const YELP_SEARCH_API = 'https://api.yelp.com/v3/businesses/search?term=foodtrucks';

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