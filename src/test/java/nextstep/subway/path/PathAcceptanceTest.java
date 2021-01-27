package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
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

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철역 최단경로 조회")
    @Test
    void selectMinimumPath() {
        // given when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 남부터미널역);

        // then
        API_요청_확인(response, HttpStatus.OK);
        지하철_최단경로_조회_확인(response, Arrays.asList("강남역", "양재역", "남부터미널역"));
    }

    @DisplayName("노선 연결이 되어있지 않으면 실패")
    @Test
    void selectPathNotConnected() {
        // given
        StationResponse 의정부역 = StationAcceptanceTest.지하철역_등록되어_있음("의정부역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(의정부역, 남부터미널역);

        // then
        API_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("존재하지 않는 지하철역 조회 시 실패")
    @Test
    void selectNotFoundStation() {
        StationResponse 의정부역 = new StationResponse(100L, "의정부역", LocalDateTime.now(), LocalDateTime.now());

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(의정부역, 남부터미널역);

        // then
        API_요청_확인(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse startStation, StationResponse arrivalStation) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(String.format("/paths?source={id}&target={id}", startStation.getId(), arrivalStation.getId()))
                .then().log().all()
                .extract();
    }

    private void 지하철_최단경로_조회_확인(ExtractableResponse<Response> response, List<String> expectedStations) {

    }

    private void API_요청_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }
}
