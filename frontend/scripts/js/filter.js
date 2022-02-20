var global_party_filter = ""

function fill_select_party(){
var list = document.getElementById("allOptions")
var speechOptions = document.getElementById("allSpeeches")

    $.ajax({
        url:"http://api.prg2021.texttechnologylab.org/speakers",
        type: "GET",
        dataType: "json",
        success: function(speakers) {
            var speakerList = speakers.result;
    
            speakerList.forEach(r => {
                var option = document.createElement('option');
                option.value = r.id + "#" + r.firstname + " " + r.name;
                option.text=r.firstname + " " + r.name;
                list.appendChild(option)
            });
        },
        error: function(redner) {
            console.log("Fehler")
        }
    })



    $.ajax({
        url:"http://localhost:4567/api/statistic",
        type: "GET",
        dataType: "json",
        success: function(speeches) {
            var speechesList = speeches.result[0].persons;
    
            speechesList.forEach(r => {
                var option = document.createElement('option');
                option.value = r.id;;
                speechOptions.appendChild(option)
            });
        },
        error: function(redner) {
            console.log("Fehler")
        }
    })





    $.ajax({
        url:"http://api.prg2021.texttechnologylab.org/parties",
        type: "GET",
        dataType: "json",
        success: function(parties) {
            var parties1 = parties.result
            //Add other parties
            parties1.forEach(party => {
                var option = document.createElement('option');
                option.value = party.id+" [Partei]"
                option.text = party.id +" [Partei]"
                list.appendChild(option)
            });   
        },
        error: function(parties) {
            console.log("Fehler")
        }
    })

    $.ajax({
        url:"http://api.prg2021.texttechnologylab.org/fractions",
        type: "GET",
        dataType: "json",
        success: function(fractions) { 
            var fractions1 = fractions.result
            fractions1.forEach(fraction => {
                var option = document.createElement('option');
                option.value = fraction.id+ " [Fraktion]"
                option.text = fraction.id + " [Fraktion]"
                list.appendChild(option)
            });    
        },
        error: function(parties) {
            console.log("Fehler")
        }
    })
    

}

function set_global_party_filter(){
        
        var e = document.getElementById("filterinput");
        if(e.value.includes("[Partei]")){
            global_party_filter = "?party="+ e.value.split(" ")[0]
            console.log(e.value.split(" ")[0])

        }else if(e.value.includes("[Fraktion]")){
            global_party_filter = "?fraction="+ e.value.split(" ")[0]
            console.log(e.value.split(" ")[0])

        }else{
            //e.value.split("#")[0]
            global_party_filter = "?user="+e.value.split("#")[0]
            set_dashboardTitle(e.value.split("#")[1]);
        }
}

$(document).ready(function () {
    // Listen to click event on the submit button
    $('#submitFilter').click(function (e) {
  
      e.preventDefault();
      set_global_party_filter();
      dashboardHTML();

    });
  });

document.querySelector('#filterinput').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
        set_global_party_filter();
        dashboardHTML();
    }
});

$(document).ready(function () {
    // Listen to click event on the submit button
    $('#submitSpeech').click(function (e) {
  
      e.preventDefault();
      setTextContent();
    });
  });

fill_select_party()