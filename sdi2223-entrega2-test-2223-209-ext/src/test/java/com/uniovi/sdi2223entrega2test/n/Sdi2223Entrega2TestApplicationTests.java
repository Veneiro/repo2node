package com.uniovi.sdi2223entrega2test.n;

import com.uniovi.sdi2223entrega2test.n.pageobjects.*;
import com.uniovi.sdi2223entrega2test.n.util.API_Rest_Utils;
import com.uniovi.sdi2223entrega2test.n.util.InsertSampleData;
import com.uniovi.sdi2223entrega2test.n.pageobjects.*;
import com.uniovi.sdi2223entrega2test.n.util.SeleniumUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega2TestApplicationTests {
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "D:\\usuario\\descargas\\sesion06\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";

    //static String Geckodriver = "E:\\Documentos\\UNIOVI\\Cuarto Curso\\SDI\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Users\\usuario\\OneDrive\\Escritorio\\Uni\\Tercero\\SDI\\Práctica\\Practica05\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    //static String PathFirefox = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/USUARIO/selenium/geckodriver-v0.30.0-macos";
    //Común a Windows y a MACOSX
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:3000";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
        InsertSampleData.insertSampleData();
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    // Registro de usuario con datos válidos
    @Test
    @Order(1)
    public void PR01() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "Miriam", "Gonzalez", "miriam@email.com", "2001-04-20", "654321", "654321");
        // Comprobamos que nos redirige al home
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // Registro de usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
    @Test
    @Order(2)
    public void PR02() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario. Nombre vacío
        PO_SignUpView.fillForm(driver, " ", "Gonzalez", "miriam@email.com", "2001-04-20", "654321", "654321");
        //Comprobamos que aparece el mensaje de error
        SeleniumUtils.textIsPresentOnPage(driver, "Name is required");
        //List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Name is required", 5);

        // Rellenamos el formulario. Apellidos vacío
        PO_SignUpView.fillForm(driver, "Miriam", "  ", "miriam@email.com", "2001-04-20", "654321", "654321");
        SeleniumUtils.textIsPresentOnPage(driver, "Surname is required");

        // Rellenamos el formulario. fecha vacía
        PO_SignUpView.fillForm(driver, "Miriam", "Gonzalez", "miriam@email.com", " ", "654321", "654321");
        SeleniumUtils.textIsPresentOnPage(driver, "Datebirth is required");

        // Rellenamos el formulario. Email vacío
        PO_SignUpView.fillForm(driver, "Miriam", "Gonzalez", " ", "2001-04-20", "654321", "654321");
        SeleniumUtils.textIsPresentOnPage(driver, "Email is required");


        // Comprobamos que sigue en la página de signup
        String checkText = "Nombre:";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // Registro de usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(3)
    public void PR03() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "Miriam", "Gonzalez", "miriam@email.com", "2001-04-20", "654321", "5");
        // Comprobamos que sigue en la página de signup
        String checkText = "Nombre:";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // Registro de usuario con datos inválidos (email existente).
    @Test
    @Order(4)
    public void PR04() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "Miriam", "Gonzalez", "user04@email.com", "2001-04-20", "654321", "654321");
        // Comprobamos que sigue en la página de signup
        String checkText = "Nombre:";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // Inicio de sesión con datos válidos (administrador).
    @Test
    @Order(5)
    public void PR05() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que es el admin el que se logueo
        String checkText = "admin@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Comprobamos que hay una tabla con los campos email, nombre y apellidos
        checkText = "Email";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        checkText = "Nombre";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        checkText = "Apellidos";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Inicio de sesión con datos válidos (usuario estándar).
    @Test
    @Order(6)
    public void PR06() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobamos que es el usuario el que se logueo
        String checkText = "user01@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        // Comprobamos que hay una tabla con los campos detalles y precio
        checkText = "Título";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        checkText = "Detalle";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Inicio de sesión con datos inválidos (usuario estándar, email existente, pero contraseña
    //incorrecta).
    @Test
    @Order(7)
    public void PR07() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user04");
        // Comprobamos que sigue en la página de login
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
    @Test
    @Order(8)
    public void PR08() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "", "");
        // Comprobamos que sigue en la página de login
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //  Hacer clic en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
    //sesión (Login).
    @Test
    @Order(9)
    public void PR09() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Comprobamos que está en la página de login
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //  Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
    @Test
    @Order(10)
    public void PR10() {
        SeleniumUtils.textIsNotPresentOnPage(driver, "Cerrar sesión");
    }

    // Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
    // sistema.
    @Test
    @Order(11)
    public void PR11() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario como admin
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que entramos como admin
        String checkText = "admin@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Comprobamos que se muestran todos los usuarios del sistema
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        int i = 2;
        int total = result.size();
        String url = "http://localhost:3000/user/list/?page=";
        while (result.size() == 5) {
            driver.navigate().to(url + i);
            result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
            total += result.size();
            i++;
        }
        Assertions.assertEquals(16, total);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
    // y dicho usuario desaparece.
    @Test
    @Order(12)
    public void PR12() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario como admin
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que entramos como admin
        String checkText = "admin@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos al listado de usuarios
        driver.navigate().to("http://localhost:3000/user/list");
        // Obtenemos el número total de usuarios que hay ahora mismo
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        // Seleccionamos el primer usuario de la lista
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/input");
        result.get(0).click();
        // Damos al botón borrar
        List<WebElement> botonBorrar = PO_View.checkElementBy(driver, "class", "btn");
        botonBorrar.get(0).click();
        // Comprobamos que el usuario user01@email.com ya no está
        checkText = "user01@email.com";
        SeleniumUtils.textIsNotPresentOnPage(driver, checkText);
        // Y que el tamaño de la lista sigue igual ya que actualiza con el anterior
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        int postSize = result.size();
        Assertions.assertEquals(5, postSize);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
    // y dicho usuario desaparece.
    @Test
    @Order(13)
    public void PR13() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario como admin
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que entramos como admin
        String checkText = "admin@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos al listado de usuarios
        driver.navigate().to("http://localhost:3000/user/list/?page=3");
        // Obtenemos el número total de usuarios que hay ahora mismo
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        int previousSize = result.size();
        // Seleccionamos el ultimo usuario de la lista (ya que el ultimo era el admin pero añadimos uno en el signup)
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/input");
        result.get(previousSize - 1).click();
        // Damos al botón borrar
        List<WebElement> botonBorrar = PO_View.checkElementBy(driver, "class", "btn");
        botonBorrar.get(0).click();
        // Comprobamos que el usuario miriam@email.com ya no está
        checkText = "user15@email.com";
        SeleniumUtils.textIsNotPresentOnPage(driver, checkText);
        // Y que el tamaño de la lista sigue igual ya que actualiza con el anterior
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        int postSize = result.size();
        Assertions.assertEquals(5, postSize);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
    // usuarios desaparecen.
    @Test
    @Order(14)
    public void PR14() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario como admin
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que entramos como admin
        String checkText = "admin@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos al listado de usuarios
        driver.navigate().to("http://localhost:3000/user/list");
        // Obtenemos el número total de usuarios que hay ahora mismo
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        // Seleccionamos los tres primeros usuarios de la lista
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/input");
        result.get(0).click();
        result.get(1).click();
        result.get(2).click();
        // Damos al botón borrar
        List<WebElement> botonBorrar = PO_View.checkElementBy(driver, "class", "btn");
        botonBorrar.get(0).click();
        // Comprobamos que los usuarios ya no están
        SeleniumUtils.textIsNotPresentOnPage(driver, "user01@email.com");
        SeleniumUtils.textIsNotPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsNotPresentOnPage(driver, "user03@email.com");
        // Y que el tamaño de la lista sigue igual ya que actualiza con el anterior
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        int postSize = result.size();
        Assertions.assertEquals(5, postSize);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Intentar borrar el usuario que se encuentra en sesión y comprobar que no ha sido borrado
    //(porque no es un usuario administrador o bien, porque, no se puede borrar a sí mismo, si está
    //autenticado) Preguntar
    @Test
    @Order(15)
    public void PR15() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario como admin
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que entramos como admin
        String checkText = "admin@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos al listado de usuarios de la ultima pagina que es donde esta admin
        driver.navigate().to("http://localhost:3000/user/list/?page=4");
        // Obtenemos el número total de usuarios que hay ahora mismo
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        // Damos al botón borrar
        List<WebElement> botonBorrar = PO_View.checkElementBy(driver, "class", "btn");
        botonBorrar.get(0).click();
        // Comprobamos que los usuarios no se han eliminado
        SeleniumUtils.textIsPresentOnPage(driver, "admin@email.com");
        // Y que el tamaño de la lista sigue igual en uno
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        int postSize = result.size();
        Assertions.assertEquals(1, postSize);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    //Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar el botón Enviar.
    //Comprobar que la oferta sale en el listado de ofertas de dicho usuario.
    @Test
    @Order(16)
    public void PR016() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user14@email.com", "user14");
        //Voy a la pagina
        driver.navigate().to("http://localhost:3000/offers/add");

        //Rellenamos el formulario
        PO_PrivateView.fillFormAddOffer(driver, "Coche rojo", "Coche de segunda mano en buen estado", 1000);

        // Cargamos la lista de ofertas
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                PO_View.getTimeout());

        int i = 2;
        int total = elements.size();
        String url = "http://localhost:3000/offers/?page=";
        while (elements.size() == 5) {
            driver.navigate().to(url + i);
            elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                    PO_View.getTimeout());
            total += elements.size();
            i++;
        }
        //Comprobamos que aparece en el listado de ofertas del usuario
        elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Coche de segunda mano en buen estado", 5);

        Assertions.assertEquals(11, total);

        Assertions.assertEquals(elements.size(), 1);
    }

    // Ir al formulario de alta de oferta, rellenarla con datos inválidos (precio negativo) y pulsar el
    // botón Enviar. Comprobar que se muestra el mensaje de campo inválido.
    @Test
    @Order(17)
    public void PR017() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user14@email.com", "user14");
        //Voy a la pagina
        driver.navigate().to("http://localhost:3000/offers/add");

        //Rellenamos el formulario
        PO_PrivateView.fillFormAddOffer(driver, "Coche rojo", "Coche de segunda mano en buen estado", -1000);

        //Comprobamos que aparece el mensaje de error
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Price must be greathet than zero", 5);

        Assertions.assertEquals(elements.size(), 1);
    }

    // Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas las que
    // existen para este usuario.
    @Test
    @Order(18)
    public void PR018() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina
        driver.navigate().to("http://localhost:3000/offers");
        //Comprobamos que aparecen todas sus ofertas
        List<WebElement> offerList = driver.findElements(By.xpath("//tbody/tr"));
        int i = 2;
        int total = 0;
        String url = "http://localhost:3000/offers?page=";
        while (!offerList.isEmpty()) {
            total += offerList.size();
            driver.navigate().to(url + i);
            offerList = driver.findElements(By.xpath("//tbody/tr"));
            i++;
        }
        Assertions.assertEquals(total, 10);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar que la lista se actualiza y
    // que la oferta desaparece.
    @Test
    @Order(19)
    public void PR019() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user14@email.com", "user14");

        // Cargamos la lista de ofertas
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                PO_View.getTimeout());

        // Eliminamos la primera oferta
        elements.get(0).click();

        int i = 2;
        int total = elements.size();
        String url = "http://localhost:3000/offers/?page=";
        while (elements.size() == 5) {
            driver.navigate().to(url + i);
            elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                    PO_View.getTimeout());
            total += elements.size();
            i++;
        }

        //Comprobamos que se ha eliminado
        Assertions.assertEquals(9, total);
    }

    // Ir a la lista de ofertas, borrar la última oferta de la lista, comprobar que la lista se actualiza y
    // que la oferta desaparece.
    @Test
    @Order(20)
    public void PR020() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user14@email.com", "user14");

        // Cargamos la lista de ofertas
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                PO_View.getTimeout());
        // Vamos al listado de usuarios
        int j = 1;
        String url2 = "http://localhost:3000/offers/?page=";
        while (elements.size() == 5) {
            driver.navigate().to(url2 + j);
            j++;
            break;
        }
        // Obtenemos el número total de usuarios que hay ahora mismo
        elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                PO_View.getTimeout());
        int previousSize = elements.size();
        // Eliminamos la ultima oferta
        elements.get(previousSize - 1).click();

        elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                PO_View.getTimeout());
        int i = 2;
        int total = elements.size();
        String url = "http://localhost:3000/offers/?page=";
        while (elements.size() == 5) {
            driver.navigate().to(url + i);
            elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                    PO_View.getTimeout());
            total += elements.size();
            i++;
        }

        //Comprobamos que se ha eliminado
        Assertions.assertEquals(9, total);
    }

    // Ir a la lista de ofertas, borrar una oferta de otro usuario, comprobar que la oferta no se
    //borra.
    @Test
    @Order(21)
    public void PR021() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user14@email.com", "user14");

        // Eliminamos la oferta del usuario 11
        driver.navigate().to("http://localhost:3000/offers/delete/645670d67f134cc55916d585");

        // Cargamos la lista de ofertas
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "No eres el dueño por lo que no tienes permisos.",
                PO_View.getTimeout());

        Assertions.assertEquals(1, elements.size());
    }

    // Ir a la lista de ofertas, borrar una oferta propia que ha sido vendida, comprobar que la
    // oferta no se borra.
    @Test
    @Order(22)
    public void PR022() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        // Cargamos la lista de ofertas
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Eliminar",
                PO_View.getTimeout());

        // Eliminamos la primera oferta que es la que esta comprada
        elements.get(0).click();

        elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "No puedes eliminarla porque esta vendida",
                PO_View.getTimeout());

        Assertions.assertEquals(1, elements.size());
    }


    // Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
    // corresponde con el listado de las ofertas existentes en el sistema
    @Test
    @Order(23)
    public void PR023() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina de buscar
        driver.navigate().to("http://localhost:3000/shop");
        //Escribo el texto a buscar
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        //Comprobamos la lista
        List<WebElement> offerList = driver.findElements(By.xpath("//tbody/tr"));
        int i = 2;
        int total = 0;
        String url = "http://localhost:3000/shop?page=";
        while (!offerList.isEmpty()) {
            total += offerList.size();
            driver.navigate().to(url + i);
            offerList = driver.findElements(By.xpath("//tbody/tr"));
            i++;
        }
        Assertions.assertEquals(total, 150);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
    // muestra la página que corresponde, con la lista de ofertas vacía.
    @Test
    @Order(24)
    public void PR024() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina de buscar
        driver.navigate().to("http://localhost:3000/shop");
        //Escribo el texto a buscar
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("Texto a buscar");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        //Comprobamos la lista
        List<WebElement> offerList = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertEquals(0, offerList.size());
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Hacer una búsqueda escribiendo en el campo un texto en minúscula o mayúscula y comprobar
    // que se muestra la página que corresponde, con la lista de ofertas que contengan dicho texto,
    // independientemente que el título esté almacenado en minúsculas o mayúscula.
    @Test
    @Order(25)
    public void PR025() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina de buscar
        driver.navigate().to("http://localhost:3000/shop");
        //Escribo el texto a buscar
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("aida");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        //Comprobamos la lista
        List<WebElement> offerList = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertEquals(1, offerList.size());
        inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("AIDA");
        elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        //Comprobamos la lista
        offerList = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertEquals(1, offerList.size());
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que
    // deja un saldo positivo en el contador del comprobador. Y comprobar que el contador se actualiza
    // correctamente en la vista del comprador.
    @Test
    @Order(26)
    public void PR026() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina de buscar
        driver.navigate().to("http://localhost:3000/shop");
        //Escribo el texto a buscar
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("SNK");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        // Comprobamos que antes tengo 100€
        String checkText = "100€";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Compramos el producto
        By enlace = By.xpath("//a[contains(@href, 'offers/buy')]");
        driver.findElement(enlace).click();
        // Comprobamos que ahora tengo 93€
        checkText = "93€";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que
    // deja un saldo 0 en el contador del comprobador. Y comprobar que el contador se actualiza
    // correctamente en la vista del comprador.
    @Test
    @Order(27)
    public void PR027() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina de buscar
        driver.navigate().to("http://localhost:3000/shop");
        //Escribo el texto a buscar
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("bio");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        // Comprobamos que antes tengo 100€
        String checkText = "100€";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Compramos el producto
        By enlace = By.xpath("//a[contains(@href, 'offers/buy')]");
        driver.findElement(enlace).click();
        // Comprobamos que ahora tengo 0€
        checkText = "0€";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Sobre una búsqueda determinada (a elección de desarrollador), intentar comprar una oferta
    // que esté por encima de saldo disponible del comprador. Y comprobar que se muestra el mensaje
    // de saldo no suficiente.
    @Test
    @Order(28)
    public void PR028() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Voy a la pagina de buscar
        driver.navigate().to("http://localhost:3000/shop");
        //Escribo el texto a buscar
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("botas");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        // Comprobamos que antes tengo 100€
        String checkText = "100€";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Intentamos comprar el producto
        By enlace = By.xpath("//a[contains(@href, 'offers/buy')]");
        driver.findElement(enlace).click();
        // Comprobamos que se muestra el mensaje de error
        checkText = "No tiene suficiente dinero para comprar la oferta";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // Ir a la opción de ofertas compradas del usuario y mostrar la lista. Comprobar que aparecen
    // las ofertas que deben aparecer.
    @Test
    @Order(29)
    public void PR029() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");
        // Vamos a ver mis compras
        driver.navigate().to("http://localhost:3000/purchases/?page=1");
        // Compruebo que el usuario tiene una oferta comprada
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        Assertions.assertEquals(elements.size(), 1);
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    //  Al crear una oferta, marcar dicha oferta como destacada y a continuación comprobar: i)
    // que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
    // actualiza adecuadamente en la vista del ofertante (comprobar saldo antes y después, que deberá
    // diferir en 20€).
    @Test
    @Order(30)
    public void PR030() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobamos que actualmente tengo 100€
        String checkText = "100€";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos a añadir oferta
        driver.navigate().to("http://localhost:3000/offers/add");
        // Marco el check de destacada
        WebElement highlight = driver.findElement(By.id("highlight"));
        highlight.click();
        // Rellenamos el formulario de añadir oferta
        PO_PrivateView.fillFormAddOffer(driver, "Aguacates", "Bolsa 5kg", 20);
        // Comprobamos que actualmente tengo 80€
        checkText = "80€";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Hago login desde otro user
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        // Voy a la tienda
        driver.navigate().to("http://localhost:3000/shop");
        // Comprobamos que la primera es la destacada que se acaba de añadir
        List<WebElement> offerList = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertTrue(offerList.get(0).getText().contains("Aguacates"));
    }

    // Sobre el listado de ofertas de un usuario con más de 20 euros de saldo, pinchar en el enlace
    // Destacada y a continuación comprobar: i) que aparece en el listado de ofertas destacadas para los
    // usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (comprobar
    // saldo antes y después, que deberá diferir en 20€ ).
    @Test
    @Order(31)
    public void PR031() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobamos que actualmente tengo 100€
        String checkText = "100€";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos a nuestras ofertas
        driver.navigate().to("http://localhost:3000/offers");
        // Pinchamos en la oferta
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a");
        result.get(3).click();
        // Comprobamos que actualmente tengo 80€
        checkText = "80€";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        // Hago login desde otro user
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        // Voy a la tienda
        driver.navigate().to("http://localhost:3000/shop");
        // Comprobamos que la primera es la destacada que se acaba de añadir
        List<WebElement> offerList = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertTrue(offerList.get(0).getText().contains("Arena Blanca"));
        // Hacemos logout
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo, pinchar en el
    // enlace Destacada y a continuación comprobar que se muestra el mensaje de saldo no suficiente.
    @Test
    @Order(32)
    public void PR032() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        // Ir a tienda y comprar "Air Frier", que le dejara un saldo de 11€
        driver.navigate().to("http://localhost:3000/shop");
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys("air frier");
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Buscar",
                PO_View.getTimeout());
        elements.get(0).click();
        // Compramos el producto
        By enlace = By.xpath("//a[contains(@href, 'offers/buy')]");
        driver.findElement(enlace).click();
        // Comprobamos que actualmente tengo 11€
        String checkText = "11€";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Vamos a nuestras ofertas
        driver.navigate().to("http://localhost:3000/offers");
        // Intentamos destacar una oferta
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a");
        result.get(1).click();
        // Comprobamos que se muestra el mensaje de error
        checkText = "No tiene suficiente dinero para destacar la oferta";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(33)
    public void PR033() {
        driver.navigate().to("http://localhost:3000/user/list");
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(34)
    public void PR034() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=conversations");
        String checkText = "Email:";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(35)
    public void PR035() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Comprobamos que es el usuario el que se logueo
        String checkText = "user01@email.com";
        driver.navigate().to("http://localhost:3000/user/list");
        checkText = "No se tienen permisos";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(36)
    public void PR036() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que es el usuario el que se logueo
        String checkText = "user01@email.com";
        driver.navigate().to("http://localhost:3000/logs");
        List<WebElement> logs = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertTrue(logs.size() > 0);
    }

    @Test
    @Order(37)
    public void PR037() {
        // Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        // Comprobamos que es el usuario el que se logueo
        String checkText = "user01@email.com";
        driver.navigate().to("http://localhost:3000/logs");
        List<WebElement> logs = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertTrue(logs.size() > 0);
        List<WebElement> botonBorrar = PO_View.checkElementBy(driver, "class", "btn");
        botonBorrar.get(0).click();
        logs = driver.findElements(By.xpath("//tbody/tr"));
        Assertions.assertTrue(logs.size() == 0);
    }

    /*    *//* Ejemplos de pruebas de llamada a una API-REST *//*
     *//* ---- Probamos a obtener lista de canciones sin token ---- *//*
    @Test
    @Order(37)
    public void PR37() {
        final String RestAssuredURL = "http://localhost:8081/api/v1.0/songs";
        Response response = RestAssured.get(RestAssuredURL);
        Assertions.assertEquals(403, response.getStatusCode());
    }

    @Test
    @Order(38)
    public void PR38() {
        final String RestAssuredURL = "http://localhost:8081/api/v1.0/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "prueba1@prueba1.com");
        requestParams.put("password", "prueba1");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());
    }
    */

    // Inicio de sesión con datos válidos
    @Test
    @Order(38)
    public void PR38() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user01");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertNotNull(response.getBody().jsonPath().get("token"));
        Assertions.assertEquals("usuario autorizado", response.getBody().jsonPath().get("message"));
    }

    // Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta)
    @Test
    @Order(39)
    public void PR39() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user05");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(401, response.getStatusCode());
        Assertions.assertNull(response.getBody().jsonPath().get("token"));
        Assertions.assertEquals("usuario no autorizado", response.getBody().jsonPath().get("message"));
    }

    // Inicio de sesión con datos válidos (campo email o contraseña vacíos)
    @Test
    @Order(40)
    public void PR40() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "");
        requestParams.put("password", "user05");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(401, response.getStatusCode());
        Assertions.assertNull(response.getBody().jsonPath().get("token"));
        Assertions.assertEquals("usuario no autorizado", response.getBody().jsonPath().get("message"));
    }

    // Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas las que
    // existen para este usuario. Esta prueba implica invocar a dos servicios: S1 y S2.
    @Test
    @Order(41)
    public void PR41() {
        // Hacemos login
        String token = API_Rest_Utils.login("user01@email.com", "user01");

        //1. Definimos la URL del servicio
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/offers";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        request.header("token", token);
        //3. Hacemos la petición
        Response response = request.get(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(140, response.getBody().jsonPath().getList("offers").size());
    }

    @Test
    @Order(42)
    public void PR042() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParamsLogin = new JSONObject();
        requestParamsLogin.put("email", "user01@email.com");
        requestParamsLogin.put("password", "user01");
        request.header("Content-Type", "application/json");
        request.body(requestParamsLogin.toJSONString());
        //Hacemos la petición
        Response response = request.post(RestAssuredURL);

        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());

        final String RestAssuredURLOffers = "http://localhost:3000/api/v1.0/offers";
        RequestSpecification requestOffers = RestAssured.given();
        requestOffers.header("token", response.asString().split("\"")[9]);
        String responseOffers = requestOffers.get(RestAssuredURLOffers).getBody().asString().split("\"")[5];

        final String RestAssuredURLConversations = "http://localhost:3000/api/v1.0/conversations";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request2 = RestAssured.given();
        request2.header("token", response.asString().split("\"")[9]);
        JSONObject requestParams = new JSONObject();
        requestParams.put("offerId", responseOffers);
        requestParams.put("buyer", "user01@email.com");
        requestParams.put("seller", "user02@email.com");
        requestParams.put("messages", "[]");
        request2.header("Content-Type", "application/json");
        request2.body(requestParams.toJSONString());
        //Hacemos la petición
        Response response2 = request2.post(RestAssuredURLConversations);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(201, response2.getStatusCode());

        String idConversation = response2.getBody().asString().split("\\\\")[5].replace("\"", "");
        final String RestAssuredURLMessages = "http://localhost:3000/api/v1.0/conversations/" + idConversation;
        RequestSpecification requestMessages = RestAssured.given();
        requestMessages.header("token", response.asString().split("\"")[9]);
        JSONObject requestParamsMessages = new JSONObject();
        requestParamsMessages.put("message", "Holaaa");
        requestMessages.header("Content-Type", "application/json");
        requestMessages.body(requestParamsMessages.toJSONString());
        //Hacemos la petición
        Response responseMessages = requestMessages.post(RestAssuredURLMessages);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responseMessages.getStatusCode());

        final String RestAssuredURLConversationsGet = "http://localhost:3000/api/v1.0/conversations/" + idConversation;
        //Preparamos el parámetro en formato JSON
        RequestSpecification requestGet = RestAssured.given();
        requestGet.header("token", response.asString().split("\"")[9]);
        //Hacemos la petición
        Response responseGet = request2.get(RestAssuredURLConversationsGet);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responseGet.getStatusCode());
        String jsonResponse = responseGet.getBody().asString();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONArray messagesArray = (JSONArray) jsonObject.get("messages");
            int messagesArraySize = messagesArray.size();
            Assertions.assertEquals(1, messagesArraySize);
        } catch (ParseException e) {
            // Manejar la excepción de análisis de JSON
            e.printStackTrace();
        }

    }

    @Test
    @Order(43)
    public void PR043() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        // Preparamos el parámetro en formato JSON para el usuario user02
        RequestSpecification requestUser02 = RestAssured.given();
        JSONObject requestParamsLoginUser02 = new JSONObject();
        requestParamsLoginUser02.put("email", "user02@email.com");
        requestParamsLoginUser02.put("password", "user02");
        requestUser02.header("Content-Type", "application/json");
        requestUser02.body(requestParamsLoginUser02.toJSONString());
        // Hacemos la petición para obtener el token del usuario user02
        Response responseUser02 = requestUser02.post(RestAssuredURL);

        // Comprobamos que el servicio ha tenido éxito
        Assertions.assertEquals(200, responseUser02.getStatusCode());

        final String RestAssuredURLOffers = "http://localhost:3000/api/v1.0/offers";
        RequestSpecification requestOffers = RestAssured.given();
        requestOffers.header("token", responseUser02.asString().split("\"")[9]);
        String responseOffers = requestOffers.get(RestAssuredURLOffers).getBody().asString().split("\"")[5];

        final String RestAssuredURLConversations = "http://localhost:3000/api/v1.0/conversations";
        // Preparamos el parámetro en formato JSON para el usuario user01
        RequestSpecification requestUser01 = RestAssured.given();
        requestUser01.header("token", responseUser02.asString().split("\"")[9]); // Usamos el token del usuario user02
        JSONObject requestParamsLoginUser01 = new JSONObject();
        requestParamsLoginUser01.put("email", "user01@email.com");
        requestParamsLoginUser01.put("password", "user01");
        requestUser01.header("Content-Type", "application/json");
        requestUser01.body(requestParamsLoginUser01.toJSONString());
        // Hacemos la petición para obtener el token del usuario user01
        Response responseUser01 = requestUser01.post(RestAssuredURL);

        // Comprobamos que el servicio ha tenido éxito
        Assertions.assertEquals(200, responseUser01.getStatusCode());

        // Obtenemos el token del usuario user01
        String tokenUser01 = responseUser01.asString().split("\"")[9];

        // Preparamos la solicitud para crear la conversación con el usuario user01
        RequestSpecification requestCreateConversation = RestAssured.given();
        requestCreateConversation.header("token", tokenUser01);
        JSONObject requestParams = new JSONObject();
        requestParams.put("offerId", responseOffers);
        requestParams.put("buyer", "user01");
        requestParams.put("seller", "user01");
        requestParams.put("messages", "[]");
        requestCreateConversation.header("Content-Type", "application/json");
        requestCreateConversation.body(requestParams.toJSONString());
        // Hacemos la petición para crear la conversación
        Response responseCreateConversation = requestCreateConversation.post(RestAssuredURLConversations);

        // Comprobamos que el servicio ha devuelto el código de error 403
        Assertions.assertEquals(403, responseCreateConversation.getStatusCode());
    }

    @Test
    @Order(44)
    public void PR044() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParamsLogin = new JSONObject();
        requestParamsLogin.put("email", "user01@email.com");
        requestParamsLogin.put("password", "user01");
        request.header("Content-Type", "application/json");
        request.body(requestParamsLogin.toJSONString());
        //Hacemos la petición
        Response response = request.post(RestAssuredURL);

        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());

        final String RestAssuredURLOffers = "http://localhost:3000/api/v1.0/offers";
        RequestSpecification requestOffers = RestAssured.given();
        requestOffers.header("token", response.asString().split("\"")[9]);
        String responseOffers = requestOffers.get(RestAssuredURLOffers).getBody().asString().split("\"")[5];

        final String RestAssuredURLConversations = "http://localhost:3000/api/v1.0/conversations";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request2 = RestAssured.given();
        request2.header("token", response.asString().split("\"")[9]);
        JSONObject requestParams = new JSONObject();
        requestParams.put("offerId", responseOffers);
        requestParams.put("buyer", "user01@email.com");
        requestParams.put("seller", "user02@email.com");
        requestParams.put("messages", "[]");
        request2.header("Content-Type", "application/json");
        request2.body(requestParams.toJSONString());
        //Hacemos la petición
        Response response2 = request2.post(RestAssuredURLConversations);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(201, response2.getStatusCode());

        String idConversation = response2.getBody().asString().split("\\\\")[5].replace("\"", "");
        final String RestAssuredURLMessages = "http://localhost:3000/api/v1.0/conversations/" + idConversation;
        RequestSpecification requestMessages = RestAssured.given();
        requestMessages.header("token", response.asString().split("\"")[9]);
        JSONObject requestParamsMessages = new JSONObject();
        requestParamsMessages.put("message", "Holaaa");
        requestMessages.header("Content-Type", "application/json");
        requestMessages.body(requestParamsMessages.toJSONString());
        //Hacemos la petición
        Response responseMessages = requestMessages.post(RestAssuredURLMessages);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responseMessages.getStatusCode());

        requestMessages = RestAssured.given();
        requestMessages.header("token", response.asString().split("\"")[9]);
        requestParamsMessages = new JSONObject();
        requestParamsMessages.put("message", "Holaaa");
        requestMessages.header("Content-Type", "application/json");
        requestMessages.body(requestParamsMessages.toJSONString());
        //Hacemos la petición
        responseMessages = requestMessages.post(RestAssuredURLMessages);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responseMessages.getStatusCode());

        final String RestAssuredURLConversationsGet = "http://localhost:3000/api/v1.0/conversations/" + idConversation;
        //Preparamos el parámetro en formato JSON
        RequestSpecification requestGet = RestAssured.given();
        requestGet.header("token", response.asString().split("\"")[9]);
        //Hacemos la petición
        Response responseGet = request2.get(RestAssuredURLConversationsGet);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responseGet.getStatusCode());
        String jsonResponse = responseGet.getBody().asString();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONArray messagesArray = (JSONArray) jsonObject.get("messages");
            int messagesArraySize = messagesArray.size();
            Assertions.assertEquals(2, messagesArraySize);
        } catch (ParseException e) {
            // Manejar la excepción de análisis de JSON
            e.printStackTrace();
        }

    }

    @Test
    @Order(45)
    public void PR45() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user01");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //Hacemos la petición
        Response response = request.post(RestAssuredURL);

        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());

        final String RestAssuredURLConversations = "http://localhost:3000/api/v1.0/conversations";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request2 = RestAssured.given();
        request2.header("token", response.asString().split("\"")[9]);
        //Hacemos la petición
        Response response2 = request2.get(RestAssuredURLConversations);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response2.getStatusCode());
    }

    @Test
    @Order(46)
    public void PR46() {
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //Preparamos el parámetro en formato JSON
        RequestSpecification requestlogin = RestAssured.given();
        JSONObject requestParamslogin = new JSONObject();
        requestParamslogin.put("email", "user01@email.com");
        requestParamslogin.put("password", "user01");
        requestlogin.header("Content-Type", "application/json");
        requestlogin.body(requestParamslogin.toJSONString());
        //Hacemos la petición
        Response responselogin = requestlogin.post(RestAssuredURL);

        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responselogin.getStatusCode());

        final String RestAssuredURLOffers = "http://localhost:3000/api/v1.0/offers";
        RequestSpecification requestOffers = RestAssured.given();
        requestOffers.header("token", responselogin.asString().split("\"")[9]);
        String responseOffers = requestOffers.get(RestAssuredURLOffers).getBody().asString().split("\"")[5];

        final String RestAssuredURLConversationsAdd = "http://localhost:3000/api/v1.0/conversations";
        //Preparamos el parámetro en formato JSON
        RequestSpecification request2 = RestAssured.given();
        request2.header("token", responselogin.asString().split("\"")[9]);
        JSONObject requestParams = new JSONObject();
        requestParams.put("offerId", responseOffers);
        requestParams.put("buyer", "user01@email.com");
        requestParams.put("seller", "user02@email.com");
        requestParams.put("messages", "[]");
        request2.header("Content-Type", "application/json");
        request2.body(requestParams.toJSONString());
        //Hacemos la petición
        Response response2 = request2.post(RestAssuredURLConversationsAdd);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(201, response2.getStatusCode());

        final String aux = "http://localhost:3000/api/v1.0/conversations";
        RequestSpecification requestconvlist = RestAssured.given();
        requestconvlist.header("token", responselogin.asString().split("\"")[9]);
        //Hacemos la petición
        Response responseconvlist = requestconvlist.get(aux);

        var conversation = responseconvlist.asString().split("\"")[5];
        final String RestAssuredURLDeleteConversations = "http://localhost:3000/api/v1.0/conversations/delete/" + conversation;
        RequestSpecification requestconvdel = RestAssured.given();
        requestconvdel.header("token", responselogin.asString().split("\"")[9]);
        //Hacemos la petición
        Response responseconvdel = requestconvdel.get(RestAssuredURLDeleteConversations);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, responseconvdel.getStatusCode());

        final String RestAssuredURLConversations = "http://localhost:3000/api/v1.0/conversations";
        RequestSpecification requestconvlist2 = RestAssured.given();
        requestconvlist2.header("token", responselogin.asString().split("\"")[9]);
        //Hacemos la petición
        Response responseconvlist2 = requestconvlist.get(RestAssuredURLConversations);
        //Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(false, responseconvlist2.asString().contains(conversation));
    }

    @Test
    @Order(48)
    public void PR048() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
    }

    @Test
    @Order(49)
    public void PR049() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "sadfsadfasdf");
        WebDriverWait wait = new WebDriverWait(driver, 1);
        List<WebElement> element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("alert-danger")));

        Assertions.assertTrue(element.size() == 1);

        String expectedText = "Usuario no encontrado";
        String actualText = element.get(0).getText();
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    @Order(50)
    public void PR050() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "");
        WebDriverWait wait = new WebDriverWait(driver, 1);
        List<WebElement> element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("alert-danger")));

        Assertions.assertTrue(element.size() == 1);

        String expectedText = "Usuario no encontrado";
        String actualText = element.get(0).getText();
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    @Order(51)
    public void PR051() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        Assertions.assertEquals(140, result.size());
    }

    @Test
    @Order(52)
    public void PR052() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var firstElement = result.get(0);
        firstElement.click();
        PO_ChatView.sendMessage(driver, "Hola, ¿Qué tal?");
        List<WebElement> messages = driver.findElements(By.className("message"));
        Assertions.assertTrue(messages.size() == 1);
    }

    @Test
    @Order(53)
    public void PR053() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var firstElement = result.get(0);
        firstElement.click();
        PO_ChatView.sendMessage(driver, "Hola, ¿Qué tal?");
        PO_ChatView.sendMessage(driver, "Me interesa la oferta, ¿crees que podríamos negociarla?");

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=login");
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=conversations");
        List<WebElement> result2 = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'Open Chat')]");
        var firstElement2 = result2.get(0);
        firstElement2.click();
        PO_ChatView.sendMessage(driver, "Hola, buenas tardes, ¿Cuánto estás dispuesto a ofrecer?");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        List<WebElement> messages = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("message")));
        Assertions.assertTrue(messages.size() == 3);
    }

    @Test
    @Order(54)
    public void PR054() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        List<WebElement> resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var firstElement = resultNewChat.get(0);
        firstElement.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=conversations");
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr");
        Assertions.assertTrue(!result.isEmpty());
    }

    @Test
    @Order(55)
    public void PR055() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        List<WebElement> resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var firstElementOfferList = resultNewChat.get(0);
        firstElementOfferList.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");

        resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var ElementOfferList = resultNewChat.get(1);
        ElementOfferList.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");

        resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        ElementOfferList = resultNewChat.get(2);
        ElementOfferList.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=conversations");
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'Close Chat')]");
        var size = result.size();
        var firstElement = result.get(0);

        firstElement.click();
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'Close Chat')]");
        Assertions.assertEquals(size - 1, result.size());

    }

    @Test
    @Order(56)
    public void PR056() {
        driver.navigate().to("http://localhost:3000/apiclient/client.html");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        List<WebElement> resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var firstElementOfferList = resultNewChat.get(0);
        firstElementOfferList.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");

        resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        var ElementOfferList = resultNewChat.get(1);
        ElementOfferList.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=offers");

        resultNewChat = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'New Chat')]");
        ElementOfferList = resultNewChat.get(2);
        ElementOfferList.click();

        driver.navigate().to("http://localhost:3000/apiclient/client.html?w=conversations");
        List<WebElement> result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'Close Chat')]");
        var size = result.size();
        var lastElement = result.get(result.size() - 1);

        lastElement.click();
        result = PO_View.checkElementBy(driver, "free", "//table/tbody/tr/td/a[contains(text(),'Close Chat')]");
        Assertions.assertEquals(size - 1, result.size());

    }

}
