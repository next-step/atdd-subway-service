package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 남부터미널역;
    private StationResponse 양재역;
    private StationResponse 광교역;
    private StationResponse 안산역;
    private StationResponse 의정부역;
    private StationResponse 오이도역;
    private String 일반고객토큰;
    private String 어린이토큰;
    private String 청소년토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        String email = "lsecret@naver.com";
        String password = "test";
        회원_생성을_요청(email, password, 34);
        String childrenEmail = "secret@naver.com";
        회원_생성을_요청(childrenEmail, password, 6);
        String teenagerEmail = "teenagerEmail@naver.com";
        회원_생성을_요청(teenagerEmail, password, 13);
        일반고객토큰 = 로그인_되어_있음(new TokenRequest(email, password));
        어린이토큰 = 로그인_되어_있음(new TokenRequest(childrenEmail, password));
        청소년토큰 = 로그인_되어_있음(new TokenRequest(teenagerEmail, password));

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        안산역 = 지하철역_등록되어_있음("안산역").as(StationResponse.class);
        의정부역 = 지하철역_등록되어_있음("의정부역").as(StationResponse.class);
        오이도역 = 지하철역_등록되어_있음("오이도역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 1)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 1)).as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-800", 강남역.getId(), 광교역.getId(), 10, 900)).as(LineResponse.class);
        사호선 = 지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-blue-600", 의정부역.getId(), 안산역.getId(), 50)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 2);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 1);
        지하철_노선에_지하철역_등록되어_있음(사호선, 안산역, 오이도역, 10);
    }

    @DisplayName("두 역의 최단 거리 경로, 요금 조회 - 추가 금액 없는 노선 이용")
    @Test
    void findPathNoExtraCharge() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(교대역, 양재역), 1250);
    }

    @DisplayName("두 역의 최단 거리 경로, 요금 조회 - 추가 금액 있는 노선 이용")
    @Test
    void findPathAddExtraCharge() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(강남역.getId(), 광교역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(강남역, 양재역, 광교역), 2150);
    }

    @DisplayName("두 역의 최단 거리 경로, 요금 조회 - 10KM ~ 50KM 구간의 경로")
    @Test
    void findPathAddMoreTenDistanceCharge() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(안산역.getId(), 의정부역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(안산역, 의정부역), 2050);
    }

    @DisplayName("두 역의 최단 거리 경로, 요금 조회 - 50KM 초과 구간의 경로")
    @Test
    void findPathAddMoreFiftyDistanceCharge() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(오이도역.getId(), 의정부역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(오이도역, 안산역, 의정부역), 2250);
    }

    @DisplayName("로그인 후 두 역의 최단 거리 경로, 요금 조회 - 할인 대상 아님")
    @Test
    void findPathAfterLogin() {
        //when
        ExtractableResponse<Response> response = 로그인_후_지하철_노선_경로_조회_요청(일반고객토큰, 오이도역.getId(), 안산역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(오이도역, 안산역), 1250);
    }

    @DisplayName("로그인 후 두 역의 최단 거리 경로, 요금 조회 - 할인 대상 (어린이)")
    @Test
    void findPathByChildren() {
        //when
        ExtractableResponse<Response> response = 로그인_후_지하철_노선_경로_조회_요청(어린이토큰, 오이도역.getId(), 안산역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(오이도역, 안산역), 450);
    }

    @DisplayName("로그인 후 두 역의 최단 거리 경로, 요금 조회 - 할인 대상 (청소년)")
    @Test
    void findPathByTeenager() {
        //when
        ExtractableResponse<Response> response = 로그인_후_지하철_노선_경로_조회_요청(청소년토큰, 오이도역.getId(), 안산역.getId());

        //then
        지하철_노선_경로_조회됨(response, Arrays.asList(오이도역, 안산역), 720);
    }

    @DisplayName("두 역의 최단 거리 경로 조회 실패 - 같은 역을 조회 할 경우")
    @Test
    void findPathFailBySameStations() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(교대역.getId(), 교대역.getId());

        //then
        지하철_노선_경로_조회_실패됨(response);
    }

    @DisplayName("두 역의 최단 거리 경로 조회 실패 - 두 역이 연결되어 있지 않을 경우")
    @Test
    void findPathFailByNotConnected() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(안산역.getId(), 교대역.getId());

        //then
        지하철_노선_경로_조회_실패됨(response);
    }

    @DisplayName("두 역의 최단 거리 경로 조회 실패 - 존재하지 않는 출발 역 조회")
    @Test
    void findPathFailByNotExistsSourceStation() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(100L, 교대역.getId());

        //then
        지하철_노선_경로_조회_실패됨(response);
    }

    @DisplayName("두 역의 최단 거리 경로 조회 실패 - 존재하지 않는 도착 역 조회")
    @Test
    void findPathFailByNotExistsTargetStation() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청(교대역.getId(), 100L);

        //then
        지하철_노선_경로_조회_실패됨(response);
    }


    public static ExtractableResponse<Response> 로그인_후_지하철_노선_경로_조회_요청(String accessToken, long source, long target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_경로_조회_요청(long source, long target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses, int expectedCharge) {
        지하철_노선_경로_응답됨(response, stationResponses);
        지하철_노선_금액_응답됨(response, expectedCharge);
    }

    public static void 지하철_노선_경로_응답됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses) {

        List<Long> stations = response.as(PathResponse.class).getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> responseStations = stationResponses.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stations).containsExactlyElementsOf(responseStations);
    }

    private void 지하철_노선_금액_응답됨(ExtractableResponse<Response> response, int expectedCharge) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getCharge()).isEqualTo(expectedCharge);
    }

    private static void 지하철_노선_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
