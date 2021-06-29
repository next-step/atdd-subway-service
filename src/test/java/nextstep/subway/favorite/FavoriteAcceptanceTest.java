package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {


    private StationResponse 강남역;
    private StationResponse 광교역;
    private TokenResponse 사용자토큰;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String LOCATION = "Location";
    private static final String BASE_URI = "/favorites";
    private static final int AGE = 20;

    @BeforeEach
    void before() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("신분당선", "빨간색", 강남역.getId(), 광교역.getId(), 10));

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자토큰 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }


    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {

        ExtractableResponse<Response> 생성응답 = 즐겨찾기_생성_요청(사용자토큰, 강남역, 광교역);
        즐겨찾기_생성_성공(생성응답);

        ExtractableResponse<Response> 조회응답 = 즐겨찾기_조회_요청(사용자토큰);
        즐겨찾기_조회_성공(조회응답);

        ExtractableResponse<Response> 삭제응답 = 즐겨찾기_삭제_요청(사용자토큰, 생성응답);
        즐겨찾기_삭제_성공(삭제응답);

    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId().toString(), target.getId().toString());

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post(BASE_URI)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(BASE_URI)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header(LOCATION);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}