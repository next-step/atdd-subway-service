package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private String 로그인_토큰;

    @BeforeEach
    void setup() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> 로그인_요청_응답 = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD);
        TokenResponse tokenResponse = 로그인_요청_응답.as(TokenResponse.class);
        로그인_토큰 = tokenResponse.getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리 한다")
    @Test
    void 즐겨찾기를_관리_한다() {
        // when
        ExtractableResponse<Response> 즐겨찾기_목록_생성_응답 = 즐겨찾기_목록_생성_요청(로그인_토큰, 강남역, 양재역);

        // then
        즐겨찾기_목록_생성_됨(즐겨찾기_목록_생성_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(로그인_토큰);

        // then
        즐겨찾기_목록_조회_됨(즐겨찾기_목록_조회_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(즐겨찾기_목록_생성_응답, 로그인_토큰);

        // then
        즐겨찾기_삭제_됨(즐겨찾기_삭제_응답);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_생성_요청(String accessToken, StationResponse source, StationResponse target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source.getId());
        params.put("target", target.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, String accessToken) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
