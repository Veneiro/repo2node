let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');

let app = express();

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Credentials", "true");
  res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
  // Debemos especificar todas las headers que se aceptan. Content-Type , token
  next();
});

let jwt = require('jsonwebtoken');
app.set('jwt', jwt);

let expressSession = require('express-session');
app.use(expressSession({
  secret: 'abcdefg',
  resave: true,
  saveUninitialized: true
}));

let crypto = require('crypto');
app.set('clave','abcdefg');
app.set('crypto',crypto);

let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const { MongoClient } = require("mongodb");
const url = 'mongodb://localhost:27017';
app.set('connectionStrings', url);

const userSessionRouter = require('./routes/userSessionRouter');

const userTokenRouter = require('./routes/userTokenRouter');
app.use("/api/v1.0/offers/", userTokenRouter);
app.use("/api/v1.0/conversations/", userTokenRouter);

app.use("/offers/add",userSessionRouter);
app.use("/offers",userSessionRouter);
app.use("/offers/buy",userSessionRouter);
app.use("/offers/highlight",userSessionRouter);
app.use("/offers/highlights",userSessionRouter);
app.use("/purchases",userSessionRouter);
app.use("/shop",userSessionRouter);

const logsRepository = require("./repositories/logsRepository.js");
const offersRepository = require("./repositories/offersRepository.js");
const usersRepository = require("./repositories/usersRepository.js");
const conversationsRepository = require("./repositories/conversationsRepository");

logsRepository.init(app, MongoClient);
usersRepository.init(app, MongoClient);
offersRepository.init(app, MongoClient);
conversationsRepository.init(app, MongoClient);

require("./routes/users.js")(app, usersRepository, offersRepository, conversationsRepository, logsRepository);
require("./routes/offers.js")(app, offersRepository, usersRepository, conversationsRepository, logsRepository);
require("./routes/api/myWallapopAPIv1.0.js")(app, conversationsRepository, usersRepository, offersRepository, logsRepository);
require("./routes/logs.js")(app, logsRepository);



const insertSampleData = require("./utils/InsertSampleData");
(async()=>{await insertSampleData.init(app, MongoClient)})();

let indexRouter = require('./routes/index');
let usersRouter = require('./routes/users');

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
