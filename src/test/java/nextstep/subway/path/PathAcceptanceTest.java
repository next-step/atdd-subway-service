package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.step.LineAcceptanceStep;
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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceStep.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceStep.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceStep.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
    }

    @DisplayName("경로찾기")
    @Test
    void findPath() {
        PathRequest request = new PathRequest(강남역.getId(), 양재역.getId());

        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(request);

        assertThat(response.as(PathResponse.class).getStations())
                .extracting("id")
                .containsExactly(강남역.getId(), 양재역.getId());
    }

    @DisplayName("경로 찾기 예외 - 같은 역 조회")
    @Test
    void validateIsSame() {
        PathRequest request = new PathRequest(강남역.getId(), 강남역.getId());

        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("경로 찾기 예외 - 포함되지 않은 역 조회")
    @Test
    void validateNotContain() {
        StationResponse 왕십리역 = StationAcceptanceTest.지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        StationResponse 잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        PathRequest request = new PathRequest(왕십리역.getId(), 잠실역.getId());

        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("경로 찾기 예외 - 환승되지 않은 역 조회")
    @Test
    void validateRelated() {
        PathRequest request = new PathRequest(양재역.getId(), 남부터미널역.getId());

        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(PathRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
