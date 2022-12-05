package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    /**
     * 강남역 --- *2호선* ---  교대역    --- *2호선* --- 사당역
     * |                      |
     * *신분당선*              *3호선*
     * |                      |
     * 양재 --- *3호선* ---  남부터미널역
     */
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;

    private String accessToken;

    /**
     * Given : 지하철역 등록되어 있음
     * And : 지하철 노선 등록되어 있음
     * And : 지하철 노선에 지하철역 등록되어 있음
     * And : 회원 등록되어 있음
     * And : 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given : 지하철역 등록
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);

        // and : 노선 등록
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        // and : 노선에 지하철역 등록
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(이호선, 사당역, 교대역, 15);

        // and : 회원 등록
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        // and : 로그인
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> createTokenResponse = 로그인_토큰_생성_요청(tokenRequest);
        로그인_토큰_생성_성공함(createTokenResponse);

        accessToken = createTokenResponse.as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청_with_accessToken(accessToken);
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }


    /**
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리")
    @Test
    void favoriteAcceptanceTestIntegration() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> favoritesResponse = 즐겨찾기_목록_조회_요청(accessToken);
        // then
        즐겨찾기_목록_조회됨(favoritesResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, accessToken);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     */
    @DisplayName("즐겨찾기 생성하기 인수 테스트")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());

        // then
        즐겨찾기_생성됨(response);
    }

    /**
     * When 동일한 즐겨찾기 생성을 요청
     * Then 즐겨찾기 실패됨
     */
    @DisplayName("즐겨찾기 생성하기 중복 등록 예외 발생 인수 테스트")
    @Test
    void createFavoriteException() {
        // when
        즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());

        // then
        즐겨찾기_실패됨(response);
    }

    /**
     * Given 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     */
    @DisplayName("즐겨찾기 목록을 조회하는 인수 테스트")
    @Test
    void getFavorites() {
        // given
        즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());
        즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 남부터미널역.getId());
        즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 강남역.getId());
        즐겨찾기_생성을_요청(accessToken, 교대역.getId(), 강남역.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        즐겨찾기_목록_조회됨(response);
    }

    /**
     * Given 즐겨찾기 생성됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 삭제 인수 테스트")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, accessToken);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * Given 기존 멤버의 즐겨찾기를 생성
     * Given 새로운 멤버를 생성
     * When 새로운 멤버가 기존 멤버의 즐겨찾기를 삭제 요청
     * Then 즐겨찾기 삭제 실패
     */
    @DisplayName("다른 멤버의 즐겨찾기를 삭제하는 경우 실패")
    @Test
    void deleteAnotherMembersFavoriteException() {
        // given
        ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());

        // given
        ExtractableResponse<Response> createMemberResponse = 회원_생성을_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_생성됨(createMemberResponse);
        ExtractableResponse<Response> createTokenResponse = 로그인_토큰_생성_요청(new TokenRequest(NEW_EMAIL, NEW_PASSWORD));
        로그인_토큰_생성_성공함(createTokenResponse);
        String newAccessToken = createTokenResponse.as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청_with_accessToken(newAccessToken);
        회원_정보_조회됨(findResponse, NEW_EMAIL, NEW_AGE);

        // when
        ExtractableResponse<Response> deleteFavoriteResponse = 즐겨찾기_삭제_요청(createFavoriteResponse, newAccessToken);

        // then
        즐겨찾기_삭제_실패됨(deleteFavoriteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, Long sourceId, Long targetId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse, String accessToken) {
        String uri = createResponse.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<FavoriteResponse> favorites = response.jsonPath().getList("", FavoriteResponse.class);
        assertThat(favorites).isNotNull();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> deleteFavoriteResponse) {
        assertThat(deleteFavoriteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}