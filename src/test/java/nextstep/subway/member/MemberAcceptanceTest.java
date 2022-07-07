package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("회원 관리 기능")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String MEMBER_ME_URL = "/members/me";

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int NEW_AGE = 21;

    private ExtractableResponse<Response> 회원_생성_결과;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        // when
        회원_생성_결과 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(회원_생성_결과);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(회원_생성_결과);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        MemberRequest 수정될_회원정보 = 회원정보_요청_생성(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(회원_생성_결과, 수정될_회원정보);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(회원_생성_결과);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        String accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(accessToken);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        MemberRequest 수정될_회원정보 = 회원정보_요청_생성(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(accessToken, 수정될_회원정보);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(accessToken);
        // then
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response,
                                                            MemberRequest memberRequest) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(MEMBER_ME_URL)
                .then().log().all()
                .extract();
    }

    public static MemberRequest 회원정보_요청_생성(String email, String password, int age) {
        return new MemberRequest(email, password, age);
    }

    private static ExtractableResponse<Response> 내_정보_수정_요청(String accessToken, MemberRequest memberRequest) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(MEMBER_ME_URL)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 내_정보_삭제_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(MEMBER_ME_URL)
                .then().log().all()
                .extract();
    }
}
