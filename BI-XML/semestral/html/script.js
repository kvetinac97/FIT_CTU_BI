console.log("Script loaded!");

// Land graph chart
if ( typeof(agriculturalLand) !== 'undefined' ) {
    let ctx = document.getElementById("landChart").getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ["Agricultural", "Forest", "Other"],
            datasets: [{
                backgroundColor: ["#00FF00", "#0000FF", "#000000"],
                data: [agriculturalLand, forestLand, otherLand]
            }]
        }
    });
}

// Language graph chart
if ( typeof(languages) !== 'undefined' && languages.size > 0 ) {
    let ctx = document.getElementById("langChart").getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: Array.from(languages.keys()),
            datasets: [{
                backgroundColor: ["#FF0000", "#00FFFF",
                    "#F0F0F0", "#FFFF00", "#00FF00",
                    "#0000FF", "#C9C9C9", "#609060"]
                    .slice(0, languages.size),
                data: Array.from(languages.values())
            }]
        }
    });
}
else {
    document.querySelector("#langChartDiv").innerHTML = document.querySelector("#langTextDiv").innerHTML;
    document.querySelector("#langTextDiv").style.display = "none";
}

// GDP division by sector of origin graph chart
if ( typeof(compositions) !== 'undefined' && compositions.has("agriculture") ) {
    let ctx = document.getElementById("gdpChart").getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ["Agriculture", "Industry", "Services"],
            datasets: [{
                backgroundColor: ["#406040", "#0099FF", "#502050"],
                data: [compositions.get("agriculture"), compositions.get("industry"), compositions.get("services")]
            }]
        }
    });
}

// Energy division
if ( typeof(fossil) !== 'undefined' ) {
    let ctx = document.getElementById("energyChart").getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ["Fossil", "Nuclear", "Hydroelectric", "Other"],
            datasets: [{
                backgroundColor: ["#8B4513", "#FFFF00", "#0099FF", "#666666"],
                data: [fossil, nuclear, hydroelectric, other]
            }]
        }
    });
}

// Map
if ( typeof(long) !== 'undefined' ) {
    document.querySelector('#label-geography').onclick = function(){
        setTimeout(function(){
            Loader.load(null, null, function() {
                var center = SMap.Coords.fromWGS84(longType === 'E' ? long : -long, latType === 'N' ? lat : -lat);
                var m = new SMap(JAK.gel("m"), center, 5);
                m.addDefaultLayer(SMap.DEF_BASE).enable();
                m.addDefaultControls();
            });
        }, 250);
    };
}