package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private LineResponse 일호선;
    private StationResponse 안양역;
    private StationResponse 수원역;

    /**
     * 교대역    --- *2호선* ---   강남역
     *    |                        |
     * *3호선*                   *신분당선*
     *    |                        |
     * 남부터미널역 --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

        안양역 = StationAcceptanceTest.지하철역_등록되어_있음("안양역").as(StationResponse.class);
        수원역 = StationAcceptanceTest.지하철역_등록되어_있음("수원역").as(StationResponse.class);

        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-blue-600", 안양역, 수원역, 3).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void 출발역과_도착역이_같은_경우() {
        // when
        ExtractableResponse<Response> response
            = 지하철_경로_요청됨(강남역, 강남역);

        // then
        지하철_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
        // when
        ExtractableResponse<Response> response
            = 지하철_경로_요청됨(강남역, 안양역);

        // then
        지하철_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void 존재하지_않은_출발역이나_도착역을_조회_할_경우() {
        // when
        ExtractableResponse<Response> response
            = 지하철_경로_요청됨(-1L, -2L);

        // then
        지하철_경로_응답_실패됨(response);
    }

    @Test
    @DisplayName("최단 경로 역 목록과 총 거리를 반환한다")
    void 최단_경로_역_목록과_총_거리를_반환한다() {
        // when
        ExtractableResponse<Response> response
            = 지하철_경로_요청됨(교대역, 양재역);

        // then
        지하철_경로_응답됨(response);
    }

    private ExtractableResponse<Response> 지하철_경로_요청됨(StationResponse source, StationResponse target) {
        return 지하철_경로_요청됨(source.getId(), target.getId());
    }

    private ExtractableResponse<Response> 지하철_경로_요청됨(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths")
            .then().log().all().extract();

        return response;
    }

    private void 지하철_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // TODO 검증로직 구현 필요
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(-1);

        List<StationResponse> stations = response.jsonPath()
            .getList("stations", StationResponse.class);
        assertThat(stations.size()).isEqualTo(-1);
        assertThat(stations).containsExactly(null);
    }

    private void 지하철_경로_응답_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
