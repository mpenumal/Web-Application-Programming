
var http = require('http'),
    url = require('url'),
    q = require('querystring'),
    fs = require('fs');

var array = [];

http.createServer(function (req, res) {
    if (req.method == "GET") {
        if (req.url == "/") {
            fs.readFile('InformationCaptureForm.html', function (err, data) {
                if (err) {
                    res.writeHead(404);
                    res.end(JSON.stringify(err));
                    return;
                }
                res.writeHead(200, {'Content-Type': 'text/html', 'Content-Length': data.length,
                                    'Cache-Control': 'no-cache'});
                res.write(data);
                res.end();
            });
        }

        else if (req.url.lastIndexOf('/coders', 0) === 0) {
            var page = '<html><head><title>Information Accumulator HTTP</title></head>';
            if (req.headers['user-agent'].includes('Chrome')) {
                var color = "#ff99ff";
                page += '<body bgcolor=' + color + '>';
            }
            else {
                page += '<body>';
            }

            var qs = q.parse(url.parse(req.url).query);
            qsFirstName = qs['firstname'];
            qsLastName = qs['lastname'];
            qsLanguage = qs['language'];
            qsWeekday = qs['weekday'];
            qsLocation = qs['location'];

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
            }

            for (var i = 0; i < selectedClients.length; i++) {
                page += 'First Name: ' + array[selectedClients[i]]['firstname'] + '<br/>';
                page += 'Last Name: ' + array[selectedClients[i]]['lastname'] + '<br/>';
                page += 'Known Languages: ' + array[selectedClients[i]]['language'] + '<br/>';
                page += 'Available days of the week: ' + array[selectedClients[i]]['weekday'] + '<br/>';
                page += 'Accesible Location: ' + array[selectedClients[i]]['location'] + '<br/><br/>';
            }
            page += '</body></html>';
            res.writeHead(200, {'Content-Type': 'text/html', 'Cache-Control': 'no-cache'});
            res.end(page);
        }

        else {
            res.writeHead(405, "Method not allowed", {'Content-Type': 'text/plain'});
            res.end("Method not allowed.");
        }
    }

    else if (req.method == "POST") {
        if (req.url == "/post_coder") {
            var postParameters = '';
            var positiveMessage = 'The Data was received successfully.';
            var negetiveMessage = 'Data was not received successfully. Please enter data in all input fields.';

            req.on('data', function(data) {
                postParameters += data;
            });
            req.on('end', function() {
                var postValues = q.parse(postParameters);
                fs.readFile('InformationCaptureForm.html',function (err, data){
                    if (err) {
                        res.writeHead(404);
                        res.end(JSON.stringify(err));
                        return;
                    }
                    res.writeHead(200, {'Content-Type': 'text/html'});
                    if (!postValues.firstname || !postValues.lastname || !postValues.language
                        || !postValues.weekday || !postValues.location) {
                        res.write(negetiveMessage +'<br/>');
                        res.write('Number of entries = ' + array.length);
                    }
                    else {
                        var clientDict = {};
                        clientDict['firstname'] = postValues.firstname;
                        clientDict['lastname'] = postValues.lastname;
                        clientDict['language'] = postValues.language;
                        clientDict['weekday'] = postValues.weekday;
                        clientDict['location'] = postValues.location;

                        array.push(clientDict);

                        res.write(positiveMessage +'<br/>');
                        res.write('Number of entries = ' + array.length);
                    }
                    res.write(data);
                    res.end();
                });
            })
        }
        else {
            res.writeHead(405, "Method not allowed", {'Content-Type': 'text/plain'});
            res.end("Method not allowed.");
        }
    }
}).listen(8081);
