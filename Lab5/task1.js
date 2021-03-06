var express = require('express'),
    ejs = require('ejs'),
    bodyParser = require('body-parser'),
    cookieParser = require('cookie-parser'),
    session = require('express-session');
var router = express.Router();

var array = [];

var app = express();
app.set('views', './');
app.set('view engine', 'ejs');
app.engine('html', ejs.renderFile);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser('COOKIEKEY'));
app.use(session({secret: 'SESSIONKEY'}));
app.listen(8081);

function loginLogic(req, res, login_type) {
    var name = [];

    if (req.signedCookies != null && req.signedCookies.user != null && req.signedCookies.user != '') {
        name = req.signedCookies.user.split(" ");
    }
    else {
        name.push(req.body.firstname);
        name.push(req.body.lastname);
    }

    res.cookie('user', name[0] + ' ' + name[1], {signed: true});

    res.status(200);
    res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
    TopMatches(req, res, name, login_type);
    //res.end();
}

function sessionClear(req) {
    delete req.session.firstname;
    delete req.session.lastname;
    delete req.session.language;
    delete req.session.weekday;
    delete req.session.location;
    delete req.session.lastpage;
}

function loginTypeLogic(login_type, req, saveresult) {
    if (login_type == 'submit') {
        if (req.session != null) {
            var clientDict = {};
            clientDict['firstname'] = req.session.firstname;
            clientDict['lastname'] = req.session.lastname;
            clientDict['language'] = req.session.language;
            clientDict['weekday'] = req.session.weekday;
            clientDict['location'] = req.session.location;

            if (clientDict['firstname'] && clientDict['lastname'] && clientDict['language'] &&
                clientDict['weekday'] && clientDict['location']) {
                if (array != null && array.length > 0) {
                    var userIndex = -1;

                    for (var i = 0; i < array.length; i++) {
                        if (array[i]['firstname'].toUpperCase() == clientDict['firstname'].toUpperCase() &&
                            array[i]['lastname'].toUpperCase() == clientDict['lastname'].toUpperCase()) {
                            userIndex = i;
                            break;
                        }
                    }
                    if (userIndex > -1) {
                        array[userIndex] = clientDict;
                    }
                    else {
                        array.push(clientDict);
                    }
                }
                else {
                    array.push(clientDict);
                }
                saveresult = 'success';
            }
            else {
                saveresult = 'fail';
            }

            sessionClear(req);
        }
        else {
            saveresult = 'fail';
        }
    }
    else if (login_type == 'cancel') {
        if (req.session != null) {
            sessionClear(req);
        }
        saveresult = 'cancel';
    }
    return saveresult;
}

function locationMatches(loc, userIndex) {
    var locMatches = [];

    for (var i = 0; i < array.length; i++) {
        if (i != userIndex && array[i]['location'] == loc) {
            locMatches.push(i);
        }
        if (locMatches.length == 3) {
            break;
        }
    }
    if (locMatches.length < 3) {
        for (var i = 0; i < array.length; i++) {
            if (i != userIndex && array[i]['location'].includes(loc)) {
                locMatches.push(i);
            }
            if (locMatches.length == 3) {
                break;
            }
        }
    }
    return locMatches;
}

