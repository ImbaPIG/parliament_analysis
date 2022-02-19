/**
 * 
 * adds image to the speech analysis card
 * 
 * @param {image} imagelink 
 */


function add_image_to_header(imagelink){

    var div = document.getElementById("speaker_image");
    var img = document.createElement("img");

    img.src = imagelink;
    img.alt = "kein Bild vorhanden"

    div.appendChild(img)

}

add_image_to_header("scripts/img/custome_img.jpeg")