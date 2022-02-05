let partyList = [];
let fractionList = [];
let speakerList = [];
let clickedButton;
let wantedModal;
let datatype;



const createModalList = async () => {
    const fractionListElem = $("#fractionList")
    const partyListElem = $("#partyList")
    const speakerListElem = $("#speakerList")

    // create lists and sort
    partyList = await getParties();
    for (let i=0; i < partyList.length; i++){
        partyListElem.append(`<li class="modal-list-item party py-1 ml-0" data-type="fraktion" data-id="${partyList[i].id}">${partyList[i].id}</li>`)
        let currParty = await getSpeakersByParty(partyList[i].id)
        speakerList = speakerList.concat(currParty)
    }
    speakerList.sort((a, b) => a.firstname.localeCompare(b.firstname));
    
    
    fractionList = await getFractions();
    for (let i=0; i < fractionList.length; i++){
        currFrac = fractionList[i];
        if (currFrac == undefined) {continue};
        fractionListElem.append(`<li class="modal-list-item fraction py-1 ml-0" data-type="fraktion" data-id="${currFrac.id}">${currFrac.id}</li>`)
    }

    for (let i=0; i < speakerList.length; i++){
        currSpeaker = speakerList[i];
        if (currFrac == undefined) {continue};
        let org =  currSpeaker.fraction ? currSpeaker.fraction : currSpeaker.party
        speakerListElem.append(`<li class="modal-list-item speaker py-1 ml-0" data-type="speaker" data-id="${currSpeaker.id}">${currSpeaker.firstname} ${currSpeaker.name} [${org}]</li>`)
    }

    // open on button click
    $(".modalTrigger").on("click", e => {
        let button = $(e.target);
        datatype = $("#datatype-selector").val()
        wantedModal = button.attr("data-modal");
        $(`#${wantedModal}-modal`).modal("show")
    })

    // handle "show all" button
    $(".showAll").on("click", e => {
        let button = $(e.target);
        datatype = $("#datatype-selector option:selected").text()
        console.log(datatype)
        wantedModal = button.attr("data-modal");
        // update according graph here ( using all data)
        chartCreator(datatype, wantedModal, "all");
    })
    
    // select on list element click
    $("ul").on("click", ".modal-list-item", (e) => {
        $(`#${wantedModal}-modal`).modal("hide")
        let currField = $(e.target);
        // update graph here
        let wantedID = currField.data("id");
        chartCreator(datatype, wantedModal, wantedID);
    })

    // search
    $(".modal-search").on("input", e => {
        let userInput = $(e.target).val().toLowerCase().replace(/\s/g, "");
        $(`.modal-list-item.${wantedModal}`)        
        .each(function () {
            let curr = $(this).html().toLowerCase().replace(/\s/g, "");
            (userInput == "" || curr.includes(userInput)) ? $(this).show() : $(this).hide();
        })
    })

} 

createModalList()