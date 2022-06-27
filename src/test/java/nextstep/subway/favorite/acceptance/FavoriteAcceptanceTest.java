package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private String accessToken;

    /**
     * 교대역    --- *2호선* (7) ---   강남역
     * |                              |
     * *3호선*                      *신분당선*
     * (3)                           (10)
     * |                              |
     * 남부터미널역  --- *3호선* (2)---   양재
     */
    @BeforeEach
    void init() {
        //지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        //지하철 노선 등록되어 있음
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 7)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        //지하철 노선에 지하철역 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        //회원 등록되어 있음
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        //로그인 되어있음
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        로그인_됨(loginResponse);

        accessToken = loginResponse.jsonPath().getString("accessToken");
    }

   /*
   *  Scenario: 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨
   * */
    @DisplayName("즐겨찾기를 관리한다.")
    @TestFactory
    Collection<DynamicTest> manageFavorite() {
        return Arrays.asList(
            DynamicTest.dynamicTest("즐겨찾기 생성", () -> {
                //when
                ExtractableResponse<Response> createFavorite = 즐겨찾기_생성_요청(accessToken, 교대역.getId(), 양재역.getId());
                //then
                즐겨찾기_생성_됨(createFavorite);
            }),
            DynamicTest.dynamicTest("즐겨찾기 목록 조회", () -> {
                //when
                ExtractableResponse<Response> favoritesResponse = 즐겨찾기_목록_조회_요청(accessToken);
                //then
                즐겨찾기_목록_응답됨(favoritesResponse);
            }),
            DynamicTest.dynamicTest("즐겨찾기 삭제", () -> {
                //given
                ExtractableResponse<Response> createFavorite = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 양재역.getId());
                //when
                ExtractableResponse<Response> deletedFavorite = 즐겨찾기_삭제_요청(accessToken, createFavorite);
                //then
                즐겨찾기_삭제됨(deletedFavorite);
            })
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceId, Long targetId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);

        return RestAssured.given()
                .log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given()
                .log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given()
                .log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