function languageMatches(lang, userIndex) {
    var languageMatchResult = [];
    var langMatches = [];
    var langIncludes = [];
    var langMatch = {};
    langMatch['index'] = -1;
    langMatch['count'] = 0;

    var languageList = lang.split(",");

    for (var i = 0; i < array.length; i++) {
        if (i != userIndex) {
            var clientLanguageList = array[i]['language'].split(",");
            langMatch['index'] = i;
            for (var j = 0; j < languageList.length; j++) {
                for (var k = 0; k < clientLanguageList.length; k++) {
                    if (clientLanguageList[k].toUpperCase() == languageList[j].toUpperCase()) {
                        langMatch['count'] += 1;
                    }
                }
            }
            if (langMatch['index'] != -1 && langMatch['count'] > 0) {
                langMatches.push({'index':langMatch['index'], 'count':langMatch['count']});
            }
        }
        langMatch['index'] = -1;
        langMatch['count'] = 0;
    }

    if (langMatches.length < 3) {
        for (var i = 0; i < array.length; i++) {
            var flag = 1;
            for (var m = 0; m < langMatches.length; m++) {
                if (langMatches[m]['index'] == i) {
                    flag = 0;
                    break;
                }
            }
            if (i != userIndex && flag > 0) {
                var clientLanguageList = array[i]['language'].split(",");
                langMatch['index'] = i;
                for (var j = 0; j < languageList.length; j++) {
                    for (var k = 0; k < clientLanguageList.length; k++) {
                        if (clientLanguageList[k].toUpperCase().includes(languageList[j].toUpperCase())) {
                            langMatch['count'] += 1;
                        }
                    }
                }
                if (langMatch['index'] != -1 && langMatch['count'] > 0) {
                    langIncludes.push({'index': langMatch['index'], 'count': langMatch['count']});
                }
            }
            langMatch['index'] = -1;
            langMatch['count'] = 0;
        }
    }

    langMatches.sort(function(a,b) {return a.count-b.count});
    langIncludes.sort(function(a,b) {return a.count-b.count});

    for (var i = 0; i < langMatches.length; i++) {
        if (langMatches[i]['count'] > 0) {
            languageMatchResult.push(langMatches[i]['index']);
            if (languageMatchResult.length == 3) {
                break;
            }
        }
    }
    if (languageMatchResult.length < 3) {
        for (var i = 0; i < langIncludes.length; i++) {
            if (langIncludes[i]['count'] > 0) {
                languageMatchResult.push(langIncludes[i]['index']);
                if (languageMatchResult.length == 3) {
                    break;
                }
            }
        }
    }

    return languageMatchResult;
}

function weekdayMatches(day, userIndex) {
    var weekdayMatchResult = [];
    var dayMatches = [];
    var dayIncludes = [];
    var dayMatch = {};
    dayMatch['index'] = -1;
    dayMatch['count'] = 0;

    var weekdayList = day;

    for (var i = 0; i < array.length; i++) {
        if (i != userIndex) {
            var clientWeekdayList = array[i]['weekday'];
            dayMatch['index'] = i;
            for (var j = 0; j < weekdayList.length; j++) {
                for (var k = 0; k < clientWeekdayList.length; k++) {
                    if (clientWeekdayList[k].toUpperCase() == weekdayList[j].toUpperCase()) {
                        dayMatch['count'] += 1;
                    }
                }
            }
            if (dayMatch['index'] != -1 && dayMatch['count'] > 0) {
                dayMatches.push({'index':dayMatch['index'], 'count':dayMatch['count']});
            }
        }
        dayMatch['index'] = -1;
        dayMatch['count'] = 0;
    }

    if (dayMatches.length < 3) {
        for (var i = 0; i < array.length; i++) {
            var flag = 1;
            for (var m = 0; m < dayMatches.length; m++) {
                if (dayMatches[m]['index'] == i) {
                    flag = 0;
                    break;
                }
            }
            if (i != userIndex && flag > 0) {
                var clientWeekdayList = array[i]['weekday'];
                dayMatch['index'] = i;
                for (var j = 0; j < weekdayList.length; j++) {
                    for (var k = 0; k < clientWeekdayList.length; k++) {
                        if (clientWeekdayList[k].toUpperCase().includes(weekdayList[j].toUpperCase())) {
                            dayMatch['count'] += 1;
                        }
                    }
                }
                if (dayMatch['index'] != -1 && dayMatch['count'] > 0) {
                    dayIncludes.push({'index': dayMatch['index'], 'count': dayMatch['count']});
                }
            }
            dayMatch['index'] = -1;
            dayMatch['count'] = 0;
        }
    }

    dayMatches.sort(function(a,b) {return a.count-b.count});
    dayIncludes.sort(function(a,b) {return a.count-b.count});

    for (var i = 0; i < dayMatches.length; i++) {
        if (dayMatches[i]['count'] > 0) {
            weekdayMatchResult.push(dayMatches[i]['index']);
            if (weekdayMatchResult.length == 3) {
                break;
            }
        }
    }
    if (weekdayMatchResult.length < 3) {
        for (var i = 0; i < dayIncludes.length; i++) {
            if (dayIncludes[i]['count'] > 0) {
                weekdayMatchResult.push(dayIncludes[i]['index']);
                if (weekdayMatchResult.length == 3) {
                    break;
                }
            }
        }
    }

    return weekdayMatchResult;
}

