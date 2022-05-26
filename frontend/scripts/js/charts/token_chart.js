/**
 * Token Visualisation
 * 
 * Maybe this is a hard one because the Tokens are so much data
 * the loading time would be extremly high on start so the default minimum is 30000
 * 
 */


let ctx;
let myChartToken;

/**
 * This function is called when a Tokenchart is created
 * 
 * @param {} Token_canvasID 
 * 
 * 
 * This function was written by
 * This function was edited by
 */
 function addTokenChart(Token_canvasID, fromDateString, toDateString){
    req = `${global_party_filter}${global_party_filter ? "&": "?"}startDate=${fromDateString}&endDate=${toDateString}`;

    $.ajax({
        url: "http://localhost:4567/api/tokens"+req,
        //url: "http://localhost:4567/api/tokens"+"?minimum=30000"+global_party_filter,
        //url: "http://localhost:4567/api/tokens"+"?minimum=30000" +extra_toke_filter,
        type: "GET",
        dataType: "json",
        success: async function(token) {

            let tokenresult = token.result
            let labels = []
            let counts = []
    
            //push reult data into dffrent lists for the chart
            tokenresult.forEach(e => {
                labels.push(e._id);
                counts.push(e.count)
                
            });

            ctx = document.getElementById(Token_canvasID).getContext('2d');
            myChartToken = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels.slice(0,100),
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
        error: function(token) {
            console.log(token)
        }
    })
}


addTokenChart("chart_token", "01.01.2000", "01.01.3000")