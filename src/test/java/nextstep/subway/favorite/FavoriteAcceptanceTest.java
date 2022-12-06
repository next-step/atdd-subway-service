package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 Feature: 즐겨찾기를 관리한다.

 * Background
 * Given 지하철역 등록되어 있음
 * And 지하철 노선 등록되어 있음
 * And 지하철 노선에 지하철역 등록되어 있음
 * And 회원 등록되어 있음
 * And 로그인 되어있음
 *
 * Scenario: 즐겨찾기를 관리
 * When 즐겨찾기 생성을 요청
 * Then 즐겨찾기 생성됨
 *
 * When 즐겨찾기 목록 조회 요청
 * Then 즐겨찾기 목록 조회됨
 *
 * When 즐겨찾기 삭제 요청
 * Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 5);
        지하철_노선에_지하철역_등록됨(response);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_성공(loginResponse);

        accessToken = loginResponse.as(TokenResponse.class).getAccessToken();

    }

    /**
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     * When 삭제된 즐겨찾기 조회 요청
     * Then 즐겨찾기 조회되지 않음     *
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 강남역.getId(), 광교역.getId());

        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> selectResponse = 즐겨찾기_목록_조회_요청(accessToken);

        즐겨찾기_목록_조회됨(selectResponse, 강남역.getId(), 광교역.getId());

        Long favoriteId = selectResponse.as(FavoriteResponse[].class)[0].getId();
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, favoriteId);

        즐겨찾기_삭제됨(deleteResponse);

        ExtractableResponse<Response> selectOneResponse = 즐겨찾기_조회_요청(favoriteId);

        즐겨찾기_조회되지_않음(selectOneResponse, ErrorEnum.NOT_EXISTS_FAVORITE.message());
    }

    private void 즐겨찾기_조회되지_않음(ExtractableResponse<Response> response, String expectedErrorMessage) {
        String errorMessage = response.body().path("errorMessage").toString();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorMessage).isEqualTo(expectedErrorMessage)
        );
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites/{favoriteId}", favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, Long source, Long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source, target))
                .when().post("/favorites/")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, Long source, Long target) {
        List<FavoriteResponse> favorites = Arrays.asList(response.as(FavoriteResponse[].class));
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.get(0).getSource().getId()).isEqualTo(source),
                () -> assertThat(favorites.get(0).getTarget().getId()).isEqualTo(target)
        );
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all()
                .extract();
    }
}
