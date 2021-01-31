package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
@SuppressWarnings("NonAsciiCharacters")
public class PathFinderAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 당산역;

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
        당산역 = StationAcceptanceTest.지하철역_등록되어_있음("당산역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private LineResponse 지하철_노선_등록되어_있음(String line, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(line, color, upStation.getId(), downStation.getId(), distance);

        return LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse, upStation, downStation, distance);
    }

    @DisplayName("최단 경로 조회")
    @Test
    void 최단경로_조회_테스트() {
        // when
        ExtractableResponse<Response> 최단_경로_조회 = 최단_경로_조회_요청(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(최단_경로_조회.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    void 출발역_도착역_연결_안됨() {
        // when
        ExtractableResponse<Response> 최단_경로_조회 = 최단_경로_조회_요청(교대역, 당산역);

        // then
        assertAll(
                () -> assertThat(최단_경로_조회.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("sourceId", source.getId())
                .pathParam("targetId", target.getId())
                .when().get("/paths?source={sourceId}&target={targetId}")
                .then().log().all().extract();
    }
}
