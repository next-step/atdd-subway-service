package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.application.farepolicy.FarePolicy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.로그인_되어_있음;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 사용자토큰;


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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 500))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 15, 200))
                .as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        삼호선 = LineAcceptanceTest.지하철_노선_조회_요청(삼호선).as(LineResponse.class);

        사용자토큰 = 로그인_되어_있음();
    }

    @Test
    @DisplayName("출발역에서 도착역으로 가는 최단 경로를 구합니다.")
    public void getShortestPath() {
        // given
        // 경로의 출발지 - 도착지
        Long 출발역 = 교대역.getId();
        Long 도착역 = 양재역.getId();

        // when
        // 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
        ExtractableResponse<Response> response = PathAcceptanceTestSupport.최단경로_조회_요청(사용자토큰, 출발역, 도착역);

        // then
        // 최단 거리 경로를 응답
        // 총 거리도 함께 응답
        // 지하철 이용 요금도 함께 응답
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStations().size()).isEqualTo(3);
        assertThat(pathResponse.getDistance()).isEqualTo(15);
        assertThat(pathResponse.getFare()).isEqualTo(FarePolicy.BASE_FARE + 100 + 200);
    }

    @ParameterizedTest
    @CsvSource(value = {"5,1550", "19,1550", "6,775", "12,775", "13,1240", "18,1240"})
    @DisplayName("출발역에서 도착역으로 가는 최단 경로의 운임을 구합니다.")
    public void getShortestPathFareByAge(String age, String expectedFare) {
        // given
        // 경로의 출발지 - 도착지
        Long 출발역 = 교대역.getId();
        Long 도착역 = 양재역.getId();

        String 신규사용자토큰 = 로그인_되어_있음(age + MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
                , Integer.parseInt(age));

        // when
        // 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
        ExtractableResponse<Response> response = PathAcceptanceTestSupport.최단경로_조회_요청(신규사용자토큰, 출발역, 도착역);

        // then
        // 지하철 이용 요금도 함께 응답
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(Integer.parseInt(expectedFare));
    }

    @Test
    @DisplayName("최단 경로를 구할 때, 출발역과 도착역이 같은 경우 예외 발생")
    public void getShortestSameStationPathOccurredException() {
        // given
        // 경로의 출발지 - 도착지
        Long 출발역 = 교대역.getId();
        Long 도착역 = 교대역.getId();

        // when
        ExtractableResponse<Response> response = PathAcceptanceTestSupport.최단경로_조회_요청(사용자토큰, 출발역, 도착역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("최단 경로를 구할 때, 출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    public void getShortestDisconnectedStationOccurredException() {
        // given

        StationResponse 산본역 = StationAcceptanceTest.지하철역_등록되어_있음("산본역")
                .as(StationResponse.class);
        StationResponse 수리산역 = StationAcceptanceTest.지하철역_등록되어_있음("수리산역")
                .as(StationResponse.class);

        LineResponse 사호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("사호선", "bg-blue-400", 산본역.getId(), 수리산역.getId(), 5))
                .as(LineResponse.class);

        Long 출발역 = 교대역.getId();
        Long 도착역 = 산본역.getId();

        // when
        ExtractableResponse<Response> response = PathAcceptanceTestSupport.최단경로_조회_요청(사용자토큰, 출발역, 도착역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("최단 경로를 구할 때, 해당 역이 없는 경우 예외 발생")
    public void getShortestNotExistStationOccurredException() {
        // given
        Long 출발역 = 교대역.getId();
        Long 도착역 = 100L;

        // when
        ExtractableResponse<Response> response = PathAcceptanceTestSupport.최단경로_조회_요청(사용자토큰, 출발역, 도착역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
