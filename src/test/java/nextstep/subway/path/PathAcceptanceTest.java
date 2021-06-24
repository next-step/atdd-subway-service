package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 판교역;
    private StationResponse 잠실역;

    /**
     * 잠실 - 20 - 강남 - 100 - 양재 - 10 - 판교
     * └---------- 10---------┘
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 100);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 10);

        LineRequest lineRequest2 = new LineRequest("이호선", "green", 잠실역.getId(), 강남역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest2).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 양재역, 10);
    }

    @DisplayName("출발역 도착역 동일할 경우 예외 발생")
    @Test
    void shortPathException1() {
        ExtractableResponse<Response> response = 최단_경로_요청(잠실역, 잠실역);

        출발역_도착역_동일할_경우_예외_발생함(response);
    }

    @DisplayName("출발역 도착역 연결 되어있지 않을 경우 예외 발생")
    @Test
    void shortPathException2() {
        StationResponse 구일역 = StationAcceptanceTest.지하철역_등록되어_있음("구일역").as(StationResponse.class);

        ExtractableResponse<Response> response = 최단_경로_요청(잠실역, 구일역);

        출발역_도착역_미연결시_경우_예외_발생함(response);
    }


    @DisplayName("출발역이나 도착역이 노선에 등록되어있지 않는경우 예외 발생")
    @Test
    void shortPathException3() {
        StationResponse 구일역 = StationAcceptanceTest.지하철역_등록되어_있음("구일역").as(StationResponse.class);
        StationResponse 구로역 = StationAcceptanceTest.지하철역_등록되어_있음("구로역").as(StationResponse.class);

        ExtractableResponse<Response> response = 최단_경로_요청(구일역, 구로역);

        출발역_도착역_노선_미등록시_예외_발생함(response);
    }

    @DisplayName("최단 경로와 거리를 구한다.")
    @Test
    void shortPath() {
        ExtractableResponse<Response> response = 최단_경로_요청(잠실역, 판교역);

        최단_경로와_거리_확인됨(response);
    }

    private ExtractableResponse<Response> 최단_경로_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={stationId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    private void 최단_경로와_거리_확인됨(ExtractableResponse<Response> response) {
        PathResponse pathResponse =  response.as(PathResponse.class);
        List<StationResponse> stationResponseList = pathResponse.getStations();

        assertThat(stationResponseList).hasSize(3);
        assertThat(stationResponseList).startsWith(잠실역);
        assertThat(stationResponseList.get(1)).isEqualTo(양재역);
        assertThat(stationResponseList).endsWith(판교역);

        최단_거리값_확인됨(pathResponse.getDistance());

    }

    private void 최단_거리값_확인됨(int distance) {
        assertThat(distance).isEqualTo(20);
    }

    private void 출발역_도착역_동일할_경우_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 출발역_도착역_미연결시_경우_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 출발역_도착역_노선_미등록시_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
