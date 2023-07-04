module.exports = {
    mongoClient: null,
    app: null,
    init: async function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
        await this.insertUsers();
        await this.insertOffers();
        await this.insertConversations();
    }, insertUsers: async function () {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'users';
            try {
                await database.collection(collectionName).drop();
            } catch (error){
                console.log("La base de datos no existe: " + error)
            }
            const usersCollection = database.collection(collectionName);
            let users = [
                {
                    name: "user01",
                    surname: "user01",
                    email: "user01@email.com",
                    password: "783a825ecf667676312bdd5e76a138e61e55d5061ef541f445e0c22e671eba9f",
                    money: 100,
                    dateOfBirth: new Date(2001,6,15),
                    role: "standard"
                },
                {
                    name: "user02",
                    surname: "user02",
                    email: "user02@email.com",
                    password: "9812db3294b48b41aa0ea4cbe44453280286c0a089a07f3f6f4a313759012ab9",
                    money: 100,
                    dateOfBirth: new Date(2001,6,17),
                    role: "standard"
                },
                {
                    name: "user03",
                    surname: "user03",
                    email: "user03@email.com",
                    password: "66e93521a9447f5082b957a3bc07dcd5841608a0a2a951cbb30a21d83f13d83a",
                    money: 100,
                    dateOfBirth: new Date(2001,6,18),
                    role: "standard"
                },
                {
                    name: "user04",
                    surname: "user04",
                    email: "user04@email.com",
                    password: "049141fc2f265c205539050c6ebc6a9a3d81c87fbf8f936030bc108330280fef",
                    money: 100,
                    dateOfBirth: new Date(2001,6,19),
                    role: "standard"
                },
                {
                    name: "user05",
                    surname: "user05",
                    email: "user05@email.com",
                    password: "bbd89bd814f8c02da5534ee62781701285aa3d586158e9c857618f9a36d18ce3",
                    money: 100,
                    dateOfBirth: new Date(2001,6,20),
                    role: "standard"
                },
                {
                    name: "user06",
                    surname: "user06",
                    email: "user06@email.com",
                    password: "308f5766b85b7842f62e0bd96873a80cad47cf9fb76ba1061957ad0f1ae4dc1f",
                    money: 100,
                    dateOfBirth: new Date(2001,6,20),
                    role: "standard"
                },
                {
                    name: "user07",
                    surname: "user07",
                    email: "user07@email.com",
                    password: "1a44cfeba26b0d6245a787a87a39e90308bcc6445916951d2cb9a80881c7d1c6",
                    money: 100,
                    dateOfBirth: new Date(2001,6,21),
                    role: "standard"
                },
                {
                    name: "user08",
                    surname: "user08",
                    email: "user08@email.com",
                    password: "60f45cdcacc671c9bc3cddc0e4cf4d5aebe65d5a674fdb7498632a2427638df3",
                    money: 100,
                    dateOfBirth: new Date(2001,6,22),
                    role: "standard"
                },
                {
                    name: "user09",
                    surname: "user09",
                    email: "user09@email.com",
                    password: "c7b08c4af12093cbf088edac0b8c9e394f8486cdb267f5950f363bd5adb93e77",
                    money: 100,
                    dateOfBirth: new Date(2001,6,23),
                    role: "standard"
                },
                {
                    name: "user10",
                    surname: "user10",
                    email: "user10@email.com",
                    password: "c4307e1f4f7a11e99a5e4bfeaf598cadcb99fc536b6eeb668ebdff87df88cff5",
                    money: 100,
                    dateOfBirth: new Date(2001,6,23),
                    role: "standard"
                },
                {
                    name: "user11",
                    surname: "user11",
                    email: "user11@email.com",
                    password: "b9809187151d1b2ada397b7c6ecfdedcaeeaa401568f7abf4706b7eab2f7f8b6",
                    money: 100,
                    dateOfBirth: new Date(2001,6,24),
                    role: "standard"
                },
                {
                    name: "user12",
                    surname: "user12",
                    email: "user12@email.com",
                    password: "b9809187151d1b2ada397b7c6ecfdedcaeeaa401568f7abf4706b7eab2f7f8b6",
                    money: 100,
                    dateOfBirth: new Date(2001,6,25),
                    role: "standard"
                },
                {
                    name: "user13",
                    surname: "user13",
                    email: "user13@email.com",
                    password: "46ef749ceaff17681fcbcf4f5637387421db020f80a3624459e2702187283527",
                    money: 100,
                    dateOfBirth: new Date(2001,6,26),
                    role: "standard"
                },
                {
                    name: "user14",
                    surname: "user14",
                    email: "user14@email.com",
                    password: "c76a26044ac42d88b2154b69e5f7006d38140ccbf91c1a93679ba78e0cc54a17",
                    money: 100,
                    dateOfBirth: new Date(2001,6,27),
                    role: "standard"
                },
                {
                    name: "user15",
                    surname: "user15",
                    email: "user15@email.com",
                    password: "5d6c0f50beb06679b619f7ef2aa46ca23caae057b21b1621ba5ae9692a9192fb",
                    money: 100,
                    dateOfBirth: new Date(2001,6,28),
                    role: "standard"
                },
                {
                    name: "admin",
                    surname: "admin",
                    email: "admin@email.com",
                    password: "ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11",
                    money: 0,
                    role: "admin"
                }
            ]
            const result = await usersCollection.insertMany(users);
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }, insertOffers: async function () {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            try {
                await database.collection(collectionName).drop();
            } catch (error){
                console.log("La base de datos no existe: " + error)
            }
            const offersCollection = database.collection(collectionName);
            let offers = [
                {
                    title: "Pokemon Escarlata",
                    detail: "Videojuego",
                    price: "50",
                    seller: "user01@email.com",
                    date: new Date(2022,1,10),
                    highlight: false
                },
                {
                    title: "Arena Blanca",
                    detail: "Libro",
                    price: "20",
                    seller: "user01@email.com",
                    buyer: "user04@email.com",
                    date: new Date(2022,1,11),
                    highlight: false
                },
                {
                    title: "Xiaomi 5T",
                    detail: "Movil",
                    price: "550",
                    seller: "user01@email.com",
                    date: new Date(2022,1,12),
                    highlight: false
                },
                {
                    title: "Camiseta blanca",
                    detail: "Ropa",
                    price: "15",
                    seller: "user01@email.com",
                    date: new Date(2022,1,13),
                    highlight: false
                },
                {
                    title: "Acer",
                    detail: "Ordenador",
                    price: "100",
                    seller: "user01@email.com",
                    date: new Date(2022,1,14),
                    highlight: false
                },
                {
                    title: "Renault Clio",
                    detail: "Coche",
                    price: "4000",
                    seller: "user01@email.com",
                    date: new Date(2022,1,15),
                    highlight: false
                },
                {
                    title: "Soporte auriculares",
                    detail: "Accesorio",
                    price: "20",
                    seller: "user01@email.com",
                    date: new Date(2022,1,16),
                    highlight: false
                },
                {
                    title: "Camiseta el nano",
                    detail: "Ropa",
                    price: "10",
                    seller: "user01@email.com",
                    date: new Date(2022,1,17),
                    highlight: false
                },
                {
                    title: "Boligrafo bic",
                    detail: "Educacion",
                    price: "2",
                    seller: "user01@email.com",
                    date: new Date(2022,1,18),
                    highlight: false
                },
                {
                    title: "Trenza del mar esmeralda",
                    detail: "Libro",
                    price: "15",
                    seller: "user01@email.com",
                    date: new Date(2022,1,19),
                    highlight: false
                },
                {
                    title: "SNK",
                    detail: "Manga",
                    price: "7",
                    seller: "user02@email.com",
                    date: new Date(2022,2,10),
                    highlight: false
                },
                {
                    title: "Zapato tacon",
                    detail: "Zapatos",
                    price: "4",
                    seller: "user02@email.com",
                    date: new Date(2022,2,11),
                    highlight: false
                },
                {
                    title: "Tejano",
                    detail: "Pantalon",
                    price: "7",
                    seller: "user02@email.com",
                    date: new Date(2022,2,12),
                    highlight: false
                },
                {
                    title: "Patinete",
                    detail: "Ocio",
                    price: "50",
                    seller: "user02@email.com",
                    date: new Date(2022,2,13),
                    highlight: false
                },
                {
                    title: "Bicicleta",
                    detail: "Transporte",
                    price: "100",
                    seller: "user02@email.com",
                    date: new Date(2022,2,14),
                    highlight: false
                },
                {
                    title: "Manta electrica",
                    detail: "Ropa",
                    price: "20",
                    seller: "user02@email.com",
                    date: new Date(2022,2,15),
                    highlight: false
                },
                {
                    title: "Zelda BOTW",
                    detail: "Videojuego",
                    price: "40",
                    seller: "user02@email.com",
                    date: new Date(2022,2,16),
                    highlight: false
                },
                {
                    title: "Apple watch",
                    detail: "Tecnologia",
                    price: "60",
                    seller: "user02@email.com",
                    date: new Date(2022,2,17),
                    highlight: false
                },
                {
                    title: "Escritorio IKEA",
                    detail: "Mueble",
                    price: "10",
                    seller: "user02@email.com",
                    date: new Date(2022,2,18),
                    highlight: false
                },
                {
                    title: "Mancuernas",
                    detail: "Deporte",
                    price: "5",
                    seller: "user02@email.com",
                    buyer: "user14@email.com",
                    date: new Date(2022,2,19),
                    highlight: false
                },
                {
                    title: "Lamina NY",
                    detail: "Arte",
                    price: "5",
                    seller: "user02@email.com",
                    date: new Date(2022,3,10),
                    highlight: false
                },
                {
                    title: "Asterix y Obelix",
                    detail: "Libro",
                    price: "5",
                    seller: "user03@email.com",
                    date: new Date(2022,3,11),
                    highlight: false
                },
                {
                    title: "Mancuernas",
                    detail: "Deporte",
                    price: "5",
                    seller: "user03@email.com",
                    date: new Date(2022,3,12),
                    highlight: false
                },
                {
                    title: "Barrica",
                    detail: "Agricultura",
                    price: "10",
                    seller: "user03@email.com",
                    date: new Date(2022,3,13),
                    highlight: false
                },
                {
                    title: "Hoz",
                    detail: "Herramienta",
                    price: "3",
                    seller: "user03@email.com",
                    date: new Date(2022,3,14),
                    highlight: false
                },
                {
                    title: "Hollow Night",
                    detail: "Videojuego",
                    price: "30",
                    seller: "user03@email.com",
                    date: new Date(2022,3,15),
                    highlight: false
                },
                {
                    title: "Almendra",
                    detail: "Libro",
                    price: "5",
                    seller: "user03@email.com",
                    date: new Date(2022,3,16),
                    highlight: false
                },
                {
                    title: "Mando PS4",
                    detail: "Tecnologia",
                    price: "7",
                    seller: "user03@email.com",
                    date: new Date(2022,3,17),
                    highlight: false
                },
                {
                    title: "Reloj",
                    detail: "Accesorio",
                    price: "5",
                    seller: "user03@email.com",
                    date: new Date(2022,3,18),
                    highlight: false
                },
                {
                    title: "Mesa auxiliar",
                    detail: "Mueble",
                    price: "35",
                    seller: "user03@email.com",
                    date: new Date(2022,3,19),
                    highlight: false
                },
                {
                    title: "Bateria",
                    detail: "Musica",
                    price: "55",
                    seller: "user03@email.com",
                    date: new Date(2022,4,10),
                    highlight: false
                },
                {
                    title: "Marina",
                    detail: "Libro",
                    price: "6",
                    seller: "user04@email.com",
                    buyer: "user14@email.com",
                    date: new Date(2022,4,11),
                    highlight: false
                },
                {
                    title: "Samsung Galaxy Buds",
                    detail: "Electronica",
                    price: "40",
                    seller: "user04@email.com",
                    date: new Date(2022,4,12),
                    highlight: false
                },
                {
                    title: "Xiaomi LED",
                    detail: "Electronica",
                    price: "30",
                    seller: "user04@email.com",
                    date: new Date(2022,4,13),
                    highlight: false
                },
                {
                    title: "Estanteria",
                    detail: "Mueble",
                    price: "42",
                    seller: "user04@email.com",
                    date: new Date(2022,4,14),
                    highlight: false
                },
                {
                    title: "Disfraz egipcio",
                    detail: "Ropa",
                    price: "4",
                    seller: "user04@email.com",
                    date: new Date(2022,4,15),
                    highlight: false
                },
                {
                    title: "Caldera",
                    detail: "Mueble",
                    price: "24",
                    seller: "user04@email.com",
                    date: new Date(2022,4,16),
                    highlight: false
                },
                {
                    title: "Desbrozadora",
                    detail: "Agricultura",
                    price: "100",
                    seller: "user04@email.com",
                    date: new Date(2022,4,17),
                    highlight: false
                },
                {
                    title: "Haikyuu",
                    detail: "Manga",
                    price: "5",
                    seller: "user04@email.com",
                    date: new Date(2022,4,18),
                    highlight: false
                },
                {
                    title: "MP4",
                    detail: "Electronica",
                    price: "30",
                    seller: "user04@email.com",
                    buyer: "user02@email.com",
                    date: new Date(2022,4,19),
                    highlight: false
                },
                {
                    title: "Martillo",
                    detail: "Herramienta",
                    price: "4",
                    seller: "user04@email.com",
                    date: new Date(2022,5,10),
                    highlight: false
                },
                {
                    title: "Nintendo Switch",
                    detail: "Consola",
                    price: "150",
                    seller: "user05@email.com",
                    date: new Date(2022,5,11),
                    highlight: false
                },
                {
                    title: "Canon 3500",
                    detail: "Camara",
                    price: "120",
                    seller: "user05@email.com",
                    date: new Date(2022,5,12),
                    highlight: false
                },
                {
                    title: "Nescafe Dolce Gusto",
                    detail: "Cafetera",
                    price: "45",
                    seller: "user05@email.com",
                    buyer: "user08@email.com",
                    date: new Date(2022,5,13),
                    highlight: false
                },
                {
                    title: "Razer Barracuda",
                    detail: "Auriculares",
                    price: "60",
                    seller: "user05@email.com",
                    date: new Date(2022,5,14),
                    highlight: false
                },
                {
                    title: "iPad",
                    detail: "Tablet",
                    price: "100",
                    seller: "user05@email.com",
                    date: new Date(2022,5,15),
                    highlight: false
                },
                {
                    title: "Air Frier",
                    detail: "Electrodomestico",
                    price: "89",
                    seller: "user05@email.com",
                    date: new Date(2022,5,16),
                    highlight: false
                },
                {
                    title: "Microondas",
                    detail: "Electrodomestico",
                    price: "48",
                    seller: "user05@email.com",
                    date: new Date(2022,5,17),
                    highlight: false
                },
                {
                    title: "Xiaomi TV",
                    detail: "Television",
                    price: "400",
                    seller: "user05@email.com",
                    date: new Date(2022,5,18),
                    highlight: false
                },
                {
                    title: "Sudadera kaotiko",
                    detail: "Ropa",
                    price: "75",
                    seller: "user05@email.com",
                    date: new Date(2022,5,19),
                    highlight: false
                },
                {
                    title: "Termo",
                    detail: "Electrodomestico",
                    price: "30",
                    seller: "user05@email.com",
                    date: new Date(2022,6,10),
                    highlight: false
                },
                {
                    title: "Humidificador",
                    detail: "Decoracion",
                    price: "10",
                    seller: "user06@email.com",
                    date: new Date(2022,6,11),
                    highlight: false
                },
                {
                    title: "Cesta mimbre",
                    detail: "Accesorio",
                    price: "5",
                    seller: "user06@email.com",
                    date: new Date(2022,6,12),
                    highlight: false
                },
                {
                    title: "Bonsai lego",
                    detail: "Decoracion",
                    price: "30",
                    seller: "user06@email.com",
                    buyer: "user09@email.com",
                    date: new Date(2022,6,13),
                    highlight: false
                },
                {
                    title: "Marco fotos",
                    detail: "Accesorio",
                    price: "5",
                    seller: "user06@email.com",
                    date: new Date(2022,6,14),
                    highlight: false
                },
                {
                    title: "Pluma estilografica",
                    detail: "Escritura",
                    price: "40",
                    seller: "user06@email.com",
                    date: new Date(2022,6,15),
                    highlight: false
                },
                {
                    title: "One Plus Nord 2",
                    detail: "Movil",
                    price: "170",
                    seller: "user06@email.com",
                    date: new Date(2022,6,16),
                    highlight: false
                },
                {
                    title: "Animal Crossing",
                    detail: "Videojuego",
                    price: "40",
                    seller: "user06@email.com",
                    date: new Date(2022,6,17),
                    highlight: false
                },
                {
                    title: "Taza dragon ball",
                    detail: "Vajilla",
                    price: "50",
                    seller: "user06@email.com",
                    date: new Date(2022,6,18),
                    highlight: false
                },
                {
                    title: "Air Pods",
                    detail: "Electronica",
                    price: "90",
                    seller: "user06@email.com",
                    date: new Date(2022,6,19),
                    highlight: false
                },
                {
                    title: "Telescopio",
                    detail: "Ocio",
                    price: "270",
                    seller: "user06@email.com",
                    date: new Date(2022,7,10),
                    highlight: false
                },
                {
                    title: "Olympus",
                    detail: "Camara",
                    price: "270",
                    seller: "user07@email.com",
                    date: new Date(2022,7,11),
                    highlight: false
                },
                {
                    title: "Tripode",
                    detail: "Accesorio",
                    price: "15",
                    seller: "user07@email.com",
                    date: new Date(2022,7,12),
                    highlight: false
                },
                {
                    title: "Raqueta padel",
                    detail: "Deporte",
                    price: "89",
                    seller: "user07@email.com",
                    date: new Date(2022,7,13),
                    highlight: false
                },
                {
                    title: "Skate penny",
                    detail: "Ocio",
                    price: "50",
                    seller: "user07@email.com",
                    buyer: "user12@email.com",
                    date: new Date(2022,7,14),
                    highlight: false
                },
                {
                    title: "Playeros adidas",
                    detail: "Ropa",
                    price: "40",
                    seller: "user07@email.com",
                    date: new Date(2022,7,15),
                    highlight: false
                },
                {
                    title: "Rotuladores alcohol",
                    detail: "Dibujo",
                    price: "170",
                    seller: "user07@email.com",
                    date: new Date(2022,7,16),
                    highlight: false
                },
                {
                    title: "Mochila Real Madrid",
                    detail: "Accesorio",
                    price: "10",
                    seller: "user07@email.com",
                    date: new Date(2022,7,17),
                    highlight: false
                },
                {
                    title: "Mesa hockey",
                    detail: "Ocio",
                    price: "200",
                    seller: "user07@email.com",
                    date: new Date(2022,7,18),
                    highlight: false
                },
                {
                    title: "Forrest Gump",
                    detail: "Pelicula",
                    price: "30",
                    seller: "user07@email.com",
                    date: new Date(2022,7,19),
                    highlight: false
                },
                {
                    title: "Elantris",
                    detail: "Libro",
                    price: "10",
                    seller: "user07@email.com",
                    date: new Date(2022,8,10),
                    highlight: false
                },
                {
                    title: "La hija de la noche",
                    detail: "Libro",
                    price: "8",
                    seller: "user08@email.com",
                    date: new Date(2022,8,11),
                    highlight: false
                },
                {
                    title: "Cuna",
                    detail: "Bebe",
                    price: "70",
                    seller: "user08@email.com",
                    buyer: "user14@email.com",
                    date: new Date(2022,8,12),
                    highlight: false
                },
                {
                    title: "Playmobil",
                    detail: "Juguete",
                    price: "8",
                    seller: "user08@email.com",
                    date: new Date(2022,8,13),
                    highlight: false
                },
                {
                    title: "Peluche elmo",
                    detail: "Juguete",
                    price: "5",
                    seller: "user08@email.com",
                    date: new Date(2022,8,14),
                    highlight: false
                },
                {
                    title: "Quad",
                    detail: "Transporte",
                    price: "500",
                    seller: "user08@email.com",
                    date: new Date(2022,8,15),
                    highlight: false
                },
                {
                    title: "Lego star wars",
                    detail: "Juguete",
                    price: "52",
                    seller: "user08@email.com",
                    date: new Date(2022,8,16),
                    highlight: false
                },
                {
                    title: "Flexo",
                    detail: "Luz",
                    price: "20",
                    seller: "user08@email.com",
                    date: new Date(2022,8,17),
                    highlight: false
                },
                {
                    title: "Tomtom",
                    detail: "Navegador GPS",
                    price: "40",
                    seller: "user08@email.com",
                    date: new Date(2022,8,18),
                    highlight: false
                },
                {
                    title: "Webcam HP",
                    detail: "Tecnologia",
                    price: "50",
                    seller: "user08@email.com",
                    date: new Date(2022,8,19),
                    highlight: false
                },
                {
                    title: "Nikon",
                    detail: "Camara",
                    price: "300",
                    seller: "user08@email.com",
                    date: new Date(2022,9,10),
                    highlight: false
                },
                {
                    title: "Cuentakilometros",
                    detail: "Accesorio bicicleta",
                    price: "60",
                    seller: "user09@email.com",
                    date: new Date(2022,9,11),
                    highlight: false
                },
                {
                    title: "Friends",
                    detail: "Serie",
                    price: "10",
                    seller: "user09@email.com",
                    date: new Date(2022,9,12),
                    highlight: false
                },
                {
                    title: "Lamina studio ghibli",
                    detail: "Decoracion",
                    price: "10",
                    seller: "user09@email.com",
                    buyer: "user01@email.com",
                    date: new Date(2022,9,13),
                    highlight: false
                },
                {
                    title: "Posavasos",
                    detail: "Accesorio",
                    price: "2",
                    seller: "user09@email.com",
                    date: new Date(2022,9,14),
                    highlight: false
                },
                {
                    title: "Limpiador cerave",
                    detail: "Belleza",
                    price: "9",
                    seller: "user09@email.com",
                    date: new Date(2022,9,15),
                    highlight: false
                },
                {
                    title: "Crema manos",
                    detail: "Cuidado de piel",
                    price: "10",
                    seller: "user09@email.com",
                    date: new Date(2022,9,16),
                    highlight: false
                },
                {
                    title: "Hollow knight",
                    detail: "Videojuego",
                    price: "40",
                    seller: "user09@email.com",
                    date: new Date(2022,9,17),
                    highlight: false
                },
                {
                    title: "Secador pelo",
                    detail: "Serie",
                    price: "10",
                    seller: "user09@email.com",
                    date: new Date(2022,9,18),
                    highlight: false
                },
                {
                    title: "Accesorio",
                    detail: "Serie",
                    price: "15",
                    seller: "user09@email.com",
                    date: new Date(2022,9,19),
                    highlight: false
                },
                {
                    title: "Apple pencil",
                    detail: "tecnologia",
                    price: "80",
                    seller: "user09@email.com",
                    date: new Date(2022,10,10),
                    highlight: false
                },
                {
                    title: "Taza personalizada",
                    detail: "Vajilla",
                    price: "50",
                    seller: "user09@email.com",
                    date: new Date(2022,10,11),
                    highlight: false
                },
                {
                    title: "Colgante llave",
                    detail: "Accesorio",
                    price: "12",
                    seller: "user10@email.com",
                    date: new Date(2022,10,12),
                    highlight: false
                },
                {
                    title: "Sofa",
                    detail: "Mueble",
                    price: "20",
                    seller: "user10@email.com",
                    date: new Date(2022,10,13),
                    highlight: false
                },
                {
                    title: "Coleccion cromos liga",
                    detail: "Coleccion",
                    price: "50",
                    seller: "user10@email.com",
                    date: new Date(2022,10,14),
                    highlight: false
                },
                {
                    title: "Lavadora",
                    detail: "Electrodomestico",
                    price: "200",
                    seller: "user10@email.com",
                    date: new Date(2022,10,15),
                    highlight: false
                },
                {
                    title: "Invictus",
                    detail: "Colonia",
                    price: "41",
                    seller: "user10@email.com",
                    date: new Date(2022,10,16),
                    highlight: false
                },
                {
                    title: "Mampara",
                    detail: "Mueble",
                    price: "80",
                    seller: "user10@email.com",
                    buyer: "user13@email.com",
                    date: new Date(2022,10,17),
                    highlight: false
                },
                {
                    title: "Balon de futbol",
                    detail: "Deporte",
                    price: "10",
                    seller: "user10@email.com",
                    date: new Date(2022,10,18),
                    highlight: false
                },
                {
                    title: "Botas de futbol",
                    detail: "Deporte",
                    price: "200",
                    seller: "user10@email.com",
                    date: new Date(2022,10,19),
                    highlight: false
                },
                {
                    title: "Ajedrez",
                    detail: "Ocio",
                    price: "8",
                    seller: "user10@email.com",
                    date: new Date(2022,11,10),
                    highlight: false
                },
                {
                    title: "F1 Manager 2022",
                    detail: "Videojuego",
                    price: "40",
                    seller: "user11@email.com",
                    date: new Date(2022,11,11),
                    highlight: false
                },
                {
                    title: "Marvel Spiderman Remastered",
                    detail: "Videojuego",
                    price: "50",
                    seller: "user11@email.com",
                    date: new Date(2022,11,12),
                    highlight: false
                },
                {
                    title: "Hogwarts Legacy",
                    detail: "Videojuego",
                    price: "70",
                    seller: "user11@email.com",
                    date: new Date(2022,11,13),
                    highlight: false
                },
                {
                    title: "Elden Ring",
                    detail: "Videojuego",
                    price: "20",
                    seller: "user11@email.com",
                    date: new Date(2022,11,14),
                    highlight: false
                },
                {
                    title: "GTA V",
                    detail: "Videojuego",
                    price: "30",
                    seller: "user11@email.com",
                    buyer: "user12@email.com",
                    date: new Date(2022,11,15),
                    highlight: false
                },
                {
                    title: "Bioshock 2",
                    detail: "Mafia 2",
                    price: "100",
                    seller: "user11@email.com",
                    date: new Date(2022,11,16),
                    highlight: false
                },
                {
                    title: "Hellblade",
                    detail: "Videojuego",
                    price: "40",
                    seller: "user11@email.com",
                    date: new Date(2022,11,17),
                    highlight: false
                },
                {
                    title: "Resident Evil Village",
                    detail: "Videojuego",
                    price: "2",
                    seller: "user11@email.com",
                    date: new Date(2022,11,18),
                    highlight: false
                },
                {
                    title: "Sekiro",
                    detail: "Videojuego",
                    price: "70",
                    seller: "user11@email.com",
                    date: new Date(2022,11,19),
                    highlight: false
                },
                {
                    title: "Las lagrimas de shiva",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,10),
                    highlight: false
                },
                {
                    title: "El si de las niñas",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,11),
                    highlight: false
                },
                {
                    title: "La bibliotecaria de Auschwitz",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,12),
                    highlight: false
                },
                {
                    title: "El viejo y el mar",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    buyer: "user05@email.com",
                    date: new Date(2022,12,13),
                    highlight: false
                },
                {
                    title: "El ojo del tiempo",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,14),
                    highlight: false
                },
                {
                    title: "El secreto del tiempo",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,15),
                    highlight: false
                },
                {
                    title: "Luces de bohemia",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,16),
                    highlight: false
                },
                {
                    title: "El chico de la ultima fila",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,17),
                    highlight: false
                },
                {
                    title: "La puerta de los tres cerrojos",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,18),
                    highlight: false
                },
                {
                    title: "Novia gitana",
                    detail: "Libro",
                    price: "10",
                    seller: "user12@email.com",
                    date: new Date(2022,12,19),
                    highlight: false
                },
                {
                    title: "Pinocho",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    buyer: "user03@email.com",
                    date: new Date(2022,1,20),
                    highlight: false
                },
                {
                    title: "Oceans eleven",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,21),
                    highlight: false
                },
                {
                    title: "Top Gun",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,22),
                    highlight: false
                },
                {
                    title: "Hora punta",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,23),
                    highlight: false
                },
                {
                    title: "Rocky IV",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,24),
                    highlight: false
                },
                {
                    title: "Fast and furious",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,25),
                    highlight: false
                },
                {
                    title: "Torrente",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,26),
                    highlight: false
                },
                {
                    title: "El juego de ender",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,27),
                    highlight: false
                },
                {
                    title: "Joker",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,28),
                    highlight: false
                },
                {
                    title: "REC",
                    detail: "Pelicula",
                    price: "15",
                    seller: "user13@email.com",
                    date: new Date(2022,1,29),
                    highlight: false
                },
                {
                    title: "Aida",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,20),
                    highlight: false
                },
                {
                    title: "Aqui no hay quien viva",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,21),
                    highlight: false
                },
                {
                    title: "El internado",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    buyer: "user06@email.com",
                    date: new Date(2022,2,22),
                    highlight: false
                },
                {
                    title: "Fisica o quimica",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,23),
                    highlight: false
                },
                {
                    title: "La que se avecina",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,24),
                    highlight: false
                },
                {
                    title: "Los Serrano",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,25),
                    highlight: false
                },
                {
                    title: "La casa de papel",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,26),
                    highlight: false
                },
                {
                    title: "El barco",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,27),
                    highlight: false
                },
                {
                    title: "Vis a vis",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,28),
                    highlight: false
                },
                {
                    title: "Los hombres de Paco",
                    detail: "Serie",
                    price: "15",
                    seller: "user14@email.com",
                    date: new Date(2022,2,29),
                    highlight: false
                },
                {
                    title: "Router",
                    detail: "Electronica",
                    price: "30",
                    seller: "user15@email.com",
                    date: new Date(2022,3,20),
                    highlight: false
                },
                {
                    title: "Telefono",
                    detail: "Electronica",
                    price: "89",
                    seller: "user15@email.com",
                    date: new Date(2022,3,21),
                    highlight: false
                },
                {
                    title: "Silla gaming",
                    detail: "Accesorio",
                    price: "50",
                    seller: "user15@email.com",
                    buyer: "user07@email.com",
                    date: new Date(2022,3,22),
                    highlight: false
                },
                {
                    title: "Diccionario",
                    detail: "Libro",
                    price: "30",
                    seller: "user15@email.com",
                    date: new Date(2022,3,23),
                },
                {
                    title: "Impresora",
                    detail: "Electronica",
                    price: "200",
                    seller: "user15@email.com",
                    date: new Date(2022,3,24),
                    highlight: false
                },
                {
                    title: "Micro",
                    detail: "Electronica",
                    price: "30",
                    seller: "user15@email.com",
                    date: new Date(2022,3,25),
                    highlight: false
                },
                {
                    title: "Kas",
                    detail: "Bebida",
                    price: "15",
                    seller: "user15@email.com",
                    date: new Date(2022,3,26),
                    highlight: false
                },
                {
                    title: "Cocacola",
                    detail: "Bebida",
                    price: "15",
                    seller: "user15@email.com",
                    date: new Date(2022,3,27),
                    highlight: false
                },
                {
                    title: "Agua",
                    detail: "Bebida",
                    price: "15",
                    seller: "user15@email.com",
                    date: new Date(2022,3,28),
                    highlight: false
                },
                {
                    title: "Cerveza",
                    detail: "Bebida",
                    price: "15",
                    seller: "user15@email.com",
                    date: new Date(2022,3,29),
                    highlight: false
                },
                {
                    title: "Agenda",
                    detail: "De Bebi Fernandez",
                    price: "15",
                    seller: "user10@email.com",
                    date: new Date(2022,4,22),
                    highlight: false
                },
                {
                    title: "Pulsera reloj",
                    detail: "Marca amazfit",
                    price: "30",
                    seller: "user11@email.com",
                    date: new Date(2022,4,21),
                    highlight: false
                },
            ]
            const result = await offersCollection.insertMany(offers);
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }, insertConversations: async function () {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'conversations';
            const conversationsCollection = database.collection(collectionName);

            await conversationsCollection.deleteMany({}); // Limpiar la tabla de conversaciones

            return true; // Indicar éxito en la eliminación
        } catch (error) {
            throw error;
        }
    }

}