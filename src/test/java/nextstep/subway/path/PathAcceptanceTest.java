package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final String AUTHORIZATION = "Authorization";
    public static String BEARER_TYPE = "Bearer";
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 혼자떨어진역;

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
        혼자떨어진역 = StationAcceptanceTest.지하철역_등록되어_있음("혼자떨어진역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 2000)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 500)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 43, 0)).as(LineResponse.class);

        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록됨(response);
    }

    @DisplayName("최단 경로로 조회한다 - 한 정거장")
    @Test
    public void shortestPathOneStation() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(new PathRequest(교대역.getId(), 강남역.getId()));

        // then
        지하철_경로_조회됨(response);
        PathResponse pathResponse = response.as(PathResponse.class);

        경로_거리_일치됨(pathResponse, 10);
        경로_역_일치됨(pathResponse, 교대역, 강남역);
        경로_금액_일치됨(pathResponse, 1750);
    }

    @DisplayName("최단 경로로 조회한다 - 두 정거장")
    @Test
    public void shortestPathTwoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(new PathRequest(교대역.getId(), 양재역.getId()));

        // then
        지하철_경로_조회됨(response);
        PathResponse pathResponse = response.as(PathResponse.class);
        경로_거리_일치됨(pathResponse, 20);
        경로_역_일치됨(pathResponse, 교대역, 강남역, 양재역);
        경로_금액_일치됨(pathResponse, 3450);
    }

    @DisplayName("어린이 최단 경로로 조회한다 - 두 정거장")
    @Test
    public void shortestPathTwoStationByChild() {

        // when
        ExtractableResponse<Response> memberResponse = MemberAcceptanceTest.회원_생성을_요청("child", "123", 6);
        // then
        MemberAcceptanceTest.회원_생성됨(memberResponse);

        // when
        ExtractableResponse<Response> tokenResponse = AuthAcceptanceTest.토큰_로그인_요청(new TokenRequest("child", "123"));
        // then
        AuthAcceptanceTest.토큰_로그인됨(tokenResponse);
        String token = BEARER_TYPE +  tokenResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> response = 사용자_지하철_경로_조회_요청(new PathRequest(교대역.getId(), 양재역.getId()), token);

        // then
        지하철_경로_조회됨(response);

        PathResponse pathResponse = response.as(PathResponse.class);
        경로_거리_일치됨(pathResponse, 20);
        경로_역_일치됨(pathResponse, 교대역, 강남역, 양재역);
        경로_금액_일치됨(pathResponse, 1900);
    }

    @DisplayName("청소년 최단 경로로 조회한다 - 두 정거장")
    @Test
    public void shortestPathTwoStationByTeenager() {

        // when
        ExtractableResponse<Response> memberResponse = MemberAcceptanceTest.회원_생성을_요청("teenager", "123", 18);
        // then
        MemberAcceptanceTest.회원_생성됨(memberResponse);

        // when
        ExtractableResponse<Response> tokenResponse = AuthAcceptanceTest.토큰_로그인_요청(new TokenRequest("teenager", "123"));
        // then
        AuthAcceptanceTest.토큰_로그인됨(tokenResponse);
        String token = BEARER_TYPE +  tokenResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> response = 사용자_지하철_경로_조회_요청(new PathRequest(교대역.getId(), 양재역.getId()), token);

        // then
        지하철_경로_조회됨(response);

        PathResponse pathResponse = response.as(PathResponse.class);
        경로_거리_일치됨(pathResponse, 20);
        경로_역_일치됨(pathResponse, 교대역, 강남역, 양재역);
        경로_금액_일치됨(pathResponse, 2830);
    }



    @DisplayName("조회 불가 케이스 - 출발역과 도착역이 같은 경우")
    @Test
    public void invalidCaseSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(new PathRequest(교대역.getId(), 교대역.getId()));

        // then
        지하철_경로_조회_실패됨(response);
    }

    @DisplayName("조회 불가 케이스 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    public void invalidCaseNotConnected() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(new PathRequest(교대역.getId(), 혼자떨어진역.getId()));

        // then
        지하철_경로_조회_실패됨(response);
    }

    @DisplayName("조회 불가 케이스 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    public void invalidCaseNotExist() {
        Long invalidId = 999999l;
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(new PathRequest(invalidId, 혼자떨어진역.getId()));

        // then
        지하철_경로_조회_실패됨(response);
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(PathRequest pathRequest) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}", pathRequest.getSourceId(), pathRequest.getTargetId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사용자_지하철_경로_조회_요청(PathRequest pathRequest, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().get("/paths?source={sourceId}&target={targetId}", pathRequest.getSourceId(), pathRequest.getTargetId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 경로_거리_일치됨(PathResponse pathResponse, int distance) {
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    public static void 경로_역_일치됨(PathResponse pathResponse, StationResponse... stationResponses) {
        List<StationResponse> actual = pathResponse.getStations();
        List<StationResponse> extpected = new ArrayList<>(Arrays.asList(stationResponses));
        assertThat(actual).isEqualTo(extpected);
    }

    public static void 경로_금액_일치됨(PathResponse pathResponse, long fee) {
        assertThat(pathResponse.getFee()).isEqualTo(fee);
    }
}
