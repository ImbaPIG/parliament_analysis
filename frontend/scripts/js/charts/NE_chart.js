//Pie chart

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

//A lot of variables which are used in the script
var minimumFilterNE="?minimum=0";
var speakerFilterNE="";
var partyFilterNE="";
var fractionFilterNE="";
var topkNE=100;

var ctxNE;
var ctxNE_persons;
var ctxNE_locations;
var ctxNE_organisations;

var configNE;
var configNE_persons;
var configNE_locations;
var configNE_organisations;

var ChartNE;
var ChartE_persons;
var ChartNE_locations;
var ChartNE_organisations;

var firstTimeNE = true;


//filters Speakers
  function FilterSpeakerNE() {
    var e = document.getElementById("RednerValueNE")
    
    if(e.value === ""){
        speakerFilterNE = "";
    }else{
        speakerFilterNE = "&user=" + e.value;
    }
    NE()
  }

  //sets PArty filter

  function FilterPartyNE() {
    var e = document.getElementById("selectPartyNE");
    if(e.value === "All"){
        partyFilterNE=""
        fractionFilterNE=""
    }else{
        partyFilterNE = "&party="+e.value;
        fractionFilterNE=""
    }
    NE();
  }

  //sets Fraction filter
  function FilterFractionNE() {
    var e = document.getElementById("selectFractionNE");
    if(e.value === "All"){
        fractionFilterNE=""
        partyFilterNE=""
    }else{
        fractionFilterNE = "&fraction="+e.value;
        partyFilterNE=""
    }
    NE();
  }

  //sets minimum filter
  function FilterMinimumNE() {
    var e = document.getElementById("MinimumValueNE")
    if(parseInt(e.value) < 0 || e.value===""){
        minimumFilterNE = "?minimum=0"
    }else{
        minimumFilterNE= "?minimum=" + e.value
    }
    NE()
  }

  //sets TopKNE
  function SetTopKNE(){
    var e = document.getElementById("TopKNE")

    if(parseInt(e.value) < 0 || e.value === ""){
        topkNE = 10
    }else{
        topkNE=e.value
    }
    NE()
  }

  //main function for named Entities
function NE(){
$.ajax({
    url: "http://api.prg2021.texttechnologylab.org/namedEntities" +minimumFilterNE+speakerFilterNE+fractionFilterNE+partyFilterNE,
    type: "GET",
    dataType: "json",
    success: async function(namedEntities){
        //agaon get a lot of variables
        var persons = namedEntities.result[0].persons;
        var organisations = namedEntities.result[1].organisations
        var locations = namedEntities.result[2].locations

        var person_sum = 0
        var data1 = []
        var label1 = []

        var data_locations= []
        var label_locations = []
        
        var data_organisations = []
        var label_organisations = []

        var top = 0
        var top_location = 0
        var top_organisations = 0

        //get top k persons
        persons.forEach(person => {
            person_sum = person_sum + person.count;
            //if (top < topkNE){
            data1.push(person.count);
            label1.push(person.element);
            top = top+1
            //}
        });

        //top k organisations
        var organisation_sum = 0
        organisations.forEach(organisation => {
            organisation_sum = organisation_sum + organisation.count;
            //if(top_organisations < topkNE){
                data_organisations.push(organisation.count);
                label_organisations.push(organisation.element);
                top_organisations = top_organisations+1;
            //}
        });

        //top k loations
        var location_sum = 0
        locations.forEach(location => {
            location_sum = location_sum + location.count;
            //if(top_location<topkNE){
                data_locations.push(location.count);
                label_locations.push(location.element)
                top_location = top_location + 1;
            //}
        });

        var lablelol = []
        for(var i= 0; i < 100; i++){
            lablelol.push(i)
        }

        //create random amount of numbers
       /* var bgColor = []
        var borderColor = []
        for (let i = 0; i< top; i++) {
            let red = parseInt(Math.floor(Math.random()*256));
            let green = parseInt(Math.floor(Math.random()*256));
            let blue = parseInt(Math.floor(Math.random()*256));

            bgColor.push('rgba('+red+','+green+','+blue+', 0.2)')
            borderColor.push('rgba('+red+','+green+','+blue+', 1)')

        }*/
        

        if(firstTimeNE){//first Time the charts get created
        ctxNE = document.getElementById('chart_NE').getContext('2d');


        //config for all things
        configNE = {
            type: 'line',
            
            data: {
                labels: lablelol,
                datasets: [
                    {
                    label: '# of Votes',
                    data: data_locations,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        //'rgba(54, 162, 235, 0.2)',
                        //'rgba(255, 206, 86, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        //'rgba(54, 162, 235, 1)',
                        //'rgba(255, 206, 86, 1)'
                    ],
                    borderWidth: 1,
                    fill: false
                },
                {
                    label: 'orga',
                    data: data_organisations,
                    backgroundColor: [
                        //'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        //'rgba(255, 206, 86, 0.2)'
                    ],
                    borderColor: [
                        //'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        //'rgba(255, 206, 86, 1)'
                    ],
                    borderWidth: 1,
                    fill: false
                },
                {
                    label: 'persons',
                    data: data1,
                    backgroundColor: [
                        //'rgba(255, 99, 132, 0.2)',
                        //'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)'
                    ],
                    borderColor: [
                        //'rgba(255, 99, 132, 1)',
                        //'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)'
                    ],
                    borderWidth: 1,
                    fill: false
                }
            ]
            },
            /*options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }*/
        }

        //crate charts
        ChartNE = new Chart(ctxNE, configNE);

        firstTimeNE = false;
        }else{//END FirstTime
        }
    },
    error:function(error) {
        console.log(error)   
    }
})

}

NE()