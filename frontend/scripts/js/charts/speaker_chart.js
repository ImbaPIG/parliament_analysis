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
function mainSpeaker(){
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
                tooltips: {
                    callbacks: {
                          title: function(tooltipItems, data) {
                            return '';
                          },
                          label: function(tooltipItem, data) {
                           /* var img = new Image();   // Create new img element

                            var canvasPosition = Chart.helpers.getRelativePosition(e, ChartSpeaker);

                            // Substitute the appropriate scale IDs
                            var dataX = chart.scales.x.getValueForPixel(canvasPosition.x);
                            var dataY = chart.scales.y.getValueForPixel(canvasPosition.y);

                            img.onload = function() {
                                ctx.drawImage(img, dataX, dataY);
                              };

                            img.src = 'img/Deutscher_Bundestag_logo.svg';*/

                            
                            
                            return data.labels[tooltipItem];
                          }
                        }
                },
            }
        })
    },

    error: function(error) {
        console.log(error)
        
    }
})

}

function addSpeakerChart(SpeakercanvasID){
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

    
            var ctxSpeaker = document.getElementById(SpeakercanvasID).getContext('2d');
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
                    tooltips: {
                        callbacks: {
                              title: function(tooltipItems, data) {
                                return '';
                              },
                              label: function(tooltipItem, data) {
                                return data.labels[tooltipItem];
                              }
                            }
                    },
                }
            })
        },
    
        error: function(error) {
            console.log(error)
            
        }
    })

}

mainSpeaker()
