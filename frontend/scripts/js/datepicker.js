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
/*const getDate = () => {
    const fromDate = new Date($('#datepicker1').val());
    const toDate = new Date($('#datepicker2').val());
    const fromDateString = `${fromDate.getDate()}.${fromDate.getMonth()+1}.${fromDate.getFullYear()}`;
    const toDateString = `${toDate.getDate()}.${toDate.getMonth()+1}.${toDate.getFullYear()}`;
}

$('#dateFilter').on("click", () => {
    getDate();
}) */

// TODO:
// change up all requests to also take date
// remove confirm date
// dates can only be modified before creating dashboard not while