function TopMatches(req, res, name, login_type) {
    var saveresult = '';
    saveresult = loginTypeLogic(login_type, req, saveresult);

    if (array != null && array.length > 0) {
        var userIndex = -1;

        for (var i = 0; i < array.length; i++) {
            if (array[i]['firstname'].toUpperCase() == name[0].toUpperCase() &&
                array[i]['lastname'].toUpperCase() == name[1].toUpperCase()) {
                userIndex = i;
                break;
            }
        }
        if (userIndex > -1) {

            var userDict = {};
            userDict['firstname'] = array[userIndex]['firstname'];
            userDict['lastname'] = array[userIndex]['lastname'];
            userDict['language'] = array[userIndex]['language'];
            userDict['weekday'] = array[userIndex]['weekday'];
            userDict['location'] = array[userIndex]['location'];

            var languageMatchIndex = languageMatches(userDict['language'], userIndex);
            var weekdayMatchIndex = weekdayMatches(userDict['weekday'], userIndex);
            var locationMatchIndex = locationMatches(userDict['location'], userIndex);

            var langMatches = [];
            var dayMatches = [];
            var locMatches = [];

            for (var m = 0; m < array.length; m++) {
                var langMatch = '';
                var dayMatch = '';
                var locMatch = '';
                if (languageMatchIndex != null && languageMatchIndex.length > 0 && languageMatchIndex.indexOf(m) >= 0) {
                    langMatch = 'First Name' + ': ' + array[m]['firstname'] + '; ';
                    langMatch += 'Last Name' + ': ' + array[m]['lastname'] + '; ';
                    langMatch += 'Known Languages' + ': ' + array[m]['language'] + '; ';
                    langMatch += 'Available days of the week' + ': ' + array[m]['weekday'] + '; ';
                    langMatch += 'Accessible Location' + ': ' + array[m]['location'] + '.';
                }

                if (weekdayMatchIndex != null && weekdayMatchIndex.length > 0 && weekdayMatchIndex.indexOf(m) >= 0) {
                    dayMatch = 'First Name' + ': ' + array[m]['firstname'] + '; ';
                    dayMatch += 'Last Name' + ': ' + array[m]['lastname'] + '; ';
                    dayMatch += 'Known Languages' + ': ' + array[m]['language'] + '; ';
                    dayMatch += 'Available days of the week' + ': ' + array[m]['weekday'] + '; ';
                    dayMatch += 'Accessible Location' + ': ' + array[m]['location'] + '.';
                }
                if (locationMatchIndex != null && locationMatchIndex.length > 0 && locationMatchIndex.indexOf(m) >= 0) {
                    locMatch = 'First Name' + ': ' + array[m]['firstname'] + '; ';
                    locMatch += 'Last Name' + ': ' + array[m]['lastname'] + '; ';
                    locMatch += 'Known Languages' + ': ' + array[m]['language'] + '; ';
                    locMatch += 'Available days of the week' + ': ' + array[m]['weekday'] + '; ';
                    locMatch += 'Accessible Location' + ': ' + array[m]['location'] + '.';
                }

                if (langMatch != '') {
                    langMatches.push(langMatch);
                }
                if (dayMatch != '') {
                    dayMatches.push(dayMatch);
                }
                if (locMatch != '') {
                    locMatches.push(locMatch);
                }
            }

            res.render('EJSTopMatches.ejs', {username: (name[0] + ' ' + name[1]), saveresult: saveresult,
                    match: 'positive', languagematch: langMatches, weekdaymatch: dayMatches, locationmatch: locMatches},
                function (err, html) {
                    if (err) {
                        req.session.err = err.status;
                        res.redirect('/error');
                    } else {
                        req.session.err = 0;
                        res.send(html);
                    }
                }
            );
        }
        else {
            res.render('EJSTopMatches.ejs', {username: (name[0] + ' ' + name[1]), saveresult: saveresult,
                match: 'negative'}, function (err, html) {
                if(err) {
                    res.redirect('/error');
                    req.session.err = err.status;
                } else {
                    req.session.err = 0;
                    res.send(html);
                }
            });
        }
    }
    else {
        res.render('EJSTopMatches.ejs', {username: (name[0] + ' ' + name[1]), saveresult: saveresult,
            match: 'negative'}, function (err, html) {
            if(err) {
                req.session.err = err.status;
                res.redirect('/error');
            } else {
                req.session.err = 0;
                res.send(html);
            }
        });
    }
}

