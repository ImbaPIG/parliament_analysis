/**
 * 
 * 
 * Finds Speaker with the most Speeches
 * 
 * (Actually because our dataset is already orderd we could just take the first Speaker but in case it isnt we will find it)
 * 
 * Prints the speaker with the most Speeches onto the Website
 * 
 * 
 * 
 * This function was written by
 * This function was edited by Moritz
 */

function addSpeakerChart(SpeakercanvasID){
    $.ajax({
        url: "http://localhost:4567/api/statistic",
        type: "GET",
        dataType: "json",
        success: function(statistic){
            let speakers = statistic.result[0].speakers
            let label_speaker  = []
            let date_speaker = []
    
            speakers.forEach(speaker => {
                date_speaker.push(speaker.count)
                label_speaker.push(speaker._id)
            });

            labels = []
            label_speaker.splice(0,10).forEach(s =>{
                labels.push(get_top_speakers(s))
                

            })
            /*labels.forEach(l =>{

                console.log(1)

            })*/
            console.log(labels)
    
            let ctxSpeaker = document.getElementById(SpeakercanvasID).getContext('2d');
            let ChartSpeaker = new Chart(ctxSpeaker, {
                type: 'bar',
                data: {
                    labels: label_speaker.slice(0,10),
                    datasets: [{
                        label: "Redner",
                        data: date_speaker,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                }
            })
        },
    
        error: function(error) {
            console.log(error)
            
        }
    })

}


function get_top_speakers(id){

    $.ajax({
        url: "http://localhost:4567/api/speakers?rednerID="+id,
        type: "GET",
        dataType: "json",
        success: function(statistic){
            let speaker = statistic.result[0]
            return speaker.firstname + " " + speaker.name

        },
    
        error: function(error) {
            console.log(error)
            
        }
    })
}

addSpeakerChart("chart_speaker")
