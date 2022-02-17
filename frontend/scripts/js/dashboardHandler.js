//import createchartHTML from "chartHandler.js";
var dashboardTitle = "Übersicht"

function dashboardHTML(){

    var input = document.getElementById("filterinput")
    set_dashboardTitle(input.value)


    let baseframe = `
    <div class=" align-items-center justify-content-between py-4 pl-4 m-0 mb-3 bg-white d-block">
                            <h1 class="h1 mb-0 text-gray-800 d-block">${dashboardTitle}</h1>
                            <div class="dashboard-selector d-block">
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

function set_dashboardTitle(title) {
    dashboardTitle = title    
}

