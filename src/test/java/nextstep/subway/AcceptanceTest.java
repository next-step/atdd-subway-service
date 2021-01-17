package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    public ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        ExtractableResponse<Response> responseExtractableResponse = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
        return responseExtractableResponse;
    }

    public ExtractableResponse<Response> 내_정보_수정_요청(String accessToken, MemberRequest memberRequest) {
        ExtractableResponse<Response> responseExtractableResponse = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .auth().oauth2(accessToken)
                .when().put("/members/me")
                .then().log().all()
                .extract();
        return responseExtractableResponse;
    }

    public ExtractableResponse<Response> 내_정보_삭제_요청(String accessToken) {
        ExtractableResponse<Response> responseExtractableResponse = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
        return responseExtractableResponse;
    }

    public ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);
        ExtractableResponse<Response> responseExtractableResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        return responseExtractableResponse;
    }
}
