package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


/**
 * Feature: 지하철 경로 관련 기능
 * <p>
 * Background
 *  Given 지하철역 여러개 등록되어 있음
 *  And 지하철 노선 여러개 등록되어 있음
 *  And 지하철 노선에 구간 여러개 등록되어 있음
 *  And 성인 회원 등록되어 있음
 *  And 로그인 되어있음
 *  And 청소년 회원 등록되어 있음
 *  And 로그인 되어있음
 *  And 어린이 회원 등록되어 있음
 *  And 로그인 되어있음
 * <p>
 * Scenario: 출발역과 도착역 사이의 최단 경로 조회
 *  When 성인 회원의 지하철 경로 조회 요청
 *  Then 성인 회원의 출발역과 도착역 사이의 최단 경로 조회됨.
 *  And 성인 회원의 총 거리도 함께 응답함
 *  And 성인 회원의 지하철 이용 요금도 함께 응답함
 * <p>
 *  When 청소년 회원의 지하철 경로 조회 요청
 *  Then 청소년 회원의 출발역과 도착역 사이의 최단 경로 조회됨.
 *  And 청소년 회원의 총 거리도 함께 응답함
 *  And 청소년 회원의 지하철 이용 요금도 함께 응답함
 * <p>
 *  When 어린이 회원의 지하철 경로 조회 요청
 *  Then 어린이 회원의 출발역과 도착역 사이의 최단 경로 조회됨.
 *  And 어린이 회원의 총 거리도 함께 응답함
 *  And 어린이 회원의 지하철 이용 요금도 함께 응답함
 * <p>
 * Scenario: 출발역과 도착역이 같은 경우
 *  When 지하철 경로 조회 요청
 *  Then 최단 경로 조회 실패
 * <p>
 * Scenario: 출발역과 도착역이 연결이 되어 있지 않은 경우
 *  When 지하철 경로 조회 요청
 *  Then 최단 경로 조회 실패
 * <p>
 * Scenario: 존재하지 않은 출발역이나 도착역을 조회 할 경우
 *  When 지하철 경로 조회 요청
 *  Then 최단 경로 조회 실패
 * <p>
 */
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 구호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 동작역;
    private StationResponse 석촌역;
    private StationResponse 남부터미널역;
    private String 성인회원;
    private String 청소년회원;
    private String 어린이회원;
    private String 성인_이메일 = "adult@gmail.com";
    private String 청소년_이메일 = "tennager@gmail.com";
    private String 어린이_이메일 = "child@gmail.com";

    /**
     * 교대역      ----- *2호선(10)* -----   강남역
     * |                                   |
     * |                                   |
     * *3호선(3)*                       *신분당선(10)*
     * |                                   |
     * |                                   |
     * 남부터미널역 ----- *3호선(2)* -----     양재
     * <p>
     * 동작역     ----- *9호선(13)* -----    석촌역
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        지하철역_여러개_등록되어_있음();

        지하철_노선_여러개_등록되어_있음();

        지하철_노선에_지하철역_등록되어_있음();

        성인_회원_등록되어_있음();
        성인_회원_로그인_됨();

        청소년_회원_등록되어_있음();
        청소년_회원_로그인_됨();

        어린이_회원_등록되어_있음();
        어린이_회원_로그인_됨();
    }

    private void 성인_회원_등록되어_있음() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(성인_이메일, PASSWORD, 19);
        회원_생성됨(createResponse);
    }

    private void 성인_회원_로그인_됨() {
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(성인_이메일, PASSWORD));
        로그인_성공(loginResponse);

        성인회원 = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 청소년_회원_등록되어_있음() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(청소년_이메일, PASSWORD, 13);
        회원_생성됨(createResponse);
    }

    private void 청소년_회원_로그인_됨() {
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(청소년_이메일, PASSWORD));
        로그인_성공(loginResponse);

        청소년회원 = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 어린이_회원_등록되어_있음() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(어린이_이메일, PASSWORD, 6);
        회원_생성됨(createResponse);
    }

    private void 어린이_회원_로그인_됨() {
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(어린이_이메일, PASSWORD));
        로그인_성공(loginResponse);

        어린이회원 = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 지하철_노선에_지하철역_등록되어_있음() {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private void 지하철역_여러개_등록되어_있음() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        동작역 = StationAcceptanceTest.지하철역_등록되어_있음("동작역").as(StationResponse.class);
        석촌역 = StationAcceptanceTest.지하철역_등록되어_있음("석촌역").as(StationResponse.class);
    }

    private void 지하철_노선_여러개_등록되어_있음() {
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 100)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 200)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 100)).as(LineResponse.class);
        구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("구호선", "bg-red-600", 동작역.getId(), 석촌역.getId(), 13, 150)).as(LineResponse.class);
    }

    /**
     * When 성인 회원의 지하철 경로 조회 요청
     * Then 성인 회원의 출발역과 도착역 사이의 최단 경로 조회됨.
     * And 성인 회원의 총 거리도 함께 응답함
     * And 성인 회원의 지하철 이용 요금도 함께 응답함
     * <p>
     * When 청소년 회원의 지하철 경로 조회 요청
     * Then 청소년 회원의 출발역과 도착역 사이의 최단 경로 조회됨.
     * And 청소년 회원의 총 거리도 함께 응답함
     * And 청소년 회원의 지하철 이용 요금도 함께 응답함
     * <p>
     * When 어린이 회원의 지하철 경로 조회 요청
     * Then 어린이 회원의 출발역과 도착역 사이의 최단 경로 조회됨.
     * And 어린이 회원의 총 거리도 함께 응답함
     * And 어린이 회원의 지하철 이용 요금도 함께 응답함
     */
    @Test
    void 출발역과_도착역_사이의_최단_경로_조회() {
        ExtractableResponse<Response> 성인_회원_경로조회_결과 = 지하철_경로_조회_요청(성인회원, 교대역.getId(), 양재역.getId());
        지하철_최단_경로_총_거리_조회됨(성인_회원_경로조회_결과, 5);
        지하철_이용_요금_조회됨(성인_회원_경로조회_결과, 1250);

        ExtractableResponse<Response> 청소년_회원_경로조회_결과 = 지하철_경로_조회_요청(청소년회원, 교대역.getId(), 양재역.getId());
        지하철_최단_경로_총_거리_조회됨(청소년_회원_경로조회_결과, 5);
        지하철_이용_요금_조회됨(청소년_회원_경로조회_결과, 720);

        ExtractableResponse<Response> 어린이_회원_경로조회_결과 = 지하철_경로_조회_요청(어린이회원, 교대역.getId(), 양재역.getId());
        지하철_최단_경로_총_거리_조회됨(어린이_회원_경로조회_결과, 5);
        지하철_이용_요금_조회됨(어린이_회원_경로조회_결과, 450);
    }

    private void 지하철_최단_경로_총_거리_조회됨(ExtractableResponse<Response> response, int distance) {
        Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    private void 지하철_이용_요금_조회됨(ExtractableResponse<Response> response, int fare) {
        Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }

    @Test
    void 출발역과_도착역이_같은_경우_예외_발생() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(성인회원, 교대역.getId(), 교대역.getId());

        // then
        지하철_최단_경로_실패됨(response, ErrorEnum.SOURCE_AND_TARGET_EQUAL_STATION.message());
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우_예외_발생() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(성인회원, 강남역.getId(), 석촌역.getId());

        // then
        지하철_최단_경로_실패됨(response, ErrorEnum.NOT_CONNECTED_STATIONS.message());
    }

    @Test
    void 존재하지_않는_출발역이나_도착역을_조회_할_경우_예외_발생() {
        // when
        ExtractableResponse<Response> 존재하지_않는_도착역_응답 = 지하철_경로_조회_요청(성인회원, 양재역.getId(), 0L);

        // then
        지하철_최단_경로_실패됨(존재하지_않는_도착역_응답, ErrorEnum.NOT_EXISTS_STATION.message());
    }

    private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response, int distance) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance)
        );
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(String accessToken, Long startStationId, Long endStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", startStationId);
        params.put("target", endStationId);
        params.put("age", 19);

        return RestAssured.given().log().all()
                .queryParams(params)
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 지하철_최단_경로_실패됨(ExtractableResponse<Response> response, String expectedErrorMessage) {
        String errorMessage = response.body().path("message").toString();
        assertThat(errorMessage).isEqualTo(expectedErrorMessage);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
