//POS Bar Chart

/**
 * Here the Bar Chart for the POS distribution is implemented
 * 
 * 
 */

//Some varibales used in the skript
var minimumFilterPOS = "?minimum=0"; //default minimum is set to 0
var speakerFilterPOS = "";
var partyFilterPOS = "";
var fractionFilterPOS = "";

var ctxPOS;
var ChartPOS;

var firstTimePOS = true;

//function that sets the minimum value
function FilterMinimumPOS() {
    var e = document.getElementById("MinimumValuePOS")
    if(parseInt(e.value) < 0 || e.value===""){
        minimumFilterPOS = "?minimum=0"
    }else{
        minimumFilterPOS = "?minimum=" + e.value
    }
    mainPOS()
}

//function that filters a speaker
function FilterSpeakerPOS() {
    var e = document.getElementById("RednerValuePOS")
    
    if(e.value === ""){
        speakerFilterPOS = "";
    }else{
        speakerFilterPOS = "&user=" + e.value;
    }
    mainPOS()
}

//function that filters a party
function FilterPartyPOS() {
    var e = document.getElementById("selectPartyPOS");
    if(e.value === "All"){
        partyFilterPOS=""
        fractionFilterPOS=""
    }else{
        partyFilterPOS = "&party="+e.value;
        fractionFilterPOS=""
    }
    mainPOS();
}

//function that filters a fraction
function FilterFractionPOS() {
    var e = document.getElementById("selectFractionPOS");
    if(e.value === "All"){
        fractionFilterPOS=""
        partyFilterPOS=""
    }else{
        fractionFilterPOS = "&fraction="+e.value;
        partyFilterPOS=""
    }
    mainPOS();
}


//main function which is called below to run the hole thing
function mainPOS() {
 $.ajax({
    url: "http://api.prg2021.texttechnologylab.org/pos"+minimumFilterPOS+speakerFilterPOS+fractionFilterPOS+partyFilterPOS,
    type: "GET",
    dataType: "json",
    success: async function(pos) {
        var posresult = pos.result
        var labels = []
        var counts = []

        //push the result data onto other array of data used in the cahrt
        posresult.forEach(e => {
            labels.push(e.pos);
            counts.push(e.count)
            
        });

        //if the chart is made for the first time
        if(firstTimePOS){
        ctxPOS = document.getElementById('chart_pos').getContext('2d');
        ChartPOS = new Chart(ctxPOS, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '# POS',
                    data: counts,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        })
        firstTimePOS = false;

    }else{

        //The chart is already made and now a new filter is set
        ChartPOS.data = {
            labels: labels,
            datasets: [{
                label: '# POS',
                data: counts,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderWidth: 1
            }]
        }
        ChartPOS.update();
    };
    },
    error: function(pos) {
        //if something happens we get an error message
        console.log("Fehler")
    }
})

}//end mainfunction

mainPOS()