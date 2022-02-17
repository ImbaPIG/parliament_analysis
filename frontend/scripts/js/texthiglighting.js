
var custome_named_Entities = ["ich","Ich","Kollegen","Kolleginnen"]
var custome_locations = ["Bundestag","Bundestages"]
var custome_organisations = ["AfD"]

/*Plan is to take the text and mark the words which appear in the Named Entitis list return the text conntent or better chnage the paragraph directly*/ 
function highlight_text() {
    var paragraph = document.getElementById("named_text").textContent
    //var paragraph = $('#named_text').text()
    //console.log(typeof paragraph)
    //paragraph.replace(/Ich/g, "<mark>ich</mark>")
    //console.log(paragraph)
    var words = paragraph.split(" ")
    console.log(words)
    var count = 0
    words.forEach(word => {
        custome_named_Entities.forEach(n =>{
            if(n === word){
                words[count] = "<mark class='blue'>" + word + "</mark>"
            }
        })
        custome_locations.forEach(l =>{
            if(l === word){
                words[count] = "<mark class='red'>" + word + "</mark>"
            }
        })
        custome_organisations.forEach(o =>{
            if(o === word){
                words[count] = "<mark class='yellow'>" + word + "</mark>"
            }
        })
        count++
    });
    marked_paragraph = ""
    words.forEach(word => {
        marked_paragraph =marked_paragraph + word + " "
    });
    $("#paragraph_body").html("<p>"+marked_paragraph+"<p>")

    

    //go through text and find the words mark them by inserting the mark things
    //need 3 mark classes red blue yellow maybe 

    //insert resulting text into paragraph again

    //return highlighted_text
}

highlight_text()