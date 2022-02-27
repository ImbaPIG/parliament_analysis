const baseURL = "http://localhost:4567/api/"

const getStatistics = async () => {
    try {
        data = await $.ajax({
            url: `${baseURL}statistic`,
            type: "GET",
            dataType: "json",
            accepts: {
                text: "application/json"
            }
        })
        return data.result
    } catch (e) {
        console.error(e);
    }
}

async function updateProgress(){
    try {
        data = await $.ajax({
            url: `${baseURL}fetchProtocollsProgress`,
            type: "GET",
            dataType: "json",
            accepts: {
                text: "application/json"
            }
        })
        return data.result
    } catch (e) {
        console.error(e);
    }
}
async function fetchProtokolls(protocollLink){
    try {
        if(typeof protocollLink == "undefined"){
            console.log("fetching default");
            await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}fetchProtocolls`
            })
        } else {
            await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}fetchProtocolls?link=${protocollLink}`
            })
        }
    } catch (e) {
        console.error(e);
    }
}


const getRednerByID = async (rednerID) => {
    try {
        let data = localStorage.getItem(`rednerByID:${rednerID}`);
        if(data){
            return JSON.parse(data)
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}speakers?id=${rednerID}`
            })
            localStorage.setItem(`rednerByID:${rednerID}`, JSON.stringify(data.result))
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}


const getRedeByID = async (redeID) => {
    try {
        let data = localStorage.getItem(`redenByID:${redeID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}speech?id=${redeID}`
            })
            localStorage.setItem(`redenByID:${redeID}`, JSON.stringify(data));
            return data;
        }
    } catch (e) {
        console.error(e);
    }
}

const getFractions = async () => {
    try {
        let data = localStorage.getItem(`fractions`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}fractions`
            })
            localStorage.setItem(`fractions`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getSpeakersByFraction = async (fraction) => {
    try {
        let data = localStorage.getItem(`speakerByFraction:${fraction}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}speakers?fraction=${fraction}`
            })
            localStorage.setItem(`speakerByFraction:${fraction}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getParties = async () => {
    try {
        let data = localStorage.getItem(`parties`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}parties`
            })
            localStorage.setItem(`parties`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getSpeakersByParty = async (party) => {
    try {
        let data = localStorage.getItem(`speakerByParty:${party}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}speakers?party=${party}`
            })
            localStorage.setItem(`speakerByParty:${party}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getSentimentBySpeaker = async (speakerID) => {
    try {
        let data = localStorage.getItem(`sentimentBySpeaker:${speakerID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}sentiment?user=${speakerID}`
            })
            localStorage.setItem(`sentimentBySpeaker:${speakerID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getSentimentByFraction = async (fractionID) => {
    try {
        let data = localStorage.getItem(`sentimentByFraction:${fractionID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}sentiment?fraction=${fractionID}`
            })
            localStorage.setItem(`sentimentByFraction:${fractionID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getPosByFraction = async (fractionID) => {
    // gets the pos named more than 5 times
    try {
        let data = localStorage.getItem(`posByFraction:${fractionID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}pos?minimum=3&fraction=${fractionID}`
            })
            localStorage.setItem(`posByFraction:${fractionID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getPosBySpeaker = async (speakerID) => {
    // gets the pos named more than 5 times
    try {
        let data = localStorage.getItem(`posByFraction:${speakerID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}pos?minimum=3&user=${speakerID}`
            })
            localStorage.setItem(`posByFraction:${speakerID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getTokenByFraction = async (fractionID) => {
    // gets the token named more than 5 times
    try {
        let data = localStorage.getItem(`tokenByFraction:${fractionID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}tokens?minimum=10&fraction=${fractionID}`
            })
            localStorage.setItem(`tokenByFraction:${fractionID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getTokenBySpeaker = async (speakerID) => {
    // gets the token named more than 5 times
    try {
        let data = localStorage.getItem(`tokenByFraction:${speakerID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}tokens?minimum=10&user=${speakerID}`
            })
            localStorage.setItem(`tokenByFraction:${speakerID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getEntityByFraction = async (fractionID) => {
    // gets the token named more than 5 times
    try {
        let data = localStorage.getItem(`entityByFraction:${fractionID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}namedEntities?minimum=10&fraction=${fractionID}`
            })
            localStorage.setItem(`entityByFraction:${fractionID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}

const getEntityBySpeaker = async (speakerID) => {
    // gets the token named more than 5 times
    try {
        let data = localStorage.getItem(`entityByFraction:${speakerID}`)
        if(data){
            return JSON.parse(data);
        } else {
            data = await $.ajax({
                method: "GET",
                dataType: "json",
                accepts: {
                    text: "application/json"
                },
                url: `${baseURL}namedEntities?user=${speakerID}`
            })
            localStorage.setItem(`entityByFraction:${speakerID}`, JSON.stringify(data.result));
            return data.result;
        }
    } catch (e) {
        console.error(e);
    }
}


