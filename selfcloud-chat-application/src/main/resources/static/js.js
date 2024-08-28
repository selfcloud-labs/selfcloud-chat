var client = null;
var color;
var conversationId = null


function showMessage(value, fromUser, userColor) {
    var newResponse = document.createElement('p');
    newResponse.style.color = userColor;
    newResponse.appendChild(document.createTextNode(fromUser));
    newResponse.appendChild(document.createTextNode(": "));
    newResponse.appendChild(document.createTextNode(value));
    var response = document.getElementById('reponse');
    response.appendChild(newResponse);
}

function connect() {
    client = Stomp.client('ws://localhost:8095/chat');
    toUser = document.getElementById('toUser').value;
    token = document.getElementById('token').value;

    client.connect(
      { Authorization: `Bearer ${token}` }, // Nagłówek Authorization
      function (frame) {
        // Subskrybuj tymczasowy temat na odpowiedź z ID konwersacji
        client.subscribe("/user/topic/init", function(message){
        conversationId = message.body.replace(/"/g, '');
        console.log(conversationId);
        document.getElementById('conversation').textContent = `Conversation ID: ${conversationId}`;

            // Teraz możesz zasubskrybować temat wiadomości z użyciem uzyskanego ID
            client.subscribe(`/topic/${conversationId}/messages`, function(message){
                const msg = JSON.parse(message.body);
                console.log("Test 1:" + msg)
                showMessage(msg.content, msg.fromUserName, msg.userColor);
            });
        });

        // Wysyła żądanie o rozpoczęcie sesji i uzyskanie ID konwersacji
        client.send("/app/start", {}, JSON.stringify({'toUser': toUser}));
    });
}

function sendMessage() {
    var messageToSend = document.getElementById('messageToSend').value;
    var toUserName = document.getElementById('toUser').value;
    console.log(toUserName)
    if (!client || !conversationId) {
        alert("Please connect to a conversation first.");
        return;
    }

    client.send("/app/chat", {}, JSON.stringify({'value': messageToSend, 'toUserName': toUserName, 'convId': conversationId}) );
}

