package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공후_토큰_조회됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_시도함;
import static nextstep.subway.line.acceptance.LineAcceptanceSupport.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.SectionAcceptanceSupport.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.path.PathAcceptanceSupport.지하철_요금_검증_완료;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단거리_경로_검증_완료;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단거리_경로_조회_성공;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단거리_길이_검증_완료;
import static nextstep.subway.path.PathAcceptanceSupport.지하철역_최단경로_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회 인수 테스트")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 강변역;
    private ExtractableResponse<Response> createResponse;
    private ExtractableResponse<Response> loginSuccessResponse;
    private String accessToken;

    /**
     * 교대역    --- *2호선* ---   강남역 -------- 잠실역 --------- 강변역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        강변역 = StationAcceptanceTest.지하철역_등록되어_있음("강변역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 12)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 잠실역, 50);
        지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 강변역, 40);
    }

    /**
     * Feature: 지하철 구간 관련 기능
     *
     *   Given 지하철역 등록되어 있음
     *   And 지하철 노선 등록되어 있음
     *   And 지하철 노선에 지하철역 등록되어 있음
     *
     *  Scenario: 지하철 최단경로를 조회
     *    Given 출발역, 도착역을 전달해 지하철역의 최단경로를 조회하면
     *    Then: 최단경로가 조회됨
    **/
    @DisplayName("지하철 최단 경로 조회 기능")
    @Test
    void find_shortest_path_test() {
        연령에_맞는_회원생성_후_로그인(20);

        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(accessToken, 강남역.getId(), 양재역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "양재역"));
        지하철역_최단거리_길이_검증_완료(response, 12);
    }

    @DisplayName("지하철 환승역 포함 최단 경로 조회 기능")
    @Test
    void find_shortest_path_test2() {
        연령에_맞는_회원생성_후_로그인(20);

        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(accessToken, 강남역.getId(), 남부터미널역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_길이_검증_완료(response, 13);
        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "교대역", "남부터미널역"));
    }


    @DisplayName("성인이 13km 이동한 경우 지하철 요금 조회")
    @Test
    void find_fare_age_test() {
        연령에_맞는_회원생성_후_로그인(20);

        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(accessToken, 강남역.getId(), 남부터미널역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_길이_검증_완료(response, 13);
        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "교대역", "남부터미널역"));
        지하철_요금_검증_완료(response, 1550);
    }

    @DisplayName("청소년이 13km 이동한 경우 지하철 요금 조회")
    @Test
    void find_fare_age_test2() {
        연령에_맞는_회원생성_후_로그인(15);

        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(accessToken, 강남역.getId(), 남부터미널역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_길이_검증_완료(response, 13);
        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "교대역", "남부터미널역"));
        지하철_요금_검증_완료(response, 816);
    }

    @DisplayName("청소년이 50km 이동한 경우 지하철 요금 조회")
    @Test
    void find_fare_distance_test() {
        연령에_맞는_회원생성_후_로그인(15);

        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(accessToken, 강남역.getId(), 잠실역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_길이_검증_완료(response, 50);
        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "잠실역"));
        지하철_요금_검증_완료(response, 1376);
    }

    @DisplayName("성인이 90km 이동한 경우 지하철 요금 조회")
    @Test
    void find_fare_distance_test2() {
        연령에_맞는_회원생성_후_로그인(22);

        ExtractableResponse<Response> response = 지하철역_최단경로_조회_요청(accessToken, 강남역.getId(), 강변역.getId());
        지하철역_최단거리_경로_조회_성공(response);

        지하철역_최단거리_길이_검증_완료(response, 90);
        지하철역_최단거리_경로_검증_완료(response, Arrays.asList("강남역", "잠실역", "강변역"));
        지하철_요금_검증_완료(response, 2450);
    }

    private void 연령에_맞는_회원생성_후_로그인(int age) {
        createResponse = 회원_생성을_요청(EMAIL, PASSWORD, age);
        회원_생성됨(createResponse);

        loginSuccessResponse = 로그인_시도함(EMAIL, PASSWORD);
        accessToken = 로그인_성공후_토큰_조회됨(loginSuccessResponse);
    }
}