app.get('/', function (req, res) {
    res.status(200);
    res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
    if (req.signedCookies == null || req.signedCookies.user == null) {
        res.render('EJSLandingPage.ejs', {}, function (err, html) {
            if (err) {
                req.session.err = err.status;
                res.redirect('/error');
            } else {
                req.session.err = 0;
                res.send(html);
            }
        });
    }
    else {
        var name = req.signedCookies.user.split(" ");
        TopMatches(req, res, name, 'login');
    }
});

app.get('/coders*', function (req, res) {
    var qsFirstName = req.query['firstname'];
    var qsLastName = req.query['lastname'];
    var qsLanguage = req.query['language'];
    var qsWeekday = req.query['weekday'];
    var qsLocation = req.query['location'];

    var weekflag = 1;
    var locflag = 1;

    if (qsWeekday != null && qsWeekday != '') {
        var weekdayList = qsWeekday.split(" ");
        var weekList = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
        for (var i = 0; i < weekdayList.length; i++) {
            if (weekList.indexOf(weekdayList[i].toUpperCase()) < 0) {
                weekflag = 0;
                break;
            }
        }
    }
    if (qsLocation != null && qsLocation != '') {
        var locList = ['DOWNTOWN', 'MESA', 'TEMPE', 'TUCSON', 'SCOTTSDALE', 'SEDONA'];
        if (locList.indexOf(qsLocation) < 0) {
            locflag = 0;
        }
    }

    if (locflag == 0 || weekflag == 0) {
        res.status(422);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.send('Unprocessable Entity: Input parameters not as expected.');
    }
    else {
        var page = '<html><head><title>Information Accumulator HTTP</title></head>';
        if (req.headers['user-agent'].includes('Chrome')) {
            var color = "#ff99ff";
            page += '<body bgcolor=' + color + '>';
        }
        else {
            page += '<body>';
        }

        var selectedClients = [];
        if (array != null && array.length > 0) {
            for (var i = 0; i < array.length; i++) {
                var fnflag = 0;
                var lnflag = 0;
                var langflag = 0;
                var dayflag = 0;
                var locflag = 0;

                if (qsFirstName == null || array[i]['firstname'].includes(qsFirstName)) {
                    fnflag = 1;
                }

                if (qsLastName == null || array[i]['lastname'].includes(qsLastName)) {
                    lnflag = 1;
                }

                if (qsLanguage == null) {
                    langflag = 1;
                }
                else {
                    var languageList = qsLanguage.split(" ");
                    var clientLanguageList = array[i]['language'].split(",");
                    for (var j = 0; j < languageList.length; j++) {
                        for (var k = 0; k < clientLanguageList.length; k++) {
                            if (clientLanguageList[k].toUpperCase().includes(languageList[j].toUpperCase())) {
                                langflag = 1;
                                break;
                            }
                        }
                        if (langflag == 1) {
                            break;
                        }
                    }
                }

                if (qsWeekday == null) {
                    dayflag = 1;
                }
                else {
                    var weekdayList = qsWeekday.split(" ");
                    var clientWeekdayList = array[i]['weekday'];
                    for (var j = 0; j < weekdayList.length; j++) {
                        for (var k = 0; k < clientWeekdayList.length; k++) {
                            if (clientWeekdayList[k].toUpperCase() == weekdayList[j].toUpperCase()) {
                                dayflag = 1;
                                break;
                            }
                        }
                        if (dayflag == 1) {
                            break;
                        }
                    }
                }

                if (qsLocation == null || array[i]['location'].includes(qsLocation)) {
                    locflag = 1;
                }

                if (fnflag == 1 && lnflag == 1 && langflag == 1 && dayflag == 1 && locflag == 1) {
                    selectedClients.push(i);
                }
            }
        }
        for (var i = 0; i < selectedClients.length; i++) {
            page += 'First Name: ' + array[selectedClients[i]]['firstname'] + '<br/>';
            page += 'Last Name: ' + array[selectedClients[i]]['lastname'] + '<br/>';
            page += 'Known Languages: ' + array[selectedClients[i]]['language'] + '<br/>';
            page += 'Available days of the week: ' + array[selectedClients[i]]['weekday'] + '<br/>';

            page += 'Accesible Location: ' + array[selectedClients[i]]['location'] + '<br/><br/>';
        }

        page += '</body></html>';

        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.send(page);
    }
});

