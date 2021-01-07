package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_PATH_URI = "/paths";

    private StationResponse 김포공항역;    // 5호선, 공항철도
    private StationResponse 까치산역;      // 2호선, 5호선
    private StationResponse 교대역;        // 2호선, 3호선
    private StationResponse 강남역;        // 2호선, 신분당선
    private StationResponse 남부터미널역;  // 3호선
    private StationResponse 양재역;        // 3호선, 신분당선
    private StationResponse 영등포역;      // 1호선
    private StationResponse 주안역;        // 1호선

    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private LineResponse 신분당선;

    @BeforeEach
    void setUpParam() {
        김포공항역 = 지하철역_등록되어_있음("김포공항역").as(StationResponse.class);
        까치산역 = 지하철역_등록되어_있음("까치산역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        영등포역 = 지하철역_등록되어_있음("영등포역").as(StationResponse.class);
        주안역 = 지하철역_등록되어_있음("주안역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(
            new LineRequest("2호선", "#009D3E", 까치산역.getId(), 교대역.getId(), 16)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 교대역, 강남역, 1);

        삼호선 = 지하철_노선_등록되어_있음(
            new LineRequest("삼호선", "#EF7C1C", 교대역.getId(), 남부터미널역.getId(), 2)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 2);

        오호선 = 지하철_노선_등록되어_있음(
            new LineRequest("오호선", "#996CAC", 김포공항역.getId(), 까치산역.getId(), 8)).as(LineResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
            new LineRequest("신분당선", "#D4003B", 강남역.getId(), 양재역.getId(), 2)).as(LineResponse.class);

        일호선 = 지하철_노선_등록되어_있음(
            new LineRequest("일호선", "#0052A4", 영등포역.getId(), 주안역.getId(), 17)).as(LineResponse.class);
    }

    @DisplayName("최단 경로 조회하기.")
    @Test
    void findShortestPath() {
        final ExtractableResponse<Response> response = 최단경로_요청(김포공항역.getId(), 양재역.getId());
        final PathFinderResponse resultFindPath = response.as(PathFinderResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultFindPath.getStations().stream().map(StationResponse::getName).collect(toList()))
            .containsExactlyElementsOf(Arrays.asList(김포공항역.getName(), 까치산역.getName(), 교대역.getName(), 강남역.getName(), 양재역.getName()));
        assertThat(resultFindPath.getDistance()).isEqualTo(27);
    }

    private static ExtractableResponse<Response> 최단경로_요청(final long departureId, final long arrivalId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(DEFAULT_PATH_URI + "?source=" + departureId + "&target=" + arrivalId)
            .then().log().all().
                extract();
    }

    @DisplayName("출발역과 도착역이 같을 때 에러를 반환한다.")
    @Test
    void findShortestPathWhenDepartureAndArrivalAreSame() {
        final ExtractableResponse<Response> response = 최단경로_요청(김포공항역.getId(), 김포공항역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("출발역에서 도착역으로 이동할 수 없는 경우 에러를 반환한다.")
    @Test
    void findShortestPathWhenCanNotMoveToArrivalFromDeparture() {
        final ExtractableResponse<Response> response = 최단경로_요청(김포공항역.getId(), 영등포역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
