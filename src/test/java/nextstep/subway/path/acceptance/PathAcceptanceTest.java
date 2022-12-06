package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 칠호선;
    private StationResponse 광산역;
    private StationResponse 학동역;
    private StationResponse 강남구청역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private Member 어린이회원 = new Member("kids@test.com", "test123", 7);
    private String 어린이회원토큰;
    private Member 청소년회원 = new Member("teen@test.com", "test124", 18);
    private String 청소년회원토큰;
    private Member 성인회원 = new Member("adult@test.com", "test324", 25);
    private String 성인회원토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철_역_등록();
        지하철_노선_등록();
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        나이별_회원_로그인();
    }


    @DisplayName("최단경로 조회에서 노선에 포함되지 않는 역을 조회하면 조회안됨")
    @Test
    void returnsExceptionWithNoneExistsStartStationInLines() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원토큰, 광산역.getId(), 남부터미널역.getId());
        최단경로_목록_조회_실패(response);
    }

    @DisplayName("최단경로 조회에서 존재하지 않는 역을 조회하면 조회안됨")
    @Test
    void returnsExceptionWithNoneExistsStartStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원토큰, -1l, 남부터미널역.getId());
        최단경로_목록_조회_실패(response);
    }

    @DisplayName("최단경로 조회에서 출발역과 도착역이 같으면 조회안됨")
    @Test
    void returnsExceptionWithSameStations() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원토큰, 남부터미널역.getId(), 남부터미널역.getId());
        최단경로_목록_조회_실패(response);
    }

    @DisplayName("최단경로 조회에서 출발역과 도착역이 연결되지않으면 조회안됨")
    @Test
    void returnsExceptionWithNoneLinkStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원토큰, 학동역.getId(), 강남역.getId());
        최단경로_목록_조회_실패(response);
    }

    @DisplayName("최단경로 조회에서 출발역과 도착역이 연결된상태로 존재하면 최단경로 조회")
    @Test
    void returnsShortestPath() {
        성인_최단_경로조회_성공();
        청소년_최단_경로조회_성공();
        어린이_최단_경로조회_성공();
    }

    private void 성인_최단_경로조회_성공() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(성인회원토큰, 강남역.getId(), 남부터미널역.getId());
        최단경로_목록_조회_성공(response);
        최단경로_거리_조회_성공(response,12);
        최단경로_요금_조회_성공(response,1350);
    }

    private void 청소년_최단_경로조회_성공() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(청소년회원토큰, 강남역.getId(), 남부터미널역.getId());
        최단경로_목록_조회_성공(response);
        최단경로_거리_조회_성공(response,12);
        최단경로_요금_조회_성공(response,800);
    }

    private void 어린이_최단_경로조회_성공() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(어린이회원토큰, 강남역.getId(), 남부터미널역.getId());
        최단경로_목록_조회_성공(response);
        최단경로_거리_조회_성공(response,12);
        최단경로_요금_조회_성공(response,500);
    }

    private void 지하철_역_등록() {
        광산역 = StationAcceptanceTest.지하철역_등록되어_있음("광산역").as(StationResponse.class);
        학동역 = StationAcceptanceTest.지하철역_등록되어_있음("학동역").as(StationResponse.class);
        강남구청역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
    }

    private void 지하철_노선_등록() {
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        칠호선 = 지하철_노선_등록되어_있음(new LineRequest("칠호선", "bg-red-600", 학동역.getId(), 강남구청역.getId(), 30)).as(LineResponse.class);
    }

    private void 나이별_회원_로그인() {
        어린이_회원_로그인();
        청소년_회원_로그인();
        성인_회원_로그인();
    }

    private void 어린이_회원_로그인() {
        회원_생성을_요청(어린이회원.getEmail(), 어린이회원.getPassword(), 어린이회원.getAge());
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(어린이회원.getEmail(), 어린이회원.getPassword()));
        로그인_성공(loginResponse);
        어린이회원토큰 = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 청소년_회원_로그인() {
        회원_생성을_요청(청소년회원.getEmail(), 청소년회원.getPassword(), 청소년회원.getAge());
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(청소년회원.getEmail(), 청소년회원.getPassword()));
        로그인_성공(loginResponse);
        청소년회원토큰 = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    private void 성인_회원_로그인() {
        회원_생성을_요청(성인회원.getEmail(), 성인회원.getPassword(), 성인회원.getAge());
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(성인회원.getEmail(), 성인회원.getPassword()));
        로그인_성공(loginResponse);
        성인회원토큰 = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, long sourceId, long targetId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("sourceId", sourceId)
                .param("targetId", targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단경로_거리_조회_성공(ExtractableResponse<Response> response,int distance) {
        Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    public static void 최단경로_요금_조회_성공(ExtractableResponse<Response> response, int fare) {
        Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }

    public static void 최단경로_목록_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단경로_목록_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
