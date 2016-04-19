var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    firstname: {
        type: String
    },
    lastname: {
        type: String
    },
    language: {
        type: String
    },
    weekday: {
        type: String
    },
    location: {
        type: String
    }
}, {collection: 'userCollection'});

var User = module.exports = mongoose.model('User', userSchema);

module.exports.getUsers = function(callback, limit) {
    User.find(callback).limit(limit);
};

module.exports.getUsersCount = function(callback, limit) {
    User.count(callback).limit(limit);
};

module.exports.getUser = function(firstname, lastname, callback) {
    User.find({"firstname": firstname, "lastname": lastname}, callback);
};

module.exports.addUser = function(user, callback) {
    User.create(user, callback);
};

module.exports.updateUser = function(firstname, lastname, user, options, callback) {
    var query = {"firstname": firstname, "lastname": lastname};
    var update = {
        "firstname": user.firstname,
        "lastname": user.lastname,
        "language": user.language,
        "weekday": user.weekday,
        "location": user.location,
    }
    User.findOneAndUpdate(query, update, options, callback);
};