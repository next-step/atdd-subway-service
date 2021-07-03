package nextstep.subway.path;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.common.dto.ErrorResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.DuplicatePathException;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exeption.NotFoundStationException;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선, 이호선, 삼호선, 분당선;
    private StationResponse 강남역, 양재역, 교대역, 남부터미널역, 선릉역, 수원역;

    /**               (10)
     *  교대역    --- *2호선* ---   강남역
     *  |                        |
     *  *3호선* (3)               *신분당선* (10)
     *  |               (2)      |
     *  남부터미널역  -- *3호선* -- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        수원역 = StationAcceptanceTest.지하철역_등록되어_있음("수원역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5))
            .as(LineResponse.class);
        분당선 = 지하철_노선_등록되어_있음(new LineRequest("분당선", "bg-yellow-600", 선릉역.getId(), 수원역.getId(), 100))
            .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("경로 조회 시나리오")
    void findPathScenario() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역, 남부터미널역);
        // then
        지하철_경로_응답됨(response);
        지하철역_목록_경로_포함됨(response, Arrays.asList(강남역.getId(), 양재역.getId(), 남부터미널역.getId()), 12);

        // when
        response = 지하철_경로_조회_요청(강남역, 강남역);
        // then
        지하철역_목록_경로_조회_출발역_동일로_인한_실패(response);

        // when
        response = 지하철_경로_조회_요청(강남역, 수원역);
        // then
        지하철역_목록_경로_조회_연결_되지_않은_경로로_인한_실패(response);

        // when
        StationResponse 동두천 = new StationResponse(101L, "동두천역", LocalDateTime.now(), LocalDateTime.now());
        response = 지하철_경로_조회_요청(동두천, 강남역);
        // then
        지하철역_목록_경로_조회_등록_되지_않은_경로로_인한_실패(response);

    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source.getId())
            .queryParam("target", target.getId())
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    private void 지하철_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_목록_경로_포함됨(ExtractableResponse<Response> response, List<Long> ids, int min) {
        PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);
        List<Long> stationIds = pathResponse.getStations().stream().map(StationResponse::getId).collect(toList());

        assertThat(ids).containsAll(stationIds);
        assertThat(min).isEqualTo(pathResponse.getDistance());
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation,
        StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

    private void 지하철역_목록_경로_조회_등록_되지_않은_경로로_인한_실패(ExtractableResponse<Response> response) {
        요청_처리_실패(response, new NotFoundStationException());
    }

    private void 지하철역_목록_경로_조회_연결_되지_않은_경로로_인한_실패(ExtractableResponse<Response> response) {
        요청_처리_실패(response, new NotConnectedPathException());
    }

    private void 지하철역_목록_경로_조회_출발역_동일로_인한_실패(ExtractableResponse<Response> response) {
        요청_처리_실패(response, new DuplicatePathException());
    }

    private void 요청_처리_실패(ExtractableResponse<Response> response, Exception e) {
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).contains(e.getMessage());
    }
}
