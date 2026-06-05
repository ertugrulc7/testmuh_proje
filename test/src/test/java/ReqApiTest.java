import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqApiTest {

    // Testlerden önce BİR KEZ çalışır. Base URL ve API Key'i tüm testler için tanımlar.
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";

        // Şifreyi (API Key) her isteğe otomatik eklemesi için varsayılan bir özellik (Spec) oluşturuyoruz
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("x-api-key", "pub_ae67af2b7d766783c7a374a251d96ac515cd14bc18248f8122bab57d19e34ff1")
                .build();
    }

    @Test
    @DisplayName("GET İsteği: Belirli bir kullanıcıyı getirme ve kontrol etme")
    public void testGetUser() {
        given()
                .log().all()
                .when()
                .get("/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .time(Matchers.lessThan(5000L))
                .body("data.id", equalTo(2))
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"));
    }

    @Test
    @DisplayName("POST İsteği: Request Body ile yeni kullanıcı oluşturma")
    public void testCreateUser() {
        String requestBody = """
                {
                    "name": "Ahmet",
                    "job": "Test Engineer"
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().all()
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .time(Matchers.lessThan(5000L))
                .body("name", equalTo("Ahmet"))
                .body("job", equalTo("Test Engineer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }
}