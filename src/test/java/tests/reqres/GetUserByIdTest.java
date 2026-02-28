package tests.reqres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GetUserByIdTest {

    @BeforeClass
    public void setup() throws Exception {
        // Set base URI dari config.properties
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");
    }

    @Test
    public void getUserByIdTest() throws IOException {
        String userId = ConfigReader.getProperty("idUser");

        Response response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .pathParam("userId", userId)
                .when()
                .get("/api/users/{userId}")
                .then()
                .log().all()
                .extract().response();

        // Print readable response summary
        System.out.println("\n========== GET USER BY ID ==========");
        System.out.println("User ID      : " + userId);
        System.out.println("Status Code  : " + response.getStatusCode());
        System.out.println("ID           : " + response.jsonPath().getString("data.id"));
        System.out.println("Email        : " + response.jsonPath().getString("data.email"));
        System.out.println("First Name   : " + response.jsonPath().getString("data.first_name"));
        System.out.println("Last Name    : " + response.jsonPath().getString("data.last_name"));
        System.out.println("Avatar       : " + response.jsonPath().getString("data.avatar"));
        System.out.println("====================================\n");

        // Validasi status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Status code bukan 200! Response: " + response.asString());

        // Validasi result.id sesuai
        String id = response.jsonPath().getString("data.id");
        Assert.assertEquals(id, userId, "Id user tidak sesuai");

        String data = response.jsonPath().getString("data.email");
        Assert.assertTrue(data.contains("reqres.in"), "Data email harus mengandung 'reqres.in'");

    }

    @Test
    public void getUserByIdEmptyTest() throws IOException {
        String userIdNegative = ConfigReader.getProperty("idUserNegative");

        Response response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .pathParam("userId", userIdNegative)
                .when()
                .get("/api/users/{userId}")
                .then()
                .log().all()
                .extract().response();

        // Print readable response summary
        System.out.println("\n========== GET USER BY ID (NOT FOUND) ==========");
        System.out.println("User ID      : " + userIdNegative);
        System.out.println("Status Code  : " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("===============================================\n");

        // Validasi status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404, "Status code bukan 404! Response: " + response.asString());

        // Validasi result
        String body = response.getBody().asString();
        Assert.assertEquals(body.trim(), "{}", "Body harus empty");


    }
}
