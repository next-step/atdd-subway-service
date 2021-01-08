package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.fare.dto.PathWithFareResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 판교역;
    private StationResponse 사당역;
    private StationResponse 이수역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        이수역 = StationAcceptanceTest.지하철역_등록되어_있음("이수역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 남부터미널역, 5,2000);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 3);
        String email = "hglee";
        String password = "1234";
        AuthAcceptanceTest.회원_등록되어_있음(email, password, 18);
        accessToken = AuthAcceptanceTest.토큰_발급_요청(email, password).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("지하철 경로를 조회하고 순서, 거리가 일치하는지 확인한다.")
    @Test
    void findPath() {
        // 지하철 경로 조회 요청
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 양재역.getId());

        // 지하철 경로 조회됨
        // then
        PathWithFareResponse as = response.as(PathWithFareResponse.class);
        assertThat(as.getStations()).extracting("name")
                .containsExactly("교대역", "남부터미널역", "양재역");
        assertThat(as.getDistance()).isEqualTo(8);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 경로를 조회하고 회원 요금이 일치하는지 확인한다.")
    @Test
    void findPathThanContainsTotalFareMember() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 양재역.getId(), accessToken);
        PathWithFareResponse as = response.as(PathWithFareResponse.class);

        assertThat(as.getFare()).isEqualTo(2320);
    }

    @DisplayName("지하철 경로를 조회하고 비회원 요금이 일치하는지 확인한다.")
    @Test
    void findPathThanContainsTotalFareNotMember() {
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 양재역.getId());
        PathWithFareResponse as = response.as(PathWithFareResponse.class);

        assertThat(as.getFare()).isEqualTo(3250);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void failFindPathNotExistStation() {
        // 지하철 경로 조회 요청
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 판교역.getId());

        // 지하철 경로 조회 실패됨
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void failFindPathEqualsStation() {
        // 지하철 경로 조회 요청
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 교대역.getId());

        // 지하철 경로 조회 실패됨
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void findPathWhenNotConnectedStation() {
        // 지하철 경로 조회 요청
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(교대역.getId(), 사당역.getId());

        // 지하철 경로 조회 실패됨
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(long source, long target) {
        return RestAssured.given().log().all().
                param("source", source).
                param("target", target).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths").
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(long source, long target, String accessToken) {
        return RestAssured.given().auth().oauth2(accessToken).log().all().
                param("source", source).
                param("target", target).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths").
                then().
                log().all().
                extract();
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance, int fare) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance, fare);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse, upStation, downStation, distance);
    }
}
