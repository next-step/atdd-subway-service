package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_구간_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


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

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                    new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)
                ).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                    new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)
                ).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                    new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)
                ).as(LineResponse.class);

        지하철_구간_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("경로를 조회함")
    @Test
    void pathTest() {
        // when
        // 경로 조회를 요청함
        ExtractableResponse<Response> response = 경로_조회를_요청함(강남역.getId(), 교대역.getId());

        // then
        // 경로 조회됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 경로_조회를_요청함(Long sourceId, Long targetId) {
        PathRequest pathRequest = new PathRequest(sourceId, targetId);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(pathRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
        return response;
    }
}

