const {ObjectId} = require("mongodb");

module.exports = function (app, logsRepository) {

    /**
     * Metodo que nos devuelve la vista con todos los logs del sistema
     */
    app.get("/logs", async function (req, res) {
        let filter = {};
        let options = {};
        logsRepository.getLogs(filter, options).then(function (logs) {
            res.render("logs", {logs: logs, userSession: req.session.user});
        });
    });

    /**
     * Método que borra todos los logs del sistema
     */
    app.post("/logs/delete", async function (req, res) {
        let filter = {};
        let options = {};
        logsRepository.deleteLogs(filter, options).then(result => {
            if (result === null || result.deletedCount === 0) {
                res.render("error", {message: "No se han podido eliminar los logs"});
            } else {
                res.redirect("/logs");
            }
        });
    });
}