app.post('/post_login', function (req, res) {
    var login_type = 'login';
    loginLogic(req, res, login_type);
});

app.post('/post_submit', function (req, res) {
    var login_type = 'submit';
    loginLogic(req, res, login_type);
});

app.post('/post_cancel', function (req, res) {
    var login_type = 'cancel';
    loginLogic(req, res, login_type);
});

app.post('/post_info_capture1', function (req, res) {
    if (req.signedCookies == null || req.signedCookies.user == null) {
        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.render('EJSLandingPage.ejs');
        res.end();
    }
    else {
        var text = 'text';
        var firstname = 'firstname';
        var lastname = 'lastname';
        var sess_firstname = '';
        var sess_lastname = '';
        var formname = 'Information Accumulator';
        var method = 'post';
        var action = 'post_info_capture2';
        var type = 'submit';
        var value = 'Next';

        if (req.session != null) {
            if (req.session.firstname != null) {
                sess_firstname = req.session.firstname;
            }
            if (req.session.lastname != null) {
                sess_lastname = req.session.lastname;
            }
            if (req.session.lastpage != null && req.session.lastpage == 2) {
                req.session.language = req.body.language;
            }
        }

        req.session.lastpage = 1;

        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        var response = '<html><head><title>Information Capture EJS</title></head><body>';

        response += '<form name='+formname+' method='+method+' action='+action+'><br>';
        response += 'First name: <input type='+text+' id='+firstname+' name='+firstname+' value='+sess_firstname+'>' +
            '&nbsp;&nbsp; Last name: <input type='+text+' id='+lastname+' name='+lastname+' value='+sess_lastname+'>' +
            '<br><br>';
        response += '<input type='+type+' value='+value+'></form>';

        response += '</body></html>';
        res.send(response);
    }
});

