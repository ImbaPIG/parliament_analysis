/**
 * 3a(1)
 * 
 * Finds Speaker with the most Speeches
 * 
 * (Actually because our dataset is already orderd we could just take the first Speaker but in case it isnt we will find it)
 * 
 * Prints the speaker with the most Speeches onto the Website
 * 
 */

 $.ajax({
    url: "http://api.prg2021.texttechnologylab.org/statistic",
    type: "GET",
    dataType: "json",
    success: function(statistic){
        var speakers = statistic.result.speakers
        var label_speaker  = []
        var date_speaker = []

        speakers.forEach(speaker => {
            date_speaker.push(speaker.count)
            label_speaker.push(speaker.id)
        });
       /* $.ajax({
            url: "http://api.prg2021.texttechnologylab.org/speakers?id="+maxSpeaker,
            type: "GET",
            dataType: "json",
        })
        .then(function(speaker) {
            document.getElementById("oneA").innerHTML = ( speaker.result[0].firstname + " " + speaker.result[0].name);     
        })*/

        var ctxSpeaker = document.getElementById('chart_speaker').getContext('2d');
        var ChartSpeaker = new Chart(ctxSpeaker, {
            type: 'bar',
            data: {
                labels: [1,2,3,4,5,6,7,8,9,10],
                datasets: [{
                    label: 'Speaker',
                    data: date_speaker,
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
                },
                onHover: (e)=>{
                    console.log("HIII");
                }
            }
        })

    },
    error: function(error) {
        console.log(error)
        
    }
})
