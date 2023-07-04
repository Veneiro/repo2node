const {check} = require('express-validator');

/**
 * Clase que valida el insertar usuario
 * @type {ValidationChain[]}
 */
exports.singUpValidatorInsert = [
    check('name', 'Name is required').trim().not().isEmpty(),
    check('name', 'Name must be 3 or more characters').trim().isLength({min: 3}),
    check('surname', 'Surname is required').trim().not().isEmpty(),
    check('surname', 'Surname must be 3 or more characters').trim().isLength({min: 3}),
    check('email', 'Email is required').trim().not().isEmpty(),
    check('email', 'Email must be 5 or more characters').trim().isLength({min: 5}),
    check('dateOfBirth', 'Datebirth is required').trim().not().isEmpty(),
]