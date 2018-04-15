$(function() {
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
});



