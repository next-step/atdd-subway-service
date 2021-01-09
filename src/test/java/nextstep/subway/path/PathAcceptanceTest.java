package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    //최단경로 조회 인수테스트

    //given
    // 여러 역이 등록되어 있는 라인들

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 왕십리역;
    private StationResponse 군자역;

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
        왕십리역 = StationAcceptanceTest.지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        군자역 = StationAcceptanceTest.지하철역_등록되어_있음("군자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 20).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 양재역, 남부터미널역, 5).as(LineResponse.class);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 왕십리역, 군자역, 5).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 교대역, 3);
    }

    @DisplayName("최단경로 찾기")
    @Test
    void manageSubwayPath() {
        //when
        ExtractableResponse<Response> findResponse = 지하철_최단경로_조회_요청(강남역.getId(), 교대역.getId());
        //then
        지하철_최단경로_조회됨(findResponse);
        지하철_최단경로_순서_정렬됨(findResponse, Arrays.asList(강남역, 양재역, 남부터미널역, 교대역));
        지하철_최단경로_거리_확인됨(findResponse, 18L);

        //when 최단경로조회(예외상황1) : 동일한 출발역과 도착역을 전달했을 경우 => id 값 비교로 알 수 있음
        ExtractableResponse<Response> findResponse2 = 지하철_최단경로_조회_요청(양재역.getId(), 양재역.getId());
        //then
        지하철_최단경로_조회_실패됨(findResponse2);

        //when 최단경로조회(예외상황2) : 연결되어 있지 않은 출발, 도착역일 경우 => 경로찾기 알고리즘을 도입했을 경우에 찾을 수 있음
        ExtractableResponse<Response> findResponse3 = 지하철_최단경로_조회_요청(왕십리역.getId(), 양재역.getId());
        //then
        지하철_최단경로_조회_실패됨(findResponse3);

        //when 최단경로조회(예외상황3) : 존재하지 않은 출발역 또는 도착역을 조회할 경우 => station 조회하면 알 수 있음.
        ExtractableResponse<Response> findResponse4 = 지하철_최단경로_조회_요청(10L, 양재역.getId());
        //then
        지하철_최단경로_조회_실패됨(findResponse4);

    }

    private void 지하철_최단경로_거리_확인됨(ExtractableResponse<Response> findResponse, long expectedDistance) {
        PathResponse path = findResponse.as(PathResponse.class);
        assertThat(path.getDistance()).isEqualTo(expectedDistance);
    }

    private void 지하철_최단경로_순서_정렬됨(ExtractableResponse<Response> findResponse, List<StationResponse> expectedStations) {
        PathResponse path = findResponse.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private void 지하철_최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철_최단경로_조회_요청(Long sourceId, Long targetId) {
        return  RestAssured.
                given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().get("/paths?source={sourceId}&target={targetID}", sourceId, targetId).
                then().log().all().
                extract();
    }


    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance));
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선에_지하철역_등록_요청(line, new SectionRequest(upStation.getId(), downStation.getId(), distance));
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }
}
