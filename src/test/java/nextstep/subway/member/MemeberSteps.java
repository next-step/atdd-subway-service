package nextstep.subway.member;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MemeberSteps {

    public static void 회원_등록_되어_있음(String email, String password, int age) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/members")
                .then().log().all()
                .extract();
    }
}
