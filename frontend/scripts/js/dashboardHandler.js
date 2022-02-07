//import createchartHTML from "chartHandler.js";


function dashboardHTML(){
    let baseframe = `
    <div class=" align-items-center justify-content-between py-4 pl-4 m-0 mb-3 bg-white d-block">
                            <h1 class="h1 mb-0 text-gray-800 d-block">Übersicht</h1>
                            <div class="dashboard-selector d-block">
                                <div class="target-picker inline-flex mt-3 my-2">
                                    <!-- picking if you want to choose a speaker, party or fraction and which one (in best case implemented using a modal) -->
                                    <label class="col-form-label">Pick:</label>
                                    <button class="btn btn-primary modalTrigger" id="speakerModal" data-modal="speaker">Speaker</button>                            
                                    <button class="btn btn-primary modalTrigger" id="partyModal" data-modal="party">Party</button>                            
                                    <button class="btn btn-primary modalTrigger" id="fractionModal" data-modal="fraction">Fraction</button>                            
                                    <button class="btn btn-primary showAll" >Alle</button>                            
                                </div>
                                <div class="dropdown no-arrow">
                                    <button id="removeDash" onclick="removeDashboard()">
                                        <i class="fas fa-times fa-2x text-danger"></i>
                                    </button>
                                </div>
                                <div class="date-picker d-flex mt-3 my-2">
                                    <!-- simple bootstrap date picker  -->
                                    <div class="container m-0 mx-3 p-0 d-inline">
                                        <form>
                                            <div class="row form-group">
                                                <label for="date" class="col-form-label">From Date</label>
                                                <div class="col-sm-4 d-inline">
                                                    <div class="input-group date" id="datepicker1">
                                                        <input type="text" class="form-control">
                                                        <span class="input-group-append">
                                                            <span class="input-group-text bg-white">
                                                                <i class="fa fa-calendar"></i>
                                                            </span>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="container m-0 mx-3 p-0 d-inline">
                                        <form>
                                            <div class="row form-group">
                                                <label for="date" class="col-form-label">To Date</label>
                                                <div class="col-sm-4 d-inline">
                                                    <div class="input-group date" id="datepicker2">
                                                        <input type="text" class="form-control">
                                                        <span class="input-group-append">
                                                            <span class="input-group-text bg-white">
                                                                <i class="fa fa-calendar"></i>
                                                            </span>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="data-picker">
                                    <!-- picking what data you want to show (pos, named entity, simple count of something etc.) -->
                                    <label for="data-point-selector">Select Data</label>
                                    <select class="form-select" aria-label="Default select example" id="datatype-selector">
                                        <option selected>Select Data Point</option>
                                        <option value="1">Part of Speech</option>
                                        <option value="2">named Entities</option>
                                        <option value="3">Three</option>
                                      </select>
                                </div>
                                
                                <!--Search Button (3d), for creting Dashboard with input as title and same functions as above -->
                                <form class="form-inline mr-auto">
                                    <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
                                    <button class="btn blue-gradient btn-rounded btn-sm my-0" type="submit">Search</button>
                                  </form>
    
                                <button class="btn btn-primary mt-4">Add Dashboard</button>
                            </div>
                        </div>



                        <div class="px-3" id="newDashboardCharts">
                            <div class="row">

                                <!-- Area Chart -->
                                <div class="col-xl-12 col-lg-7">
                                    <div class="card shadow mb-4">
                                        <!-- Card Header - Dropdown -->
                                        <div
                                            class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                            <h6 class="m-0 font-weight-bold text-primary">Earnings Overview</h6>
                                            <div class="dropdown no-arrow">
                                                <i class="fas fa-times fa-2x text-danger"></i>
                                            </div>
                                        </div>
                                        <!-- Card Body -->
                                        <div class="card-body">
                                            <div class="chart-area">
                                                <canvas id="myAreaChart"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

    `

    $("#newdashboard").html( baseframe );
    //now trying to add charts
    //create chart should return just the hole div with the canvas id and after that we create the actual chart content

    //chartBaseFrame = createChart(canvasID)
    chartBaseFrame = createchart("newSentiment", "Sentiment")
    $("#newDashboardCharts").html(chartBaseFrame);
    addSentiment("newSentiment")

    //add POS
    chartBaseFrame = chartBaseFrame + create_bar_chart("newPOS", "POS")
    $("#newDashboardCharts").html(chartBaseFrame);
    addPOSchart("newPOS")

    //addToken
    chartBaseFrame = chartBaseFrame + create_line_chart("newToken", "Token")
    $("#newDashboardCharts").html(chartBaseFrame);
    addTokenChart("newToken")

    //add NE
    chartBaseFrame = chartBaseFrame + createchart("newNE", "Named Entities")
    $("#newDashboardCharts").html(chartBaseFrame);
    addNamedEntities("newNE")

    //add Speaker
    chartBaseFrame = chartBaseFrame + create_bar_chart("newSpeaker", "Speaker")
    $("#newDashboardCharts").html(chartBaseFrame);
    addSpeakerChart("newSpeaker")

}



function createchart(canvasID, chartName){
    let baseframe = `
    <div class="row">
        <div class="col-xl-12 col-lg-7">
            <div class="card shadow mb-4" >
                <div class="card-header">
                    <h6 class="m-0 font-weight-bold text-primary">${chartName}</h6>
                </div>
                <div class="card-body">
                    <div class="">
                        <canvas id="${canvasID}" height="100"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `
    return baseframe;
}


function create_bar_chart(canvasID, chartName){
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
    
        `
        return baseframe;
}

function create_line_chart(canvasID, chartName){
    let baseframe = `
    <div class="row">
        <div class="col-xl-12 col-lg-7">
            <div class="card shadow mb-4" >
                <div class="card-header">
                    <h6 class="m-0 font-weight-bold text-primary">${chartName}</h6>
                </div>
                <div class="card-body">
                    <div class="">
                        <canvas id="${canvasID}" height="300"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    `
    return baseframe;
}

//dashboardHTML()
function removeDashboard(){
    //console.log("Jo sgeht")
    $("#newdashboard").html( '' )
}

