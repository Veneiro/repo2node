module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    getOffers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            const offers = await offersCollection.find(filter, options).toArray();
            return offers;
        } catch (error) {
            throw (error);
        }
    },
    // Función para obtener las ofertas de la base de datos con paginación
    getOffersPgForShop: async function (filter, options, page, limit) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);

            const total = await offersCollection.countDocuments(filter);

            const cursor = offersCollection.find(filter, options)
                .skip((page - 1) * limit)
                .limit(limit);

            const offers = await cursor.toArray();

            return { offers, total };
        } catch (error) {
            throw (error);
        }
    },
    getOffersPgForOffers: async function (filter, options, page) {
        try {
            const limit = 5;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            const offersCollectionCount = await offersCollection.find(filter, options).count();
            const cursor = offersCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const offers = await cursor.toArray();
            const result = {offers: offers, total: offersCollectionCount};
            return result;
        } catch (error) {
            throw (error);
        }
    },
    getPurchases: async function (filter, options, page) {
        try {
            const limit = 5;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const purchasesCollection = database.collection(collectionName);
            const purchasesCollectionCount = await purchasesCollection.find(filter, options).count();
            const cursor = purchasesCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const purchases = await cursor.toArray();
            const result = {offers: purchases, total: purchasesCollectionCount};
            return result;
        } catch (error) {
            throw (error);
        }
    },insertOffer: function (offer, callbackFunction) {
        this.mongoClient.connect(this.app.get('connectionStrings'), function (err, dbClient) {
            if (err) {
                callbackFunction(null)
            } else {
                const database = dbClient.db("myWallapop");
                const collectionName = 'offers';
                const offersCollection = database.collection(collectionName);
                offersCollection.insertOne(offer)
                    .then(result => callbackFunction(result.insertedId))
                    .then(() => dbClient.close())
                    .catch(err => callbackFunction({error: err.message}));
            }
        });
    }, deleteOffer: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            const result = await offersCollection.deleteOne(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }, updateOffer: async function(newOffer, filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            const result = await offersCollection.updateOne(filter, {$set: newOffer}, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },
    /**
     * Metodo que nos permite eliminar una oferta
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    deleteOffersForUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            const result = await offersCollection.deleteMany(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },
};