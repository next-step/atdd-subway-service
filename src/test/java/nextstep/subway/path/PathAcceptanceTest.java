package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethods.*;
import static nextstep.subway.fare.domain.FareType.*;
import static nextstep.subway.line.acceptance.LineAcceptanceMethods.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethods.*;
import static nextstep.subway.member.MemberAcceptanceMethods.*;
import static nextstep.subway.path.PathAcceptanceMethods.*;
import static nextstep.subway.station.StationAcceptanceMethods.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.fare.calculator.FareCalculator;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_5 = 5;
    private static final int DISTANCE_3 = 3;

    private static final int FARE_1000 = 1000;
    private static final int FARE_900 = 900;
    private static final int FARE_800 = 800;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private TokenResponse token;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음(StationRequest.of("강남역")).as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음(StationRequest.of("양재역")).as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음(StationRequest.of("교대역")).as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음(StationRequest.of("남부터미널역")).as(StationResponse.class);

        LineRequest 신분당선_Request = LineRequest.of("신분당선", "RED", FARE_1000, 강남역.getId(), 양재역.getId(), DISTANCE_10);
        LineRequest 이호선_Request = LineRequest.of("이호선", "GREED", FARE_900, 교대역.getId(), 강남역.getId(), DISTANCE_10);
        LineRequest 삼호선_Request = LineRequest.of("삼호선", "ORANGE", FARE_800, 남부터미널역.getId(), 양재역.getId(), DISTANCE_5);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_Request).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_Request).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_Request).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), SectionRequest.of(교대역.getId(), 남부터미널역.getId(), DISTANCE_3));

        회원_생성을_요청(MemberRequest.of(EMAIL, PASSWORD, AGE));
        token = 회원_로그인_됨(TokenRequest.of(EMAIL, PASSWORD)).as(TokenResponse.class);
    }

    @DisplayName("출발역에서 도착역까지 최단경로를 조회한다.")
    @Test
    void findShortestPath1() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId(), token);

        // then
        지하철_최단경로_조회됨(response,
                           Arrays.asList(교대역, 남부터미널역, 양재역),
                           DISTANCE_3 + DISTANCE_5,
                           BASIC.getFare() + 삼호선.getFare());
    }

    @DisplayName("출발역과 도착역이 같을 때, 최단경로를 조회한다.")
    @Test
    void findShortestPath2() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 교대역.getId(), token);

        // then
        지하철_최단경로_조회됨(response, Collections.singletonList(교대역), 0, 0);
    }

    @DisplayName("출발역이 노선상에 존재하지 않을때, 최단경로를 조회한다.")
    @Test
    void findShortestPath3() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(서울역.getId(), 교대역.getId(), token);

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("도착역이 노선상에 존재하지 않을때, 최단경로를 조회한다.")
    @Test
    void findShortestPath4() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 서울역.getId(), token);

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("출발역과 도착역이 서로 연결되어 있지 않을때, 최단경로를 조회한다.")
    @Test
    void findShortPath5() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);
        StationResponse 시청역 = 지하철역_등록되어_있음(StationRequest.of("시청역")).as(StationResponse.class);

        LineRequest 일호선_Request = LineRequest.of("일호선", "BLUE", FARE_1000, 서울역.getId(), 시청역.getId(), DISTANCE_5);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(일호선_Request).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 서울역.getId(), token);

        // then
        지하철_최단경로_조회_실패(response);
    }

    @DisplayName("추가요금이 없는 노선에서 출발역에서 도착역까지 최단경로를 조회한다.")
    @ParameterizedTest
    @ValueSource(ints = {3, 10, 50})
    void findShortPath6(int distance) {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);
        StationResponse 시청역 = 지하철역_등록되어_있음(StationRequest.of("시청역")).as(StationResponse.class);
        StationResponse 종각역 = 지하철역_등록되어_있음(StationRequest.of("종각역")).as(StationResponse.class);

        LineRequest 일호선_Request = LineRequest.of("일호선", "BLUE", 0, 서울역.getId(), 시청역.getId(), distance);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(일호선_Request).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(일호선.getId(), SectionRequest.of(시청역.getId(), 종각역.getId(), DISTANCE_5));

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(서울역.getId(), 종각역.getId(), token);

        // then
        지하철_최단경로_조회됨(response,
                           Arrays.asList(서울역, 시청역, 종각역),
                           distance + DISTANCE_5,
                           FareCalculator.calculatePathFare(distance + DISTANCE_5));
    }

    @DisplayName("추가요금이 있는 노선에서 출발역에서 도착역까지 최단경로를 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {"1000,1", "1500,10", "2500,50", "5000,60"})
    void findShortPath7(int fare, int distance) {
        // given
        StationResponse 모란역 = 지하철역_등록되어_있음(StationRequest.of("모란역")).as(StationResponse.class);
        LineRequest 분당선_Request = LineRequest.of("분당선", "YELLOW", fare, 양재역.getId(), 모란역.getId(), distance);
        LineResponse 분당선 = 지하철_노선_등록되어_있음(분당선_Request).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 모란역.getId(), token);

        // then
        지하철_최단경로_조회됨(response,
                           Arrays.asList(교대역, 남부터미널역, 양재역, 모란역),
                           DISTANCE_3 + DISTANCE_5 + distance,
                           FareCalculator.calculatePathFare(DISTANCE_3 + DISTANCE_5 + distance) + fare);
    }

    @DisplayName("로그인을 하지 않은 상태에서 경로조회를 한다.")
    @ParameterizedTest
    @EmptySource
    void findShortPath8(String token) {
        // given
        TokenResponse emptyToken = TokenResponse.from(token);

        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId(), emptyToken);

        // then
        지하철_최단경로_조회_실패(response);
    }
}
