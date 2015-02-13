/*******************************************************************************
 * @author: nothing to be proud of
 *
 * scripts to be included on the html file
 * <script type="text/javascript" language="javascript" src="../js/widgets/text-monitor.js"></script>
 * <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
 ******************************************************************************/

$(document).ready(function () {

    var nodes = document.getElementsByClassName("text-monitor");
    var len = nodes.length;
    var inputs = {};
    var currentAlarms = {};
    counter = 0;
    
    function changeAlarm(severity, id, widget) {
        var currentAlarm = currentAlarms[id];
        if (currentAlarm) {
            widget.classList.remove(currentAlarm);
        }
        switch (severity) {
            case "MINOR":
                currentAlarm = "alarm-minor";
                break;
            case "MAJOR":
                currentAlarm = "alarm-major";
                break;
            case "INVALID":
                currentAlarm = "alarm-invalid";
                break;
            case "UNDEFINED":
                currentAlarm = "alarm-undefined";
                break;
            default:
                currentAlarm = "alarm-none";
                break;
        }
        currentAlarms[id] = currentAlarm;
        widget.classList.add(currentAlarm);
    }

    for (var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            counter++;
            id = "text-monitor-" + counter;
            nodes[i].id = id;
        }
        var input = document.createElement("input");
        input.id = id;
        input.disabled = true;
        input.style.width = "100%";
        input.style.height = "100%";
        input.style.font = "inherit";
        input.style.textAlign = "inherit";
        var div = document.getElementById(id);
        div.appendChild(input);

        if (channelname !== null && channelname.trim().length > 0) {
            var callback = function (evt, channel) {
                switch (evt.type) {
                    case "connection": //connection state changed
                        break;
                    case "value": //value changed
                        var channelValue = channel.getValue();
                        if ("value" in channelValue) {
                            inputs[channel.getId()].value = (channelValue.value);
                        } else {
                            inputs[channel.getId()].value = (channelValue.type.name);
                        }
                         
                        if ("alarm" in channelValue) {
                            changeAlarm(channelValue.alarm.severity, channel.getId(), inputs[channel.getId()]);
                        } else {
                            changeAlarm("NONE", channel.getId(), inputs[channel.getId()]);
                        }
                        inputs[channel.getId()].parentNode.removeAttribute("title");
                        break;
                    case "error": //error happened
                        changeAlarm("INVALID", channel.getId(), inputs[channel.getId()]);
                        inputs[channel.getId()].parentNode.title = evt.error;
                        break;
                    case "writeCompleted": // write finished.
                        break;
                    default:
                        break;
                }
            };
            var channel = wp.subscribeChannel(channelname, callback, true);
            inputs[channel.getId()] = input;
        }
    }

});

window.onbeforeunload = function () {
    wp.close();
};
