$(function() {
    const $selectAll = $('#selectAll');
    if ($selectAll.length === 0) {
        return;
    }

    $selectAll.on('change', function() {
        const checked = $(this).is(':checked');
        $('.operator-check').prop('checked', checked);
    });
});