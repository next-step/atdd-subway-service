package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 신도림역;
    private StationResponse 구로역;
    private StationResponse 판교역;
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

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        신도림역 = 지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        구로역 = 지하철역_등록되어_있음("구로역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 100, 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 300, 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 양재역.getId(), 남부터미널역.getId(), 5))
            .as(LineResponse.class);
        일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-red-600", 신도림역.getId(), 구로역.getId(), 5))
            .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 교대역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    public void findPath() {
        // when
        final ExtractableResponse<Response> response = 지하철역_최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철역_최단_경로_응답됨(response, HttpStatus.OK);
        지하철역_최단_경로_일치함(response.as(PathResponse.class), 8,
            Arrays.asList(교대역.toStation(), 남부터미널역.toStation(), 양재역.toStation()));

    }

    @DisplayName("최단 경로 조회 예외를 처리한다.")
    @Test
    void given_ExceptionCase_when_FindShortestPath_then_ReturnBadRequest() {
        // 출발역과 도착역이 같음
        // when
        final ExtractableResponse<Response> response = 지하철역_최단_경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        지하철역_최단_경로_응답됨(response, HttpStatus.BAD_REQUEST);

        // 출발역과 도착역이 연결되어 있지 않음
        // when
        final ExtractableResponse<Response> response2 = 지하철역_최단_경로_조회_요청(교대역.getId(), 구로역.getId());

        // then
        지하철역_최단_경로_응답됨(response2, HttpStatus.BAD_REQUEST);

        // 존재하지 않는 출발역이나 도착역을 조회함
        // when
        final ExtractableResponse<Response> response3 = 지하철역_최단_경로_조회_요청(교대역.getId(), 판교역.getId());

        // then
        지하철역_최단_경로_응답됨(response3, HttpStatus.BAD_REQUEST);
    }

    public static ExtractableResponse<Response> 지하철역_최단_경로_조회_요청(final Long sourceId, final Long targetId) {
        return RestAssured
            .given().log().all()
            .queryParam("source", sourceId)
            .queryParam("target", targetId)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    private void 지하철역_최단_경로_응답됨(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private void 지하철역_최단_경로_일치함(final PathResponse pathResponse, final int expectedDistance,
        final List<Station> expectedStations) {
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
        assertThat(pathResponse.getStations()).isEqualTo(expectedStations);
    }
}
