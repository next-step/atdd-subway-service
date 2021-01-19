package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인으로_토큰_발급_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static StationResponse 강남역;
    public static StationResponse 광교역;
    public String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_되어있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        //when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, new FavoriteRequest(강남역.getId(), 광교역.getId()));

        //then
        즐겨찾기_생성됨(createResponse);

        //when
        ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(accessToken);

        //then
        즐겨찾기_목록_응답됨(listResponse);
        즐겨찾기_목록_포함됨(listResponse);

        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, accessToken);
        //then
        즐겨찾기_삭제됨(deleteResponse);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> listResponse) {
        FavoriteResponse[] results = listResponse.body().as(FavoriteResponse[].class);
        assertThat(results[0].getSource()).isEqualTo(강남역);
        assertThat(results[0].getTarget()).isEqualTo(광교역);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static String 로그인_되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인으로_토큰_발급_요청(email, password);
        return response.body().as(TokenResponse.class).getAccessToken();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}