const chartAdder = async (dataType, objectType, ID) => {
    // example function call chartAdder(4, "speaker", "11004328")
    // legend for dataType
    // 0 - Select Data Point
    // 1 - Part of Speech
    // 2 - named Entities
    // 3 - Token
    // 4 - Sentiment

    // TODO add dom element with with unique number in id to adress loading animation when data fetching is finished (using charHTML function)


    if (dataType == 0) {
        // show user errormessage: please select a dataType
    }
    else if (dataType == 1) {
        // POS
        if (objectType == "speaker") {
            // fetch POS for Speaker (ID is given by the ID variable)
            // add function call to POS chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "fraction") {
            // fetch POS for fraction (ID is given by the ID variable)
            // add function call to POS chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "party") {
            // fetch POS for party (ID is given by the ID variable)
            // add function call to POS chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "all") {
            // fetch POS for everyone
            // add function call to POS chart create function and give it data, labels and chartID of created html-canvas
        }
    }
    else if (dataType == 2) {
        // named Entities
        if (objectType == "speaker") {
            // fetch named Entities for Speaker (ID is given by the ID variable)
            // add function call to named Entities chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "fraction") {
            // fetch named Entities for fraction (ID is given by the ID variable)
            // add function call to named Entities chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "party") {
            // fetch named Entities for party (ID is given by the ID variable)
            // add function call to named Entities chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "all") {
            // fetch named Entities for everyone
            // add function call to named Entities chart create function and give it data, labels and chartID of created html-canvas
        }
    }
    else if (dataType == 3) {
        // Token
        if (objectType == "speaker") {
            // fetch Token for Speaker (ID is given by the ID variable)
            // add function call to Token chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "fraction") {
            // fetch Token for fraction (ID is given by the ID variable)
            // add function call to Token chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "party") {
            // fetch Token for party (ID is given by the ID variable)
            // add function call to Token chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "all") {
            // fetch Token for everyone
            // add function call to Token chart create function and give it data, labels and chartID of created html-canvas
        }
    }
    else if (dataType == 4) {
        // Sentiment
        if (objectType == "speaker") {
            // fetch Sentiment for Speaker (ID is given by the ID variable)
            // add function call to Sentiment chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "fraction") {
            // fetch Sentiment for fraction (ID is given by the ID variable)
            // add function call to Sentiment chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "party") {
            // fetch Sentiment for party (ID is given by the ID variable)
            // add function call to Sentiment chart create function and give it data, labels and chartID of created html-canvas
        }
        else if (objectType == "all") {
            // fetch Sentiment for everyone
            // add function call to Sentiment chart create function and give it data, labels and chartID of created html-canvas
        }
    }

}

const removeChart = e => {
    // TODO
    // get using add on click event on all red crosses
    // remove parent of e.target from dom 
}


const charHTML = (canvasID, chartName, ) => {
let baseframe = `
<div class="row">
    <div class="col-xl-12 col-lg-7">
        <div class="card shadow mb-4" >
            <div class="card-header">
                <h6 class="m-0 font-weight-bold text-primary">${chartName}</h6>
            </div>
            <div class="card-body">
                <div class="chart-bar">
                    <canvas id="${canvasID}" height="100"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>
`}