package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 신도림역;
    private StationResponse 서울역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 수서역;

    private String accessToken;

    /**
     * 교대역    --- *2호선* ---      강남역
     *   |                              |
     * *3호선*                      *신분당선*
     *   |                              |
     * 남부터미널역  --- *3호선* ---   양재  --- 수서역
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        신도림역 = StationAcceptanceTest.지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        수서역 = StationAcceptanceTest.지하철역_등록되어_있음("수서역").as(StationResponse.class);

        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-blue-600", 신도림역, 서울역, 10, 0);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대역, 양재역, 5, 500);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 수서역, 50);

        MemberAcceptanceTest.회원_등록되어_있음("ehdgml3206@gmail.com", "1234", 31);
        accessToken = AuthAcceptanceTest.로그인_되어_있음("ehdgml3206@gmail.com", "1234");
    }

    @DisplayName("지하철 구간의 최단 경로를 조회한다. (사용자 연령: 성인, 거리: 12km, 노선: 추가요금(900)")
    @Test
    void findShortestPath_adult_12km_900() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 강남역, 남부터미널역);

        // then
        최단_경로_조회_응답됨(response);
        최단_경로_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 남부터미널역));
        최단_경로_거리_확인됨(response, 12L);
        최단_경로_지하철_이용_요금_확인됨(response,  2250);
    }

    @DisplayName("지하철 구간의 최단 경로를 조회한다. (사용자 연령: 성인, 거리: 55km, 노선: 추가요금(500)")
    @Test
    void findShortestPath_adult_55km_500() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 교대역, 수서역);

        // then
        최단_경로_조회_응답됨(response);
        최단_경로_순서_정렬됨(response, Arrays.asList(교대역, 남부터미널역, 양재역, 수서역));
        최단_경로_거리_확인됨(response, 55L);
        최단_경로_지하철_이용_요금_확인됨(response,  2650);
    }

    @DisplayName("지하철 구간의 최단 경로를 조회한다. (사용자 연령: 청소년, 거리: 55km, 노선: 추가요금(500)")
    @Test
    void findShortestPath_teenager_55km_500() {
        // given
        MemberAcceptanceTest.회원_등록되어_있음("test@gmail.com", "1234", 15);
        accessToken = AuthAcceptanceTest.로그인_되어_있음("test@gmail.com", "1234");

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 교대역, 수서역);

        // then
        최단_경로_조회_응답됨(response);
        최단_경로_순서_정렬됨(response, Arrays.asList(교대역, 남부터미널역, 양재역, 수서역));
        최단_경로_거리_확인됨(response, 55L);
        최단_경로_지하철_이용_요금_확인됨(response,  1840);
    }

    @DisplayName("지하철 구간의 최단 경로를 조회한다. (사용자 연령: 아동, 거리: 55km, 노선: 추가요금(500)")
    @Test
    void findShortestPath_child_55km_500() {
        // given
        MemberAcceptanceTest.회원_등록되어_있음("test@gmail.com", "1234", 8);
        accessToken = AuthAcceptanceTest.로그인_되어_있음("test@gmail.com", "1234");

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 교대역, 수서역);

        // then
        최단_경로_조회_응답됨(response);
        최단_경로_순서_정렬됨(response, Arrays.asList(교대역, 남부터미널역, 양재역, 수서역));
        최단_경로_거리_확인됨(response, 55L);
        최단_경로_지하철_이용_요금_확인됨(response,  1150);
    }


    @DisplayName("출발역과 도착역이 같을 경우 응답에 실패한다.")
    @Test
    void findShortestPath_exception() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 강남역, 강남역);

        // then
        최단_경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 응답에 실패한다.")
    @Test
    void findShortestPath_exception_2() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 신도림역, 강남역);

        // then
        최단_경로_조회_실패됨(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 응답에 실패한다.")
    @Test
    void findShortestPath_exception_3() {
        // given
        StationResponse 당산역 = StationAcceptanceTest.지하철역_등록되어_있음("당산역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(accessToken, 당산역, 강남역);

        // then
        최단_경로_조회_실패됨(response);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, StationResponse sourceStation, StationResponse targetStation) {
        PathRequest pathRequest = new PathRequest(sourceStation.getId(), targetStation.getId());

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(pathRequest)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    public static void 최단_경로_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
    }

    private void 최단_경로_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());
        List<Long> expectedStationIds = expectedStations.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private void 최단_경로_거리_확인됨(ExtractableResponse<Response> response, long expectedDistance) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getDistance()).isEqualTo(expectedDistance);
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 최단_경로_지하철_이용_요금_확인됨(ExtractableResponse<Response> response, int expectFare) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(path.getFare()).isEqualTo(expectFare);
    }
}
