



// some custome list to simulate text highlighting
var custome_named_Entities = ["ich","Ich","Kollegen"]
var custome_locations = ["Bundestag"]
var custome_organisations = ["AfD","SPD"]

/**
 * This function is used for highlighting the text
 * 
 * we get the text content (speech) and then we collect each word and than look up if the word is
 * a Named Entitie and color it.
 * 
 * After that we put the words back together again
 * and display the text
 * 
 */
function highlight_text(paragraph,namedEntities,locations,organisations) {
    //var paragraph = document.getElementById("named_text").textContent
    var words = paragraph.split(" ")
    var count = 0


    //color each word if it is in a named Entitie
    words.forEach(word => {
        custome_named_Entities.forEach(n =>{
            if(word.toLowerCase().includes(n.toLowerCase())){
                words[count] = "<mark class='blue'>" + word + "</mark>"
            }
        })
        custome_locations.forEach(l =>{
            if(word.toLowerCase().includes(l.toLowerCase())){
                words[count] = "<mark class='red'>" + word + "</mark>"
            }
        })
        custome_organisations.forEach(o =>{
            if(word.toLowerCase().includes(o.toLowerCase())){
                words[count] = "<mark class='yellow'>" + word + "</mark>"
                console.log(o, count)
            }
        })
        count++
    });

    //put the words together again
    marked_paragraph = ""
    words.forEach(word => {
        marked_paragraph =marked_paragraph + word + " "
    });
    $("#paragraph_body").html("<p>"+marked_paragraph+"<p>")

}



function setTextContent(){

    var input = document.getElementById("filterspeeches")
    $.ajax({
        url: "http://localhost:4567/api/speech?redeID="+input.value,
        type: "GET",
        dataType: "json",
        success: async function(speech) {
            console.log(speech)
            var speechresult = speech.result[0]

            highlight_text(speechresult.content, speechresult.analyzed.persons, speechresult.analyzed.locations, speechresult.analyzed.organisations)
        },
        error: function(pos) {
            //if something happens we get an error message
            console.log("Fehler")
        }
    })

}






highlight_text()



