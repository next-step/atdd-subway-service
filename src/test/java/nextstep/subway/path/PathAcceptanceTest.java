package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 구호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 당산역;
    private StationResponse 선유도역;

    @Autowired
    private LineService lineService;
    private static final String EMAIL_TEST_INFO = "test@test.com";
    private static final String PASSWORD_TEST_INFO = "test1234";
    private static final int AGE_TEST_INFO = 20;
    private static final int INFANCY_FARE = 0;
    private static final int CHILD_TEENAGER_DC_FARE = 350;
    private static final int BASIC_FARE = 1250;
    private static final float CHILD_RATE = 0.5f;
    private static final float TEENAGER_RATE = 0.8f;

    private TokenResponse adultTokenResponse;
    private TokenResponse teenagerTokenResponse;
    private TokenResponse childTokenResponse;
    private TokenResponse infancyTokenResponse;


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
        ExtractableResponse<Response> adultLoginResponse = allowedMemberLogin(EMAIL_TEST_INFO, PASSWORD_TEST_INFO, AGE_TEST_INFO);
        adultTokenResponse = adultLoginResponse.as(TokenResponse.class);

        ExtractableResponse<Response> teenagerLoginResponse = allowedMemberLogin("teenager@test.com", "teeanger", 13);
        teenagerTokenResponse = teenagerLoginResponse.as(TokenResponse.class);

        ExtractableResponse<Response> childLoginTokenResponse = allowedMemberLogin("child@test.com", "child", 6);
        childTokenResponse = childLoginTokenResponse.as(TokenResponse.class);

        ExtractableResponse<Response> infancyLoginResponse = allowedMemberLogin("infancy@test.com", "infancy", 3);
        infancyTokenResponse = infancyLoginResponse.as(TokenResponse.class);

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        당산역 = StationAcceptanceTest.지하철역_등록되어_있음("당산역").as(StationResponse.class);
        선유도역 = StationAcceptanceTest.지하철역_등록되어_있음("선유도역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 300);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 400);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 100);

        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-red-600", 당산역, 선유도역, 9, 0);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("최단거리 테스트")
    void shortestPathTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 양재역.getId(), adultTokenResponse);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        List<String> stationNames = pathResponse.getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());
        assertThat(stationNames).contains("교대역", "남부터미널역", "양재역");
    }

    @Test
    @DisplayName("10KM이내 최단거리 테스트 요금 구하기 성인")
    void shortestPathFareAdultBasicSectionTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(당산역.getId(), 선유도역.getId(), adultTokenResponse);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(BASIC_FARE);
    }

    @Test
    @DisplayName("10KM이내 최단거리 테스트 요금 구하기 유년기")
    void shortestPathFareChildTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(당산역.getId(), 선유도역.getId(), childTokenResponse);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo((int)((BASIC_FARE - CHILD_TEENAGER_DC_FARE) * CHILD_RATE));
    }

    @Test
    @DisplayName("10KM이내 최단거리 테스트 요금 구하기 일반 청소년기")
    void shortestPathFareTeenagerTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(당산역.getId(), 선유도역.getId(), teenagerTokenResponse);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo((int)((BASIC_FARE - CHILD_TEENAGER_DC_FARE) * TEENAGER_RATE));
    }

    @Test
    @DisplayName("10KM이내 최단거리 테스트 요금 구하기 일반 유아기")
    void shortestPathFareInfancyTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(당산역.getId(), 선유도역.getId(), infancyTokenResponse);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(INFANCY_FARE);
    }



    @Test
    @DisplayName("시작역과 종료역이 같을 때 에러처리")
    void isSameSourceAndTargetTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 교대역.getId(), adultTokenResponse);
        지하철_탐색_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 source 역 탐색할때 에러처리")
    void isNotExistSourceTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(-1L, 교대역.getId(), adultTokenResponse);
        지하철_탐색_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 target 역 탐색할때 에러처리")
    void isNotExistTargetTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), -1L, adultTokenResponse);
        지하철_탐색_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어있지 않은 경우 에러처리")
    void isSourceAndTargetStationsNotConnectLineTest() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 당산역.getId(), adultTokenResponse);
        지하철_탐색_실패됨(response);
    }

    public static ExtractableResponse<Response> 최단_거리_요청(Long sourceId, Long targetId, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 지하철_탐색_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        lineService.addLineStation(lineResponse.getId(), sectionRequest);
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return lineService.saveLine(lineRequest);
    }

    private ExtractableResponse<Response> login(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all().
                extract();
    }

    private ExtractableResponse<Response> allowedMemberLogin(String email, String password, Integer age) {
        signUpMember(email, password, age);
        return login(email, password);
    }

    private ExtractableResponse<Response> signUpMember(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

}
