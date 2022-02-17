$.ajax({
    url: "http://localhost:4567/api/route/tokens",
    type: "GET",
    dataType: "json",
    success: async function(token) {
        console.log("what")
    }
})