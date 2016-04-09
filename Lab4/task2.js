
var express = require('express'),
    url = require('url'),
    q = require('querystring'),
    ejs = require('ejs'),
    bodyParser = require('body-parser');
var router = express.Router();

var array = [];

var app = express();
app.set('views', './');
app.set('view engine', 'ejs');
app.engine('html', ejs.renderFile);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.listen(8081);

app.get('/', function (req, res) {
    res.status(200);
    res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
    res.render('EJSInformationCaptureForm.ejs', {message: ''});
    res.end();
});

app.get('/coders*', function (req, res) {

    var fullUrl = req.protocol + '://' + req.get('host') + req.originalUrl;

    var qs = q.parse(url.parse(req.url).query);
    qsFirstName = qs['firstname'];
    qsLastName = qs['lastname'];
    qsLanguage = qs['language'];
    qsWeekday = qs['weekday'];
    qsLocation = qs['location'];

    var page = '<html><head><title>Information Accumulator HTTP</title></head>';
    if (req.headers['user-agent'].includes('Chrome')) {
        var color = "#ff99ff";
        page += '<body bgcolor=' + color + '>';
    }
    else {
        page += '<body>';
    }

    var selectedClients = [];
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
            var clientWeekdayList = array[i]['weekday'].split(",");
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

        for (var i = 0; i < selectedClients.length; i++) {
            page += 'First Name: ' + array[selectedClients[i]]['firstname'] + '<br/>';
            page += 'Last Name: ' + array[selectedClients[i]]['lastname'] + '<br/>';
            page += 'Known Languages: ' + array[selectedClients[i]]['language'] + '<br/>';
            page += 'Available days of the week: ' + array[selectedClients[i]]['weekday'] + '<br/>';
            page += 'Accesible Location: ' + array[selectedClients[i]]['location'] + '<br/><br/>';
        }
    }
    page += '</body></html>';

    res.status(200);
    res.set({'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
    res.send(page);
});

app.post('/post_coder', function (req, res) {
    var postResult = 'negative';
    var clientDict = {};
    clientDict['firstname'] = req.body.firstname;
    clientDict['lastname'] = req.body.lastname;
    clientDict['language'] = req.body.language;
    clientDict['weekday'] = req.body.weekday;
    clientDict['location'] = req.body.location;

    if (clientDict['firstname'] && clientDict['lastname'] && clientDict['language'] &&
        clientDict['weekday'] && clientDict['location']) {
        array.push(clientDict);
        postResult = 'positive';
    }
    res.status(200);
    res.set({'Content-Type': 'text/html'});
    res.render('EJSInformationCaptureForm.ejs', {message: postResult, count: array.length});
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
    res.status(400);
    res.send("This is a bad request.");
});

app.post('/error', function (req, res) {
    res.status(400);
    res.send("This is a bad request.");
});

app.get('/*', function (req, res) {
    res.status(405);
    res.send("This method is not allowed.");
});

app.post('/*', function (req, res) {
    res.status(405);
    res.send("This method is not allowed.");
});