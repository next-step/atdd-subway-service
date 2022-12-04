package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.ErrorMessage;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.ErrorResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 분당선;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 오리역;
    private StationResponse 수내역;

    /**
     * 교대역    --- *2호선* 10 ---   강남역
     * |                        |
     * *3호선* 3                  *신분당선* 10
     * |                        |
     * 남부터미널역  --- *3호선* 2---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        오리역 = StationAcceptanceTest.지하철역_등록되어_있음("오리역").as(StationResponse.class);
        수내역 = StationAcceptanceTest.지하철역_등록되어_있음("수내역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("분당선", "bg-yellow-600", 오리역.getId(), 수내역.getId(), 10)).as(LineResponse.class);


    }

    /**
     * Given 경로가 생성되어 있고, 두개 역이 해당 경로 위에 존재할때
     * When 두개 역 사이의 최단경로를 조회하면
     * Then 최단 경로 거리, 최단 경로를 확인할 수 있다.
     */
    @DisplayName("두 역 사이의 최단경로를 조회할 수 있다.")
    @Test
    void pathFind_success() {
        // given
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 남부터미널역.getId());
        // then
        경로가_일치함(response, "강남역", "양재역", "남부터미널역");
        경로의_총_거리가_일치함(response, 12);
    }
    /**
     * Given 경로가 생성되어 있고, 두개 역이 해당 경로 위에 존재할때
     * When 경로 조회를 요청하는 두개 역이 같으면
     * Then 400 BAD_REQUEST 반환한다.
     */
    @DisplayName("동일한 두개 역의 경로를 조회하면 BAD_REQUEST 반환한다.")
    @Test
    void pathFind_exception_same_station() {
        // given

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 강남역.getId());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.as(ErrorResponse.class).getMessage()).isEqualTo(ErrorMessage.FIND_PATH_SAME_STATION);
    }
    /**
     * Given 경로가 생성되어 있고, 두개 역이 해당 경로 위에 존재할때
     * When 경로 조회를 요청하는 두개 역이 연결되어 있지 않으면 (connected 검증)
     * Then 400 BAD_REQUEST 반환한다.
     */
    @DisplayName("연결되지 않은 두개 역의 경로를 조회하면 BAD_REQUEST 반환한다")
    @Test
    void pathFind_exception_not_connected_station() {
        // given

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 수내역.getId());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.as(ErrorResponse.class).getMessage()).isEqualTo(ErrorMessage.FIND_PATH_NOT_CONNECTED);
    }

    /**
     * Given 경로가 생성되어 있고
     * When 경로를 조회하는 두개 역중 하나라도 해당 경로 위에 존재하지 않는다면
     * Then 400 BAD_REQUEST 반환한다.
     */
    @DisplayName("경로위에 존재하지 않는 역의 경로를 조회하면 BAD_REQUEST 반환한다")
    @Test
    void pathFind_exception_station_not_on_path() {
        // given

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 남부터미널역.getId());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.as(ErrorResponse.class).getMessage()).isEqualTo(ErrorMessage.FIND_PATH_OF_STATION_NOT_ON_GRAPH);
    }

    // 필요 한 것 : 1. 경로의 역들을 조회 -> 경로 위에 두개 역 조회 / 2. 경로의 역이 연결되어 있는지 조회 /

    // 경로조회 api

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={source}&target={target}",source, target)
                .then().log().all()
                .extract();
    }

    public static void 경로가_일치함(ExtractableResponse response, String... 역들) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class).getStations().stream().map(it -> it.getName()).collect(
                Collectors.toList())).containsExactly(역들);
    }

    public static void 경로의_총_거리가_일치함(ExtractableResponse response, int 거리) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(거리);
    }
    
}
