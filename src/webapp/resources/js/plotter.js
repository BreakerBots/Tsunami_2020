// Plotter.js

//Setup
var plotterDesmos = Desmos.GraphingCalculator(document.querySelector('#plotterWindow'), {
	keypad: false,
	expressions: false,
	settingsMenu: false
});

//Visuals
function plotterReset() {
	for (var i = 0; i < Object.keys(Desmos.Colors).length; i++) {
		plotterDesmos.setExpression({
			id: "table" + Object.keys(Desmos.Colors)[i],
			type: 'table',
			columns: [
				{
					latex: 'x',
					values: [],
					lines: true,
					points: false,
					color: Object.values(Desmos.Colors)[i]
				},
				{
					latex: 'y',
					values: [],
					lines: true,
					points: false,
					color: Object.values(Desmos.Colors)[i]
				}
			]
		});
	}
} plotterReset();
function plotterAddToTable(x, y, color) {
	var exp = plotterDesmos.getExpressions()[Object.keys(Desmos.Colors).indexOf(color)];
	exp.columns[0].values.push(x);
	exp.columns[1].values.push(y);
	plotterDesmos.setExpression(exp);
}

//Pause/Play
function handlePlotterPausePlay() {
	plotterRunning = !plotterRunning;
	document.querySelector("#plotter-pause-play").src = plotterRunning ? "resources/images/pause.png" : "resources/images/play.png";
}
window.addEventListener("keydown", handlePlotterKeyPressed, false);
function handlePlotterKeyPressed(evt) {
	if (location.hash == "#plotter") {
		if (evt.code == "Space") {
			handlePlotterPausePlay();
			evt.preventDefault();
		}
		else if (evt.key == "s" && evt.ctrlKey) {
			handleSavePlotter();
			evt.preventDefault();
		}
	}
}

//Save Plotter
function handleSavePlotter() {
	if (plotterRunning)
		handleTunerPausePlay();

	let csvContent = "data:text/csv;charset=utf-8,";

	var expressions = plotterDesmos.getExpressions();

	//remove empty tables
	for (var i = 0; i < expressions.length; i++) {
		if (expressions[i].columns[0].values.length < 1) {
			expressions.splice(i, 1);
			i--;
		}
	}

	var biggestTableSize = 0;
	expressions.forEach(function (exp) {
		csvContent += exp.id + "_x," + exp.id + "_y,";
		if (exp.columns[0].values.length > biggestTableSize)
			biggestTableSize = exp.columns[0].values.length;
	});
	csvContent = csvContent.slice(0, -1) + "\r\n";

	for (var i = 0; i < biggestTableSize; i++) {
		expressions.forEach(function (exp) {
			if (exp.columns[0].values.length > i)
				csvContent += exp.columns[0].values[i] + "," + exp.columns[1].values[i] + ",";
			else csvContent += ",,";
		});
		csvContent = csvContent.slice(0, -1) + "\r\n";
	}

	var encodedUri = encodeURI(csvContent);
	var link = document.createElement("a");
	link.setAttribute("href", encodedUri);
	link.setAttribute("download", "breakerboard_plotter_data_" + Math.round(Math.random() * 100000) + ".csv");
	document.body.appendChild(link);

	link.click();
}

//HTTP Requests
var plotterRunning = true;
function plotterUpdate() {
	if (plotterRunning && robotConnected) {
		var xhr = new XMLHttpRequest();
		xhr.timeout = 200;
		xhr.onreadystatechange = function () {
			if (this.readyState != 4) return;

			//Data Recieved
			if (this.status == 200) {
				var data = JSON.parse(this.responseText);
				data.forEach(function (point) {
					if (point.color == "RESET")
						plotterReset();
					else plotterAddToTable(point.x, point.y, point.color);
				});
			}
		};
		//Send Request
		xhr.open("POST", "/plotter/get", true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send("");
	}
}
setInterval(plotterUpdate, 250);