package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.MemberAcceptanceTest;
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

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 중앙선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 덕소역;
    private StationResponse 구리역;
    private StationResponse 남부터미널역;

    private String 성인_인증토큰;
    private String 청소년_인증토큰;
    private String 어린이_인증토큰;

    /**
     * 교대역    --- *2호선*(10) --- 강남역
     * |                        |
     * *3호선*(3)                *신분당선*(10)
     * |                        |
     * 남부터미널역 ---*3호선*(2) ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        덕소역 = StationAcceptanceTest.지하철역_등록되어_있음("덕소역").as(StationResponse.class);
        구리역 = StationAcceptanceTest.지하철역_등록되어_있음("구리역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        중앙선 = 지하철_노선_등록되어_있음("중앙선", "bg-red-600", 덕소역, 구리역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);


        MemberAcceptanceTest.회원_생성을_요청("adult@email.com", "password", 20);
        TokenRequest 성인_인증_토큰_요청 = new TokenRequest("adult@email.com", "password");
        ExtractableResponse<Response> 성인_로그인_요청_결과 = AuthAcceptanceTest.로그인_요청(성인_인증_토큰_요청);
        성인_인증토큰 = MemberAcceptanceTest.인증_토근_가져오기(성인_로그인_요청_결과);

        MemberAcceptanceTest.회원_생성을_요청("teenager@email.com", "password", 15);
        TokenRequest 청소년 = new TokenRequest("teenager@email.com", "password");
        ExtractableResponse<Response> 청소년_로그인_요청_결과 = AuthAcceptanceTest.로그인_요청(청소년);
        청소년_인증토큰 = MemberAcceptanceTest.인증_토근_가져오기(청소년_로그인_요청_결과);

        MemberAcceptanceTest.회원_생성을_요청("child@email.com", "password", 8);
        TokenRequest 어린이_인증_토큰_요청 = new TokenRequest("child@email.com", "password");
        ExtractableResponse<Response> 어린이_로그인_요청_결과 = AuthAcceptanceTest.로그인_요청(어린이_인증_토큰_요청);
        어린이_인증토큰 = MemberAcceptanceTest.인증_토근_가져오기(어린이_로그인_요청_결과);

    }

    @DisplayName("최단경로를 찾아보자 (비로그인상태)")
    @Test
    void findShortPath() {
        ExtractableResponse<Response> response = 지하철_경로를_조회(교대역, 양재역);

        자허철_최단경로_조회됨(response);
        지하철_경로_역명이_동일함(response);
        지하철_노선_최단경로가_일치함(response);
        지하철_요금_일치함(response);
    }

    @DisplayName("어른이 최단경로를 찾을 경우 요금확인")
    @Test
    void findShortPathForAdult() {

        ExtractableResponse<Response> response = 로그인한_상태로_지하철_경로를_조회(성인_인증토큰, 교대역, 양재역);

        자허철_최단경로_조회됨(response);
        지하철_경로_역명이_동일함(response);
        지하철_노선_최단경로가_일치함(response);
        지하철_요금_일치함(response);
    }

    @DisplayName("청소년이 최단경로를 찾을 경우 요금확인")
    @Test
    void findShortPathForTeenager() {

        ExtractableResponse<Response> response = 로그인한_상태로_지하철_경로를_조회(청소년_인증토큰, 교대역, 양재역);

        자허철_최단경로_조회됨(response);
        지하철_경로_역명이_동일함(response);
        지하철_노선_최단경로가_일치함(response);
        청소년_지하철_요금_일치함(response);
    }

    @DisplayName("어린이가 최단경로를 찾을 경우 요금확인")
    @Test
    void findShortPathForChild() {

        ExtractableResponse<Response> response = 로그인한_상태로_지하철_경로를_조회(어린이_인증토큰, 교대역, 양재역);

        자허철_최단경로_조회됨(response);
        지하철_경로_역명이_동일함(response);
        지하철_노선_최단경로가_일치함(response);
        어린이_지하철_요금_일치함(response);
    }

    @DisplayName("출발역과 도착역이 같은경우는 경로조회가 안됨")
    @Test
    void sameStationSourceAndTarget() {
        ExtractableResponse<Response> response = 지하철_경로를_조회(강남역, 강남역);

        경로_조회가_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이되어 있지 않은경우는 경로조회가 안됨")
    @Test
    void notConnectedStationSourceAndTarget() {
        ExtractableResponse<Response> response = 지하철_경로를_조회(강남역, 덕소역);

        경로_조회가_실패됨(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우 안됨")
    @Test
    void notExistStationSourceAndTarget() {

        StationResponse 잠실역 = new StationResponse(100L, "잠실역");
        ExtractableResponse<Response> response = 지하철_경로를_조회(잠실역, 강남역);

        경로_조회가_실패됨(response);
    }

    private void 경로_조회가_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 자허철_최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_최단경로가_일치함(ExtractableResponse<Response> response) {
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(5);
    }

    private void 지하철_경로_역명이_동일함(ExtractableResponse<Response> response) {
        List<StationResponse> stations = response.as(PathResponse.class).getStations();
        List<String> stationNames = stations.stream().map(stationResponse -> stationResponse.getName()).collect(Collectors.toList());

        List<String> expectedNames = Arrays.asList(교대역, 남부터미널역, 양재역).stream().map(stationResponse -> stationResponse.getName()).collect(Collectors.toList());
        assertThat(stationNames).containsExactlyElementsOf(expectedNames);
    }

    private void 지하철_요금_일치함(ExtractableResponse<Response> response) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    private void 청소년_지하철_요금_일치함(ExtractableResponse<Response> response) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(720);
    }

    private void 어린이_지하철_요금_일치함(ExtractableResponse<Response> response) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(450);
    }

    private ExtractableResponse<Response> 지하철_경로를_조회(StationResponse source, StationResponse target) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
                .then().log().all().
                        extract();
        return response;
    }

    private ExtractableResponse<Response> 로그인한_상태로_지하철_경로를_조회(String token, StationResponse source, StationResponse target) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
                .then().log().all().
                        extract();
        return response;
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStationResponse, StationResponse downStationResponse, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse, upStationResponse, downStationResponse, distance);
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

}
