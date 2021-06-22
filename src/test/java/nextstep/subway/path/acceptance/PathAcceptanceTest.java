package nextstep.subway.path.acceptance;

import com.google.common.collect.ImmutableList;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.BearerAuthToken;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static nextstep.subway.member.MemberAcceptanceTest.createMember;
import static nextstep.subway.member.MemberAcceptanceTest.loginAndGetToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 수인분당선;

    private StationResponse 강남역;
    private StationResponse 서초역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 매봉역;
    private StationResponse 도곡역;
    private StationResponse 한티역;
    private StationResponse 양재시민의숲역;
    private StationResponse 부평역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = createStation("강남역");
        서초역 = createStation("서초역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");
        매봉역 = createStation("매봉역");
        도곡역 = createStation("도곡역");
        한티역 = createStation("한티역");
        양재시민의숲역 = createStation("양재시민의숲역");
        부평역 = createStation("부평역");

        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 14);
        이호선 = createLine("이호선", "bg-green-600", 교대역, 강남역, 12);
        삼호선 = createLine("삼호선", "bg-orange-600", 교대역, 남부터미널역, 9);
        수인분당선 = createLine("수인분당선", "bg-yellow-600", 한티역, 도곡역, 1);

        createSection(이호선, 서초역, 교대역, 44);
        createSection(삼호선, 남부터미널역, 양재역, 18);
        createSection(수인분당선, 양재역, 매봉역, 1);
        createSection(수인분당선, 매봉역, 도곡역, 1);
        createSection(신분당선, 양재역, 양재시민의숲역, 1);
    }

    private StationResponse createStation(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    private LineResponse createLine(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance))
                                 .as(LineResponse.class);
    }

    private void createSection(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    @DisplayName("출발역과 도착역이 같으면 오류")
    @Test
    void findPathFail01() {
        findPathFail(강남역, 강남역);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 오류")
    @Test
    void findPathFail02() {
        findPathFail(강남역, 부평역);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회하면 오류")
    @Test
    void findPathFail03() {

        LocalDateTime dontCareTime = LocalDateTime.now();
        StationResponse noExistStation1 = new StationResponse(10000L, "NO_1", dontCareTime, dontCareTime);
        StationResponse noExistStation2 = new StationResponse(20000L, "NO_2", dontCareTime, dontCareTime);

        findPathFail(noExistStation1, noExistStation2);
    }

    @DisplayName("최단 거리 탐색 - 같은 노선, 추가요금 없는 노선, 10km 이하")
    @Test
    void findPathSuccess01() {
        findPathSuccess(교대역, 남부터미널역, 9, ImmutableList.of(강남역, 양재역), 1250);
    }

    @DisplayName("최단 거리 탐색 - 다른 노선, 신분당선 추가 이용요금 900원, 10km 초과 ~ 50km 이하")
    @Test
    void findPathSuccess02() {
        findPathSuccess(교대역, 양재역, 26, ImmutableList.of(교대역, 강남역, 양재역), 2650);
    }

    @DisplayName("최단 거리 탐색 - 같은 노선, 추가요금 없는 노선, 50km 초과")
    @Test
    void findPathSuccess03() {
        findPathSuccess(서초역, 강남역, 56, ImmutableList.of(서초역, 교대역, 강남역), 1950);
    }

    @DisplayName("최단 거리 탐색 시나리오 - 추가 요금 노선으로 환승, 어린이 요금")
    @TestFactory
    Stream<DynamicTest> findPathScenarioTest01() {

        // 어린이는 6세 이상 13세 미만
        // 양재시민의숲역 -> 한티역 각 구간 당 1km로 설정, 총 4km
        // 신분당선 추가요금 900원, 수인분당선 추가요금 500원
        // (기본운임 + 신분당선 추가요금 - 350)의 50% = (1250 + 900 - 350) / 2 = 900

        MemberRequest member = new MemberRequest("test@test", "1234", 10);
        BearerAuthToken token = new BearerAuthToken();

        return Stream.of(
            dynamicTest("어린이 계정 생성", createMember(member)),
            dynamicTest("어린이 계정으로 로그인 및 토큰 생성", loginAndGetToken(member, token)),
            dynamicTest("양재시민의숲역부터 도곡역 최단거리 탐색 및 요금 조회",
                        findPathWithToken(token, 양재시민의숲역, 한티역, 4,
                                          ImmutableList.of(양재시민의숲역, 양재역, 매봉역, 도곡역, 한티역), 900))
        );
    }

    @DisplayName("최단 거리 탐색 시나리오 - 추가 요금 노선으로 환승, 청소년 요금")
    @TestFactory
    Stream<DynamicTest> findPathScenarioTest02() {

        // 청소년은 13세 이상 19세 미만
        // 양재시민의숲역 -> 한티역 각 구간 당 1km로 설정, 총 4km
        // 신분당선 추가요금 900원, 수인분당선 추가요금 500원(가정)
        // (기본운임 + 신분당선 추가요금 - 350)의 80% = (1250 + 900 - 350) / 5 * 4  = 1440

        MemberRequest member = new MemberRequest("test@test", "1234", 15);
        BearerAuthToken token = new BearerAuthToken();

        return Stream.of(
            dynamicTest("청소년 계정 생성", createMember(member)),
            dynamicTest("청소년 계정으로 로그인 및 토큰 생성", loginAndGetToken(member, token)),
            dynamicTest("양재시민의숲역부터 도곡역 최단거리 탐색 및 요금 조회",
                        findPathWithToken(token, 양재시민의숲역, 한티역, 4,
                                          ImmutableList.of(양재시민의숲역, 양재역, 매봉역, 도곡역, 한티역), 1440))
        );
    }

    private Executable findPathWithToken(BearerAuthToken token, StationResponse source, StationResponse destination, int distance, List<StationResponse> path, int fee) {
        return () -> {
            Map<String, Long> queryParameters = new HashMap<>();
            queryParameters.put("source", source.getId());
            queryParameters.put("target", destination.getId());

            ExtractableResponse<Response> response =
                RestAssured.given()
                           .queryParams(queryParameters).log().all()
                           .auth().oauth2(token.getToken())
                           .when().get("/paths")
                           .then().log().all()
                           .extract();

            PathResponse pathResponse = response.as(PathResponse.class);

            assertThat(pathResponse.getDistance()).isEqualTo(distance);
            assertThat(pathResponse.getStations()).isEqualTo(path);
            assertThat(pathResponse.getFee()).isEqualTo(fee);
        };
    }

    private ExtractableResponse<Response> findPathRequest(Long source, Long destination) {

        Map<String, Long> queryParameters = new HashMap<>();
        queryParameters.put("source", source);
        queryParameters.put("target", destination);

        return RestAssured.given().queryParams(queryParameters).log().all()
                          .when().get("/paths")
                          .then().log().all()
                          .extract();
    }

    private void findPathFail(StationResponse source, StationResponse destination) {

        // when
        ExtractableResponse<Response> response = findPathRequest(source.getId(), destination.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void findPathSuccess(StationResponse source, StationResponse destination, int distance, List<StationResponse> path, int fee) {

        // when
        PathResponse response = findPathRequest(source.getId(), destination.getId()).as(PathResponse.class);

        // then
        assertThat(response.getDistance()).isEqualTo(distance);
        assertThat(response.getStations()).isEqualTo(path);
        assertThat(response.getFee()).isEqualTo(fee);
    }
}
