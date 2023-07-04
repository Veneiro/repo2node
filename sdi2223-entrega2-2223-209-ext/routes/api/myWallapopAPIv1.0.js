const {ObjectId} = require("mongodb");
module.exports = function (app, conversationsRepository, usersRepository, offersRepository) {

    /**
     * Metodo que permite registrarse en la api
     */
    app.post('/api/v1.0/users/login', function (req, res) {
        try {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {email: req.body.email, password: securePassword};
            let options = {};
            usersRepository.findUser(filter, options).then(user => {
                if (user === null) {
                    res.status(401); // Unauthorized
                    res.json({
                        message: "usuario no autorizado",
                        authenticated: false
                    });
                } else {
                    let token = app.get("jwt").sign(
                        {
                            user: user.email,
                            time: Date.now() / 1000
                        }, "secreto");
                    res.status(200);
                    res.json({
                        message: "usuario autorizado",
                        authenticated: true,
                        token: token
                    });
                }
            }).catch(error => {
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar las credenciales",
                    authenticated: false
                });
            });
        } catch (e) {
            res.status(500);
            res.json({
                error: "Se ha producido un error al verificar las credenciales",
                authenticated: false
            });
        }
    });

    /**
     * Lista las ofertas disponibles en la api
     */
    app.get('/api/v1.0/offers', function (req, res) {
        let filter = {seller: {$ne: res.user}};
        let options = {};

        offersRepository.getOffers(filter, options).then(offers => {
            res.status(200);
            res.send({offers: offers});
        }).catch(error => {
            res.status(500);
            res.json({error: "Se ha producido un error al listar las ofertas"});
        });
    });

    app.get('/api/v1.0/offers/:id', function (req, res) {
        try {
            const offerId = req.params.id;
            let filter = { _id: ObjectId(offerId) };
            let options = {};

            offersRepository.getOffers(filter, options).then(offers => {
                if (offers.length === 0) {
                    res.status(404);
                    res.json({ error: 'La oferta especificada no existe.' });
                } else {
                    const offer = offers[0];
                    res.status(200);
                    res.json({ offer: offer });
                }
            }).catch(error => {
                res.status(500);
                res.json({ error: "Se ha producido un error al obtener la oferta por su ID." });
            });
        } catch (e) {
            res.status(500);
            res.json({ error: "Se ha producido un error, revise que el ID de la oferta sea válido." });
        }
    });

    /**
     * {
     *   "_id": {
     *     "$oid": "6453ceb57d39611123a36bec"
     *   },
     *   "offerId": "1",
     *   "seller": "prueba2@prueba2.com",
     *   "buyer": "prueba1@prueba1.com"
     *   "messages": [
     *   {
     *   "author": "prueba1@prueba1.com",
     *   "message": "Hola, me interesa tu oferta",
     *   "date": "2020-05-01T12:00:00.000Z"
     *   },
     *   {
     *   "author": "prueba2@prueba2.com",
     *   "message": "Hola, cuesta 20 euros",
     *   "date": "2020-05-01T12:00:00.000Z"
     *   }
     *   ]
     * }
     */

    /**
     * Create a conversation
     */
    app.post('/api/v1.0/conversations', async function (req, res) {
        try {
            const offerId = req.body.offerId;

            // Verificar si ya existe una conversación asociada a la oferta
            const existingConversation = await conversationsRepository.getConversationByOfferId(offerId);
            if (existingConversation) {
                // Si existe, retornar la conversación existente
                res.status(200);
                res.json(JSON.stringify(existingConversation));
                return;
            }

            let filter = { _id: ObjectId(offerId) };
            let offer = await offersRepository.getOffers(filter, {});

            // Verificar si se encontró la oferta
            if (offer.length === 0) {
                res.status(404);
                res.json({ error: 'La oferta especificada no existe.' });
                return;
            }

            // Verificar si el usuario que crea la conversación es el vendedor
            if (offer[0].seller === res.user) {
                res.status(403);
                res.json({ error: 'No se permite al vendedor crear la conversación.' });
                return;
            }

            let conversation = {
                offerId: offerId,
                seller: offer[0].seller,
                buyer: res.user,
                messages: []
            };

            conversationsRepository.addConversation(conversation)
                .then(result => {
                    res.status(201);
                    res.json(JSON.stringify(result));
                })
                .catch(error => {
                    res.status(500);
                    res.json({ error: "Se ha producido un error al añadir la conversación" });
                });
        } catch (e) {
            res.status(500);
            res.json({ error: "Se ha producido un error al crear la conversación" });
        }
    });

    /**
     * Sends a message to the conversation
     */
    app.post('/api/v1.0/conversations/:id', function (req, res) {
        try {
            let conversationId = ObjectId(req.params.id);
            let filter = {_id: conversationId};
            let message = {
                message: req.body.message,
                sender: res.user,
                date: new Date(),
                read: false
            }
            if (message.message === undefined || message.message === null || message.message.trim() === "") {
                res.status(422);
                res.json({error: "El mensaje no puede estar vacío"});
            }
            let update = {$push: {messages: message}};
            let options = {returnOriginal: false};
            validateUser(conversationId, res.user, function (errors) {
                if (errors !== null && errors.length > 0) {
                    res.status(422);
                    res.json({error: errors});
                } else {
                    conversationsRepository.updateConversation(filter, update, options).then(result => {
                        if (result === null || result.value === null) {
                            res.status(404);
                            res.json({error: "ID inválido o no existe, no se ha actualizado el registro."});
                        } else {
                            res.send(JSON.stringify(result.value));
                        }
                    }).catch(e => {
                        res.status(500);
                        res.json({error: "Se ha producido un error, revise que el ID sea válido."})
                    });
                }
            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error, revise que el ID sea válido."})
        }
    });

    /**
     * Get the messages of a conversation and mark as read the messages of the other user
     */
    app.get('/api/v1.0/conversations/:id', function (req, res) {
        try {
            let conversationId = ObjectId(req.params.id);
            let filter = {_id: conversationId};
            let options = {};
            validateUser(conversationId, res.user, function (errors) {
                if (errors !== null && errors.length > 0) {
                    res.status(422);
                    res.json({error: errors});
                } else {
                    conversationsRepository.getConversation(filter, options).then(conversation => {
                        if (conversation === null) {
                            res.status(404);
                            res.json({error: "ID inválido o no existe, no se ha encontrado el registro."});
                        } else {
                            let messages = conversation.messages;
                            let otherUser = conversation.seller === res.user ? conversation.buyer : conversation.seller;
                            let update = {$set: {"messages.$[elem].read": true}};
                            let arrayFilters = [{"elem.sender": otherUser}];
                            let options = {returnOriginal: false, arrayFilters: arrayFilters};
                            conversationsRepository.updateConversation(filter, update, options).then(result => {
                                if (result === null || result.value === null) {
                                    res.status(404);
                                    res.json({error: "ID inválido o no existe, no se ha actualizado el registro."});
                                } else {
                                    conversationsRepository.getConversation(filter, options).then(conversation => {
                                        if (conversation === null) {
                                            res.status(404);
                                            res.json({error: "ID inválido o no existe, no se ha encontrado el registro."});
                                        } else {
                                            messages = conversation.messages;
                                            res.send(JSON.stringify({messages: messages}));
                                        }
                                    }).catch(e => {
                                        res.status(500);
                                        res.json({error: "Se ha producido un error, revise que el ID sea válido."})
                                    });
                                }
                            }).catch(e => {
                                res.status(500);
                                res.json({error: "Se ha producido un error, revise que el ID sea válido."})
                            });
                        }
                    }).catch(e => {
                        res.status(500);
                        res.json({error: "Se ha producido un error, revise que el ID sea válido."})
                    });
                }
            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error, revise que el ID sea válido."})
        }
    });

    /**
     * Nos muestra las conversaciones en la api
     */
    app.get('/api/v1.0/conversations', function (req, res) {
        let filter = {$or: [{buyer: res.user}, {seller: res.user}]};
        let options = {};
        conversationsRepository.getConversations(filter, options).then(conversations => {
            res.status(200);
            res.send({conversations: conversations});
        }).catch(error => {
            res.status(500);
            res.json({error: "Se ha producido un error al listar las conversaciones"});
        });
    });

    /**
     * Permite borrar las conversaciones en la api
     */
    app.get('/api/v1.0/conversations/delete/:id', async function (req, res) {
        try {
            let conversationId = ObjectId(req.params.id);
            let filter = {_id: conversationId};
            validateUser(conversationId, res.user, function (errors) {
                if (errors !== null && errors.length > 0) {
                    res.status(422);
                    res.json({error: errors});
                } else {
                    conversationsRepository.deleteConversation(filter, {}).then(result => {
                        if (result === null || result.deletedCount === 0) {
                            res.status(404);
                            res.json({error: "ID inválido o no existe, no se ha borrado el registro."});
                        } else {
                            res.send(JSON.stringify(result));
                        }
                    }).catch(e => {
                        res.status(500);
                        res.json({error: "Se ha producido un error, revise que el ID sea válido."})
                    });
                }

            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error, revise que el ID sea válido."})
        }
    });
    /**
     * Get a conversation by offer ID
     */
    app.get('/api/v1.0/conversations/offer/:offerId', function (req, res) {
        try {
            const offerId = req.params.offerId;
            let filter = { offerId: offerId };
            let options = {};

            conversationsRepository.getConversation(filter, options).then(conversation => {
                if (conversation === null) {
                    res.status(404);
                    res.json({ error: "No se ha encontrado ninguna conversación asociada a la oferta especificada." });
                } else {
                    res.status(200);
                    res.json({ conversation: conversation });
                }
            }).catch(error => {
                res.status(500);
                res.json({ error: "Se ha producido un error al obtener la conversación por el ID de la oferta." });
            });
        } catch (e) {
            res.status(500);
            res.json({ error: "Se ha producido un error, revise que el ID de la oferta sea válido." });
        }
    });



    /**
     * Valida el usuario
     * @param conversationId
     * @param user
     * @param callback
     */
    function validateUser(conversationId, user, callback) {
        let errors = new Array();
        let filter = {_id: conversationId, $or: [{buyer: user}, {seller: user}]};
        let options = {};
        conversationsRepository.getConversations(filter, options).then(conversation => {
            if (conversation == null || conversation.length <= 0) {
                errors.push({
                    "value": user,
                    "msg": "Error en la validación del usuario",
                    "param": "author",
                    "location": "body"
                })
            }
            if (errors === null || errors.length > 0) {
                callback(errors);
            } else {
                callback(null);
            }
        });
    }

}