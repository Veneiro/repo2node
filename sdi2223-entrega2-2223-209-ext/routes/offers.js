const {ObjectId} = require("mongodb");
const {validationResult} = require("express-validator");
const {offersValidatorInsert} = require('../validator/offersValidator');
const usersRepository = require("../repositories/usersRepository");

module.exports = function (app, offersRepository, usersRepository, conversationsRepository, logsRepository){

    /**
     * Método que lista las ofertas del usuario
     */
    app.get('/offers', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers"
        };
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let filter = { seller: req.session.user };
            let options = {};
            let page = parseInt(req.query.page); // Es un String
            if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
                // Puede no venir el parámetro
                page = 1;
            }
            offersRepository.getOffersPgForOffers(filter, options, page).then(result => {
                let lastPage = Math.ceil(result.total / 5);
                let pages = []; // Páginas a mostrar
                for (let i = Math.max(1, page - 2); i <= Math.min(page + 2, lastPage); i++) {
                    pages.push(i);
                }
                let response = {
                    offers: result.offers,
                    pages: pages,
                    currentPage: page,
                    money: req.session.money,
                    userSession: req.session.user
                };
                res.render("offers.twig", response);
            }).catch(error => {
                res.render("error", { message: "Se ha producido un error al listar las ofertas del usuario" });
            });
        } else {
            res.render("error", { message: "No tiene permisos para ver las ofertas" });
        }
    });

    /**
     * Nos devuleve la vista de las ofertas añadidas
     */
    app.get('/offers/add', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers/add"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let response = {
                userSession: req.session.user,
                money: req.session.money
            }
            if (req.session.role === 'standard') {
                res.render("offers/add.twig", response);
            } else {
                res.render("error", {message: "Este usuario no tiene permisos"})

            }
        } else {
            res.render("error", {message: "No tiene permisos para añadir una oferta"});
        }

    });

    /**
     * Con una peticion post añade las ofertas con los valores establecidos
     */
    app.post('/offers/add', offersValidatorInsert, async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "POST /offers/add"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === 'standard') {
            try {
                const errors = validationResult(req);
                if (!errors.isEmpty()) {
                    res.render("offers/add.twig", {errors: errors.array()});
                } else {
                    let highlightValue = false;
                    if (req.body.highlight != undefined) {
                        highlightValue = true;
                    }
                    let offer = {
                        title: req.body.title,
                        detail: req.body.detail,
                        price: req.body.price,
                        date: new Date(),
                        seller: req.session.user,
                        userSession: req.session.user,
                        money: req.session.money,
                        highlight: highlightValue
                    }
                    if (req.session.money > 20) {
                        offersRepository.insertOffer(offer, function (offerId) {
                            if (offerId === null) {
                                res.render("error", {message: "No se ha podido crear la oferta. El recurso ya existe."});
                            } else {
                                req.session.money = req.session.money - 20;
                                res.redirect("/offers");
                            }
                        });
                    } else {
                        res.render("error", {message: "No tiene suficiente dinero para destacar la oferta"});
                    }
                }
            } catch (e) {
                res.render("error", {message: "Se ha producido un error al intentar crear la oferta."});
            }
        } else {
            res.render("error", {message: "Este usuario no tiene permisos"})
        }
    });

    /**
     * Permite eliminar una oferta a partir de su id
     */
    app.get('/offers/delete/:id', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers/delete/:id"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === 'standard') {
            try {
                let offerId = ObjectId(req.params.id);
                let filter = { _id: offerId };
                let oferta = await offersRepository.getOffers(filter, {});
                let isUserOffer = await checkUserOffer(req.session.user, offerId);
                if (!isUserOffer) {
                    res.render("error", {message: "No eres el dueño por lo que no tienes permisos."});
                } else if (oferta[0].buyer) {
                    res.render("error", {message: "No puedes eliminarla porque esta vendida"});
                } else {
                    let result = await offersRepository.deleteOffer(filter, {});
                    if (result === null || result.deletedCount === 0) {
                        res.render("error", {message: "No se ha podido eliminar el registro"})
                    } else {
                        let filterConversations = {_id: offerId}
                        conversationsRepository.deleteConversationForOffers(filterConversations, {}).then(conversation => {
                            if (conversation === null) {
                                res.render("error", {message: "No se han podido eliminar las ofertas del usuario"});
                            }
                        }).catch(error => {
                            res.render("error", {message: "Se ha producido un error al eliminar las conversaciones de la oferta"});
                        })
                        res.redirect("/offers")
                    }
                }
            } catch (e) {
                res.render("error", {message: "Se ha producido un error, revise que el ID sea válido."});
            }
        } else {
            res.render("error", {message: "Este usuario no tiene permisos"});
        }
    });

    /**
     * Comprueba que es el usario el que publica la noticia
     * @param user
     * @param offerId
     * @returns {Promise<* | undefined>}
     */
    function checkUserOffer(user, offerId) {
        let filter = { _id: offerId, seller: user };
        let options = {};
        return offersRepository.getOffers(filter, options)
            .then(offer => {
                if (offer == null || offer.length <= 0) {
                    return false;
                } else {
                    return true;
                }
            });
    }

    /**
     * Nos lista todas las ofertas de la aplicacion en forma de tienda
     */
    app.get('/shop', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /shop"
        };
        await logsRepository.insertLog(log);

        if (req.session.role === "standard") {
            try {
                const searchQuery = req.query.search || "";
                const filter = {};
                if (searchQuery) {
                    filter.title = { $regex: searchQuery, $options: "i" };
                }
                const options = { sort: { highlight: -1, title: 1 } };
                const user = req.session.user;
                const page = parseInt(req.query.page) || 1;
                const limit = 5;

                const { offers, total } = await offersRepository.getOffersPgForShop(filter, options, page, limit);

                const lastPage = Math.ceil(total / limit);
                const pages = getPaginationArray(page, lastPage);

                offers.forEach(offer => {
                    offer.owner = (offer.seller === user);
                    offer.available = !offer.buyer;
                });

                const response = {
                    offers: offers,
                    pages: pages,
                    currentPage: page,
                    userSession: req.session.user,
                    money: req.session.money,
                    search: searchQuery
                };

                res.render("shop.twig", response);
            } catch (error) {
                res.render("error", { message: "Se ha producido un error al listar las ofertas por página" });
            }
        } else {
            res.render("error", { message: "No tiene permisos para entrar en la tienda" });
        }
    });


    function getPaginationArray(currentPage, lastPage) {
        const range = 2; // Número de páginas a mostrar a cada lado de la página actual
        const pages = [];
        let start = Math.max(currentPage - range, 1);
        let end = Math.min(currentPage + range, lastPage);

        for (let i = start; i <= end; i++) {
            pages.push(i);
        }

        return pages;
    }
    /**
     * Nos muestra un listado de las ofertas destacadas
     */
    app.get('/offers/highlights', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers/highlights"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let filter = {$and: [{"highlight": true}, {"seller": req.session.user}]};
            offersRepository.getOffers(filter, {}).then(ofertas => {
                if (ofertas != null) {
                    let response = {
                        userSession: req.session.user,
                        money: req.session.money,
                        offers: ofertas
                    }
                    res.render("offers/highlights.twig", response);
                }
            }).catch(error => {
                res.render("error", {message: "Se ha producido un error al obtener sus ofertas destacadas"});
            })
        } else {
            res.render("error", {message: "No tiene permisos para ver sus ofertas destacadas"});
        }
    });

    /**
     * Permite comprar una de las ofertas de la tienda
     */
    app.get('/offers/buy/:id', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers/buy/:id"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let filterEmail = {email: req.session.user};
            usersRepository.findUser(filterEmail, {}).then(userResult => {
                let offerId = ObjectId(req.params.id);
                let filter = {_id: offerId};
                offersRepository.getOffers(filter, {}).then(oferta => {
                    if (oferta == null || oferta.length == 0) {
                        res.render("error", {message: "No se ha encontrado oferta con ese id"});
                    } else {
                        if (userResult.money >= oferta[0].price) {
                            let comprador = {buyer: userResult.email};
                            offersRepository.updateOffer(comprador, filter, {}).then(result1 => {
                                if (result1 != null && result1.modifiedCount != 0) {
                                    let moneyUpdate = userResult.money - oferta[0].price;
                                    let newMoney = {money: moneyUpdate};
                                    req.session.money = newMoney.money;
                                    usersRepository.updateUser(newMoney, userResult, {}).then(result => {
                                        if (result == null || result.modifiedCount == 0) {
                                            res.render("error", {message: "Se ha producido un error al modificar el saldo."});
                                        } else {
                                            res.redirect("/purchases");
                                        }
                                    }).catch(error => {
                                        res.render("error", {message: "Se ha producido un error al modificar el saldo."});
                                    })
                                }
                            }).catch(error => {
                                res.render("error", {message: "Se ha producido un error al modificar la oferta."});
                            });
                        } else {
                            res.render("error", {message: "No tiene suficiente dinero para comprar la oferta"});
                        }
                    }
                })
            }).catch(error => {
                res.render("error", {message: "No se encuentra usuario con este email."});
            });
        } else {
            res.render("error", {message: "No tiene permisos para comprar ofertas"});
        }
    });

    /**
     * Nos lista las ofertas normales
     */
    app.get('/offers/normal/:id', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers/normal/:id"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let filterEmail = {email: req.session.user};
            usersRepository.findUser(filterEmail, {}).then(userResult => {
                let offerId = ObjectId(req.params.id);
                let filter = {_id: offerId};
                offersRepository.getOffers(filter, {}).then(oferta => {
                    let normal = {highlight: false};
                    offersRepository.updateOffer(normal, filter, {}).then(result => {
                        if (result != null || result.modifiedCount != 0) {
                            let moneyUpdate = userResult.money + 20;
                            let newMoney = {money: moneyUpdate};
                            req.session.money = newMoney.money;
                            usersRepository.updateUser(newMoney, userResult, {}).then(result => {
                                if (result == null && result.modifiedCount == 0) {
                                    res.render("error", {message: "Se ha producido un error al modificar el saldo."});
                                } else {
                                    res.redirect("/offers/highlights");
                                }
                            }).catch(error => {
                                res.render("error", {message: "Se ha producido un error al modificar el saldo."});
                            })
                        }
                    }).catch(error => {
                        res.render("error", {message: "Se ha producido un error al modificar la oferta."});
                    });
                })
            }).catch(error => {
                res.render("error", {message: "No se encuentra usuario con este email"});
            });
        } else {
            res.render("error", {message: "No tiene permisos para hacer normal una oferta"});
        }
    });

    /**
     * Nos lista las ofertas destacadas
     */
    app.get('/offers/highlight/:id', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /offers/highlight/:id"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let filterEmail = {email: req.session.user};
            usersRepository.findUser(filterEmail, {}).then(userResult => {
                let offerId = ObjectId(req.params.id);
                let filter = {_id: offerId};
                offersRepository.getOffers(filter, {}).then(oferta => {
                    if (userResult.money > 20) {
                        let destacada = {highlight: true};
                        offersRepository.updateOffer(destacada, filter, {}).then(result => {
                            if (result != null || result.modifiedCount != 0) {
                                let moneyUpdate = userResult.money - 20;
                                let newMoney = {money: moneyUpdate};
                                req.session.money = newMoney.money;
                                usersRepository.updateUser(newMoney, userResult, {}).then(result => {
                                    if (result == null && result.modifiedCount == 0) {
                                        res.render("error", {message: "Se ha producido un error al modificar el saldo."});
                                    } else {
                                        res.redirect("/offers/highlights");
                                    }
                                }).catch(error => {
                                    res.render("error", {message: "Se ha producido un error al modificar el saldo."});
                                })
                            }
                        }).catch(error => {
                            res.render("error", {message: "Se ha producido un error al modificar la oferta."});
                        });
                    } else {
                        res.render("error", {message: "No tiene suficiente dinero para destacar la oferta"});
                    }
                })
            }).catch(error => {
                res.render("error", {message: "No se encuentra usuario con este email."});
            });
        } else {
            res.render("error", {message: "No tiene permisos para destacar una oferta"});
        }
    });

    /**
     * Nos muestra una lista de las ofertas compradas por el usuario
     */
    app.get('/purchases', async function (req, res) {
        let log = {
            logType: "PET",
            date: new Date(),
            message: "GET /purchases"
        }
        await logsRepository.insertLog(log);
        if (req.session.role === "standard") {
            let filter = {buyer: req.session.user};
            let options = {};
            let page = parseInt(req.query.page); // Es String !!!
            if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") { //Puede no venir el param
                page = 1;
            }
            offersRepository.getPurchases(filter, options, page).then(offers => {
                let lastPage = offers.total / 5;
                if (offers.total % 5 > 0) { // Sobran decimales
                    lastPage = lastPage + 1;
                }
                let pages = []; // paginas mostrar
                for (let i = page - 2; i <= page + 2; i++) {
                    if (i > 0 && i <= lastPage) {
                        pages.push(i);
                    }
                }
                let response = {
                    offers: offers.offers,
                    pages: pages,
                    currentPage: page,
                    userSession: req.session.user,
                    money: req.session.money
                }
                res.render("purchase.twig", response);
            }).catch(error => {
                res.render("error", {message: "Se ha producido un error al listar las ofertas del usuario"});
            });
        } else {
            res.render("error", {message: "No tiene permisos para ver las compras"});
        }

    })

    /**
     * app.get('/songs/edit/:id', function(req,res){
     *         let filter = {_id:ObjectId(req.params.id)};
     *         songsRepository.findSong(filter,{}).then(song=>{
     *             res.render("songs/edit.twig", {song:song});
     *         }).catch(error=>{
     *             res.send("Se ha producido un erro al recuperar la canción " + error)
     *         });
     *     });
     *     app.post('/songs/edit/:id', function (req, res) {
     *         let song = {
     *             title: req.body.title,
     *             kind: req.body.kind,
     *             price: req.body.price,
     *             author: req.session.user
     *         }
     *         let songId = req.params.id;
     *         let filter = {_id: ObjectId(songId)};
     *         //que no se cree un documento nuevo, si no existe
     *         const options = {upsert: false}
     *         songsRepository.updateSong(song, filter, options).then(result => {
     *             step1UpdateCover(req.files, songId, function (result) {
     *                 if (result == null) {
     *                     res.send("Error al actualizar la portada o el audio de la canción");
     *                 } else {
     *                     res.send("Se ha modificado el registro correctamente");
     *                 }
     *             });
     *         }).catch(error => {
     *             res.send("Se ha producido un error al modificar la canción " + error)
     *         });
     *     });
     *     function step1UpdateCover(files, songId, callback) {
     *         if (files && files.cover != null) {
     *             let image = files.cover;
     *             image.mv(app.get("uploadPath") + '/public/covers/' + songId + '.png', function (err) {
     *                 if (err) {
     *                     callback(null); // ERROR
     *                 } else {
     *                     step2UpdateAudio(files, songId, callback); // SIGUIENTE
     *                 }
     *             });
     *         } else {
     *             step2UpdateAudio(files, songId, callback); // SIGUIENTE
     *         }
     *     };
     *     function step2UpdateAudio(files, songId, callback) {
     *         if (files && files.audio != null) {
     *             let audio = files.audio;
     *             audio.mv(app.get("uploadPath") + '/public/audios/' + songId + '.mp3', function (err) {
     *                 if (err) {
     *                     callback(null); // ERROR
     *                 } else {
     *                     callback(true); // FIN
     *                 }
     *             });
     *         } else {
     *             callback(true); // FIN
     *         }
     *     };
     *     app.get('/songs/:id', function (req, res) {
     *         let filter = {_id: ObjectId(req.params.id)};
     *         let options = {_id: ObjectId(req.params.id)};
     *         let user = req.session.user;
     *
     *         songsRepository.findSong(filter, {}).then(song => {
     *             canBuy(user, song._id, function(canBuy) {
     *                 commentsRepository.getComments({}, options).then(comments =>{
     *                     let settings = {
     *                         url: "https://www.freeforexapi.com/api/live?pairs=EURUSD",
     *                         method: "get",
     *                         headers: {"token": "ejemplo",}
     *                     }
     *                     let rest = app.get("rest");
     *                     rest(settings, function (error, response, body) {
     *                         console.log("cod: " + response.statusCode + " Cuerpo :" + body);
     *                         let responseObject = JSON.parse(body);
     *                         let rateUSD = responseObject.rates.EURUSD.rate; // nuevo campo "usd" redondeado a dos decimales
     *                         let songValue = rateUSD * song.price;
     *                         song.usd = Math.round(songValue * 100) / 100;
     *                         res.render("songs/song.twig", {song: song, comments: comments, canBuy: canBuy});
     *                     })
     *                 })
     *             });
     *         }).catch(error => {
     *             res.send("Se ha producido un error al buscar la canción " + error)
     *         });
     *     });
     */


}