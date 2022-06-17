package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathFareAcceptanceTest extends AcceptanceTest {
    public static final String CHILD_EMAIL = "child@nextstep.com";
    public static final String CHILD_PASSWORD = "child";
    public static final Integer CHILD_AGE = 6;

    public static final String YOUTH_EMAIL = "youth@nextstep.com";
    public static final String YOUTH_PASSWORD = "youth";
    public static final Integer YOUTH_AGE = 13;

    public static final String ADULT_EMAIL = "adult@nextstep.com";
    public static final String ADULT_PASSWORD = "adult";
    public static final Integer ADULT_AGE = 19;

    private TokenResponse childTokenResponse;
    private TokenResponse youthTokenResponse;
    private TokenResponse adultTokenResponse;

    private StationResponse 종합운동장;
    private StationResponse 잠실새내;
    private StationResponse 잠실;
    private StationResponse 석촌;
    private StationResponse 가락시장;
    private StationResponse 오금;
    private StationResponse 천호;
    private StationResponse 마천;

    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private LineResponse 팔호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(CHILD_EMAIL, CHILD_PASSWORD, CHILD_AGE);
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(CHILD_EMAIL, CHILD_PASSWORD));
        childTokenResponse = 로그인_성공(response);

        회원_생성을_요청(YOUTH_EMAIL, YOUTH_PASSWORD, YOUTH_AGE);
        response = 로그인_요청(new TokenRequest(YOUTH_EMAIL, YOUTH_PASSWORD));
        youthTokenResponse = 로그인_성공(response);

        회원_생성을_요청(ADULT_EMAIL, ADULT_PASSWORD, ADULT_AGE);
        response = 로그인_요청(new TokenRequest(ADULT_EMAIL, ADULT_PASSWORD));
        adultTokenResponse = 로그인_성공(response);
    }

    @DisplayName("종합운동장 ~ 잠실새내 최단 경로, 거리, 비용 조회")
    @Test
    void testPathDistanceFare01() {
        // given : 지하철역 등록되어 있음
        종합운동장 = 지하철역_등록되어_있음("종합운동장").as(StationResponse.class);
        잠실새내 = 지하철역_등록되어_있음("잠실새내").as(StationResponse.class);

        // given : 지하철노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("2호선", "green", 종합운동장.getId(), 잠실새내.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // when : 출발역에서 도착역까지의 최단 거리 경로 조회
        ExtractableResponse<Response> 어린이_조회 = 최단경로_조회(childTokenResponse, 종합운동장, 잠실새내);
        ExtractableResponse<Response> 청소년_조회 = 최단경로_조회(youthTokenResponse, 종합운동장, 잠실새내);
        ExtractableResponse<Response> 어른_조회 = 최단경로_조회(adultTokenResponse, 종합운동장, 잠실새내);

        // then : 최단경로, 거리, 요금 조회
        최단경로_지하철목록_거리_비용_조회(어린이_조회, Arrays.asList(종합운동장, 잠실새내), 10, 450);
        최단경로_지하철목록_거리_비용_조회(청소년_조회, Arrays.asList(종합운동장, 잠실새내), 10, 720);
        최단경로_지하철목록_거리_비용_조회(어른_조회, Arrays.asList(종합운동장, 잠실새내), 10, 1250);
    }

    @DisplayName("종합운동장 ~ 석촌 최단 경로, 거리, 비용 조회")
    @Test
    void testPathDistanceFare02() {
        // given : 지하철역 등록되어 있음
        종합운동장 = 지하철역_등록되어_있음("종합운동장").as(StationResponse.class);
        잠실새내 = 지하철역_등록되어_있음("잠실새내").as(StationResponse.class);
        잠실 = 지하철역_등록되어_있음("잠실").as(StationResponse.class);
        석촌 = 지하철역_등록되어_있음("석촌").as(StationResponse.class);

        // given : 지하철노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("2호선", "green", 종합운동장.getId(), 잠실새내.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("8호선", "pink", 잠실.getId(), 석촌.getId(), 20);
        팔호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // given : 지하철구간 등록되어 있음
        지하철_노선에_지하철역_등록_요청(이호선, 잠실새내, 잠실, 20);

        // when : 출발역에서 도착역까지의 최단 거리 경로 조회
        ExtractableResponse<Response> 어린이_조회 = 최단경로_조회(childTokenResponse, 종합운동장, 석촌);
        ExtractableResponse<Response> 청소년_조회 = 최단경로_조회(youthTokenResponse, 종합운동장, 석촌);
        ExtractableResponse<Response> 어른_조회 = 최단경로_조회(adultTokenResponse, 종합운동장, 석촌);

        // then : 최단경로, 거리, 요금 조회
        최단경로_지하철목록_거리_비용_조회(어린이_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌), 50, 850);
        최단경로_지하철목록_거리_비용_조회(청소년_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌), 50, 1360);
        최단경로_지하철목록_거리_비용_조회(어른_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌), 50, 2050);
    }

    @DisplayName("종합운동장 ~ 오금 최단 경로, 거리, 비용 조회")
    @Test
    void testPathDistanceFare03() {
        // given : 지하철역 등록되어 있음
        종합운동장 = 지하철역_등록되어_있음("종합운동장").as(StationResponse.class);
        잠실새내 = 지하철역_등록되어_있음("잠실새내").as(StationResponse.class);
        잠실 = 지하철역_등록되어_있음("잠실").as(StationResponse.class);
        석촌 = 지하철역_등록되어_있음("석촌").as(StationResponse.class);
        가락시장 = 지하철역_등록되어_있음("가락시장").as(StationResponse.class);
        오금 = 지하철역_등록되어_있음("오금").as(StationResponse.class);
        천호 = 지하철역_등록되어_있음("천호").as(StationResponse.class);
        마천 = 지하철역_등록되어_있음("마천").as(StationResponse.class);

        // given : 지하철노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("2호선", "green", 종합운동장.getId(), 잠실새내.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("3호선", "orange", 가락시장.getId(), 오금.getId(), 20);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("5호선", "purple", 천호.getId(), 마천.getId(), 90);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("8호선", "pink", 잠실.getId(), 석촌.getId(), 10);
        팔호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // given : 지하철구간 등록되어 있음
        지하철_노선에_지하철역_등록_요청(이호선, 잠실새내, 잠실, 10);
        지하철_노선에_지하철역_등록_요청(오호선, 천호, 오금, 50);
        지하철_노선에_지하철역_등록_요청(팔호선, 석촌, 가락시장, 20);
        지하철_노선에_지하철역_등록_요청(팔호선, 천호, 잠실, 30);

        // when : 출발역에서 도착역까지의 최단 거리 경로 조회
        ExtractableResponse<Response> 어린이_조회 = 최단경로_조회(childTokenResponse, 종합운동장, 오금);
        ExtractableResponse<Response> 청소년_조회 = 최단경로_조회(youthTokenResponse, 종합운동장, 오금);
        ExtractableResponse<Response> 어른_조회 = 최단경로_조회(adultTokenResponse, 종합운동장, 오금);

        // then : 최단경로, 거리, 요금 조회
        최단경로_지하철목록_거리_비용_조회(어린이_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌, 가락시장, 오금), 70, 1000);
        최단경로_지하철목록_거리_비용_조회(청소년_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌, 가락시장, 오금), 70, 1600);
        최단경로_지하철목록_거리_비용_조회(어른_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌, 가락시장, 오금), 70, 2350);
    }

    // Scenario
    @DisplayName("[시나리오] 지하철 최단 경로를 조회한다.")
    @Test
    void testWithScenario() {
        // given : 지하철역 등록되어 있음
        종합운동장 = 지하철역_등록되어_있음("종합운동장").as(StationResponse.class);
        잠실새내 = 지하철역_등록되어_있음("잠실새내").as(StationResponse.class);
        잠실 = 지하철역_등록되어_있음("잠실").as(StationResponse.class);
        석촌 = 지하철역_등록되어_있음("석촌").as(StationResponse.class);
        가락시장 = 지하철역_등록되어_있음("가락시장").as(StationResponse.class);
        오금 = 지하철역_등록되어_있음("오금").as(StationResponse.class);
        천호 = 지하철역_등록되어_있음("천호").as(StationResponse.class);
        마천 = 지하철역_등록되어_있음("마천").as(StationResponse.class);

        // given : 지하철노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("2호선", "green", 종합운동장.getId(), 잠실새내.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("3호선", "orange", 가락시장.getId(), 오금.getId(), 20);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("5호선", "purple", 천호.getId(), 마천.getId(), 90);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("8호선", "pink", 잠실.getId(), 석촌.getId(), 10);
        팔호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // given : 지하철구간 등록되어 있음
        지하철_노선에_지하철역_등록_요청(이호선, 잠실새내, 잠실, 10);
        지하철_노선에_지하철역_등록_요청(오호선, 천호, 오금, 50);
        지하철_노선에_지하철역_등록_요청(팔호선, 석촌, 가락시장, 20);
        지하철_노선에_지하철역_등록_요청(팔호선, 천호, 잠실, 30);

        // when: 최단 경로 조회 요청
        ExtractableResponse<Response> 어린이_조회 = 최단경로_조회(childTokenResponse, 종합운동장, 가락시장);
        ExtractableResponse<Response> 청소년_조회 = 최단경로_조회(youthTokenResponse, 종합운동장, 가락시장);
        ExtractableResponse<Response> 어른_조회 = 최단경로_조회(adultTokenResponse, 종합운동장, 가락시장);

        // then: 최단 경로가 조회됨
        최단경로_지하철목록_거리_비용_조회(어린이_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌, 가락시장), 50, 850);
        최단경로_지하철목록_거리_비용_조회(청소년_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌, 가락시장), 50, 1360);
        최단경로_지하철목록_거리_비용_조회(어른_조회, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌, 가락시장), 50, 2050);

        // when: 최단 경로를 가지는 신규 노선 구간 등록 되어있음
        지하철_노선_등록되어_있음(new LineRequest("최단경로노선", "bg-black-600", 종합운동장.getId(), 가락시장.getId(), 15)).as(LineResponse.class);

        // and: 최단 경로 조회 요청
        ExtractableResponse<Response> 최단경로노선_어린이_조회 = 최단경로_조회(childTokenResponse, 종합운동장, 가락시장);
        ExtractableResponse<Response> 최단경로노선_청소년_조회 = 최단경로_조회(youthTokenResponse, 종합운동장, 가락시장);
        ExtractableResponse<Response> 최단경로노선_어른_조회 = 최단경로_조회(adultTokenResponse, 종합운동장, 가락시장);

        // then: 최단 경로가 조회됨
        최단경로_지하철목록_거리_비용_조회(최단경로노선_어린이_조회, Arrays.asList(종합운동장, 가락시장), 15, 500);
        최단경로_지하철목록_거리_비용_조회(최단경로노선_청소년_조회, Arrays.asList(종합운동장, 가락시장), 15, 800);
        최단경로_지하철목록_거리_비용_조회(최단경로노선_어른_조회, Arrays.asList(종합운동장, 가락시장), 15, 1350);

        // when: 새로운 노선 등록됨
        StationResponse 김포공항 = 지하철역_등록되어_있음("김포공항").as(StationResponse.class);
        StationResponse 여의도 = 지하철역_등록되어_있음("여의도").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("9호선", "gold", 김포공항.getId(), 여의도.getId(), 10)).as(LineResponse.class);

        // and: 연결된 구간으로 갈 수 없는 목적지 최단 경로 조회
        ExtractableResponse<Response> 목적지_역으로_이동_불가능한_최단_경로_조회됨 = 최단경로_조회(잠실, 여의도);

        // then: 최단 경로 조회에 실패
        최단경로_조회_실패(목적지_역으로_이동_불가능한_최단_경로_조회됨);

        // when: 존재하지 않는 역을 목적지로 최단 경로 조회
        ExtractableResponse<Response> 존재하지_않는_목적지_역으로_최단_경로_조회됨 = 최단경로_조회(석촌,  new StationResponse(10000L, "존재하지 않는 역"));

        // then: 최단 경로 조회에 실패
        최단경로_조회_실패(존재하지_않는_목적지_역으로_최단_경로_조회됨);
    }

    public static ExtractableResponse<Response> 최단경로_조회(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={stationId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 최단경로_조회(TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/paths?source={stationId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static void 최단경로_지하철목록_거리_비용_조회(ExtractableResponse<Response> response,
                                          List<StationResponse> expectedStations, int distance, int fare) {
        PathResponse path = response.as(PathResponse.class);

        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
        assertThat(path.getDistance()).isEqualTo(distance);
        assertThat(path.getFare()).isEqualTo(fare);
    }

    public static void 최단경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
