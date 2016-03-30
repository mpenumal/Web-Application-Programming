var net = require('net');
var EventEmitter = require('events').EventEmitter;

var lumberjackEmitter = new EventEmitter();
var dataInput, clientName, command, value, intValue;
var clientRunningTotal = {};
var returnResult;
var server = net.createServer(function (sock) {
    console.log("Connection Established.");
    sock.on('data', function(data) {
        dataInput = data.toString().split(' ');
        clientName = dataInput[0].toString();
        command = dataInput[1].toString();
        value = dataInput[2];
        intValue = parseInt(value);

        if ((command != 'a' && command != 'm' && command != 's' && command != 'q') ||
            isNaN(intValue) || (parseFloat(value) % 1) !== 0) {
            console.log("Invalid request specification.");
        }
        else {
            if (clientName == 'ASU') {
                setTimeout(function() {
                        countOperations(sock, clientName, command, value, intValue);
                }, 30000);
            }
            else if (clientName == 'UA') {
                process.nextTick(function() {
                    countOperations(sock, clientName, command, value, intValue);
                });
            }
            else if (clientName == 'NAU') {
                lumberjackEmitter.emit('lumberjack');
                countOperations(sock, clientName, command, value, intValue);
            }
            else {
                countOperations(sock, clientName, command, value, intValue);
            }
        }
    });
});
server.on('error', function () {
    console.log("Error on the server side.");
});
server.listen(3000);

lumberjackEmitter.on('lumberjack', function() {
    returnResult = "I saw lumberjack!  ";
});

function countOperations(sock, clientName, command, value, intValue) {
    var temp = clientRunningTotal[clientName];
    switch (command) {
        case 'a':
            if (clientName in clientRunningTotal) {
                clientRunningTotal[clientName] = temp + intValue;
            }
            else {
                clientRunningTotal[clientName] = intValue;
            }
            returnResult += JSON.stringify(clientRunningTotal[clientName]);
            sock.write(returnResult, function () {
                console.log("Add operation Completed.");
            });
            break;
        case 'm':
            if (clientName in clientRunningTotal) {
                clientRunningTotal[clientName] = temp - intValue;
            }
            else {
                clientRunningTotal[clientName] = intValue;
            }
            returnResult += JSON.stringify(clientRunningTotal[clientName]);
            sock.write(returnResult, function () {
                console.log("Subtract operation Completed.");
            });
            break;
        case 's':
            if (clientName in clientRunningTotal) {
                clientRunningTotal[clientName] = intValue;
            }
            else {
                clientRunningTotal[clientName] = intValue;
            }
            returnResult += JSON.stringify(clientRunningTotal[clientName]);
            sock.write(returnResult, function () {
                console.log("Set operation Completed.");
            });
            break;
        case 'q':
            returnResult += JSON.stringify(clientRunningTotal);
            sock.write(returnResult, function () {
            });
            process.exit()
            break;
        default:
            console.log("Invalid request specification.");
            break;
    }
}

process.on('exit', function() {
    console.log("Server program exit.");
});