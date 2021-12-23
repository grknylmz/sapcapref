const express = require('express');
const app = express();
const bodyParser = require('body-parser');

const xsenv = require('@sap/xsenv');
xsenv.loadEnv();
const services = xsenv.getServices({
    uaa: { tag: 'xsuaa' }
});

const xssec = require('@sap/xssec');
const passport = require('passport');
passport.use('JWT', new xssec.JWTStrategy(services.uaa));
app.use(passport.initialize());
app.use(passport.authenticate('JWT', { session: false }));

app.use(bodyParser.json());

app.get('/user/userattributes', function (req, res) {
    var isAuthorized = req.authInfo.checkScope('$XSAPPNAME.Rank');
    if (isAuthorized) {
        res.status(200).json(req.authInfo.userAttributes);
    } else {
        res.status(403).send('Forbidden');
    }
});

app.get('/user/userinfo', function (req, res) {
    var result = req.authInfo.userInfo;
    var email = result.email;
    var neptun = email.substr(0, email.indexOf('@'));
    if(neptun.length > 6) neptun = "GUESTU";
    Object.assign(result, {"neptun" : neptun});
    res.status(200).json(result);
});

const port = process.env.PORT || 5001;
app.listen(port, function () {
    console.info('Listening on http://localhost:' + port);
});