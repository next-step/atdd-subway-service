package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_조회_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 신논현역, 언주역, 선정릉역, 강남역, 역삼역, 선릉역, 양재역, 매봉역, 도곡역, 한티역;
    private LineResponse _9호선, _3호선, _2호선, 분당선, 신분당선;
    public static final String PATH_FINDER_URL_TEMPLATE = "/paths?source={source}&target={target}";

    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철역_생성();
        노선_생성();
        구간_생성();
    }

    @Test
    @DisplayName("최단 경로 조회 기능의 인수테스트를 위해 생성한 노선 및 지하철 역 확인")
    public void checkLinesAndStations() {
        // When
        ExtractableResponse<Response> _9호선_조회_응답 = 지하철_노선_조회_요청(_9호선);
        ExtractableResponse<Response> _2호선_조회_응답 = 지하철_노선_조회_요청(_2호선);
        ExtractableResponse<Response> _3호선__조회_응답 = 지하철_노선_조회_요청(_3호선);
        ExtractableResponse<Response> 분당선_조회_응답 = 지하철_노선_조회_요청(분당선);
        ExtractableResponse<Response> 신분당선_조회_응답 = 지하철_노선_조회_요청(신분당선);

        // Then
        지하철역_목록_포함됨(_9호선_조회_응답, Arrays.asList(신논현역, 언주역, 선정릉역));
        지하철역_목록_포함됨(_2호선_조회_응답, Arrays.asList(강남역, 역삼역, 선릉역));
        지하철역_목록_포함됨(_3호선__조회_응답, Arrays.asList(양재역, 매봉역, 도곡역));
        지하철역_목록_포함됨(분당선_조회_응답, Arrays.asList(선정릉역, 선릉역, 한티역, 도곡역));
        지하철역_목록_포함됨(신분당선_조회_응답, Arrays.asList(신논현역, 강남역, 양재역));
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<StationResponse> createdResponses) {
        List<Long> expectedStationIds = createdResponses.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> resultStationIds = response.jsonPath().getList("stations", StationResponse.class).stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    @Test
    @DisplayName("지하철 최단 경로를 조회한다.")
    public void findShortestPath() {
        // When
        ExtractableResponse<Response> response = 최단_경로_조회_요청(선정릉역, 양재역);

        // Then
        최단_경로_조회됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우, 최단 경로를 조회할 수 없다.")
    public void throwException_WhenSourceStationIsEqualToTargetStation() {
        // When
        ExtractableResponse<Response> response = 최단_경로_조회_요청(선정릉역, 양재역);

        // Then
        최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우, 최단 경로를 조회할 수 없다.")
    public void throwException_WhenSourceStationAndTargetStationIsNotConnected() {
        // When
        ExtractableResponse<Response> response = 최단_경로_조회_요청(선정릉역, 양재역);

        // Then
        최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("출발역이나 도착역이 존재하지 않는 경우, 최단 경로를 조회할 수 없다.")
    public void throwException_When() {
        // When
        ExtractableResponse<Response> response = 최단_경로_조회_요청(선정릉역, 양재역);

        // Then
        최단_경로_조회_실패됨(response);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse 출발역, StationResponse 목적역) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(PATH_FINDER_URL_TEMPLATE, 출발역.getId(), 목적역.getId())
            .then().log().all()
            .extract();
    }

    private void 최단_경로_조회됨(ExtractableResponse<Response> response, StationResponse... expected) {
        List<StationResponse> actual = response.jsonPath().getList(".", StationResponse.class);
        assertThat(actual).containsExactly(expected);
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철역_생성() {
        신논현역 = StationAcceptanceTest.지하철역_등록되어_있음("신논현역").as(StationResponse.class);
        언주역 = StationAcceptanceTest.지하철역_등록되어_있음("언주역").as(StationResponse.class);
        선정릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선정릉역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        매봉역 = StationAcceptanceTest.지하철역_등록되어_있음("매봉역").as(StationResponse.class);
        도곡역 = StationAcceptanceTest.지하철역_등록되어_있음("도곡역").as(StationResponse.class);
        한티역 = StationAcceptanceTest.지하철역_등록되어_있음("한티역").as(StationResponse.class);
    }

    private void 노선_생성() {
        _9호선 = 지하철_노선_등록되어_있음(new LineRequest("9호선", "brown", 신논현역.getId(), 선정릉역.getId(), 25)).as(LineResponse.class);
        _2호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 강남역.getId(), 선릉역.getId(), 16)).as(LineResponse.class);
        _3호선 = 지하철_노선_등록되어_있음(new LineRequest("3호선", "orange", 양재역.getId(), 도곡역.getId(), 18)).as(LineResponse.class);
        분당선 = 지하철_노선_등록되어_있음(new LineRequest("분당선", "yellow", 선정릉역.getId(), 도곡역.getId(), 19)).as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "red", 신논현역.getId(), 양재역.getId(), 17)).as(LineResponse.class);
    }

    private void 구간_생성() {
        구간_추가(_9호선, 신논현역, 언주역, 7);
        구간_추가(_2호선, 강남역, 역삼역, 6);
        구간_추가(_3호선, 양재역, 매봉역, 10);
        구간_추가(분당선, 선정릉역, 선릉역, 4);
        구간_추가(분당선, 선릉역, 한티역, 9);
        구간_추가(신분당선, 신논현역, 강남역, 5);
    }

    private void 구간_추가(LineResponse 노선, StationResponse 상행역, StationResponse 하행역, int 구간_거리) {
        지하철_노선에_지하철역_등록_요청(노선, 상행역, 하행역, 구간_거리);
    }
}
