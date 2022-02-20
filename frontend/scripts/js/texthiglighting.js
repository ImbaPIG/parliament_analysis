



// some custome list to simulate text highlighting
let custome_named_Entities = ["ich","Ich","Kollegen"]
let custome_locations = ["Bundestag"]
let custome_organisations = ["AfD","SPD"]

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
    //let paragraph = document.getElementById("named_text").textContent
    let words = paragraph.split(" ")
    let count = 0


    //color each word if it is in a named Entitie
    words.forEach(word => {
        namedEntities.forEach(n =>{
            if(word.toLowerCase().includes(n.toLowerCase())){
                words[count] = "<mark class='blue'>" + word + "</mark>"
            }
        })
        locations.forEach(l =>{
            if(word.toLowerCase().includes(l.toLowerCase())){
                words[count] = "<mark class='red'>" + word + "</mark>"
            }
        })
        organisations.forEach(o =>{
            if(word.toLowerCase().includes(o.toLowerCase())){
                words[count] = "<mark class='yellow'>" + word + "</mark>"
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

    let input = document.getElementById("filterspeeches")
    $.ajax({
        url: "http://localhost:4567/api/speech?redeID="+input.value,
        type: "GET",
        dataType: "json",
        success: async function(speech) {
            let speechresult = speech.result[0]

            highlight_text(speechresult.content, speechresult.analyzed.persons, speechresult.analyzed.locations, speechresult.analyzed.organisations)
        },
        error: function(e) {
            console.log(e)
        }
    })

}






highlight_text()



