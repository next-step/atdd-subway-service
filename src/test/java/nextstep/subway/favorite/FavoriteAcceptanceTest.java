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

import java.util.Arrays;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "wjdals300@email.com";
    public static final String PASSWORD = "1234";
    public static final int AGE = 32;
    StationResponse 강남역;
    StationResponse 광교역;
    TokenResponse 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 지하철역 등록되어 있음
        ExtractableResponse<Response> 강남역response = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> 광교역response = 지하철역_등록되어_있음("광교역");
        ExtractableResponse<Response> stationsResponse = 지하철역_목록_조회_요청();
        지하철역_목록_포함됨(stationsResponse, Arrays.asList(강남역response, 광교역response));

        강남역 = 강남역response.as(StationResponse.class);
        광교역 = 광교역response.as(StationResponse.class);

        // 지하철 노선 등록되어 있음
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        ExtractableResponse<Response> lineCreateResponse = 지하철_노선_등록되어_있음(신분당선);

        // 지하철 노선에 지하철역 등록되어 있음
        ExtractableResponse<Response> lineStationsResponse = 지하철_노선_목록_조회_요청(lineCreateResponse);
        지하철_노선_응답됨(lineStationsResponse);

        // 회원 등록되어 있음
        ExtractableResponse<Response> memberCreateResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(memberCreateResponse);

        // 로그인 되어있음
        ExtractableResponse<Response> loginResponse = 회원_로그인_요청(EMAIL, PASSWORD);
        토큰 = loginResponse.as(TokenResponse.class);
        회원_로그인_됨(loginResponse);
    }

    @DisplayName("나의 즐겨찾기를 관리한다.")
    @Test
    void manageMyFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(토큰.getAccessToken(), 강남역.getId(), 광교역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> updateResponse = 즐겨찾기_목록_조회_요청(토큰.getAccessToken());
        // then
        즐겨찾기_목록_조회됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(토큰.getAccessToken(), createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.CREATED.value()).isEqualTo(response.statusCode());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.NO_CONTENT.value()).isEqualTo(response.statusCode());
    }
}