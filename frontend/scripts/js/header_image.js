/**
 * 
 * adds image to the speech analysis card
 * 
 * @param {image} imagelink 
 * 
 * This function was written by Moritz
 * This function was edited by Erik
 */

 function get_image_link_from_speaker(id){

    $.ajax({
        url: "http://localhost:4567/api/speakers?rednerID=" + id,
        type: "GET",
        dataType: "json",
        success: function(speaker){
            console.log(speaker)
            let picture_url = speaker.result[0].picture
            
            display_speaker_information(speaker.result[0])
            add_image_to_header(picture_url)
        }
    })

}

/**
 * 
 * adds the speaker image to the Texthighlighting method
 * 
 * @param {*} imagelink 
 * 
 * This function was written by Moritz
 * This function was edited by Erik
 */
function add_image_to_header(imagelink){

    var div = document.getElementById("speaker_image");
    var img = document.createElement("img");

    img.src = imagelink;
    img.alt = "kein Bild vorhanden"

    div.appendChild(img)

}


/**
 * 
 * function used to display the Speaker information of a speech
 * 
 * @param {*} speaker 
 * 
 * This function was written by <Name>
 * This function was edited by <Name>
 */
function display_speaker_information(speaker){
    var div = document.getElementById("speaker_image");

    div.innerHTML = ""

    var paragraph = document.createElement("p");
    var speaker_info = "Name: " + speaker.firstname + " " + speaker.name +"<br>" + "Partei: " + speaker.party + "<br>" + "Fraktion: " + speaker.fraction

    paragraph.innerHTML = speaker_info

    div.appendChild(paragraph)

}