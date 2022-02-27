//POS Bar Chart

/**
 * Here the Bar Chart for the POS distribution is implemented
 * 
 * 
 */

var ctxPOS;
var ChartPOS;

/**
 * 
 * This functions ic alled when a POS chart is created
 * 
 * @param {*} POScanvasID is the ID of the canvas
 * 
 * This function was written by Jannik
 * This function was edited by Özlem
 */
 function addPOSchart(POScanvasID, fromDateString, toDateString){
    req = `${global_party_filter}${global_party_filter ? "&": "?"}startDate=${fromDateString}&endDate=${toDateString}`;


    $.ajax({
        url: "http://localhost:4567/api/pos"+req,
        //url: "http://localhost:4567/api/pos"+global_party_filter,
        type: "GET",
        dataType: "json",
        success: async function(pos) {
            var posresult = pos.result
            var labels = []
            var counts = []
    
            //push the result data onto other array of data used in the cahrt
            posresult.forEach(e => {
                labels.push(e._id);
                counts.push(e.count)
                
            });
    
            //if the chart is made for the first time
            ctxPOS = document.getElementById(POScanvasID).getContext('2d');
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
            });
        },
        error: function(e) {
            console.log(e)
        }
    })

}

addPOSchart("chart_pos", "01.01.2000", "01.01.3000")