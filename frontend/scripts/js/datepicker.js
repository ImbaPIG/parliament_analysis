
//Written
$(() => {
    $('#datepicker1').datepicker({
        format: 'DD.MM.YYYY'
    });
    $('#datepicker2').datepicker({
        format: 'DD.MM.YYYY'
    });
})

const fixDate = (str) => {
    if (String(str).length == 1){
        return "0"+String(str)
    } else {
        return str
    }
}
