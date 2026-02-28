package base;

import io.restassured.RestAssured;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

import java.io.FileReader;
import java.io.IOException;

public class BaseTest {

    protected String token; // deklarasi token sebagai field class

    @BeforeClass
    public void setup() throws IOException {
        // Set base URI
        String baseUrl = ConfigReader.getProperty("baseUrl");
        RestAssured.baseURI = baseUrl;

        // Baca token dari file JSON
        JSONObject tokenJson = new JSONObject(new FileReader("src/resources/json/token.json"));
        token = tokenJson.getString("token");

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token kosong. Jalankan LoginTest dulu.");
        }

        System.out.println("Token berhasil dibaca: " + token);
    }
}
