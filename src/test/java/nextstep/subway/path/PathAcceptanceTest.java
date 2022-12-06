package nextstep.subway.path;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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

import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


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

        LineRequest 신분당선_등록 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        LineRequest 이호선_등록 = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10);
        LineRequest 삼호선_등록 = new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_등록).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_등록).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_등록).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void 최단_경로_조회() {
        ExtractableResponse<Response> response = 최단_경로_찾기(강남역, 남부터미널역);
        PathResponse pathResponse = response.as(PathResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getStations()
                        .stream().map(it -> it.getName())
                        .collect(Collectors.toList())).containsExactly("강남역", "양재역", "남부터미널역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12)
        );
    }

    public static ExtractableResponse<Response> 최단_경로_찾기(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
                .then().log().all()
                .extract();
    }
}
