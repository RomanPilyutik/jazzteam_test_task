$(document).ready(function () {
    getLaunchedRobotIds();
    getLogs()
});

function getLogs() {
    var printedLines = 0;
    setInterval(function () {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/log/remainingLines",
            cache: false,
            data: {printedLines: printedLines},
            success: function (res) {
                $.each(res.lines, function (i, item) {
                    var line = item.toString() + '\n';
                    $("#logs").append(line);
                });
                printedLines = res.printedLines;
                $('#logs').scrollTop($('#logs')[0].scrollHeight);
            },
            error: function (e) {
                alert(e);
            }
        });
    }, 1000)
}

function getLaunchedRobotIds() {
    var currentRobotsOnPage = [];
    $("#robots").find("option").each(function(i, option) {
        currentRobotsOnPage.push(option.value)
    });
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/robot/launchedRobots",
        cache: false,
        success: function (robots) {
            $.each(robots, function (i, robot) {
                if($.inArray(robot.id, currentRobotsOnPage) >= 0) {
                    currentRobotsOnPage = jQuery.grep(currentRobotsOnPage, function(value) {
                        return value != robot.id;
                    });
                } else {
                    $('#robots')
                        .append($("<option></option>")
                            .attr("value", robot.id)
                            .text(robot.id));
                }
            });
            $.each(currentRobotsOnPage, function(i, value) {
                $("#robots").find("option[value='" + value + "']").remove();
            });
            setTimeout(getLaunchedRobotIds, 500);
        },
        error: function (e) {
            alert(e);
        }
    });
}

function setCommand(sendToAll) {
    var targetRobotIds = [];
    if(sendToAll) {
        $("#robots").find("option").each(function(i, option) {
            targetRobotIds.push(option.value)
        });
    } else {
        $("#robots").find("option:selected").each(function(i, option) {
            targetRobotIds.push(option.value)
        });
    }
    if(!targetRobotIds && !sendToAll) {
        alert("Please select target robot id");
        return false
    }
    var commandType = $("#commands").val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({commandType: commandType, targetRobotIds: targetRobotIds}),
        dataType: "json",
        url: "/robot/setCommand",
        cache: false
    });
}

