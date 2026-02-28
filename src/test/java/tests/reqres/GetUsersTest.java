package tests.reqres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetUsersTest {

    private static final String GET_USERS_ENDPOINT = "/api/users";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");
    }

    @Test
    public void getUsersByPageTest() {

        String pageParam = ConfigReader.getProperty("pageParam");
        String perPageExpected = ConfigReader.getProperty("perPageParam");

        Response response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .queryParam("page", pageParam)
                .when()
                .get(GET_USERS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();

        // Status Code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code bukan 200");

        System.out.println("\n========== RESPONSE SUMMARY ==========");
        System.out.println("Status Code  : " + response.getStatusCode());
        System.out.println("Page         : " + response.jsonPath().getInt("page"));
        System.out.println("Per Page     : " + response.jsonPath().getInt("per_page"));
        System.out.println("Total Data   : " + response.jsonPath().getList("data").size());
        System.out.println("======================================\n");

        // Page validation
        int actualPage = response.jsonPath().getInt("page");
        Assert.assertEquals(String.valueOf(actualPage), pageParam, "Page tidak sesuai");

        // per_page validation
        int actualPerPage = response.jsonPath().getInt("per_page");
        Assert.assertEquals(String.valueOf(actualPerPage), perPageExpected, "per_page tidak sesuai");

        // data length validation
        List<Map<String, Object>> dataList = response.jsonPath().getList("data");
        Assert.assertEquals(String.valueOf(dataList.size()), perPageExpected,
                "Jumlah data tidak sesuai dengan per_page");

        System.out.println("Sample Users:");
        dataList.forEach(user ->
                System.out.println(
                        "ID: " + user.get("id") +
                        " | Name: " + user.get("first_name") + " " + user.get("last_name") +
                        " | Avatar: " + user.get("avatar")
                )
        );

        // Minimal satu user punya last_name dan avatar
        boolean hasValidUser = dataList.stream()
                .anyMatch(user ->
                        user.get("last_name") != null &&
                                user.get("avatar") != null
                );

        Assert.assertTrue(hasValidUser,
                "Tidak ada user yang memiliki last_name dan avatar");
    }
}