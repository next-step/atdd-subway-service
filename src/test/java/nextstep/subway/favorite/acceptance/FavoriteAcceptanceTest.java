package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


/**
 * Feature: 즐겨찾기를 관리한다.
 * <p>
 * Scenario: 즐겨찾기를 관리
 * <p>
 * When 즐겨찾기 생성을 요청
 * <p>
 * Then 즐겨찾기 생성됨
 * <p>
 * When 즐겨찾기 목록 조회 요청
 * <p>
 * Then 즐겨찾기 목록 조회됨
 * <p>
 * When 즐겨찾기 삭제 요청
 * <p>
 * Then 즐겨찾기 삭제됨
 */

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private String EMAIL = "email@email.com";
    private String PASSWORD = "password";
    private int AGE = 20;
    private static String 토큰;

    /**
     * Background
     * <p>
     * Given 지하철역 등록되어 있음
     * <p>
     * And 지하철 노선 등록되어 있음
     * <p>
     * And 지하철 노선에 지하철역 등록되어 있음
     * <p>
     * And 회원 등록되어 있음
     * <p>
     * And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(
                LineResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        토큰 = 토큰_요청(new TokenRequest(EMAIL, PASSWORD)).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        /**
         * When 즐겨찾기 생성을 요청
         *     Then 즐겨찾기 생성됨
         *     When 즐겨찾기 목록 조회 요청
         *     Then 즐겨찾기 목록 조회됨
         *     When 즐겨찾기 삭제 요청
         *     Then 즐겨찾기 삭제됨
         */

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(강남역.getId(), 양재역.getId());
        // then
        즐겨찾기_생성됨(createResponse);
    }

    private static ExtractableResponse<Response> 즐겨찾기_생성을_요청(Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}