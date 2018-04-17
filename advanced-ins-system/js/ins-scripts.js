// $(function() {
//     $('#messages div').click(function() {
//         $(this).fadeOut();
//     });
//     setTimeout(function() {
//         $('#messages div.alert-success').fadeOut();
//     }, 3000);
//     $(".return").on('click',function () {
//         window.history.back();
//         console.log('click')
//     });
// });
function startApp() {

    // const baseUrl = "http://localhost:8080/";
    const baseUrl = "http://87.120.100.227:8080/";


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
    $("#infoBox, #errorBox, #warninngBox").click(function () {
        $(this).fadeOut();
    });

    // $('#loadingBox').hide();
    // $('#infoBox').hide();
    // $('#errorBox').hide();


    showHideMenuLinks();



    function showHideMenuLinks() {
        $('#loadingBox').hide();
        $('#infoBox').hide();
        $('#errorBox').hide();
        $('#warninngBox').hide();
        $("#contractAreaDisplay").hide();
        $("#premiumAreaDisplay").hide();
    }
    $("#premiumArea").on('click', function (){
        showHideMenuLinks();
        $('#myContracts').empty();
        $("#premiumAreaDisplay").show();
        }
    )

    $("#contractArea").on('click', function (){
            showHideMenuLinks();
            $('#myPremiums').empty();
            $("#contractAreaDisplay").show();
    }
    )

    //CONTRACT SEARCH
    $("#buttonSearch").on('click', function () {
        {
            let egn = $('#egn').val();
            console.log(egn);
            if (egn !== "") {


                $('#myContracts').empty();
                // showView('viewMyMessages');
                $.ajax({
                        method: "GET",
                        url: baseUrl + 'contracts/client?egn=' + egn,  //Contracts
                        // headers: getKinveyUserAuthHeaders(),
                        success: loadMessagesSuccess,
                        error: handleAjaxError
                    }
                )
            }


            function loadMessagesSuccess(messages) {

                if (messages.length == 0) {
                    // $('#myContracts').text('No contracts in the system.');
                    showWarnning("No contracts in the system.");
                } else {
                    showInfo('Contracts Received loaded.');
                    let messagesTable = $('<table class="table table-striped">')
                        .append($('<thead>').append($('<tr>').append(
                            '<th scope="col">#</th><th scope="col">Contract ID</th><th scope="col">Product</th>',
                            '<th scope="col">Duration(Years)</th>'))).append($('<tbody>'));
                    let num = 0;
                    for (let message of messages){
                        num++;
                        appendMessageRow(num, message, messagesTable);
                    }
                    $('#myContracts').append(messagesTable);
                }
            }
        }
    });
    //PREMIUM SEARCH
    $("#buttonSearchPremium").on('click', function () {
        {
            let egn = $('#egnPrem').val();
            console.log(egn);
            if (egn !== "") {


                $('#myPremiums').empty();
                // showView('viewMyMessages');
                $.ajax({
                        method: "GET",
                        url: baseUrl + 'premiums/client?egn=' + egn,  //Contracts
                        // headers: getKinveyUserAuthHeaders(),
                        success: loadMessagesSuccess,
                        error: handleAjaxError
                    }
                )
            }


            function loadMessagesSuccess(messages) {
                  if (messages.length == 0) {
                    // $('#myContracts').text('No premiums in the system.');
                    showWarnning("No premiums in the system.");
                } else {
                      showInfo('Premiums Received loaded.');
                    let messagesTable = $('<table class="table table-striped">')
                        .append($('<thead>').append($('<tr>').append(
                            '<th scope="col">#</th><th scope="col">Contract ID</th><th scope="col">Start Date</th><th scope="col">End Date</th>',
                            '<th scope="col">Premium Amount(EUR)</th>'))).append($('<tbody>'));
                    let num = 0;
                    for (let message of messages){
                        num++;
                        appendMessageRowPrem(num, message, messagesTable);
                    }
                    $('#myPremiums').append(messagesTable);
                }
            }
        }
    });
}



//List of Messages------------------------------------------------------
// function showContractView() {
//     let egn = $('#egn').val();
//     console.log(egn);
//     if (egn !== "") {
//
//
//         $('#myMessages').empty();
//         // showView('viewMyMessages');
//         $.ajax({
//                 method: "GET",
//                 url: baseUrl + 'contracts/client?egn=' + egn,  //Contracts
//                 // headers: getKinveyUserAuthHeaders(),
//                 success: loadMessagesSuccess,
//                 error: handleAjaxError
//             }
//         )
//     }
//
//
//     function loadMessagesSuccess(messages) {
//         showInfo('Messages Received loaded.');
//         if (messages.length == 0) {
//             $('#myMessages').text('No messages in the system.');
//         } else {
//             let messagesTable = $('<table>')
//                 .append($('<tr>').append(
//                     '<th>From</th><th>Message</th>',
//                     '<th>Date Received</th>'));// Removed <th>Actions</th>
//             for (let message of messages)
//                 appendMessageRow(message, messagesTable);
//             $('#myMessages').append(messagesTable);
//         }
//     }
// }
function appendMessageRow(num,message, messagesTable) {
    let contract_id = message.id;
    let duration = message.duration;
    let product = message.product;
    // messagesTable.append($('<tbody>').append($('<th scope="row">').text(num).append( $('<tr>').append(
    //     $('<th>').text(contract_id),
    //     $('<td>').text(product),
    //     $('<td>').text(duration)
    // ))));



    messagesTable.append($("<tr><th scope=\"row\">"+num+"</th><th>"+contract_id+"</th><td>"+product+"</td><td>"+duration+"</td></tr>"))



}
function appendMessageRowPrem(num,message, messagesTable) {
    let contract_id = message.cntrctId;
    let start_date = message.startDate;
    let end_date = message.endDate;
    let operation_amount = message.operationAmount;
    // messagesTable.append($('<tbody>').append($('<th scope="row">').text(num).append( $('<tr>').append(
    //     $('<th>').text(contract_id),
    //     $('<td>').text(product),
    //     $('<td>').text(duration)
    // ))));



    messagesTable.append($("<tr><th scope=\"row\">"+num+"</th><th>"+contract_id+"</th><td>"+formatDate(start_date)+"</td><td>"+formatDate(end_date)+"</td><td>"+operation_amount+"</td></tr>"))



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
function showWarnning(warninngMsg) {
    $('#warninngBox').text(warninngMsg);
    $('#warninngBox').show();
}

//Display Error message in main screen
function showError(errorMsg) {
    $('#errorBox').text("Error: " + errorMsg);
    $('#errorBox').show();
}

//New Functions
function formatDate(dateISO8601) {
    let date = new Date(dateISO8601);
    if (Number.isNaN(date.getDate()))
        return '';
    return date.getDate() + '.' + padZeros(date.getMonth() + 1) +
        "." + date.getFullYear()
        // + ' ' + date.getHours() + ':' +
        // padZeros(date.getMinutes()) + ':' + padZeros(date.getSeconds());

    function padZeros(num) {
        return ('0' + num).slice(-2);
    }
}