function startApp() {

    const kinveyBaseUrl = "https://baas.kinvey.com/";
    const kinveyAppKey = "kid_HkoWR_cQl";
    const kinveyAppSecret = "cf6a1683b10e47788161b6becc5afee8";
    const kinveyAppAuthHeaders = {
        'Authorization': "Basic " +
        btoa(kinveyAppKey + ":" + kinveyAppSecret),
    };


    // Clear user auth data
    sessionStorage.clear();
    $(document).on({
        ajaxStart: function () {
            $("#loadingBox").show()
        },
        ajaxStop: function () {
            $("#loadingBox").hide()
        }
    });
    showHideMenuLinks();

    //showView('viewHome');
    showHomeView();

    // Bind the navigation menu links
    $("#linkMenuAppHome").click(showHomeView);
    $("#linkMenuLogin ").click(showLoginView);
    $("#linkMenuRegister").click(showRegisterView);
    $("#linkMenuMyMessages").click(listMessages);
    $("#linkUserHomeMyMessages").click(listMessages);

    $("#linkMenuArchiveSent").click(deleteMessages);
    $("#linkUserHomeArchiveSent").click(deleteMessages);

    $("#linkMenuSendMessage").click(showCreateMessageView);
    $("#linkUserHomeSendMessage").click(showCreateMessageView);

    $("#linkMenuLogout ").click(logoutUser);


    // Bind the form submit buttons
    $("#formLogin").submit(loginUser); //Submit handle form check for requered fields
    //$("#buttonRegisterUser").click(registerUser);
    $("#formRegister").submit(registerUser);   //Submit handle form check for requered fields
    $('#formSendMessage input[type="submit"]').click(createMessage);
    $("#buttonEditMessage").click(editMessage);

    // Bind the info / error boxes: hide on click
    $("#infoBox, #errorBox").click(function () {
        $(this).fadeOut();
    });

    // Attach AJAX "loading" event listener



    function showHideMenuLinks() {
        $('#menu a').hide();
        $('#loadingBox').hide();
        $('#infoBox').hide();
        $('#errorBox').hide();
        if (sessionStorage.getItem("authToken")) {
            //Logged in user
            $('#linkMenuAppHome').show();
            $('#linkMenuMyMessages').show();
            $('#linkMenuSendMessage').show();
            $('#linkMenuArchiveSent').show(); //Archive TO DO
            //$('#viewUserHome').show(); //Archive TO DO
            $('#linkMenuLogout ').show();
        }
        else {
            //No user logged in
            $('#linkMenuAppHome').show();
            $('#linkMenuLogin').show();
            $('#linkMenuRegister').show();
            $('#spanMenuLoggedInUser').text('');
        }
    }

    function showView(viewName) {
        // Hide all views and show the selected view only
        $('main > section').hide();
        $('#' + viewName).show();
    }

    function showHomeView() {
        showView("viewHome");
        if(sessionStorage.getItem("authToken")){
            showView("viewUserHome")
        }else
        {showView("viewAppHome")
        }

    }

    function showLoginView() {
        showView("viewLogin");
        $('#formLogin').trigger('reset'); //keywoard reset do clear of form

    }

    function showRegisterView() {
        $('#formRegister').trigger('reset'); //keywoard reset do clear of form
        showView('viewRegister');

    }

    //List of Messages------------------------------------------------------
    function listMessages() {
        $('#myMessages').empty();
        showView('viewMyMessages');
        $.ajax({
            method: "GET",
            url: kinveyBaseUrl + "appdata/" + kinveyAppKey + "/messages",  //MESSAGES
            headers: getKinveyUserAuthHeaders(),
            success: loadMessagesSuccess,
            error: handleAjaxError
        });
        function loadMessagesSuccess(messages) {
            showInfo('Messages Received loaded.');
            if (messages.length == 0) {
                $('#myMessages').text('No messages in the system.');
            } else {
                let messagesTable = $('<table>')
                    .append($('<tr>').append(
                        '<th>From</th><th>Message</th>',
                        '<th>Date Received</th>'));// Removed <th>Actions</th>
                for (let message of messages)
                    appendMessageRow(message, messagesTable);
                $('#myMessages').append(messagesTable);
            }
        }

        function appendMessageRow(message, messagesTable) {
            let links = [];
          let usernameRecep = message.recipient_username;
            let name = message.sender_name;
            if(name==="undefined"){
                name=false
            }
            let username = message.sender_username;
            let dateSent = message._kmd.ect;
            let name_username =  formatSender(name, username);
            //alert(name_username)
           let dateFormat = formatDate(dateSent);
           let currentUser =  sessionStorage.getItem('username');
           if(currentUser==usernameRecep) {
               messagesTable.append($('<tr>').append(
                   $('<td>').text(name_username),
                   $('<td>').text(message.text),
                   $('<td>').text(dateFormat)

               ));
           }
        }

    }

    //List of Messages------------------------------------------------------
//List of Messages to Delete------------------------------------------------------
    function deleteMessages() {
        $('#sentMessages').empty();
        showView('viewArchiveSent');
        $.ajax({
            method: "GET",
            url: kinveyBaseUrl + "appdata/" + kinveyAppKey + "/messages",  //MESSAGES
            headers: getKinveyUserAuthHeaders(),
            success: loadMessagesSuccess,
            error: handleAjaxError
        });
        function loadMessagesSuccess(messages) {
            //showInfo('Messages Sent loaded.');
            if (messages.length == 0) {
                $('#sentMessages').text('No messages in the system.');
            } else {
                let messagesTable = $('<table>')
                    .append($('<tr>').append(
                        '<th>To</th><th>Message</th>',
                        '<th>Date Sent</th><th>Actions</th>'));
                for (let message of messages)
                    appendMessageRowDelete(message, messagesTable);
                $('#sentMessages').append(messagesTable);
            }
        }

        function appendMessageRowDelete(message, messagesTable) {
                 let link = $('<input type="button" value="Delete">').on('click', function () {
                 deleteMessageById(message._id)
             });
            let username = message.sender_username;
            let name = message.recipient_username;
           let dateSent = message._kmd.ect;
            let dateFormat = formatDate(dateSent);
            let currentUser =  sessionStorage.getItem('username');
            if(currentUser==username) {
                messagesTable.append($('<tr>').append(
                    $('<td>').text(name),
                    $('<td>').text(message.text),
                    $('<td>').text(dateFormat),
                    $('<td>').append(link)  //jquery multiple Append from [DOMelement1,DOMelement2 ....]
                ));
            }
        }

    }
    //Delete Message By ID
    function deleteMessageById(messageId) {
        $.ajax({
            method: "DELETE",
            url: kinveyBaseUrl + "appdata/" + kinveyAppKey + "/messages/" + messageId,
            headers: getKinveyUserAuthHeaders(),
            success: deleteMessagesSuccess,
            error: handleAjaxError
        });
        function deleteMessagesSuccess() {
            showInfo("Message Deleted.");
            deleteMessages()
        }
    }

    //Common Method for Authentication
    function getKinveyUserAuthHeaders() {
        return {
            'Authorization': "Kinvey " +
            sessionStorage.getItem('authToken'),
        };
    }

    function showCreateMessageView() {
        event.preventDefault();
        $('#formSendMessage').trigger('reset'); //keywoard reset do clear of form
        $('#msgRecipientUsername').empty();

        $.ajax({
            method: "GET",
            url: kinveyBaseUrl + "user/" + kinveyAppKey + "/",  //USERS
            headers: getKinveyUserAuthHeaders(),
            success: loadUsersSuccess,
            error: handleAjaxError
        });
        function loadUsersSuccess(users) {
            let currentUser =  sessionStorage.getItem('username');
          for(let user of users) {
              let user_name = user.username;
              //if (currentUser !== user_name) {   // Filter Current User Not needed
                  let userToLoadTmp = user.name;
                  let userToLoad = userToLoadTmp;
                  if (userToLoad === undefined) {
                      userToLoad = ""
                  }

                  if (userToLoad === "") {
                      $(`<option>${user_name}</option>`)
                          .attr("value", user_name)
                          .appendTo($('#msgRecipientUsername'))
                  } else {
                      $(`<option>${user_name} (${userToLoad})</option>`)
                          .attr("value", user_name)
                          .appendTo($('#msgRecipientUsername'))
                  }
              }

        }
        showView('viewSendMessage');
    }

    function logoutUser() {
        sessionStorage.clear();
        $('#spanMenuLoggedInUser').text('');
        showHideMenuLinks();
        showHomeView();
        showInfo('Logout successful.')

    }

    function loginUser() {
        event.preventDefault();  //Stop default behaviur and don create new page after submit!!!
        let userData = {
            username: $('#formLogin input[name=username]').val(),
            password: $('#formLogin input[name=password]').val()
        };
        $.ajax({
            method: "POST",
            url: kinveyBaseUrl + "user/" + kinveyAppKey + "/" + 'login',
            headers: kinveyAppAuthHeaders,
            data: JSON.stringify(userData),
            contentType: "application/json",
            success: loginSuccess,
            error: handleAjaxError
        });
        function loginSuccess(userInfo) {
            saveAuthInSession(userInfo); //Record of session storage
            showHideMenuLinks();  //Change of menu links
            showHomeView();
            showInfo('Login successful.');
        }
    }

    function registerUser(event) {
        event.preventDefault();  //Stop default behaviur and don create new page after submit!!!
        let userData = {
            username: $('#formRegister input[name=username]').val(),
            password: $('#formRegister input[name=password]').val(),
            name: $('#formRegister input[name=name]').val()
        };
        $.ajax({
            method: "POST",
            url: kinveyBaseUrl + "user/" + kinveyAppKey + "/",
            headers: kinveyAppAuthHeaders,
            data: JSON.stringify(userData),
            contentType: "application/json",
            success: registerSuccess,
            error: handleAjaxError
        });
        function registerSuccess(userInfo) {
            saveAuthInSession(userInfo); //Record of session storage
            showHideMenuLinks();  //Change of menu links
            listMessages();
            showInfo('User registration successful.');
        }

    }

    function handleAjaxError(response) {
        let errorMsg = JSON.stringify(response);
        if (response.readyState === 0)  // Response of AJAX contain readyState property
            errorMsg = "Cannot connect due to network error.";
        if (response.responseJSON &&
            response.responseJSON.description)
            errorMsg = response.responseJSON.description;
        showError(errorMsg);
    }

    //Display Information message in main screen
    function showInfo(message) {
        $('#infoBox').text(message);
        $('#infoBox').show();
        setTimeout(function () {
            $('#infoBox').fadeOut();
        }, 3000);
    }

    //Display Error message in main screen
    function showError(errorMsg) {
        $('#errorBox').text("Error: " + errorMsg);
        $('#errorBox').show();
    }

    function saveAuthInSession(userInfo) {
        let userAuth = userInfo._kmd.authtoken;
        sessionStorage.setItem('authToken', userAuth);
        let userId = userInfo._id;
        sessionStorage.setItem('userId', userId);
        let username = userInfo.username;
        let name = userInfo.name;
        sessionStorage.setItem('username', username);
        sessionStorage.setItem('name', name);
        //Add Welcome user Text in main menu
        $('#spanMenuLoggedInUser').text(
            "Welcome, " + username + "!");
        $('#viewUserHomeHeading').text(
            "Welcome, " + username + "!");

    }

    //Create Message----------------------------------------------------------
    function createMessage() {
        event.preventDefault();
        let currentUser =  sessionStorage.getItem('username');
        let name =  sessionStorage.getItem('name');
        //let recipient_username =  $('#formSendMessage').find('#msgRecipientUsername').val();
       let recipient_username = $( "#msgRecipientUsername option:selected" ).prop('value');
        let messageData = {
           // title: $('#formSendMessage input[name=title]').val(),
            sender_username: currentUser,
            sender_name: name,
            recipient_username: recipient_username ,
            text: $('#formSendMessage').find("#msgText").val()
        };
        $.ajax({
            method: "POST",
            url: kinveyBaseUrl + "appdata/" + kinveyAppKey + "/messages",
            headers: getKinveyUserAuthHeaders(),
            data: messageData,
            success: createMessageSuccess,
            error: handleAjaxError
        });
        function createMessageSuccess(response) {
           //
            showInfo('Message sent.');
            showHomeView();
        }
    }

    //Create Message----------------------------------------------------------

    //New Functions
    function formatDate(dateISO8601) {
        let date = new Date(dateISO8601);
        if (Number.isNaN(date.getDate()))
            return '';
        return date.getDate() + '.' + padZeros(date.getMonth() + 1) +
            "." + date.getFullYear() + ' ' + date.getHours() + ':' +
            padZeros(date.getMinutes()) + ':' + padZeros(date.getSeconds());

        function padZeros(num) {
            return ('0' + num).slice(-2);
        }
    }

    function formatSender(name, username) {
        if (!name)
            return username;
        else
            return username + ' (' + name + ')';
    }


}