package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
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

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 길음역;
    private LineResponse 사호선;
    private LineResponse 삼호선;
    private String 어린이_로그인_토큰;
    private String 청소년_로그인_토큰;
    private String 성인_로그인_토큰;

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
        어린이_로그인_토큰 = 회원가입_후_로그인("test1", "test1", 12);
        청소년_로그인_토큰 = 회원가입_후_로그인("test2", "test2", 18);
        성인_로그인_토큰 = 회원가입_후_로그인("test3", "test3", 19);

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        길음역 = StationAcceptanceTest.지하철역_등록되어_있음("길음역").as(StationResponse.class);

        사호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-red-600", 길음역.getId(), 사당역.getId(), 50))
                .as(LineResponse.class);
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 6));
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10));
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 300))
                .as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 교대역, 3);
    }

    @Test
    void 어린이_최단_경로_조회() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(어린이_로그인_토큰, 남부터미널역.getId(), 강남역.getId());

        //then
        최단_경로에_지하철역_순서_정렬됨(response, Arrays.asList(남부터미널역, 교대역, 강남역));
        최단_경로_총_거리값_확인됨(response, 13);
        최단_경로_총_지하철_요금_정보_확인됨(response, 650);
    }

    @Test
    void 청소년_최단_경로_조회() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(청소년_로그인_토큰, 남부터미널역.getId(), 강남역.getId());

        //then
        최단_경로에_지하철역_순서_정렬됨(response, Arrays.asList(남부터미널역, 교대역, 강남역));
        최단_경로_총_거리값_확인됨(response, 13);
        최단_경로_총_지하철_요금_정보_확인됨(response, 1040);
    }

    @Test
    void 성인_최단_경로_조회() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인_로그인_토큰, 남부터미널역.getId(), 강남역.getId());

        //then
        최단_경로에_지하철역_순서_정렬됨(response, Arrays.asList(남부터미널역, 교대역, 강남역));
        최단_경로_총_거리값_확인됨(response, 13);
        최단_경로_총_지하철_요금_정보_확인됨(response, 1650);
    }

    @Test
    void 출발역과_도착역중_실제_존재하지_않는_역이_포함되어_있으면_최단_경로를_조회할_수_없다() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인_로그인_토큰, 0, 강남역.getId());

        //then
        최단_경로_조회_실패됨(response);
    }

    @Test
    void 출발역과_도착역이_연결되어있지_않으면_최단_경로를_조회할_수_없다() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인_로그인_토큰, 사당역.getId(), 강남역.getId());

        //then
        최단_경로_조회_실패됨(response);
    }

    @Test
    void 출발역과_도착역이_같으면_최단_경로를_조회할_수_없다() {
        //when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인_로그인_토큰, 남부터미널역.getId(), 남부터미널역.getId());

        //then
        최단_경로_조회_실패됨(response);
    }

    public static String 회원가입_후_로그인(String email, String password, int age) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(email, password, age);
        MemberAcceptanceTest.회원_생성됨(createResponse);
        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(email, password);
        AuthAcceptanceTest.로그인_성공(loginResponse);
        return loginResponse.jsonPath().getString("accessToken");
    }

    public static void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 최단_경로_총_거리값_확인됨(ExtractableResponse<Response> response, long distance) {
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance);
    }

    public static void 최단_경로_총_지하철_요금_정보_확인됨(ExtractableResponse<Response> response, long fare) {
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(fare);
    }

    public static void 최단_경로에_지하철역_순서_정렬됨(
            ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream().map(StationResponse::getId).collect(Collectors.toList());

        List<Long> expectedStationIds =
                expectedStations.stream().map(StationResponse::getId).collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(
            String accessToken, long sourceStationId, long targetStationId) {
        return RestAssured.given().log().all().when().accept(MediaType.APPLICATION_JSON_VALUE).auth()
                .oauth2(accessToken)
                .get("/paths?source={sourceStationId}&target={targetStationId}", sourceStationId, targetStationId)
                .then().log().all().extract();
    }
}