app.post('/post_info_capture2', function (req, res) {
    if (req.signedCookies == null || req.signedCookies.user == null) {
        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.render('EJSLandingPage.ejs');
        res.end();
    }
    else {
        var text = 'text';
        var lang = 'language';
        var sess_lang = '';
        var formname = 'Information Accumulator';
        var method = 'post';
        var action1 = 'post_info_capture1';
        var action2 = 'post_info_capture3';
        var type = 'submit';
        var next = 'Next';
        var prev = 'Previous';
        var rows = 1;
        var columns = 50;

        if (req.session != null) {
            if (req.session.language != null) {
                sess_lang = req.session.language;
            }
            if (req.session.lastpage != null && req.session.lastpage == 1) {
                req.session.firstname = req.body.firstname;
                req.session.lastname = req.body.lastname;
            }
            if (req.session.lastpage != null && req.session.lastpage == 3) {
                req.session.weekday = req.body.weekday;
            }
        }

        req.session.lastpage = 2;

        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        var response = '<html><head><title>Information Capture EJS</title></head><body>';

        response += '<form name=' + formname + ' method=' + method + '><br>';
        response += 'Known Languages (add comma seperated values):';
        response += ' <textarea rows=' + rows + ' cols=' + columns + ' name=' + lang + '>' + sess_lang + '</textarea><br><br>';
        response += '<input type=' + type + ' value=' + prev + ' formaction=' + action1 + '>&nbsp;';
        response += '<input type=' + type + ' value=' + next + ' formaction=' + action2 + '> </form>';

        response += '</body></html>';
        res.send(response);
    }
});

app.post('/post_info_capture3', function (req, res) {
    if (req.signedCookies == null || req.signedCookies.user == null) {
        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.render('EJSLandingPage.ejs');
        res.end();
    }
    else {
        var checkbox = 'checkbox';
        var weekday = 'weekday';
        var style = 'display:none;';
        var None = "None";
        var Monday = "Monday";
        var Tuesday = "Tuesday";
        var Wednesday = "Wednesday";
        var Thursday = "Thursday";
        var Friday = "Friday";
        var Saturday = "Saturday";
        var Sunday = "Sunday";
        var sess_days = [];
        var formname = 'Information Accumulator';
        var method = 'post';
        var action1 = 'post_info_capture2';
        var action2 = 'post_info_capture4';
        var type = 'submit';
        var next = 'Next';
        var prev = 'Previous';

        if (req.session != null) {
            if (req.session.weekday != null) {
                var temp_sess = req.session.weekday;
                for (var i = 0; i < temp_sess.length; i++) {
                    sess_days.push(temp_sess[i]);
                }
            }
            if (req.session.lastpage != null && req.session.lastpage == 2) {
                req.session.language = req.body.language;
            }
            if (req.session.lastpage != null && req.session.lastpage == 4) {
                req.session.location = req.body.location;
            }
        }

        req.session.lastpage = 3;

        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        var response = '<html><head><title>Information Capture EJS</title></head><body>';

        response += '<form name=' + formname + ' method=' + method + '><br>';

        response += 'Available days of the week:';
        response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + None + ' checked disabled style=' + style + '><br>';
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Monday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Monday + ' checked> Monday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Monday + ' > Monday';
        }
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Tuesday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Tuesday + ' checked> Tuesday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Tuesday + ' > Tuesday';
        }
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Wednesday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Wednesday + ' checked> Wednesday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Wednesday + ' > Wednesday';
        }
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Thursday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Thursday + ' checked> Thursday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Thursday + ' > Thursday';
        }
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Friday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Friday + ' checked> Friday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Friday + ' > Friday';
        }
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Saturday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Saturday + ' checked> Saturday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Saturday + ' > Saturday';
        }
        if (sess_days != null && sess_days.length > 0 && sess_days.indexOf('Sunday') >= 0) {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Sunday + ' checked> Sunday';
        }
        else {
            response += '<input type=' + checkbox + ' name=' + weekday + ' value=' + Sunday + ' > Sunday';
        }
        response += '<br><br>';

        response += '<input type=' + type + ' value=' + prev + ' formaction=' + action1 + '>&nbsp;';
        response += '<input type=' + type + ' value=' + next + ' formaction=' + action2 + '> </form>';

        response += '</body></html>';
        res.send(response);
    }
});

