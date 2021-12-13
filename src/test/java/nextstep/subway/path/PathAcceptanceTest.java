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
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.ApiUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 남부터미널역;
    private StationResponse 양재역;

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

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단경로 찾기")
    @Test
    void getShortestPathTest() {

        long source = 강남역.getId();
        long target = 남부터미널역.getId();

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(tokenRequest);
        String accessToken = loginResponse.body().jsonPath().getString("accessToken");

        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        //when
        ExtractableResponse<Response> pathsResponse = ApiUtils.get("/paths", accessToken, params);

        //then
        PathResponse paths = pathsResponse.body().jsonPath().getObject(".", PathResponse.class);
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(paths.getStations().stream().map(StationResponse::getName)).containsExactly("강남역", "양재역", "남부터미널역");
        assertThat(paths.getDistance()).isEqualTo(12);
        assertThat(paths.getFare()).isEqualTo(1350);
    }

    @DisplayName("최단경로 찾기")
    @Test
    void getShortestPathUnAuth() {

        long source = 강남역.getId();
        long target = 남부터미널역.getId();

        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        //when
        ExtractableResponse<Response> pathsResponse = ApiUtils.get("/paths", null, params);

        //then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
