package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
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
    }

    /**
     * Given 노선을 생성하고
     * When 출발역과 도착역이 같게 설정하여 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("같은 출발역과 도착역을 입력하여 최단 거리를 검색한다.")
    @Test
    void 최단경로조회_출발역_도착역_같음() {
        // given
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10));

        // when
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 교대역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 노선을 생성하고
     * When 연결되어 있지 않은 출발역과 도착역의 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("연결되어 있지 않은 출발역과 도착역을 입력하여 최단 거리를 검색한다.")
    @Test
    void 최단경로조회_출발역_도착역_연결안됨() {
        // given
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10));
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("삼호선", "orange", 남부터미널역.getId(), 양재역.getId(), 5));

        // when
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 양재역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 노선을 생성하고
     * When 어떤 구간에도 포함되어 있지 않은 출발역이나 도착역으로 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("어떤 구간에도 포함되어 있지 않은 출발역이나 도착역으로 최단 거리를 검색한다.")
    @Test
    void 최단경로조회_출발역_도착역_구간_미포함() {
        // given
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10));

        // when
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 양재역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 노선을 생성하고
     * When 존재하지 않는 출발역이나 도착역으로 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("존재하지 않는 출발역이나 도착역으로 최단 거리를 검색한다.")
    @Test
    void 최단경로조회_출발역_도착역_존재하지않음() {
        // given
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10));

        // when
        ExtractableResponse<Response> response = 최단_경로_조회(-1L, 교대역.getId());

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    /**
     * Given 노선을 생성하고
     * When 출발역과 도착역을 입력하면
     * Then 최단 거리를 검색할 수 있다
     */
    @DisplayName("출발역과 도착역을 입력하여 최단 거리를 검색한다.")
    @Test
    void 최단경로조회_정상() {
        // given
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 10));
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10));
        LineResponse 삼호선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("삼호선", "orange", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        // when
        PathResponse pathResponse = 최단_경로_조회(양재역.getId(), 교대역.getId()).as(PathResponse.class);

        // then
        assertThat(pathResponse).satisfies(path -> {
            assertThat(path.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                    .containsExactly("양재역", "남부터미널역", "교대역");
            assertEquals(5, path.getDistance());
        });
    }

    private ExtractableResponse<Response> 최단_경로_조회(Long source, Long target) {
        PathRequest pathRequest = new PathRequest(source, target);

        return RestAssured.given().log().all()
                .body(pathRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/paths")
                .then().log().all()
                .extract();
    }
}
