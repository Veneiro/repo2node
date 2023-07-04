module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    /**
     * Metodo que nos devuelve todas las coneversaciones del sistema
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    getConversations: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const conversations = await conversationsCollection.find(filter, options).toArray();
            return conversations;
        } catch (error) {
            throw (error);
        }
    },
    deleteConversation: async function (filter, options) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const result = await conversationsCollection.deleteOne(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },
    updateConversation: async function (filter, update, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const result = await conversationsCollection.findOneAndUpdate(filter, update, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },
    getConversation: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const result = await conversationsCollection.findOne(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },
    getConversationByOfferId: async function (offerId) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const conversation = await conversationsCollection.findOne({ offerId: offerId });
            return conversation;
        } catch (error) {
            throw error;
        }
    },
    addConversation: async function (conversation) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const result = await conversationsCollection.insertOne(conversation);
            return result;
        } catch (error) {
            throw (error);
        }
    },
    /**
     * Metodo que elimina un de las conevrsaciones seleccionadas
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    deleteConversationForUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const result = await conversationsCollection.deleteMany(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    },
    /**
     * Metodo que elimina un de las conevrsaciones seleccionadas
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    deleteConversationForOffers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);
            const result = await conversationsCollection.deleteMany(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }
};