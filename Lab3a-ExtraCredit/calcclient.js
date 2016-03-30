// simplesockclient to go with simplesockserver.js
var sock = require('net').Socket(),
    clientName = process.argv[2],
    command = process.argv[3],
    value = process.argv[4];

sock.on('data', function(data) {
	console.log('Response: ' + data);
    // kill client after server's response
    sock.destroy();
});

sock.on('close', function() {
    console.log('Connection closed');
});

sock.on('error', function() {
    console.log('Error while connecting to the server.');
});

// make a request
sock.connect(3000);
sock.write(clientName+' '+command+' '+value);
