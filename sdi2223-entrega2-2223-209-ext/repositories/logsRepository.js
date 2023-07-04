module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    /**
     * Metodo que nos permite insertar un log
     * @param filter
     * @param options
     * @returns {Promise<*>}
     */
    insertLog: function (log) {
        this.mongoClient.connect(this.app.get('connectionStrings'), function (err, dbClient) {
            if (err) {
                callbackFunction(null)
            } else {
                const database = dbClient.db("myWallapop");
                const collectionName = 'logs';
                const logsCollection = database.collection(collectionName);
                logsCollection.insertOne(log);
            }
        });
    }, getLogs: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'logs';
            const logsCollection = database.collection(collectionName);
            const result = await logsCollection.find(filter, options).toArray();
            return result;
        } catch (error) {
            throw (error);
        }
    }, deleteLogs: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'logs';
            const logsCollection = database.collection(collectionName);
            const result = await logsCollection.deleteMany(filter, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }
};