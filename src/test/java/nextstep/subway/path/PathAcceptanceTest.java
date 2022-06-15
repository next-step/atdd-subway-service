package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

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

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("출발역과 도착역 정보로 최단경로 요청시 경로 정보를 알려준다.")
    @Test
    void findShortedRoute() {
        // When
        final ExtractableResponse<Response> response = 최단_경로_검색(교대역, 양재역);

        // Then
        최단_경로_기준으로_지하철역_정보가_출력됨(response, Arrays.asList(양재역, 남부터미널역, 교대역));
    }

    /**
     * When : 등록되지 않은 지하철 역으로 요청 시
     * Then : 검색이 되지 않는다.
     */
    @DisplayName("등록되지 않은 지하철역 정보로 경로 요청시 검색이 되지 않는다.")
    @Test
    void findInvalidStationId() {
        // Given
        final StationResponse 등록되지않은역 = new StationResponse(100L, "잘못된역", LocalDateTime.now(), LocalDateTime.now());

        // When
        final ExtractableResponse<Response> reponse = 최단_경로_검색(등록되지않은역, 양재역);

        // Then
        검색이_안됨(reponse);
    }

    /**
     * Given : 출발역과 도착역이 서로 연결되어 있지 않은 상태에서
     * When : 출발역과 도착역 정보로 경로 검색 시
     * Then : 검색이 되지 않는다.
     */
    @DisplayName("출발역과 도착역 구간이 서로 연결 되어 있지 않으면 검색이 되지 않는다.")
    @Test
    void noConnectionSectionTest() {
        // Given
        final StationResponse 수원역 = StationAcceptanceTest.지하철역_등록되어_있음("수원역").as(StationResponse.class);
        final StationResponse 병점역 = StationAcceptanceTest.지하철역_등록되어_있음("병점역").as(StationResponse.class);
        final LineResponse 일호선 = 지하철_노선_등록되어_있음("일호선", "bg-red-600", 수원역, 병점역, 10);

        // When
        final ExtractableResponse<Response> response = 최단_경로_검색(교대역, 병점역);

        // Then
        검색이_안됨(response);
    }

    /**
     * When : 출발역과 도착역을 같은 값으로 경로 요청 시
     * Then : 검색이 되지 않는다.
     */
    @DisplayName("경로 검색시 출발역과 도착역이 같으면 검색이 안된다.")
    @Test
    void sameStationTest() {
        // When
        final ExtractableResponse<Response> response = 최단_경로_검색(교대역, 교대역);

        // Then
        검색이_안됨(response);
    }

    public static void 최단_경로_기준으로_지하철역_정보가_출력됨(ExtractableResponse<Response> response, List<StationResponse> expectedResult) {
        final PathResponse pathResponse = response.as(PathResponse.class);
        final List<String> resultStationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        final List<String> expectedStationNames = expectedResult.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(resultStationNames).containsExactlyElementsOf(expectedStationNames);
    }

    public ExtractableResponse<Response> 최단_경로_검색(final StationResponse source, final StationResponse target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse preStation, StationResponse downStation, int distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(name, color, preStation.getId(), downStation.getId(), 10)).as(LineResponse.class);
    }


    public static void 검색이_안됨(ExtractableResponse<Response> reponse) {
        assertThat(HttpStatus.valueOf(reponse.statusCode())).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
