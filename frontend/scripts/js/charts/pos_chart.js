//POS Bar Chart

/**
 * Here the Bar Chart for the POS distribution is implemented
 * 
 * 
 */

var ctxPOS;
var ChartPOS;

var firstTimePOS = true;


/**
 * 
 * @param {*} POScanvasID 
 * 
 * This function was written by <Name>
 * This function was edited by <Name>
 */
function addPOSchart(POScanvasID){
    $.ajax({
        url: "http://localhost:4567/api/pos"+global_party_filter,
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
        error: function(pos) {
            //if something happens we get an error message
            console.log("Fehler")
        }
    })

}
//mainPOS()
addPOSchart("chart_pos")