app.post('/post_info_capture4', function (req, res) {
    if (req.signedCookies == null || req.signedCookies.user == null) {
        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.render('EJSLandingPage.ejs');
        res.end();
    }
    else {
        var radio = 'radio';
        var checked = 'checked';
        var location = 'location';
        var Downtown = "Downtown";
        var Mesa = "Mesa";
        var Tempe = "Tempe";
        var Tucson = "Tucson";
        var Scottsdale = "Scottsdale";
        var Sedona = "Sedona";
        var sess_loc = '';
        var formname = 'Information Accumulator';
        var method = 'post';
        var action1 = 'post_info_capture3';
        var action2 = 'post_info_review';
        var type = 'submit';
        var next = 'Next';
        var prev = 'Previous';

        if (req.session != null) {
            if (req.session.location != null) {
                sess_loc = req.session.location;
            }
            if (req.session.lastpage != null && req.session.lastpage == 3) {
                req.session.weekday = req.body.weekday;
            }
        }

        req.session.lastpage = 4;

        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        var response = '<html><head><title>Information Capture EJS</title></head><body>';

        response += '<form name=' + formname + ' method=' + method + '><br>';

        response += 'Accesible Location:<br>';
        if (sess_loc == null || sess_loc == '') {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Downtown + ' checked=' + checked + '> Downtown';
        }
        else {
            if (sess_loc == Downtown) {
                response += '<input type=' + radio + ' name=' + location + ' value=' + Downtown + ' checked=' + checked + '> Downtown';
            }
            else {
                response += '<input type=' + radio + ' name=' + location + ' value=' + Downtown + '> Downtown';
            }
        }
        if (sess_loc != null && sess_loc == Mesa) {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Mesa + ' checked=' + checked + '> Mesa';
        }
        else {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Mesa + '> Mesa';
        }
        if (sess_loc != null && sess_loc == Tempe) {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Tempe + ' checked=' + checked + '> Tempe';
        }
        else {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Tempe + '> Tempe';
        }
        if (sess_loc != null && sess_loc == Tucson) {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Tucson + ' checked=' + checked + '> Tucson';
        }
        else {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Tucson + '> Tucson';
        }
        if (sess_loc != null && sess_loc == Scottsdale) {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Scottsdale + ' checked=' + checked + '> Scottsdale';
        }
        else {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Scottsdale + '> Scottsdale';
        }
        if (sess_loc != null && sess_loc == Sedona) {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Sedona + ' checked=' + checked + '> Sedona';
        }
        else {
            response += '<input type=' + radio + ' name=' + location + ' value=' + Sedona + '> Sedona';
        }
        response += '<br><br>';

        response += '<input type=' + type + ' value=' + prev + ' formaction=' + action1 + '>&nbsp;';
        response += '<input type=' + type + ' value=' + next + ' formaction=' + action2 + '> </form>';

        response += '</body></html>';
        res.send(response);
    }
});

