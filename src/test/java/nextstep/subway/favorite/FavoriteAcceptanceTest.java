package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";


    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private TokenResponse 로그인된_회원;

    @BeforeEach
    public void setUp() {
        super.setUp();
        /*
        Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
        And 회원 등록되어 있음
        And 로그인 되어있음
        */
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역,10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 교대역, 남부터미널역,5);

        AuthAcceptanceTest.회원이_등록되어_있음(EMAIL, PASSWORD, 20);

        로그인된_회원 = AuthAcceptanceTest.로그인_되어_있음(EMAIL, PASSWORD);
    }

/*
Scenario: 즐겨찾기를 관리
        When 즐겨찾기 생성을 요청 (2건)
        Then 즐겨찾기 생성됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회됨
        When 즐겨찾기 삭제할 즐겨찾기 조회
        Then 즐겨찾기 즐겨찾기가 조회 됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제됨
        WHEN 삭제된 즐겨 찾기 조회
        Then 삭제된 즐겨 찾기 조회되지 않음
*/
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        //when
        final ExtractableResponse<Response> createFavorite = 즐겨찾기_생성_요청(로그인된_회원, new FavoriteRequest(강남역.getId(), 양재역.getId()));
        final ExtractableResponse<Response> createFavorite2 = 즐겨찾기_생성_요청(로그인된_회원, new FavoriteRequest(강남역.getId(), 교대역.getId()));

        //then
        즐겨찾기가_생성됨(createFavorite);
        즐겨찾기가_생성됨(createFavorite2);

        //when
        final ExtractableResponse<Response> retrieveFavorites = 즐겨찾기_목록_조회_요청(로그인된_회원);
        //then
        즐겨찾기_목록_조회됨(retrieveFavorites, Arrays.asList(양재역.getId(), 교대역.getId()));

        final ExtractableResponse<Response> responseFavorite = 즐겨찾기_조회_요청(createFavorite);
        즐겨찾기가_조회됨(responseFavorite);

        FavoriteResponse 첫번째_즐겨찾기 = responseFavorite.as(FavoriteResponse.class);

        final ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(로그인된_회원, 첫번째_즐겨찾기.getId());
        즐겨찾기가_삭제됨(deleteResponse);

        final ExtractableResponse<Response> searchDeleteFavorite = 즐겨찾기_조회_요청(첫번째_즐겨찾기.getId());
        즐겨찾기가_조회가_되지않음(searchDeleteFavorite);
    }

    private void 즐겨찾기가_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("source.name")).isNotEmpty();
    }

    private void 즐겨찾기가_조회가_되지않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> retrieveFavorites, List<Long> favoritTargets) {
        assertThat(retrieveFavorites.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrieveFavorites.jsonPath().getList("target.id",Long.class)).containsAnyElementsOf(favoritTargets);
    }


    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, FavoriteRequest favoriteReqeust) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(favoriteReqeust)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header(HttpHeaders.LOCATION))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(long favoriteId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("favoriteId", favoriteId)
                .when().get("/favorites/{favoriteId}")
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, long favoriteId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .pathParam("favoriteId", favoriteId)
                .when().delete("/favorites/{favoriteId}")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기가_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }





}