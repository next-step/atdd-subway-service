package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final int 기본요금 = 1250;
    private static final int 이호선_요금 = 0;
    private static final int 삼호선_요금 = 200;
    private static final int 신분당선_요금 = 1000;

    private static final int 요금할인_공제액 = 350;
    private static final double 할인율_청소년 = 0.2;
    private static final double 할인율_어린이 = 0.5;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 건대입구역;

    /**            거리 10                  거리 20                  거리 30
     * 교대역   --- *2호선* ---   강남역   --- *2호선* ---   잠실역   --- *2호선* ---   건대입구역
     * |                        |
     * *3호선*                   *신분당선*
        거리 3                    거리 10
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *                거리 2
     */

    @BeforeAll
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        건대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("건대입구역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 신분당선_요금);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10, 이호선_요금);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대역, 남부터미널역, 3, 삼호선_요금);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 2);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 잠실역, 20);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 건대입구역, 30);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (할인혜택 미적용)")
    @Test
    void findShortestPath_할인혜택_미적용() {
        // when
        ExtractableResponse<Response> 경로조회결과 = 지하철_경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        지하철_최단경로_조회됨(경로조회결과,
                Arrays.asList("교대역", "남부터미널역", "양재역"),
                5,
                기본요금 + 삼호선_요금);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (할인혜택 미적용, 10km 초과 추가요금 적용)")
    @Test
    void findShortestPath_할인혜택_미적용_10km_초과() {
        // given
        int 예상거리 = 30;
        int 추가요금 = 400;

        // when
        ExtractableResponse<Response> 경로조회결과 = 지하철_경로_조회_요청(교대역.getId(), 잠실역.getId());

        // then
        지하철_최단경로_조회됨(경로조회결과,
                Arrays.asList("교대역", "강남역", "잠실역"),
                예상거리,
                기본요금 + 이호선_요금 + 추가요금);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (할인혜택 미적용, 50km 초과 추가요금 적용)")
    @Test
    void findShortestPath_할인혜택_미적용_50km_초과() {
        // given
        int 예상거리 = 60;
        int 추가요금 = 1000;

        // when
        ExtractableResponse<Response> 경로조회결과 = 지하철_경로_조회_요청(교대역.getId(), 건대입구역.getId());

        // then
        지하철_최단경로_조회됨(경로조회결과,
                Arrays.asList("교대역", "강남역", "잠실역", "건대입구역"),
                예상거리,
                기본요금 + 이호선_요금 + 추가요금);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (청소년 할인 적용)")
    @Test
    void findShortestPath_청소년_할인() {
        // given
        MemberAcceptanceTest.회원_생성을_요청("teenager2@test.com", "1a2b3c", 16);
        String 청소년_회원 = MemberAcceptanceTest.회원_로그인_요청("teenager2@test.com", "1a2b3c");
        int 예상거리 = 12;
        int 요금 = 2350;
        int 할인액 = (int) ((2350 - 요금할인_공제액) * 할인율_청소년);

        // when
        ExtractableResponse<Response> 경로조회결과 = 지하철_경로_조회_요청_회원(청소년_회원, 강남역.getId(), 남부터미널역.getId());

        // then
        지하철_최단경로_조회됨(경로조회결과,
                Arrays.asList("강남역", "양재역", "남부터미널역"),
                예상거리,
                요금 - 요금할인_공제액 - 할인액);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (청소년 할인 적용, 50km 초과 추가요금 적용)")
    @Test
    void findShortestPath_청소년_할인_50km_초과() {
        // given
        MemberAcceptanceTest.회원_생성을_요청("teenager1@test.com", "1a2b3c", 16);
        String 청소년_회원 = MemberAcceptanceTest.회원_로그인_요청("teenager1@test.com", "1a2b3c");
        int 예상거리 = 60;
        int 요금 = 2250;
        int 할인액 = (int) ((2250 - 요금할인_공제액) * 할인율_청소년);

        // when
        ExtractableResponse<Response> 경로조회결과 = 지하철_경로_조회_요청_회원(청소년_회원, 교대역.getId(), 건대입구역.getId());

        // then
        지하철_최단경로_조회됨(경로조회결과,
                Arrays.asList("교대역", "강남역", "잠실역", "건대입구역"),
                예상거리,
                요금 - 요금할인_공제액 - 할인액);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (어린이 할인 적용)")
    @Test
    void findShortestPath_어린이_할인() {
        // given
        MemberAcceptanceTest.회원_생성을_요청("children@test.com", "1a2b3c", 7);
        String 어린이 = MemberAcceptanceTest.회원_로그인_요청("children@test.com", "1a2b3c");
        int 예상거리 = 12;
        int 요금 = 2350;
        int 할인액 = (int) ((2350 - 요금할인_공제액) * 할인율_어린이);

        // when
        ExtractableResponse<Response> 경로조회결과 = 지하철_경로_조회_요청_회원(어린이, 강남역.getId(), 남부터미널역.getId());

        // then
        지하철_최단경로_조회됨(경로조회결과,
                Arrays.asList("강남역", "양재역", "남부터미널역"),
                예상거리,
                요금 - 요금할인_공제액 - 할인액);
    }

    @DisplayName("노선 미포함 역을 경로 조회시 실패")
    @Test
    void findPath_Fail() {
        ExtractableResponse<Response> 노선_미포함_경로_조회 = 지하철_경로_조회_요청(강남역.getId(), StationFixtures.천호역.getId());
        지하철_경로_조회_실패함(노선_미포함_경로_조회);
    }

    public ExtractableResponse<Response> 지하철_경로_조회_요청(Long source, Long target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청_회원(String accessToken, Long source, Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 지하철_최단경로_조회됨(ExtractableResponse<Response> response, List<String> expectedStations, int distance, int fare) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.body().as(PathResponse.class);
        assertThat(pathResponse.getStations())
                .map(StationResponse::getName)
                .asList()
                .containsExactlyElementsOf(expectedStations);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    public LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int additionalFare) {
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, additionalFare));
        return response.body().as(LineResponse.class);
    }

    public static void 지하철_경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
