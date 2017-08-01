import {
  GraphQLObjectType,
  GraphQLSchema,
  GraphQLList,
  GraphQLString,
  GraphQLNonNull
} from 'graphql';

import {
  GraphQLLimitedString
} from 'graphql-custom-types';

import { getPosts, getAuthor, getAuthors, getComments, createPost } from './dynamo';

const Detail = new GraphQLObjectType({
  name: "Detail",
  description: "Detail of a food truck",
  fields: () => ({
        id: {type: GraphQLString},
        name: {type: GraphQLString},
        location: {
            type: Location,
            resolve: function({id}) {
                return getLocation(id);
            }
        },
        
    })
});

const Comment = new GraphQLObjectType({
  name: "Comment",
  description: "Comment on the blog post",
  fields: () => ({
      id: {type: GraphQLString},
      content: {type: GraphQLString},
      author: {
        type: Author,
        resolve: function({author}) {
          return getAuthor(author);
        }
      }
    })
});

const Post = new GraphQLObjectType({
  name: "Post",
  description: "Blog post content",
  fields: () => ({
    id: {type: GraphQLString},
    title: {type: GraphQLString},
    bodyContent: {type: GraphQLString},
    author: {
      type: Author,
      resolve: function({author}) {
        return getAuthor(author);
      }
    },
    comments: {
      type: new GraphQLList(Comment),
      resolve: function(post) {
        return getComments();
      }
    }
  })
});

const Query = new GraphQLObjectType({
  name: 'BlogSchema',
  description: "Root of the Blog Schema",
  fields: () => ({
    posts: {
      type: new GraphQLList(Post),
      description: "List of posts in the blog",
      resolve: function(source, {category}) {
        return getPosts();
      }
    },
    authors: {
      type: new GraphQLList(Author),
      description: "List of Authors",
      resolve: function() {
        return getAuthors();
      }
    },
    author: {
      type: Author,
      description: "Get Author by id",
      args: {
        id: {type: new GraphQLNonNull(GraphQLString)}
      },
      resolve: function(source, {id}) {
        return getAuthor(author);
      }
    }
  })
});

const Mutuation = new GraphQLObjectType({
  name: 'BlogMutations',
  fields: {
    createPost: {
      type: Post,
      description: "Create blog post",
      args: {
        id: {type: new GraphQLNonNull(GraphQLString)},
        title: {type: new GraphQLLimitedString(10, 30)},
        bodyContent: {type: new GraphQLNonNull(GraphQLString)},
        author: {type: new GraphQLNonNull(GraphQLString), description: "Id of the author"}
      },
      resolve: function(source, args) {
        return createPost(args);
      }
    }
  }
});

const Schema = new GraphQLSchema({
  query: Query,
  mutation: Mutuation
});

export default Schema;

curl -X POST -H "Authorization: Bearer UOG4x25kRFF6bWtb-Sq8wP2J3mD9NZfSKbKdweHWO0nC7C-A5-ROuVH30RQ7_2tQrYpIAvOuIjI9OBtON8BtUb49la3UGXmc0B_tgTddC14pp0ceMTSHY_xxnyhtWXYx" -H "Content-Type: application/graphql" https://api.yelp.com/v3/graphql --data '
{
  search(term: "burrito",
         location: "san francisco") {
    total
    business {
      name
      rating
      review_count
      location {
        address1
        city
        state
        country
      }
      hours {
        open {
          start
          end
        }
      }
    }
  }
}'