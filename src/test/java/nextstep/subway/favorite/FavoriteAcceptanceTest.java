package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.step.LineAcceptanceStep;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.step.MemberAcceptanceStep;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceStep.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        MemberAcceptanceStep.회원_등록되어_있음("mkkim90@email.com", "password", 32);
        TokenResponse tokenResponse = AuthAcceptanceTest.로그인_되어있음("mkkim90@email.com", "password").as(TokenResponse.class);
        accessToken = tokenResponse.getAccessToken();
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // when
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, favoriteRequest);

        // then
        즐겨찾기_생성됨(response, HttpStatus.CREATED);
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findALlFavorites() {
        // when
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        즐겨찾기_생성_요청(accessToken, favoriteRequest);
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(강남역.getId(), 광교역.getId());
        즐겨찾기_생성_요청(accessToken, favoriteRequest2);

        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        즐겨찾기_목록_조회됨(response, HttpStatus.OK);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, favoriteRequest);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, createResponse);

        // then
        즐겨찾기_삭제됨(response, HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken,FavoriteRequest favoriteRequest) {
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response, HttpStatus created) {
        Assertions.assertThat(response.statusCode()).isEqualTo(created.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, HttpStatus ok) {
        Assertions.assertThat(response.statusCode()).isEqualTo(ok.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String url = response.header("location");
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url)
                .then().log().all().extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response, HttpStatus noContent) {
        Assertions.assertThat(response.statusCode()).isEqualTo(noContent.value());
    }
}