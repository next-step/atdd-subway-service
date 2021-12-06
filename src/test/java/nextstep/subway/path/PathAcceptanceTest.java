package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.getIdsByStationResponses;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    StationResponse 교대역;
    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 남부터미널역;

    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //background
        //given
        // 지하철 역 등록되어 있음
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // 노선 등록되어 있음
        //신분당선 (강남-양재, 10)
        LineRequest lineRequest_신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_신분당선).as(LineResponse.class);

        //2호선 (교대-강남, 10)
        LineRequest lineRequest_이호선 = new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_이호선).as(LineResponse.class);

        //3호선 (교대-남부터미널-양재, 3-2)
        LineRequest lineRequest_삼호선 = new LineRequest("삼호선", "orange", 교대역.getId(), 남부터미널역.getId(), 3);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_삼호선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 2);

    }

    @Test
    @DisplayName("강남역-남부터미널역 최단 거리 경로 조회")
    void shortestPath_강남_남부터미널() {
        //when
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        //최단 경로 지하철 역 목록 조회됨 (강남역, 양재역, 남부터미널), 12
        최단_경로_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 남부터미널역), 12);
    }

    @Test
    @DisplayName("교대역-양재역 최단 거리 경로 조회")
    void shortestPath_교대_양재() {
        //when
        ExtractableResponse<Response> response = 경로_조회_요청(교대역.getId(), 양재역.getId());

        //then
        // 최단 경로 지하철 역 목록 조회됨 (교대역, 남부터미널역, 양재역), 5
        최단_경로_지하철역_순서_정렬됨(response, Arrays.asList(교대역, 남부터미널역, 양재역), 5);

    }

    private void 최단_경로_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
        List<StationResponse> expectedStations, int distance) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = getIdsByStationResponses(pathResponse.getStations());
        List<Long> expectedStationIds = getIdsByStationResponses(expectedStations);
        // 역 순서 정렬
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
        // 최단 경로
        assertThat(pathResponse.getDistance()).isEqualTo(distance);

    }

    private ExtractableResponse<Response> 경로_조회_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source)
            .param("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

}
