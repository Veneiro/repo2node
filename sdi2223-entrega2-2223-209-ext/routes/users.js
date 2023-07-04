const {ObjectId} = require("mongodb");
const {validationResult} = require("express-validator");
const {singUpValidatorInsert} = require('../validator/SingupValidator');

module.exports = function (app, usersRepository, offersRepository, conversationsRepository, logsRepository) {

    /**
     * Nos devuelve la vista del sign up
     */
    app.get('/users/signup', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /users/signup"
        }
        await logsRepository.insertLog(log);
        res.render("signup.twig");
    });

    /**
     * Nos permite hacer una peticion post con los datos del registro
     */
    app.post('/users/signup', singUpValidatorInsert, async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "POST /users/signup"
        }
        await logsRepository.insertLog(log);
        if (req.body.password !== req.body.password2) {
            res.redirect("/users/signup" +
                "?message=Se ha producido un error al registrar el usuario" +
                "&messageType=alert-danger");
        } else {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let user = {
                name: req.body.name,
                surname: req.body.surname,
                email: req.body.email,
                password: securePassword,
                dateOfBirth: req.body.dateOfBirth,
                money: 100,
                role: "standard"
            }
            let findUser = {email: user.email}
            usersRepository.findUser(findUser, {}).then(async result => {
                if (result !== null) {
                    res.redirect("/users/signup" +
                        "?message=Se ha producido un error al registrar el usuario" +
                        "&messageType=alert-danger");
                } else {
                    try {
                        const errors = validationResult(req);
                        if (!errors.isEmpty()) {
                            res.status(422);
                            //res.json({error: "No se ha podido crear el usuario. El recurso ya existe."});
                            res.render("signup.twig", {errors: errors.array()});
                        } else {
                            let log2 = {
                                logType: "ALTA",
                                date: new Date(),
                                message: "Alta de usuario: " + user.email
                            }
                            await logsRepository.insertLog(log2);
                            usersRepository.insertUser(user).then(userId => {
                                res.redirect("/users/login" +
                                    "?message=Usuario registrado correctamente" +
                                    "&messageType=alert-info");
                            });
                        }
                    } catch (e) {
                        res.status(500);
                        res.json({error: "Se ha producido un error al intentar crear el user: " + e})
                    }

                }
            }).catch(error => {
                res.redirect("/users/signup" +
                    "?message=Se ha producido un error al registrar el usuario" +
                    "&messageType=alert-danger");
            });
        }
    });

    /**
     * Nos devuelve una vista del login
     */
    app.get('/users/login', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /users/login"
        }
        await logsRepository.insertLog(log);
        res.render("login.twig");
    });

    /**
     * Nos permite hacer una peticion post con los datos del registro
     */
    app.post('/users/login', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "POST /users/login"
        }
        await logsRepository.insertLog(log);
        let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let filter = {
            email: req.body.email,
            password: securePassword
        };
        let options = {};
        usersRepository.findUser(filter, options).then(async user => {
            if (user == null) {
                log = {
                    logType: "LOGIN-ERR",
                    date: new Date(),
                    message: "Error al hacer login: " + req.body.email
                }
                await logsRepository.insertLog(log);
                req.session.user = null;
                req.session.role = null;
                res.redirect("/users/login" +
                    "?message=Email o password incorrecto" +
                    "&messageType=alert-danger ");
            } else {
                log = {
                    logType: "LOGIN-EX",
                    date: new Date(),
                    message: "Login correcto: " + req.body.email
                }
                await logsRepository.insertLog(log);
                req.session.user = user.email;
                req.session.money = user.money;
                req.session.role = user.role;
                if (user.role == "standard") {
                    res.redirect("/offers");
                } else if (user.role == "admin") {
                    res.redirect("/user/list");
                } else {
                    res.render("error", {message: "Error no es ningun rol de usuario"});
                    // res.send("Error no es ningun rol de usuario")
                }
            }
        }).catch(error => {
            req.session.user = null;
            req.session.role = null;
            res.redirect("/users/login" +
                "?message=Se ha producido un error al buscar el usuario" +
                "&messageType=alert-danger ");
        });
    });

    /**
     * Nos permite deslogearnos
     */
    app.get('/users/logout', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /users/logout"
        }
        await logsRepository.insertLog(log);
        log = {
            logType: "LOGOUT",
            date: new Date(),
            message: "Logout correcto: " + req.session.user
        }
        await logsRepository.insertLog(log);
        req.session.user = null;
        req.session.role = null;
        res.redirect("/users/login");
    });

    /**
     * Lista todos los usuarios de la aplicacion
     */
    app.get("/user/list", async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /user/list"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === 'admin') {
            let filter = {};
            let options = {};
            let page = parseInt(req.query.page); // Es String !!!
            if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") { //Puede no venir el param
                page = 1;
            }
            usersRepository.getUsersPg(filter, options, page).then(result => {
                let lastPage = result.total / 5;
                if (result.total % 5 > 0) { // Sobran decimales
                    lastPage = lastPage + 1;
                }
                let pages = []; // paginas mostrar
                for (let i = page - 2; i <= page + 2; i++) {
                    if (i > 0 && i <= lastPage) {
                        pages.push(i);
                    }
                }
                let response = {
                    users: result.users,
                    pages: pages,
                    currentPage: page,
                    userSession: req.session.user
                }
                res.render("users/list.twig", response);
            }).catch(error => {
                res.render("error", {message: "Se ha producido un error al listar los usuarios"});
            });
        } else if(req.session.role === "standard"){
            res.render("error", {message: "No se tienen permisos"});
        } else {
            res.redirect("/users/login")
        }
    });

    /**
     * Nos permite eliminar uno o varios de los usuarios
     */
    app.post('/user/delete', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "POST /user/delete"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === 'admin') {
            let usersToDelete = req.body.usersToDelete;
            console.log(usersToDelete)

            for (let i = 0; i < usersToDelete.length; i++) {
                let filter = {email: usersToDelete[i]};
                let options = {};

                usersRepository.deleteUser(filter, options).then(result => {
                    if (result === null || result.deletedCount === 0) {
                        res.render("error", {message: "No se han podido eliminar los usuarios seleccionados"});
                    } else {
                        let filterUser = {$or: [{buyer: usersToDelete[i]}, {seller: usersToDelete[i]}]};
                        offersRepository.deleteOffersForUser(filterUser, {}).then(offers => {
                            if (offers === null) {
                                res.render("error", {message: "No se han podido eliminar las ofertas del usuario"});
                            } else {
                                let filterConversation = {$or: [{buyer: usersToDelete[i]}, {seller: usersToDelete[i]}]};
                                conversationsRepository.deleteConversationForUser(filterConversation, {}).then(conversation => {
                                    if (conversation === null) {
                                        res.render("error", {message: "No se han podido eliminar las ofertas del usuario"});
                                    }
                                }).catch(error => {
                                    res.render("error", {message: "Se ha producido un error al eliminar las conversaciones del usuario"});
                                })
                            }
                        }).catch(error => {
                            res.render("error", {message: "Se ha producido un error al eliminar las ofertas del usuario"});
                        })
                    }
                }).catch(error => {
                    res.render("error", {message: "Se ha producido un error al eliminar el usuario"});
                });
            }
            res.redirect("/user/list");
        } else {
            res.render("error", {message: "Este usuario no tiene permisos"})
        }
    });

};