package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 선릉역;
    private StationResponse 수서역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        수서역 = 지하철역_등록되어_있음("수서역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 양재역.getId(), 강남역.getId(), 4, 900)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 선릉역.getId(), 20, 700)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 3)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(이호선, 교대역, 강남역, 9);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 2);
    }

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void findPath() {
        회원_생성을_요청("email@email.com", "password", AGE);
        ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest("email@email.com", "password"));
        String token = tokenResponse.as(TokenResponse.class).getAccessToken();

        ExtractableResponse<Response> response = 경로_조회_요청(남부터미널역.getId(), 선릉역.getId(), "Bearer", token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class).getStations()).extracting(StationResponse::getName)
                .containsExactly("남부터미널역", "양재역", "강남역", "선릉역");
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(18);
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(2050);
    }

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target, String bearerType, String token) {
        return RestAssured
                .given().log().all()
                .header("Authorization", bearerType + token)
                .when().get("/paths?source={source}&target={target}", source, target)
                .then().log().all()
                .extract();
    }
}
