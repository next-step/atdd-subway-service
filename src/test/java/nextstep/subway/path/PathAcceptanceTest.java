package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 이수역;
    private StationResponse 동작역;

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
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        이수역 = StationAcceptanceTest.지하철역_등록되어_있음("이수역").as(StationResponse.class);
        동작역 = StationAcceptanceTest.지하철역_등록되어_있음("동작역").as(StationResponse.class);

        지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 4);
        지하철_노선_등록되어_있음("이호선", "bg-green-500", 교대역, 강남역, 10);
        지하철_노선_등록되어_있음("사호선", "bg-blue-300", 사당역, 이수역, 5);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-400", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @TestFactory
    Stream<DynamicTest> 지하철_경로_조회_시나리오() {
        return Stream.of(
                dynamicTest("출발역과 도착역에 대한 최단 거리를 조회한다.", this::출발역과_도착역에_대한_최단_거리를_조회한다),
                dynamicTest("출박역과 도착역이 같은 경우 조회할 수 없다.", this::출발역과_도착역이_같은_경우_조회할_수_없다),
                dynamicTest("출발역과 도착역이 연결되어 있지 않으면 조회할 수 없다.", this::출발역과_도착역이_연결되어_있지_않으면_조회할_수_없다),
                dynamicTest("출발역이 노선에 존재하지 않으면 조회할 수 없다.", this::출발역이_노선에_존재하지_않으면_조회할_수_없다),
                dynamicTest("도착역이 노선에 존재하지 않으면 조회할 수 없다.", this::도착역이_노선에_존재하지_않으면_조회할_수_없다)
        );
    }

    private void 출발역과_도착역에_대한_최단_거리를_조회한다() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역, 교대역);

        // then
        최단_경로_목록_응답됨(최단_경로_조회_응답);
    }

    private void 출발역과_도착역이_같은_경우_조회할_수_없다() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역, 강남역);

        // then
        최단_경로_조회_실패(최단_경로_조회_응답);
    }

    private void 출발역과_도착역이_연결되어_있지_않으면_조회할_수_없다() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역, 사당역);

        // then
        최단_경로_조회_실패(최단_경로_조회_응답);
    }

    private void 출발역이_노선에_존재하지_않으면_조회할_수_없다() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(동작역, 사당역);

        // then
        최단_경로_조회_실패(최단_경로_조회_응답);
    }

    private void 도착역이_노선에_존재하지_않으면_조회할_수_없다() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(사당역, 동작역);

        // then
        최단_경로_조회_실패(최단_경로_조회_응답);
    }

    private static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse upStation, StationResponse downStation) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={upStationId}&target={downStationId}", upStation.getId(), downStation.getId())
                .then().log().all()
                .extract();
    }

    private static void 최단_경로_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(9);
    }

    private static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
