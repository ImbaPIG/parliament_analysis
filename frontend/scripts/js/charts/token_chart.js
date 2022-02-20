/**
 * Token Visualisation
 * 
 * Maybe this is a hard one because the Tokens are so much data
 * the loading time would be extremly high on start so the default minimum is 30000
 * 
 */


var ctx;
var myChartToken;


//Mian function to visualize the token distribution
/*function mainToken(){
$.ajax({
    url: "http://localhost:4567/api/tokens"+minimumfilter,
    type: "GET",
    dataType: "json",
    success: async function(token) {
        var tokenresult = token.result
        
        var labels = []
        var counts = []

        //push reult data into dffrent lists for the chart
        tokenresult.forEach(e => {
            labels.push(e._id);
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
    error: function(e) {
        console.error(e)
    }
})
}*/

function addTokenChart(Token_canvasID){
    $.ajax({
        url: "http://localhost:4567/api/tokens"+global_party_filter,
        type: "GET",
        dataType: "json",
        success: async function(token) {

            var tokenresult = token.result
            var labels = []
            var counts = []
    
            //push reult data into dffrent lists for the chart
            tokenresult.forEach(e => {
                labels.push(e._id);
                counts.push(e.count)
                
            });

            ctx = document.getElementById(Token_canvasID).getContext('2d');
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
    },
    
    //error message
        error: function(token) {
            console.log(token)
        }
    })
}


addTokenChart("chart_token")
//mainToken();