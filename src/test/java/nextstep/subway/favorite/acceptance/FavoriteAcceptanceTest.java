package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    /**
     * 사당역 --- *2호선* --- 교대역    --- *2호선* ---   강남역
     * |                         |
     * *3호선*                   *신분당선*
     * |                         |
     * 남부터미널역  --- *3호선* ---  양재
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
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(accessToken, 사당역.getId(), 양재역.getId());
        // then
        즐겨찾기_생성됨(response);

        // TODO : implements
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
     * When 즐겨찾기 생성을 요청
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
    }

    /**
     * Given 즐겨찾기 생성됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 삭제 인수 테스트")
    @Test
    void deleteFavorite() {
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

    private static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 즐겨찾기_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}