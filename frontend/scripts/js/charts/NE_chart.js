/***
 * 3 b (4)
 * 
 * Gives a visualitaion of the Named Entities, 
 * First a visaulisation of the Entities combined and then for persons , locations and organisations seperatly
 * 
 * it gives always the top 10 results otherwise the pie charts looks a bit scuffed
 *  (i hope this is okay but it is nothing mentioned about it in the exercise)
 * 
 * The code is a bit longe because there are four charts but in every chart the same thing happens
 * 
 */

//A lot of letiables which are used in the script
let ctxNE;
let configNE;
let ChartNE;

function addNamedEntities(NEcanvasID){
    $.ajax({
        url: "http://localhost:4567/api/namedEntities" + global_party_filter,
        type: "GET",
        dataType: "json",
        success: async function(namedEntities){

            let persons = namedEntities.result[0].persons;
            let organisations = namedEntities.result[0].organisations
            let locations = namedEntities.result[0].locations
    
            let data_persons = []
            let label_persons = []
    
            let data_locations= []
            let label_locations = []
            
            let data_organisations = []
            let label_organisations = []
    

            persons.forEach(person => {
                data_persons.push(person.count);
                label_persons.push(person.element);
            });

            organisations.forEach(organisation => {
                data_organisations.push(organisation.count);
                label_organisations.push(organisation.element);
            });
    
            locations.forEach(location => {
                data_locations.push(location.count);
                label_locations.push(location.element)
            });
    
            let lablelol = []
            for(let i= 0; i < 100; i++){
                lablelol.push(i)
            }

            ctxNE = document.getElementById(NEcanvasID).getContext('2d');
            //config
            configNE = {
                type: 'line',
                data: {
                    labels: lablelol,
                    datasets: [
                        {
                        label: 'locations',
                        data: data_locations,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                        ],
                        borderWidth: 1,
                        fill: false
                    },
                    {
                        label: 'orga',
                        data: data_organisations,
                        backgroundColor: [
                            'rgba(54, 162, 235, 0.2)',
                        ],
                        borderColor: [
                            'rgba(54, 162, 235, 1)',
                        ],
                        borderWidth: 1,
                        fill: false
                    },
                    {
                        label: 'persons',
                        data: data_persons,
                        backgroundColor: [
                            'rgba(255, 206, 86, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 206, 86, 1)'
                        ],
                        borderWidth: 1,
                        fill: false
                    }
                ]
                },
            }
            //crate charts
            ChartNE = new Chart(ctxNE, configNE);
        },
        error:function(error) {
            console.error(error)   
        }
    })
}


addNamedEntities("chart_NE")