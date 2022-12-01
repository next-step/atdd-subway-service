package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestUtils.로그인되어_있음;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_목록_조회_실패됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_삭제_실패됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_생성_실패됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_생성_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestUtils.즐겨찾기_생성됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestUtils.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestUtils.회원_등록되어_있음;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private StationResponse 신논현역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 판교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        신논현역 = 지하철역_등록되어_있음("신논현역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        LineRequest 신분당선_등록_요청 = new LineRequest("신분당선", "bg-red-600", 신논현역.getId(), 판교역.getId(), 30);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(신분당선_등록_요청).as(LineResponse.class);
        LineRequest 삼호선_등록_요청 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 10);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(삼호선_등록_요청).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 신논현역, 강남역, 5);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 판교역, 15);
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD).as(TokenResponse.class);
        accessToken = tokenResponse.getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     회원
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     *     유효하지 않은 토큰
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성 실패됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 실패됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제 실패됨
     */
    @TestFactory
    @DisplayName("즐겨찾기 기능 통합 인수 테스트")
    Collection<DynamicTest> manageFavorite() {
        String wrongToken = "wrongToken";

        return Arrays.asList(
                DynamicTest.dynamicTest("회원은 즐겨찾기를 생성할 수 있다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 양재역.getId());
                    ExtractableResponse<Response> response2 = 즐겨찾기_생성_요청(accessToken, 신논현역.getId(), 교대역.getId());

                    // then
                    즐겨찾기_생성됨(response);
                    즐겨찾기_생성됨(response2);
                }),
                DynamicTest.dynamicTest("회원은 즐겨찾기 목록을 조회 할 수 있다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

                    // then
                    즐겨찾기_목록_조회됨(response);
                }),
                DynamicTest.dynamicTest("회원은 즐겨찾기를 삭제할 수 있다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, 1L);

                    // then
                    즐겨찾기_삭제됨(response);
                }),
                DynamicTest.dynamicTest("유효하지 않은 토큰은 즐겨찾기를 생성할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(wrongToken, 양재역.getId(), 판교역.getId());

                    // then
                    즐겨찾기_생성_실패됨(response);
                }),
                DynamicTest.dynamicTest("유효하지 않은 토큰은 즐겨찾기 목록을 조회 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(wrongToken);

                    // then
                    즐겨찾기_목록_조회_실패됨(response);
                }),
                DynamicTest.dynamicTest("유효하지 않은 토큰은 즐겨찾기를 삭제할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(wrongToken, 1L);

                    // then
                    즐겨찾기_삭제_실패됨(response);
                })
        );
    }
}
