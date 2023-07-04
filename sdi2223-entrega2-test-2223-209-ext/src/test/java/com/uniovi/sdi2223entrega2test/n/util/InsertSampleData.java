package com.uniovi.sdi2223entrega2test.n.util;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class InsertSampleData {

    static String uri = "mongodb://localhost:27017";
    static String dbName = "myWallapop";

    static MongoClient mongoClient = MongoClients.create(uri);
    static MongoDatabase database = mongoClient.getDatabase(dbName);

    public static void insertSampleData() {
        InsertSampleData insertSampleData = new InsertSampleData();
        insertSampleData.insertUsers();
        insertSampleData.insertOffers();
        insertSampleData.insertConversations();
    }

    private void insertUsers() {

        try {
            database.getCollection("users").drop();
        } catch (Exception e) {
            System.out.println("Error al eliminar la colección");
        }
        MongoCollection users = database.getCollection("users");

        List<Document> usersList = Arrays.asList(
                new Document().append("name", "user01").append("surname", "user01").append("email", "user01@email.com").append("password", "783a825ecf667676312bdd5e76a138e61e55d5061ef541f445e0c22e671eba9f").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user02").append("surname", "user02").append("email", "user02@email.com").append("password", "9812db3294b48b41aa0ea4cbe44453280286c0a089a07f3f6f4a313759012ab9").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user03").append("surname", "user02").append("email", "user03@email.com").append("password", "66e93521a9447f5082b957a3bc07dcd5841608a0a2a951cbb30a21d83f13d83a").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user04").append("surname", "user02").append("email", "user04@email.com").append("password", "049141fc2f265c205539050c6ebc6a9a3d81c87fbf8f936030bc108330280fef").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user05").append("surname", "user02").append("email", "user05@email.com").append("password", "bbd89bd814f8c02da5534ee62781701285aa3d586158e9c857618f9a36d18ce3").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user06").append("surname", "user02").append("email", "user06@email.com").append("password", "308f5766b85b7842f62e0bd96873a80cad47cf9fb76ba1061957ad0f1ae4dc1f").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "admin").append("surname", "admin").append("email", "admin@email.com").append("password", "ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11").append("role","admin"),
                new Document().append("name", "user07").append("surname", "user02").append("email", "user07@email.com").append("password", "1a44cfeba26b0d6245a787a87a39e90308bcc6445916951d2cb9a80881c7d1c6").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user08").append("surname", "user02").append("email", "user08@email.com").append("password", "60f45cdcacc671c9bc3cddc0e4cf4d5aebe65d5a674fdb7498632a2427638df3").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user09").append("surname", "user02").append("email", "user09@email.com").append("password", "c7b08c4af12093cbf088edac0b8c9e394f8486cdb267f5950f363bd5adb93e77").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user10").append("surname", "user02").append("email", "user10@email.com").append("password", "c4307e1f4f7a11e99a5e4bfeaf598cadcb99fc536b6eeb668ebdff87df88cff5").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user11").append("surname", "user02").append("email", "user11@email.com").append("password", "b9809187151d1b2ada397b7c6ecfdedcaeeaa401568f7abf4706b7eab2f7f8b6").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user12").append("surname", "user02").append("email", "user12@email.com").append("password", "b9809187151d1b2ada397b7c6ecfdedcaeeaa401568f7abf4706b7eab2f7f8b6").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user13").append("surname", "user02").append("email", "user13@email.com").append("password", "46ef749ceaff17681fcbcf4f5637387421db020f80a3624459e2702187283527").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user14").append("surname", "user02").append("email", "user14@email.com").append("password", "c76a26044ac42d88b2154b69e5f7006d38140ccbf91c1a93679ba78e0cc54a17").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard"),
                new Document().append("name", "user15").append("surname", "user02").append("email", "user15@email.com").append("password", "5d6c0f50beb06679b619f7ef2aa46ca23caae057b21b1621ba5ae9692a9192fb").append("money", 100).append("dateOfBirth", new Date(2001,6,15)).append("role","standard")
        );

        users.insertMany(usersList);
    }

    private void insertOffers() {

        try {
            database.getCollection("offers").drop();
        } catch (Exception e) {
            System.out.println("Error al eliminar la colección");
        }
        MongoCollection offers = database.getCollection("offers");

        List<Document> offersList = Arrays.asList(
                new Document().append("title", "Pokemon Escarlata").append("detail", "Videojuego").append("price", "50").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Arena Blanca").append("detail", "Libro").append("price", "20").append("seller", "user01@email.com").append("buyer", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Xiaomi 5T").append("detail", "Movil").append("price", "550").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Camiseta blanca").append("detail", "Ropa").append("price", "50").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Acer").append("detail", "Ordenador").append("price", "100").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Renault Clio").append("detail", "Coche").append("price", "4000").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Soporte auriculares").append("detail", "Accesorio").append("price", "20").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Camiseta el nano").append("detail", "Ropa").append("price", "10").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Boligrafo bic").append("detail", "Educacion").append("price", "2").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Trenza del mar esmeralda").append("detail", "Libro").append("price", "15").append("seller", "user01@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "SNK").append("detail", "Manga").append("price", "7").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Zapato tacon").append("detail", "Zapatos").append("price", "4").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Tejano").append("detail", "Pantalon").append("price", "7").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Patinete").append("detail", "Manga").append("price", "7").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Bicicleta").append("detail", "Transporte").append("price", "100").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Manta electrica").append("detail", "Ropa").append("price", "20").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Zelda BOTW").append("detail", "Videojuego").append("price", "40").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Apple watch").append("detail", "Tecnologia").append("price", "60").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Escritorio IKEA").append("detail", "Mueble").append("price", "10").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mancuernas").append("detail", "Deporte").append("price", "7").append("seller", "user02@email.com").append("buyer", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Lamina NY").append("detail", "Arte").append("price", "7").append("seller", "user02@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mancuernas").append("detail", "Deporte").append("price", "7").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Asterix y Obelix").append("detail", "Libro").append("price", "5").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Barrica").append("detail", "Agricultura").append("price", "10").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Hoz").append("detail", "Herramienta").append("price", "3").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Hollow Night").append("detail", "Videojuego").append("price", "50").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Almendra").append("detail", "Libro").append("price", "7").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mando PS4").append("detail", "Tecnologia").append("price", "70").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Reloj").append("detail", "Accesorio").append("price", "5").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mesa auxiliar").append("detail", "Mueble").append("price", "35").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Bateria").append("detail", "Musica").append("price", "55").append("seller", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Marina").append("detail", "Libro").append("price", "6").append("seller", "user04@email.com").append("buyer", "user03@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Samsung Galaxy Buds").append("detail", "Electronica").append("price", "55").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Xiaomi LED").append("detail", "Electronica").append("price", "30").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Estanteria").append("detail", "Mueble").append("price", "42").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Disfraz egipcio").append("detail", "Ropa").append("price", "4").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Caldera").append("detail", "Mueble").append("price", "24").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Desbrozadora").append("detail", "Agricultura").append("price", "100").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Haikyuu").append("detail", "Manga").append("price", "5").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "MP4").append("detail", "Electronica").append("price", "30").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Martillo").append("detail", "Herramienta").append("price", "4").append("seller", "user04@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Nintendo Switch").append("detail", "Consola").append("price", "155").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Canon 3500").append("detail", "Camara").append("price", "120").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Nescafe Dolce Gusto").append("detail", "Cafetera").append("price", "55").append("seller", "user05@email.com").append("buyer", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Razer Barracuda").append("detail", "Auriculares").append("price", "30").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "iPad").append("detail", "Tablet").append("price", "100").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Air Frier").append("detail", "Electrodomestico").append("price", "89").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Microondas").append("detail", "Electrodomestico").append("price", "55").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Xiaomi TV").append("detail", "Television").append("price", "55").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Sudadera kaotiko").append("detail", "Ropa").append("price", "20").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Termo").append("detail", "Electrodomestico").append("price", "74").append("seller", "user05@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Humidificador").append("detail", "Decoracion").append("price", "10").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Bonsai lego").append("detail", "Decoracion").append("price", "30").append("seller", "user06@email.com").append("buyer", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Marco fotos").append("detail", "Accesorio").append("price", "5").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Pluma estilografica").append("detail", "Escritura").append("price", "55").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "One Plus Nord 2").append("detail", "Movil").append("price", "170").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Animal Crossing").append("detail", "Videojuego").append("price", "5").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Taza dragon ball").append("detail", "Vajilla").append("price", "50").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Air Pods").append("detail", "Electronica").append("price", "90").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Telescopio").append("detail", "Ocio").append("price", "270").append("seller", "user06@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Olympus").append("detail", "Camara").append("price", "270").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Tripode").append("detail", "Accesorio").append("price", "15").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Raqueta padel").append("detail", "Deporte").append("price", "89").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Skate penny").append("detail", "Ocio").append("price", "89").append("seller", "user07@email.com").append("buyer", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Playeros adidas").append("detail", "Ropa").append("price", "40").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Rotuladores alcohol").append("detail", "Dibujo").append("price", "15").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mochila Real Madrid").append("detail", "Accesorio").append("price", "10").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mesa hockey").append("detail", "Ocio").append("price", "200").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Forrest Gump").append("detail", "Pelicula").append("price", "30").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Elantris").append("detail", "Libro").append("price", "15").append("seller", "user07@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "La hija de la noche").append("detail", "Libro").append("price", "8").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Cuna").append("detail", "Bebe").append("price", "70").append("seller", "user08@email.com").append("buyer", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Playmobil").append("detail", "Juguete").append("price", "15").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Peluche elmo").append("detail", "Juguete").append("price", "8").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Quad").append("detail", "Transporte").append("price", "500").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Lego star wars").append("detail", "Juguete").append("price", "15").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Flexo").append("detail", "Luz").append("price", "20").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Tomtom").append("detail", "Navegador GPS").append("price", "40").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Webcam HP").append("detail", "Tecnologia").append("price", "50").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Nikon").append("detail", "Camara").append("price", "300").append("seller", "user08@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Cuentakilometros").append("detail", "Accesorio bicicleta").append("price", "60").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Lamina studio ghibli").append("detail", "Decoracion").append("price", "10").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Posavasos").append("detail", "Accesorio").append("price", "2").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Limpiador cerave").append("detail", "Belleza").append("price", "9").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Crema manos").append("detail", "Cuidado de piel").append("price", "10").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Hollow knight").append("detail", "Videojuego").append("price", "40").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Secador pelo").append("detail", "Serie").append("price", "15").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Accesorio").append("detail", "Serie").append("price", "15").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Apple pencil").append("detail", "tecnologia").append("price", "80").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Taza personalizada").append("detail", "Vajilla").append("price", "15").append("seller", "user09@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Colgante llave").append("detail", "Accesorio").append("price", "15").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Sofa").append("detail", "Mueble").append("price", "20").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Coleccion cromos liga").append("detail", "Coleccion").append("price", "50").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Lavadora").append("detail", "Electrodomestico").append("price", "200").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Invictus").append("detail", "Colonia").append("price", "41").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Mampara").append("detail", "Mueble").append("price", "80").append("seller", "user10@email.com").append("buyer", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Balon de futbol").append("detail", "Deporte").append("price", "15").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Botas de futbol").append("detail", "Deporte").append("price", "200").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Ajedrez").append("detail", "Ocio").append("price", "15").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "F1 Manager 2022").append("detail", "Videojuego").append("price", "40").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Marvel Spiderman Remastered").append("detail", "Videojuego").append("price", "50").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Hogwarts Legacy").append("detail", "Videojuego").append("price", "70").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Elden Ring").append("detail", "Videojuego").append("price", "20").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "GTA V").append("detail", "Videojuego").append("price", "40").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Bioshock 2").append("detail", "Mafia 2").append("price", "100").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Hellblade").append("detail", "Videojuego").append("price", "40").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Resident Evil Village").append("detail", "Videojuego").append("price", "2").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Sekiro").append("detail", "Videojuego").append("price", "70").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Las lagrimas de shiva").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El si de las niñas").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "La bibliotecaria de Auschwitz").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El viejo y el mar").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El ojo del tiempo").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El secreto del tiempo").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Luces de bohemia").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El chico de la ultima fila").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "La puerta de los tres cerrojos").append("detail", "Libro").append("price", "15").append("seller", "user12@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Novia gitana").append("detail", "Libro").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Pinocho").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Oceans eleven").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Top Gun").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Hora punta").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Rocky IV").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Fast and furious").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Torrente").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El juego de ender").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Joker").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "REC").append("detail", "Pelicula").append("price", "15").append("seller", "user13@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Aida").append("detail", "Pelicula").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Aqui no hay quien viva").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El internado").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Fisica o quimica").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "La que se avecina").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Los Serrano").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "La casa de papel").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "El barco").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Vis a vis").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Los hombres de Paco").append("detail", "Serie").append("price", "15").append("seller", "user14@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Router").append("detail", "Electronica").append("price", "15").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Telefono").append("detail", "Electronica").append("price", "30").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Silla gaming").append("detail", "Accesorio").append("price", "50").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Diccionario").append("detail", "Libro").append("price", "15").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Impresora").append("detail", "Electronica").append("price", "200").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Micro").append("detail", "Electronica").append("price", "30").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Kas").append("detail", "Bebida").append("price", "15").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Cocacola").append("detail", "Bebida").append("price", "15").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Agua").append("detail", "Bebida").append("price", "15").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Cerveza").append("detail", "Bebida").append("price", "15").append("seller", "user15@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Agenda").append("detail", "De Bebi Fernandez").append("price", "30").append("seller", "user10@email.com").append("date", new Date(2001,6,15)).append("highlight",false),
                new Document().append("title", "Pulsera reloj").append("detail", "Marca amazfit").append("price", "45").append("seller", "user11@email.com").append("date", new Date(2001,6,15)).append("highlight",false)
                );

        offers.insertMany(offersList);
    }

    private void insertConversations() {
        try {
            database.getCollection("conversations").drop();
        } catch (Exception e) {
            System.out.println("Error al eliminar la colección");
        }
    }

}