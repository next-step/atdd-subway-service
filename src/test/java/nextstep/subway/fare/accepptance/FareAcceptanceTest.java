package nextstep.subway.fare.accepptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethod.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethod.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceMethod.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceMethod.회원_정보_조회_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceMethod.*;
import static nextstep.subway.station.StationAcceptanceMethod.지하철역_등록되어_있음;

public class FareAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 신도림역;
    private StationResponse 광교역;
    private MemberResponse 일반회원;
    private MemberResponse 청소년회원;
    private MemberResponse 어린이회원;

    /**
     * 교대역    --- *2호선*(10) ---  강남역 --- *2호선*(36) --- 신도림역
     * |                            |
     * *3호선*(12)               *신분당선*(10)
     * |                            |
     * 남부터미널역 --- *3호선*(15) --- 양재역 --- *신분당선*(21)  --- 광교역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        신도림역 = 지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 900)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10, 0)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 27, 0)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 12);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 신도림역, 36);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 광교역, 21);

        일반회원 = 회원_정보_조회_요청(회원_생성을_요청("basic@email.com", MemberAcceptanceTest.PASSWORD, 19)).as(MemberResponse.class);
        청소년회원 = 회원_정보_조회_요청(회원_생성을_요청("teenagers@email.com", MemberAcceptanceTest.PASSWORD, 13)).as(MemberResponse.class);
        어린이회원 = 회원_정보_조회_요청(회원_생성을_요청("kids@email.com", MemberAcceptanceTest.PASSWORD, 6)).as(MemberResponse.class);
    }

    /**
     * Scenario: 총 거리가 10km 이내이면서 노선 추가 요금이 없음
     *  When 교대역부터 강남역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (교대역 -(10)-> 강남역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 1250원)
     */
    @DisplayName("총 거리가 10km 이내인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_and_distance_and_fare() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 강남역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(교대역, 강남역));
        // and
        지하철_최단경로_총거리_확인됨(response, 10);
        // and
        지하철_이용요금_조회됨(response, 1250);
    }

    /**
     * Scenario: 총 거리가 15km 인 경우 요금 조회
     *  When 남부터미널역부터 양재역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (남부터미널역 -(15)-> 양재역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 1250원, 추가운임: 100원)
     */
    @DisplayName("총 거리가 15km 인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_greater_than_10km() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(남부터미널역.getId(), 양재역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(남부터미널역, 양재역));
        // and
        지하철_최단경로_총거리_확인됨(response, 15);
        // and
        지하철_이용요금_조회됨(response, 1350);
    }

    /**
     * Scenario: 총 거리가 58km 인 경우 요금 조회
     *  When 남부터미널역부터 신도림역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (남부터미널역 -(12)-> 교대역 -(10)-> 강남역 -(36)-> 신도림역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 1250원, 추가운임: 600원)
     */
    @DisplayName("총 거리가 58km 인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_greater_than_50km() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(남부터미널역.getId(), 신도림역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(남부터미널역, 교대역, 강남역, 신도림역));
        // and
        지하철_최단경로_총거리_확인됨(response, 58);
        // and
        지하철_이용요금_조회됨(response, 1850);
    }

    /**
     * Scenario: 경로 중 추가 요금이 있는 노선 포함 시 요금 조회
     *  When 교대역부터 양재역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (교대역 -(10)-> 강남역 -(10)-> 양재역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 1250원, 추가운임: 200원, 노선추가: 900원)
     */
    @DisplayName("경로 중 추가 요금이 있는 노선 포함 시 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_extra_line() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(교대역, 강남역, 양재역));
        // and
        지하철_최단경로_총거리_확인됨(response, 20);
        // and
        지하철_이용요금_조회됨(response, 2350);
    }

    /**
     * Scenario: 로그인 사용자가 일반(19세)인 경우 요금 조회
     *  When 일반 회원 로그인 후
     *  And 교대역부터 양재역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (교대역 -(10)-> 강남역 -(10)-> 양재역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 1250원, 추가운임: 200원, 노선추가: 900원)
     */
    @DisplayName("일반(19세)인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_basic() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(일반회원.getEmail(), MemberAcceptanceTest.PASSWORD));
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        // and
        ExtractableResponse<Response> response = 회원_지하철_최단경로_조회_요청(tokenResponse, 교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(교대역, 강남역, 양재역));
        // and
        지하철_최단경로_총거리_확인됨(response, 20);
        // and
        지하철_이용요금_조회됨(response, 2350);
    }

    /**
     * Scenario: 로그인 사용자가 청소년(13세)인 경우 요금 조회
     *  When 청소년 회원 로그인 후
     *  And 교대역부터 양재역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (교대역 -(10)-> 강남역 -(10)-> 양재역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 900원, 추가운임: 200원, 노선추가: 900원, 연령할인(20%): 400원)
     */
    @DisplayName("청소년(13세)인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_teenagers() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(청소년회원.getEmail(), MemberAcceptanceTest.PASSWORD));
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        // and
        ExtractableResponse<Response> response = 회원_지하철_최단경로_조회_요청(tokenResponse, 교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(교대역, 강남역, 양재역));
        // and
        지하철_최단경로_총거리_확인됨(response, 20);
        // and
        지하철_이용요금_조회됨(response, 1600);
    }

    /**
     * Scenario: 로그인 사용자가 어린이(6세)인 경우 요금 조회
     *  When 어린이 회원 로그인 후
     *  And 교대역부터 양재역까지의 최단 경로를 조회하면
     *  Then 최단 경로가 조회된다 (교대역 -(10)-> 강남역 -(10)-> 양재역)
     *  And 총 거리가 조회된다
     *  And 지하철 이용 요금이 조회된다(기본운임: 1250원, 추가운임: 200원, 노선추가: 900원, 연령할인(50%): 1000원)
     */
    @DisplayName("어린이(6세)인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void find_path_kids() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(어린이회원.getEmail(), MemberAcceptanceTest.PASSWORD));
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        // and
        ExtractableResponse<Response> response = 회원_지하철_최단경로_조회_요청(tokenResponse, 교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_응답됨(response);
        지하철_최단경로_포함됨(response, Arrays.asList(교대역, 강남역, 양재역));
        // and
        지하철_최단경로_총거리_확인됨(response, 20);
        // and
        지하철_이용요금_조회됨(response, 1000);
    }
}
