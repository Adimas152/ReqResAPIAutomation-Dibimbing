package tests.auth;

import body.auth.LoginBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.FileWriter;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class LoginTest {

    @Test
    public void login() throws IOException {
        // Set base URI dari ConfigReader
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");

        // Buat body login
        LoginBody loginBody = new LoginBody();

        // Kirim request POST ke endpoint login REST
        Response response = given()
                .header("Content-Type", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .body(loginBody.loginData().toString())
                .when()
                .post("/api/login") // endpoint login REST
                .then()
                .statusCode(200)
                .extract().response();

        // Print response
        System.out.println("Response: " + response.asString());

        // Validasi token di response body
        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token should not be null");
        Assert.assertFalse(token.isEmpty(), "Token should not be empty");
        System.out.println("Token: " + token);

//        // Simpan token ke file JSON (sessionToken dihapus)
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", token);

        try (FileWriter file = new FileWriter("src/resources/json/token.json")) {
            file.write(tokenJson.toString(4)); // 4 = indentation
            file.flush();
        }

        System.out.println("Token berhasil disimpan di resources/json/token.json");
    }
}
