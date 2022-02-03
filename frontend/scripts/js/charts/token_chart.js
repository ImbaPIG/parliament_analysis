/**
 * Token Visualisation
 * 
 * Maybe this is a hard one because the Tokens are so much data
 * the loading time would be extremly high on start so the default minimum is 30000
 * 
 */

//some variables used in the sript
var minimumfilter = "?minimum=30000"
var firsttime = true
var partyfilter =""
var fractionfilter =""
var ctx;
var myChartToken;
var rednerFilterToken=""

//sets the minimum to another value
function filterMinimumToken() {
    var e = document.getElementById("MinimumTokenValue")
    if(parseInt(e.value) < 0 || e.value===""){
        minimumfilter = "?minimum=30000"
    }else{
        minimumfilter = "?minimum=" + e.value
    }
    mainToken()
}

//sets party Filter
function filterParty(){
    var e = document.getElementById("selectToken");
    if(e.value === "All"){
        partyfilter=""
        fractionfilter=""
    }else{
    partyfilter = "&party="+e.value;
    fractionfilter=""
    }
    mainToken();
}

//sets fraction Filter
function filterFraction(){
    var e = document.getElementById("selectFractionToken");
    if(e.value === "All"){
        fractionfilter=""
        partyfilter=""
    }else{
    fractionfilter = "&fraction="+e.value;
    partyfilter=""
    }
    mainToken();
}

//sets Speaker Filter
function filterRednerToken(){
    var e = document.getElementById("RednerTokenValue")
    
    if(e.value === ""){
        rednerFilterToken = "";
        minimumfilter="?minimum=30000"
    }else{
        rednerFilterToken = "&user=" + e.value;
        minimumfilter = "?minimum=1000"
    }
    mainToken()
}


//Mian function to visualize the token distribution
function mainToken(){
$.ajax({
    url: "http://api.prg2021.texttechnologylab.org/tokens"+minimumfilter + rednerFilterToken+ fractionfilter + partyfilter,
    type: "GET",
    dataType: "json",
    success: async function(token) {
        var tokenresult = token.result
        
        var labels = []
        var counts = []

        //push reult data into dffrent lists for the chart
        tokenresult.forEach(e => {
            labels.push(e.token);
            counts.push(e.count)
            
        });
        if(firsttime){ //is true for the first time the chart will be made
        ctx = document.getElementById('chart_token').getContext('2d');
        myChartToken = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: '# Token',
                    data: counts.concat([0]),
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

        firsttime=false;
    }else{
        myChartToken.data = {
            labels: labels,
            datasets: [{
                label: '# Token',
                data: counts.concat([0]),
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderWidth: 1
            }]
        }
        myChartToken.update();
    };
},

//error message
    error: function(pos) {
        console.log("Fehler")
    }
})
}

mainToken();