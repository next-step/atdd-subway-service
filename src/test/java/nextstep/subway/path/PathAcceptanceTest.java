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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }


    @DisplayName("경로 조회")
    @Test
    void findPath() {
        // When 지하철 경로 조회 요청
        PathResponse pathResponse = 지하철_경로_조회_요청(남부터미널역, 강남역);
        // Then 지하철 경로 조회됨
        지하철_경로_조회됨(pathResponse, Arrays.asList(남부터미널역, 교대역, 양재역, 강남역));
        // AND 지하철 경로 길이가 예상과 같음
        지하철_경로_길이_같음(pathResponse, 12);
    }

    private void 지하철_경로_길이_같음(PathResponse pathResponse, int expectedDistance) {
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private void 지하철_경로_조회됨(PathResponse pathResponse, List<StationResponse> expectedStations) {
        for (int i = 0; i < expectedStations.size(); i++) {
            assertThat(expectedStations.get(i)).isEqualTo(pathResponse.getStations().get(i));
        }
    }

    private PathResponse 지하철_경로_조회_요청(StationResponse 남부터미널역, StationResponse 강남역) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}}", 남부터미널역.getId(), 강남역.getId())
                .then().log().all()
                .extract()
                .body()
                .as(PathResponse.class);
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStationResponse,
                                        StationResponse downStationResponse, int distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(lineName, color, upStationResponse.getId(), downStationResponse.getId(), distance))
                .body().as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStationResponse, StationResponse downStationResponse, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse, upStationResponse, downStationResponse, distance);
    }

}