app.post('/post_info_review', function (req, res) {
    if (req.signedCookies == null || req.signedCookies.user == null) {
        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        res.render('EJSLandingPage.ejs');
        res.end();
    }
    else {
        var text = 'text';
        var firstname = 'firstname';
        var lastname = 'lastname';
        var sess_firstname = '';
        var sess_lastname = '';
        var sess_lang = '';
        var sess_days = [];
        var sess_loc = '';
        var formname = 'Information Accumulator';
        var method = 'post';
        var action1 = 'post_info_capture4';
        var action2 = 'post_submit';
        var action3 = 'post_cancel';
        var type = 'submit';
        var prev = 'Previous';
        var submit = 'Submit';
        var cancel = 'Cancel';

        if (req.session != null) {
            if (req.session.firstname != null) {
                sess_firstname = req.session.firstname;
            }
            if (req.session.lastname != null) {
                sess_lastname = req.session.lastname;
            }
            if (req.session.language != null) {
                sess_lang = req.session.language;
            }
            if (req.session.weekday != null) {
                sess_days = req.session.weekday;
            }
            if (req.session.lastpage != null && req.session.lastpage == 4) {
                req.session.location = req.body.location;
                sess_loc = req.body.location;
            }
            else if (req.session.location != null) {
                sess_loc = req.session.location;
            }
        }

        req.session.lastpage = 5;

        res.status(200);
        res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
        var response = '<html><head><title>Information Capture EJS</title></head><body>';

        response += '<form name=' + formname + ' method=' + method + '><br>';

        response += 'First name: ' + sess_firstname + '&nbsp;&nbsp;Last name: ' + sess_lastname + '<br>';
        response += 'Known Languages: ' + sess_lang + '<br>';
        response += 'Available days of the week: ' + sess_days + '<br>';
        response += 'Accesible Location: ' + sess_loc + '<br><br>';

        response += '<input type=' + type + ' value=' + prev + ' formaction=' + action1 + '>&nbsp;';
        response += '<input type=' + type + ' value=' + submit + ' formaction=' + action2 + '>&nbsp;';
        response += '<input type=' + type + ' value=' + cancel + ' formaction=' + action3 + '> </form>';

        response += '</body></html>';
        res.send(response);
    }
});

router.get('/firstname/:name', function(req, res) {
    var nameParameter = req.params.name;
    var page = '<html><head><title>Information Accumulator HTTP</title></head><body>';

    for (var i = 0; i < array.length; i++) {
        if (array[i]['firstname'].includes(nameParameter)) {
            page += 'First Name: ' + array[i]['firstname'] + '<br/>';
            page += 'Last Name: ' + array[i]['lastname'] + '<br/>';
            page += 'Known Languages: ' + array[i]['language'] + '<br/>';
            page += 'Available days of the week: ' + array[i]['weekday'] + '<br/>';
            page += 'Accesible Location: ' + array[i]['location'] + '<br/><br/>';
        }
    }
    page += '</body></html>';
    res.status(200);
    res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
    res.send(page);
});

router.get('/lastname/:name', function(req, res) {
    var nameParameter = req.params.name;
    var page = '<html><head><title>Information Accumulator HTTP</title></head><body>';

    for (var i = 0; i < array.length; i++) {
        if (array[i]['lastname'].includes(nameParameter)) {
            page += 'First Name: ' + array[i]['firstname'] + '<br/>';
            page += 'Last Name: ' + array[i]['lastname'] + '<br/>';
            page += 'Known Languages: ' + array[i]['language'] + '<br/>';
            page += 'Available days of the week: ' + array[i]['weekday'] + '<br/>';
            page += 'Accesible Location: ' + array[i]['location'] + '<br/><br/>';
        }
    }
    page += '</body></html>';
    res.status(200);
    res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
    res.send(page);
});

app.use('/get_coder', router);

app.get('/error', function (req, res) {
    if (req.session.err && req.session.err != 0) {
        res.status(req.session.err);
        if (req.session.err == 404) {
            res.send("File does not exist.");
        }
        else if (req.session.err == 400) {
            res.send("This is a bad request.");
        }
        else {
            res.send("We got a " + req.session.err + " error.");
        }
    }
    else {
        res.status(404);
        res.send("File does not exist.");
    }
});

app.post('/error', function (req, res) {
    if (req.session.err && req.session.err != 0) {
        res.status(req.session.err);
        if (req.session.err == 404) {
            res.send("File does not exist.");
        }
        else if (req.session.err == 400) {
            res.send("This is a bad request.");
        }
        else {
            res.send("We got a " + req.session.err + " error.");
        }
    }
    else {
        res.status(404);
        res.send("File does not exist.");
    }
});

app.get('/*', function (req, res) {
    res.status(405);
    res.send("This method is not allowed.");
});

app.post('/*', function (req, res) {
    res.status(405);
    res.send("This method is not allowed.");
});