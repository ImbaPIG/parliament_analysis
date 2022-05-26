/**
 * 
 * 3 b (1)
 * 
 * Sentiment Radar chart 
 * 
 * 
 * 
 */



let ctxSentiment;
let ChartSentiment;

/**
 * This function is called when a Sentment Chart is created
 * 
 * @param {*} canvasID is the canvas ID of said chart
 * 
 * This function was written by
 * This function was edited by  ,Erik
 */
 function addSentiment(canvasID, fromDateString, toDateString){
    req = `${global_party_filter}${global_party_filter ? "&": "?"}startDate=${fromDateString}&endDate=${toDateString}`;

        $.ajax({
        url: "http://localhost:4567/api/sentiment" + req,
        type: "GET",
        dataType: "json",
        success: async function (sentiments) {
    
            //put the data into a format which is used by the scatter chart
            let result = sentiments.result
            let data = []
            
            let neutral_count = 0;
            let positive_count = 0;
            let negative_count = 0;
            
            result.forEach(e => {
               if(parseFloat(e.sentiment) > 0){
                    positive_count = positive_count + e.count;
               }else if(parseFloat(e.sentiment) < 0){
                    negative_count = negative_count + e.count;
               }else{
                    neutral_count = neutral_count + e.count;
               }
            });
    
            data = [Math.log(neutral_count),Math.log(positive_count),Math.log(negative_count)]
            label=["neutral", "positive", "negative"]
    
            ctxSentiment = document.getElementById(canvasID).getContext('2d');
            ChartSentiment = new Chart(ctxSentiment, {
                type: 'radar',
                data: {
                    labels: label,
                    datasets: [{
                        label: 'log(Sentimentcount)',
                        data: data,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        fill: true,
                    }],
                }
            })

        },
        error: function(params) {
            console.error(params)
        }
    })
}

addSentiment("chart_sentiment", "01.01.2000", "01.01.3000")

