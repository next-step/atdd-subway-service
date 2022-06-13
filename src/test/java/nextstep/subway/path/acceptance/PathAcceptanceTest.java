package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선, 이호선, 삼호선, 일호선;
    private StationResponse 강남역, 양재역, 교대역, 남부터미널역, 용산역, 서울역, 없는역;

    /**
     * 교대역    --- *2호선* ---   강남역         서울역 --- *1호선* --- 용산역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
        용산역 = 지하철역_등록되어_있음("용산역").as(StationResponse.class);
        없는역 = new StationResponse(99L, "없는역", LocalDateTime.now(), LocalDateTime.now());

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-blue-100", 서울역.getId(), 용산역.getId(), 7)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 연결되어 있는 출발, 도착역을 경로조회 하면
     *  Then 최단거리 결과로 알려준다.
     */
    @Test
    @DisplayName("정상적인 출발, 도착역을 경로조회하면 최단거리를 알려준다.")
    void searchShortestPath() {
        // when
        ExtractableResponse<Response> 지하철_경로_조회_요청_결과 = 지하철_경로_조회_요청(강남역, 남부터미널역);

        // then
        최단_거리_확인(지하철_경로_조회_요청_결과, 12, Arrays.asList(강남역, 양재역, 남부터미널역));
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 연결되어 있지 않은 출발, 도착역을 경로조회 하면
     *  Then 경로 조회를 할 수 없다.
     */
    @Test
    @DisplayName("연결되어 있지 않은 출발, 도착역은 경로조회 할 수 없다.")
    void searchNotLinkedPath() {
        // when
        ExtractableResponse<Response> 올바르지_않은_지하철_경로_조회_결과 = 지하철_경로_조회_요청(강남역, 서울역);

        // then
        경로_조회_결과_실패(올바르지_않은_지하철_경로_조회_결과);
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 출발, 도착역이 같으면
     *  Then 경로 조회를 할 수 없다.
     */
    @Test
    @DisplayName("출발, 도착역이 동일하면 경로조회 할 수 없다.")
    void searchSameStationPath() {
        // when
        ExtractableResponse<Response> 동일한_지하철_경로_조회_결과 = 지하철_경로_조회_요청(강남역, 강남역);

        // then
        경로_조회_결과_실패(동일한_지하철_경로_조회_결과);
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 존재하지 않는 역을 경로조회 하면
     *  Then 경로 조회를 할 수 없다.
     */
    @Test
    @DisplayName("존재하지 않는 역은 경로조회 할 수 없다.")
    void searchNotExistStationPath() {
        // when
        ExtractableResponse<Response> 없는역_지하철_경로_조회_결과 = 지하철_경로_조회_요청(없는역, 강남역);

        // then
        경로_조회_결과_실패(없는역_지하철_경로_조회_결과);
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse 출발역, StationResponse 도착역) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", 출발역.getId());
        params.put("target", 도착역.getId());

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단_거리_확인(ExtractableResponse<Response> 지하철_경로_조회_요청_결과, int 거리, List<StationResponse> 역리스트) {
        PathResponse response = 지하철_경로_조회_요청_결과.as(PathResponse.class);

        List<Long> 응답_역_아이디_리스트 = response.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> 예상된_역_아이디_리스트 = 역리스트.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(거리),
                () -> assertThat(응답_역_아이디_리스트).containsExactlyElementsOf(예상된_역_아이디_리스트)
        );
    }

    private void 경로_조회_결과_실패(ExtractableResponse<Response> 응답_결과) {
        assertThat(응답_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
