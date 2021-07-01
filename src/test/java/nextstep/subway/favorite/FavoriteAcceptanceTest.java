package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;
    private LineRequest 구신분당선;
    private TokenResponse 로그인_사용자;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        // gien
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선);
        MemberAcceptanceTest.회원_등록되어_있음(new MemberRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,
                MemberAcceptanceTest.AGE));
        로그인_사용자 = AuthAcceptanceTest.로그인_되어_있음(new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD))
                .as(TokenResponse.class);
    }

    /**
     * Scenario: 즐겨찾기를 관리
     *
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @TestFactory
    @DisplayName("즐겨찾기를 관리한다.")
    List<DynamicTest> manage_favorite() {
        return Arrays.asList(
                dynamicTest("즐겨찾기 생성", () -> {
                    // when
                    ExtractableResponse<Response> 즑겨찾기_생성_요청_결과 = 즐겨찾기_생성을_요청(로그인_사용자, new FavoriteRequest(강남역.getId(), 광교역.getId()));

                    // then
                    즐겨찾기_생성됨(즑겨찾기_생성_요청_결과);
                }),
                dynamicTest("즐겨찾기 목록 조회", () -> {
                    // when
                    ExtractableResponse<Response> 즑겨찾기_목록_요청_결과 = 즐겨찾기_목록_조회_요청(로그인_사용자);

                    // then
                    즐겨찾기_목록_조회됨(즑겨찾기_목록_요청_결과);
                }),
                dynamicTest("즐겨찾기 삭제", () -> {
                    // given
                    ExtractableResponse<Response> 즑겨찾기_목록_요청_결과 = 즐겨찾기_목록_조회_요청(로그인_사용자);

                    // when
                    ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(로그인_사용자, 즑겨찾기_목록_요청_결과.jsonPath().getList("id", Long.class).get(0));

                    // then
                    즐겨찾기_삭제됨(즐겨찾기_삭제_요청_결과);
                })
        );
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse loginUser, FavoriteRequest favoriteRequest) {
        return RestAssured.given().log().all()
                .when()
                .header(AuthorizationExtractor.AUTHORIZATION, makeAuthorizationHeaderValue(loginUser))
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL_VALUE)
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse loginUser) {
        return RestAssured.given().log().all()
                .when()
                .header(AuthorizationExtractor.AUTHORIZATION, makeAuthorizationHeaderValue(loginUser))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse loginUser, long favoriteId) {
        return RestAssured.given().log().all()
                .when()
                .header(AuthorizationExtractor.AUTHORIZATION, makeAuthorizationHeaderValue(loginUser))
                .accept(MediaType.ALL_VALUE)
                .delete("/favorites/" + favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private String makeAuthorizationHeaderValue(TokenResponse loginUser) {
        return AuthorizationExtractor.BEARER_TYPE + " " + loginUser.getAccessToken();
    }
}
