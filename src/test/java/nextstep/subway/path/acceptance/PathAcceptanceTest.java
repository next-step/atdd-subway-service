package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.acceptance.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.acceptance.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 미연결역1;
    private StationResponse 미연결역2;
    private LineResponse 미연결노선;
    private StationResponse 미존재역;
    private String 회원_성인_이메일 = "adult@email.com";
    private String 회원_청소년_이메일 = "teenager@email.com";
    private String 회원_어린이_이메일 = "children@email.com";


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * 교대역 - 양재
     * 교대역 --3--> 남부터미널 --2--> 양재   ==> 5
     * 교대역 --10--> 강남역 --10--> 양재    ==> 20
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(회원_성인_이메일, "password", 20);
        MemberAcceptanceTest.회원_생성을_요청(회원_청소년_이메일, "password", 13);
        MemberAcceptanceTest.회원_생성을_요청(회원_어린이_이메일, "password", 6);

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 0)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 0)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        미존재역 = StationResponse.of(new Station(99L, "미존재역"));
        미연결역1 = StationAcceptanceTest.지하철역_등록되어_있음("미연결역1").as(StationResponse.class);
        미연결역2 = StationAcceptanceTest.지하철역_등록되어_있음("미연결역2").as(StationResponse.class);
        미연결노선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("미연결노선", "bg-red-600", 미연결역1.getId(), 미연결역2.getId(), 5, 0)).as(LineResponse.class);
    }

    @TestFactory
    @DisplayName("사용자별로 경로조회를 요청한다")
    Stream<DynamicTest> findPathByMember() {
        return Stream.of(
                dynamicTest("비로그인 사용자 경로조회 요청", () -> {
                    //when 비로그인 사용자 경로조회
                    PathResponse pathResponse = 경로_조회_요청(교대역.getId(), 양재역.getId()).as(PathResponse.class);
                    //then 최단거리,요금,거리 확인
                    경로_최단거리_요금확인(pathResponse, 5, 1250);
                }),
                dynamicTest("로그인 성인 회원 경로조회 요청", () -> {
                    //given
                    TokenResponse 성인회원_토큰 = AuthAcceptanceTest.로그인_요청_성공(회원_성인_이메일, "password");
                    //when 로그인 성인 경로조회
                    PathResponse pathResponse = 경로_조회_요청(성인회원_토큰, 교대역.getId(), 양재역.getId()).as(PathResponse.class);
                    //then 최단거리,요금,거리 확인
                    경로_최단거리_요금확인(pathResponse, 5, 1250);
                }),
                dynamicTest("로그인 청소년 회원 경로조회 요청", () -> {
                    //when 로그인 청소년 경로조회
                    TokenResponse 청소년회원_토큰 = AuthAcceptanceTest.로그인_요청_성공(회원_청소년_이메일, "password");
                    //then 최단거리,요금,거리 확인
                    PathResponse pathResponse = 경로_조회_요청(청소년회원_토큰, 교대역.getId(), 양재역.getId()).as(PathResponse.class);
                    //then 최단거리,요금,거리 확인
                    경로_최단거리_요금확인(pathResponse, 5, 720);
                }),
                dynamicTest("로그인 어린이 회원 경로조회 요청", () -> {
                    //when 로그인 어린이 경로조회
                    TokenResponse 어린이회원_토큰 = AuthAcceptanceTest.로그인_요청_성공(회원_어린이_이메일, "password");
                    //then 최단거리,요금,거리 확인
                    PathResponse pathResponse = 경로_조회_요청(어린이회원_토큰, 교대역.getId(), 양재역.getId()).as(PathResponse.class);
                    //then 최단거리,요금,거리 확인
                    경로_최단거리_요금확인(pathResponse, 5, 450);
                })
        );
    }


    @Test
    @DisplayName("지하철역의 경로를 조회한다")
    void findPath() {
        //when
        PathResponse pathResponse = 경로_조회_요청(교대역.getId(), 양재역.getId()).as(PathResponse.class);
        //then
        경로_최단거리_요금확인(pathResponse, 5, 1250);
        경로_최단거리_지하철_순서_확인(pathResponse, Arrays.asList(교대역, 남부터미널역, 양재역));
    }

    @Test
    @DisplayName("지하철역의 경로를 조회한다(출발역과 도착역이 같은 경우 실패한다)")
    void findPath_case_EqualStation() {
        //when && then
        경로_조회_요청_실패한다(교대역.getId(), 교대역.getId());
    }

    @Test
    @DisplayName("지하철역의 경로를 조회한다(출발역과 도착역이 연결이 되어 있지 않은 경우 실패한다)")
    void findPath_case_notConnectStation() {
        //when && then
        경로_조회_요청_실패한다(교대역.getId(), 미연결역2.getId());
    }

    @Test
    @DisplayName("지하철역의 경로를 조회한다(존재하지 않는 출발역이나 도착역을 조회 할 경우 실패한다)")
    void findPath_case_nonExistentStation() {
        //when && then
        경로_조회_요청_실패한다(미존재역.getId(), 교대역.getId());
    }

    private void 경로_최단거리_지하철_순서_확인(PathResponse pathResponse, List<StationResponse> stations) {
        List<Long> stationsId = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationsId = stations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationsId).isEqualTo(expectedStationsId);
    }

    private void 경로_최단거리_요금확인(PathResponse pathResponse, double distance, double fare) {
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 경로_조회_요청(TokenResponse token, Long source, Long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .queryParam("source", source)
                .queryParam("target", target)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private void 경로_조회_요청_실패한다(Long sourceId, Long targetId) {
        ExtractableResponse<Response> response = 경로_조회_요청(sourceId, targetId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
