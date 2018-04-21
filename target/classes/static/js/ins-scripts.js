$(function() {
    // const baseUrl = "https://spring-mvc-advanced-ins.herokuapp.com/";
    // const baseUrl = "http://87.120.100.227:8080/";
    const baseUrl = "http://87.120.100.227:8080/";
    $('#messages div').click(function() {
        $(this).fadeOut();
    });
    setTimeout(function() {
        $('#messages div.alert-success').fadeOut();
    }, 3000);
    $(".return").on('click',function () {
        window.history.back();
        console.log('click')
    });

    $(document).on({
        ajaxStart: function () {
            $(".loadingBoxShow").show()
        },
        ajaxStop: function () {
            $(".loadingBoxShow").hide()
        }
    });
    $(".infoBoxShow, .errorBoxShow, .warninngBoxShow").click(function () {
        $(this).fadeOut();
    });
    hideInfos();
    function hideInfos() {
        $('.loadingBoxShow').hide();
        $('.infoBoxShow').hide();
        $('.errorBoxShow').hide();
        $('.warninngBoxShow').hide();
    }
    $(".cancel-search-form").click(cancelForm);
    $(".search-form-modal").click(cancelForm);


    //REST
    //Product SEARCH
    $("#searchProductLink").on('click', function () {
        {
            hideInfos();
            let idntfr = $('#productId').val();
            console.log(idntfr);
            if (idntfr !== "") {
                $('#myProducts').empty();
                $.ajax({
                        method: "GET",
                        url: baseUrl + 'rest/products?idntfr=' + idntfr,  //Products
                        success: loadMessagesSuccess,
                        error: handleAjaxError
                    }
                )
            }
            function loadMessagesSuccess(messages) {
                if (messages.length == 0) {
                      showWarnning("Not Found");
                } else {
                      showInfo('Products Received loaded.');
                    let messagesTable = $('<table class="table table-responsive-sm table-striped">')
                        .append($('<thead>').append($('<tr>').append(
                            '<th scope="col">#</th><th scope="col">ID</th><th scope="col">Identifier:Label</th>'))).append($('<tbody>'));
                    let num = 0;
                    for (let message of messages){
                        num++;
                        appendMessageRow(num, message, messagesTable);
                    }

                    $('#myProducts').append(messagesTable).show();
                    $(".select-product").click(selectProduct);
                    $(".cancel-search-form").click(cancelForm)
                }
            }
        }
    });

    function appendMessageRow(num,message, messagesTable) {
        let product_id = message.id;
        let idntfr = message.idntfr;
        let label = message.label;

          messagesTable.append($("<tr><th scope=\"row\">"+num+"</th><th>"+product_id+"</th><td><a class='select-product' id='"+idntfr+"' data-dismiss='modal' href='#' >"+idntfr+":"+label+"</a></td></tr>"));
    }
    function selectProduct(){
        let idSelected = $(this).attr("id");
        $('#product').val(idSelected);
        $('#myProducts').hide();
        hideInfos();
          $('#productId').val('');
    }


    //Distributor SEARCH
    $("#searchDistributorLink").on('click', function () {
        {
            hideInfos();
            let idntfr = $('#distributorId').val();
            console.log(idntfr);
            if (idntfr !== "") {
                $('#myDistributors').empty();
                $.ajax({
                        method: "GET",
                        url: baseUrl + 'rest/distributors?searchDistributorCriteria=' + idntfr,  //Distributors
                        success: loadMessagesSuccess,
                        error: handleAjaxError
                    }
                )
            }


            function loadMessagesSuccess(messages) {

                if (messages.length == 0) {
                    showWarnning("Not Found");
                } else {
                     showInfo('Distributors Received loaded.');
                    let messagesTable = $('<table class="table table-responsive-sm table-striped">')
                        .append($('<thead>').append($('<tr>').append(
                            '<th scope="col">#</th><th scope="col">ID</th><th scope="col">Full Name</th>',
                            '<th scope="col">Organization ID</th>'))).append($('<tbody>'));
                    let num = 0;
                    for (let message of messages){
                        num++;
                        appendMessageRowDistributor(num, message, messagesTable);
                    }

                    $('#myDistributors').append(messagesTable).show();
                    $(".select-distributor").click(selectDistributor);
                   }
            }
        }
    });

    function appendMessageRowDistributor(num,message, messagesTable) {
        let distributor_id = message.id;
        let fullName = message.fullName;
        let organization = message.organization;

        messagesTable.append($("<tr><th scope=\"row\">"+num+"</th><th>"+distributor_id+"</th><td><a class='select-distributor' id='"+distributor_id+"' data-dismiss='modal' href='#' >"+fullName+"</a></td><td>"+organization+"</td></tr>"));

    }
    function cancelForm(){
        hideInfos();
        $('#myDistributors').hide();
        $('#distributorId').val('');
        $('#myProducts').hide();
        $('#productId').val('');
        $('#myPersons').hide();
        $('#personId').val('');

    }
    function selectDistributor(){
        let idSelected = $(this).attr("id");
        $('#distributor').val(idSelected);
        $('#myDistributors').hide();
        hideInfos();
        $('#distributorId').val('');
    }





    //Owner Search
    $("#searchOwnerLink").on('click', function () {
        {
            hideInfos();
            let idntfr = $('#ownerId').val();
            console.log(idntfr);
            if (idntfr !== "") {
               $('#myOwners').empty();
                $.ajax({
                        method: "GET",
                        url: baseUrl + 'rest/persons?searchPersonCriteria=' + idntfr,  //Contracts
                        success: loadMessagesSuccess,
                        error: handleAjaxError
                    }
                )
            }

            function loadMessagesSuccess(messages) {

                if (messages.length == 0) {

                    showWarnning("Not Found");
                } else {
                     showInfo('Persons Received loaded.');
                    let messagesTable = $('<table class="table table-responsive-sm table-striped">')
                        .append($('<thead>').append($('<tr>').append(
                            '<th scope="col">#</th><th scope="col">ID</th><th scope="col">Full Name:EGN</th>'))).append($('<tbody>'));
                    let num = 0;
                    for (let message of messages){
                        num++;
                        appendMessageRowOwner(num, message, messagesTable);
                    }

                    $('#myOwners').append(messagesTable).show();
                    $(".select-owner").click(selectOwner);
                }
            }
        }
    });

    function appendMessageRowOwner(num,message, messagesTable) {
        let person_id = message.id;
        let fulName = message.fullName;
        let egn = message.egn;

        messagesTable.append($("<tr><th scope=\"row\">"+num+"</th><th>"+person_id+"</th><td><a class='select-owner' id='"+person_id+"' data-dismiss='modal' href='#' >"+fulName+":"+egn+"</a></td></tr>"));
    }

    function selectOwner(){
        let idSelected = $(this).attr("id");
        $('#owner').val(idSelected);
        $('#myOwners').hide();
        hideInfos();
        $('#ownerId').val('');
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
        $('.infoBoxShow').text(message);
        $('.infoBoxShow').show();
        setTimeout(function () {
            $('.infoBoxShow').fadeOut();
        }, 3000);
    }
//Display Error message in main screen
    function showWarnning(warninngMsg) {
        $('.warninngBoxShow').text(warninngMsg);
        $('.warninngBoxShow').show();
    }

//Display Error message in main screen
    function showError(errorMsg) {
        $('.errorBoxShow').text("Error: " + errorMsg);
        $('.errorBoxShow').show();
    }
});



