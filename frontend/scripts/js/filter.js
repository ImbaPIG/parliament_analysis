let global_party_filter = ""
let extra_toke_filter=""



/**
 * This function fills the input options in the adding dashboard settings
 * 
 * 
 * 
 * This function was written by
 * This function was edited by Moritz, Erik
 */
function fill_select_party(){
    let list = document.getElementById("allOptions")
    let speechOptions = document.getElementById("allSpeeches")

    //get all Speakers and add them to the list
    $.ajax({
        url:"http://api.prg2021.texttechnologylab.org/speakers",
        type: "GET",
        dataType: "json",
        success: function(speakers) {
            let speakerList = speakers.result;
    
            speakerList.forEach(r => {
                let option = document.createElement('option');
                option.value = r.id + "#" + r.firstname + " " + r.name;
                option.text=r.firstname + " " + r.name;
                list.appendChild(option)
            });
        },
        error: function(e) {
            console.log(e)
        }
    })


    //Search parties and add tehm to the search list
    $.ajax({
        url:"http://api.prg2021.texttechnologylab.org/parties",
        type: "GET",
        dataType: "json",
        success: function(parties) {
            let parties1 = parties.result
            //Add other parties
            parties1.forEach(party => {
                let option = document.createElement('option');
                option.value = party.id+" [Partei]"
                option.text = party.id +" [Partei]"
                list.appendChild(option)
            });   
        },
        error: function(e) {
            console.log(e)
        }
    })

    //add Fractions to Searchlist
    $.ajax({
        url:"http://api.prg2021.texttechnologylab.org/fractions",
        type: "GET",
        dataType: "json",
        success: function(fractions) { 
            let fractions1 = fractions.result
            fractions1.forEach(fraction => {
                let option = document.createElement('option');
                option.value = fraction.id+ " [Fraktion]"
                option.text = fraction.id + " [Fraktion]"
                list.appendChild(option)
            });    
        },
        error: function(e) {
            console.error(e)
        }
    })


    //Add Speeches to Speech search List
    $.ajax({
        url:"http://localhost:4567/api/statistic",
        type: "GET",
        dataType: "json",
        success: function(speeches) {
            let speechesList = speeches.result[0].persons;
    
            speechesList.forEach(r => {
                let option = document.createElement('option');
                option.value = r.id;;
                speechOptions.appendChild(option)
            });
        },
        error: function(e) {
            console.error(e)
        }
    })
    

}


/**
 * sets the global filter
 * 
 * 
 * This function was written
 * This function was edited by
 */
function set_global_party_filter(){
        
        let e = document.getElementById("filterinput");
        if(e.value.includes("[Partei]")){
            global_party_filter = "?party="+ e.value.split(" ")[0]
            extra_toke_filter = "?party="+ e.value.split(" ")[0]
            //console.log(e.value.split(" ")[0])

        }else if(e.value.includes("[Fraktion]")){
            global_party_filter = "?fraktion="+ e.value.split(" ")[0]
            extra_toke_filter = "&fraktion="+ e.value.split(" ")[0]
            //console.log(e.value.split(" ")[0])

        }else{
            //e.value.split("#")[0]
            global_party_filter = "?rednerID="+e.value.split("#")[0]
            extra_toke_filter = "&rednerID="+e.value.split("#")[0]

            //set_dashboardTitle(e.value.split("#")[1]);
        }
}





$(document).ready(function () {
    // Listen to click event on the submit button
    $('#submitFilter').click(function (e) {
  
      e.preventDefault();
      set_global_party_filter();
      dashboardHTML();

    });
    // submitProtokoll
    $('#submitProtokoll').click(function (e) {
        let protokollProgress = 0.0;
        e.preventDefault();
        let protokollLink = $("#protokollinput").val();
        let progressBar = $("#progressbarProtokoll");
        fetchProtokolls(protokollLink);

        setInterval( () => {
            if(protokollProgress == 0.0){return;}
            console.log(protokollProgress);
            progressBar.text = protokollProgress;
            progressBar.width(parseInt(protokollProgress) + "%");
            updateProgress().then(progress => protokollProgress = progress);
        }, 1000)

    });
  });

document.querySelector('#filterinput').addEventListener('keypress', function (e) {
    //Key Listener for pressing enter in the dasboard input
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