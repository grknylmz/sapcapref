module.exports = {
    handler: function (event, context) {
        console.log( JSON.stringify(event || null));

        var variables = ""
        for (var name in event)
    variables += name + "\n";

        //return "Test event " + JSON.stringify(event || null) + " Context " + JSON.stringify(context || null);
        console.log(variables)
        return variables;

    }
}