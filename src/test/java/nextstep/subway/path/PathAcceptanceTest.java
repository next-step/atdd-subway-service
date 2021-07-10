package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;


@DisplayName("지하철 경로 조회")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 오호선;
    private LineResponse 이호선;
    private LineResponse 일호선;
    private LineResponse 신분당선;

    private StationResponse 양평역;
    private StationResponse 영등포구청역;
    private StationResponse 영등포시장역;
    private StationResponse 신길역;
    private StationResponse 여의도역;
    private StationResponse 당산역;
    private StationResponse 영등포역;
    private StationResponse 대구역;
    private StationResponse 야탑역;
    private StationResponse 모란역;

    @BeforeAll
    public void setUp() {
        super.setUp();

        양평역 = 지하철역_등록되어_있음("양평역").as(StationResponse.class);
        영등포구청역 = 지하철역_등록되어_있음("영등포구청역").as(StationResponse.class);
        영등포시장역 = 지하철역_등록되어_있음("영등포시장역").as(StationResponse.class);
        신길역 = 지하철역_등록되어_있음("신길역").as(StationResponse.class);
        여의도역 = 지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        당산역 = 지하철역_등록되어_있음("당산역").as(StationResponse.class);
        영등포역 = 지하철역_등록되어_있음("영등포역").as(StationResponse.class);

        오호선 = 지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 양평역.getId(), 영등포구청역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 영등포구청역.getId(), 당산역.getId(), 10)).as(LineResponse.class);
        일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-red-600", 신길역.getId(), 영등포역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(오호선, 영등포구청역, 영등포시장역, 5);
        지하철_노선에_지하철역_등록_요청(오호선, 영등포시장역, 신길역, 10);
        지하철_노선에_지하철역_등록_요청(오호선, 신길역, 여의도역, 10);
    }

    /*
    # 지하철 노선도 #

                            (2호선)                          (1호선)
     (5호선)  양평역 - 10 - 영등포구청역 - 5 - 영등포시장역 - 10 - 신길역 - 10 여의도 역  (5호선)
                              ㅣ                               ㅣ
                              10                               5
                              ㅣ                               ㅣ
                             당산역                           영등포역
                             (2호선)                          (1호선)

     */


    @DisplayName("지하철 경로를 조회한다.")
    @TestFactory
    Stream<DynamicTest> findPaths_성공() {
        return Stream.of(
                dynamicTest("경로를 조회하고 확인한다.(0회 환승 / 출발역: 영등포구청역, 도착역: 신길역)", 경로_조회_요청_및_성공_확인(영등포구청역, 신길역, asList(영등포구청역, 영등포시장역, 신길역), 15)),
                dynamicTest("경로를 조회하고 확인한다.(1회 환승 / 출발역: 당산역, 도착역: 양평역)", 경로_조회_요청_및_성공_확인(당산역, 양평역, asList(당산역, 영등포구청역, 양평역), 20)),
                dynamicTest("경로를 조회하고 확인한다.(2회 환승 / 출발역: 영등포역, 도착역: 당산역)", 경로_조회_요청_및_성공_확인(영등포역, 당산역, asList(영등포역, 신길역, 영등포시장역, 영등포구청역, 당산역), 30))
        );
    }

    @DisplayName("지하철 경로 조회에 실패 한다.")
    @TestFactory
    Stream<DynamicTest> findPaths_실패() {
        대구역 = new StationResponse(Long.MAX_VALUE, "대구역", LocalDateTime.now(), LocalDateTime.now());
        야탑역 = 지하철역_등록되어_있음("야탑역").as(StationResponse.class);
        모란역 = 지하철역_등록되어_있음("모란역").as(StationResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 야탑역.getId(), 모란역.getId(), 10)).as(LineResponse.class);

        return Stream.of(
                dynamicTest("경로 조회에 실패한다. (출발지, 도착지 동일 / 출발역: 영등포구청역, 도착역: 영등포구청역)", 경로_조회_요청_및_실패_확인(영등포구청역, 영등포구청역)),
                dynamicTest("경로 조회에 실패한다. (존재 하지 않는 출발지 / 출발역: 대구역, 도착역: 양평역)", 경로_조회_요청_및_실패_확인(대구역, 양평역)),
                dynamicTest("경로 조회에 실패한다. (존재 하지 않는 도착지 / 출발역: 양평역, 도착역: 대구역)", 경로_조회_요청_및_실패_확인(양평역, 대구역)),
                dynamicTest("경로 조회에 실패한다. (연결 되어 있지 않은 출발지, 도착지 / 출발역: 영등포구청역, 도착역: 모란역)", 경로_조회_요청_및_실패_확인(영등포구청역, 모란역))
        );
    }

    private Executable 경로_조회_요청_및_실패_확인(StationResponse sourceStation, StationResponse targetStation) {
        return () -> {
            // when
            ExtractableResponse<Response> response = 경로_조회_요청(sourceStation, targetStation);

            // then
            경로_조회_실패_확인(response);
        };
    }

    private Executable 경로_조회_요청_및_성공_확인(StationResponse sourceStation, StationResponse targetStation, List<StationResponse> expectedStations, int expectedDistance) {
        return () -> {
            // when
            ExtractableResponse<Response> response = 경로_조회_요청(sourceStation, targetStation);

            // then
            경로_조회_성공_확인(response);
            경로상_경유_지하철역_확인(response.jsonPath().getList("stations", StationResponse.class), expectedStations);
            경로상_총_거리_확인(response.jsonPath().getInt("distance"), expectedDistance);
        };
    }

    private static ExtractableResponse<Response> 경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
        PathRequest pathRequest = new PathRequest(sourceStation.getId(), targetStation.getId());

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}"
                        , sourceStation.getId()
                        , targetStation.getId())
                .then().log().all()
                .extract();
    }

    private static void 경로_조회_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    private static void 경로_조회_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }

    private static void 경로상_경유_지하철역_확인(List<StationResponse> actualResult, List<StationResponse> expectedResult) {
        assertThat(actualResult.containsAll(expectedResult)).isTrue();
    }

    private static void 경로상_총_거리_확인(int actualResult, int expectedResult) {
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
