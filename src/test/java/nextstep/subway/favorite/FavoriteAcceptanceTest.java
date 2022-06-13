package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private final String FAVORITE_URI = "/favorites";
    private StationResponse 강남역, 교대역, 서초역, 양재역, 판교역, 고속터미널역;
    private LineResponse 이호선, 삼호선, 신분당선;
    private TokenResponse 몬드_토큰, 스루기_토큰;

    /**
     * Given 지하철역 등록되어 있고
     *   And 지하철 노선 등록되어 있고
     *   And 지하철 노선 지하철역 등록되어 있고
     *   And 회원 등록되어 있고
     *   And 로그인 되어 있다
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        서초역 = 지하철역_등록되어_있음("서초역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);

        // given
        LineRequest 이호선_생성 = new LineRequest("이호선", "yellow", 서초역.getId(), 강남역.getId(), 5);
        이호선 = 지하철_노선_생성_요청(이호선_생성).as(LineResponse.class);
        LineRequest 삼호선_생성 = new LineRequest("삼호선", "orange", 고속터미널역.getId(), 교대역.getId(), 6);
        삼호선 = 지하철_노선_생성_요청(삼호선_생성).as(LineResponse.class);
        LineRequest 신분당선_생성 = new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 7);
        신분당선 = 지하철_노선_생성_요청(신분당선_생성).as(LineResponse.class);

        // given
        지하철_노선에_지하철역_등록_요청(이호선, 교대역, 서초역, 10);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 양재역, 3);
//        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 15);

        // given
        회원_생성을_요청("mond@mond.com", "mond", 10);
        회원_생성을_요청("srugi@srugi.com", "srugi", 6);

        // given
        몬드_토큰 = 로그인_요청(new TokenRequest("mond@mond.com", "mond")).as(TokenResponse.class);
        스루기_토큰 = 로그인_요청(new TokenRequest("srugi@srugi.com", "srugi")).as(TokenResponse.class);
    }

    /**
     *  When 즐겨찾기 생성을 요청하면
     *  Then 즐겨찾기가 생성된다
     *  When 즐겨찾기 목록 조회하면
     *  Then 즐겨찾기 목록이 조회된다
     *  ---
     *  When 다른 로그인 정보로 즐겨찾기 목록 조회하면
     *  Then 즐겨찾기 목록이 노출되지 않는다
     *  ---
     *  When 즐겨찾기 삭제 요청하면
     *  Then 즐겨찾기가 삭제된다
     */
    @Test
    @DisplayName("즐겨찾기 관리 성공 시나리오")
    void favoriteSuccessScenario() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = 즐겨찾기_생성_요청(몬드_토큰, 서초역, 강남역);
        // then
        즐겨찾기_생성_요청_성공(즐겨찾기_생성_요청_결과);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회(몬드_토큰);
        // then
        즐겨찾기_목록_확인(즐겨찾기_목록_조회_결과);

        // when
        ExtractableResponse<Response> 남의_즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회(스루기_토큰);
        // then
        즐겨찾기_목록_확인(남의_즐겨찾기_목록_조회_결과);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(몬드_토큰, 즐겨찾기_생성_요청_결과);
        // then
        즐겨찾기_삭제_확인(즐겨찾기_삭제_요청_결과);
    }

    /**
     *  When 존재하지 않는 역을 즐겨찾기 생성 요청하면
     *  Then 즐겨찾기가 생성되지 않는다
     *  When 연결되어 있지 않는 역을 즐겨찾기 생성 요청하면
     *  Then 즐겨찾기가 생성되지 않는다
     */
    @Test
    @DisplayName("즐겨찾기 관리 실패 시나리오(없는 역, 연결되어 있지 않는 역)")
    void favoriteFailScenario() {
        // when
        StationResponse 없는역 = new StationResponse(9L, "없는역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> 없는_역_생성_요청_결과 = 즐겨찾기_생성_요청(몬드_토큰, 없는역, 없는역);
        // then
        즐겨찾기_생성_요청_실패(없는_역_생성_요청_결과);

        // when
        ExtractableResponse<Response> 연결_안된_역_생성_요청_결과 = 즐겨찾기_생성_요청(몬드_토큰, 강남역, 판교역);
        // then
        즐겨찾기_생성_요청_실패(연결_안된_역_생성_요청_결과);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse 토큰, StationResponse 시작역, StationResponse 종료역) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(시작역.getId(), 종료역.getId());

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, 토큰.addBearerAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post(FAVORITE_URI)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_요청_성공(ExtractableResponse<Response> 즐겨찾기_생성_요청_결과) {
        assertThat(즐겨찾기_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_생성_요청_실패(ExtractableResponse<Response> 즐겨찾기_생성_요청_결과) {
        assertThat(즐겨찾기_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회(TokenResponse 토큰) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, 토큰.addBearerAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITE_URI)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_확인(ExtractableResponse<Response> 즐겨찾기_목록_조회_결과) {
        FavoriteResponse favoriteResponse = 즐겨찾기_목록_조회_결과.as(FavoriteResponse.class);

        // TODO : 구현
        assertThat(favoriteResponse).isNotNull();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse 토큰, ExtractableResponse<Response> 즐겨찾기_생성_요청_결과) {
        final String uri = 즐겨찾기_생성_요청_결과.header("Location");
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, 토큰.addBearerAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제_확인(ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과) {
        assertThat(즐겨찾기_삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
