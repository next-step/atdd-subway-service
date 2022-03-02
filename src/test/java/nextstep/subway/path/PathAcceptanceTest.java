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

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 100);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 700);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 1400);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철 최단경로 및 요금을 조회한다.")
    @Test
    void find_shortest_path() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={source}&target={target}", 교대역.getId(), 양재역.getId())
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2650);
    }

    @DisplayName("로그인한 사용자의 지하철 최단경로 및 요금을 조회한다.")
    @Test
    void login_user_find_shortest_path() {
        MemberAcceptanceTest.회원_생성을_요청("email@email.com", "password", 14);
        String token = AuthAcceptanceTest.회원_토큰_요청("email@email.com", "password").jsonPath().getString("accessToken");

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + token)
            .when().get("/paths?source={source}&target={target}", 교대역.getId(), 양재역.getId())
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2190);
    }

    private LineResponse 지하철_노선_등록되어_있음(String line, String color, StationResponse startStation,
        StationResponse endStation, int distance, int additionalFare) {
        LineRequest request = new LineRequest(line, color, startStation.getId(), endStation.getId(), distance, additionalFare);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(request).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse startStation,
        StationResponse endStation, int distance) {
        ExtractableResponse<Response> response = LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, startStation, endStation, distance);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨(response);
    }

}
