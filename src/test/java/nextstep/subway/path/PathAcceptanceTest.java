package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.ApiRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 이촌역;
    private StationResponse 삼각지역;
    private StationResponse 새로운역1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        이촌역 = StationAcceptanceTest.지하철역_등록되어_있음("이촌역").as(StationResponse.class);
        삼각지역 = StationAcceptanceTest.지하철역_등록되어_있음("삼각지역").as(StationResponse.class);
        새로운역1 = StationAcceptanceTest.지하철역_등록되어_있음("새로운역1").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 이촌역, 삼각지역, 5);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void searchPath() {
        LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        ExtractableResponse<Response> searchResponse = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 남부터미널역);
        최단_경로를_반영한_역들이_조회됨(searchResponse, 강남역, 양재역, 남부터미널역);

        ExtractableResponse<Response> searchResponseSameStation = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 강남역);
        최단_경로_조회_실패(searchResponseSameStation);

        ExtractableResponse<Response> searchResponseUnConnectedStation = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 이촌역);
        최단_경로_조회_실패(searchResponseUnConnectedStation);

        ExtractableResponse<Response> searchResponseUnRelatedStation = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 새로운역1);
        최단_경로_조회_실패(searchResponseUnRelatedStation);
    }

    private void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ListAssert<StationResponse> 최단_경로를_반영한_역들이_조회됨(ExtractableResponse<Response> searchResponse, StationResponse... stationResponses) {
        return assertThat(searchResponse.jsonPath().getList("stations", StationResponse.class)).containsExactly(stationResponses);
    }

    private ExtractableResponse<Response> 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(StationResponse source, StationResponse target) {
        return ApiRequest.get("/paths?source=" + source.getId() + "&target=" + target.getId());
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance))
                .as(LineResponse.class);
    }

    public static void 지하철_노선에_지하철역_결과_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}
