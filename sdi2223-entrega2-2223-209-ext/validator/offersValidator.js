const {check} = require('express-validator');
/**
 * Clase que valida las ofertas insertadas
 * @type {ValidationChain[]}
 */
exports.offersValidatorInsert = [
    check('title', 'Title is required').trim().not().isEmpty(),
    check('title', 'Title must be 5 or more characters').trim().isLength({min: 5}),
    check('detail', 'Detail is required').trim().not().isEmpty(),
    check('detail', 'Detail must be 5 or more characters').trim().isLength({min: 5}),
    check('price', 'Price is required').trim().not().isEmpty(),
    check('price', 'Price must be a numbers').isNumeric(),
    check('price').custom(value => {
        if (value < 0){
            throw new Error('Price must be greathet than zero')
        }
        return true;
    })
]