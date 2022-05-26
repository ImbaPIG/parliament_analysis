//import createchartHTML from "chartHandler.js";


/**
 * This module handles the dashbaord adding functions
 * 
 * 
 * 
 * 
 */



//standard dashboard title
var dashboardTitle = "Übersicht"



/**
 * dashboard HTML creates a new dashboard in the HTML file
 * 
 * 
 * the daboard will be added at the end of the current dashboard
 * 
 *  This function is written by Moritz, Erik
 *  This function is modifeid by
 */
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

    //Here the new dashboard gets added to the maindashboard
    $("#newdashboard").html( baseframe );
    set_global_party_filter()

    const fromDate = new Date($('#datepicker1').val());
    const toDate = new Date($('#datepicker2').val());
    let fromDateString = `${fixDate(fromDate.getDate().toString())}.${fixDate(fromDate.getMonth()+1)}.${fromDate.getFullYear()}`;
    let toDateString = `${fixDate(toDate.getDate().toString())}.${fixDate(toDate.getMonth()+1)}.${toDate.getFullYear()}`;
    fromDateString = (fromDateString.includes("NaN") ||fromDateString === undefined ) ? "01.01.2000" : fromDateString;
    toDateString = (toDateString.includes("NaN") ||toDateString === undefined) ? "01.01.3000" : toDateString;


    fillCharts(fromDateString, toDateString);
}


/**
 * function that is used to fill the new dashbaord charts
 * @param {} fromDateString 
 * @param {*} toDateString 
 * 
 *  This function is written by Moritz, Erik
 *  This function is modifeid by
 */
const fillCharts = (fromDateString, toDateString) => {
    //now additional charts get created and added to the new dashboard

    //add Sentiment chart
    chartBaseFrame = createchart("newSentiment", "Sentiment")
    $("#newDashboardCharts").html(chartBaseFrame);
    addSentiment("newSentiment", fromDateString, toDateString)

    //add POS
    chartBaseFrame = chartBaseFrame + create_bar_chart("newPOS", "POS")
    $("#newDashboardCharts").html(chartBaseFrame);
    addPOSchart("newPOS", fromDateString, toDateString)

    //addToken
    chartBaseFrame = chartBaseFrame + create_line_chart("newToken", "Token")
    $("#newDashboardCharts").html(chartBaseFrame);
    addTokenChart("newToken", fromDateString, toDateString)

    //add NE
    chartBaseFrame = chartBaseFrame + createchart("newNE", "Named Entities")
    $("#newDashboardCharts").html(chartBaseFrame);
    addNamedEntities("newNE", fromDateString, toDateString)

    //add Speaker
    chartBaseFrame = chartBaseFrame + create_bar_chart("newSpeaker", "Speaker")
    $("#newDashboardCharts").html(chartBaseFrame);
    addSpeakerChart("newSpeaker")

}


//this functions creates a baseframe for a chart
/**
 * 
 * @param {*} canvasID 
 * @param {*} chartName 
 * @returns 
 * 
 * This functions is written by
 */
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


/**
 * This function is used to create a bar chart
 * 
 * 
 * @param {*} canvasID 
 * @param {*} chartName 
 * @returns 
 * 
 * This function is written by
 */
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


/**
 * This function is used to create a Linechart
 * 
 * @param {*} canvasID 
 * @param {*} chartName 
 * @returns 
 * 
 * This function is written by Moritz
 */
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

/**
 * This function gets called if we want to remove a dasboard
 * 
 * the dasboard gets removed by changing the inside of the new dashbaord div
 * 
 * This function is written
 * 
 */
function removeDashboard(){
    $("#newdashboard").html( '' )
}


/**
 * This functions sets the title of a dashboard
 * 
 * @param {*} title 
 * 
 * This function is written
 */
function set_dashboardTitle(title) {

    if(title.includes("#")){
        //title.split("#")
        dashboardTitle = title.split("#")[1]
    }else{
        dashboardTitle = title  
    }  
}


