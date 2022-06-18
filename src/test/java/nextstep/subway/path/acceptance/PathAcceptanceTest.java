package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.getAccessToken;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 혜화역;
    private StationResponse 사당역;
    private String token;

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
        혜화역 = StationAcceptanceTest.지하철역_등록되어_있음("혜화역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        사호선 = 지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-red-600", 혜화역.getId(), 사당역.getId(), 20)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = getAccessToken(로그인_요청(EMAIL, PASSWORD));
    }

    // 최단경로 조회
    @DisplayName("최단경로 조회")
    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> 최단경로_조회_결과 = 최단경로_조회(교대역, 양재역);

        // then
        최단경로_조회됨(최단경로_조회_결과);
        최단경로_지하철역_목록과_거리_요금_조회됨(최단경로_조회_결과, new PathResponse(Arrays.asList(교대역, 남부터미널역, 양재역), 5, 1250));

    }

    @DisplayName("최단경로 조회 실패 시나리오")
    @Test
    public void findPath_fail_scenario() throws Exception {
        // given
        StationResponse 존재하지않는역 = StationAcceptanceTest.지하철역_등록되어_있음("존재하지않는역").as(StationResponse.class);
        
        // when
        ExtractableResponse<Response> 출발역과_도착역이_같은_최단경로_조회_결과 = 최단경로_조회(교대역, 교대역);
        // Then 조회 요청이 실패됨
        최단경로_조회_실패됨(출발역과_도착역이_같은_최단경로_조회_결과);

        // when
        ExtractableResponse<Response> 출발역과_도착역이_연결되어_있지않은_최단경로_조회_결과 = 최단경로_조회(혜화역, 남부터미널역);
        // Then 조회 요청이 실패됨
        최단경로_조회_실패됨(출발역과_도착역이_연결되어_있지않은_최단경로_조회_결과);

        // when
        ExtractableResponse<Response> 존재하지_않는_역_최단경로_조회_결과 = 최단경로_조회(교대역, 존재하지않는역);
        // Then 조회 요청이 실패됨
        최단경로_조회_실패됨(존재하지_않는_역_최단경로_조회_결과);
    }

    private  ExtractableResponse<Response> 최단경로_조회(StationResponse 출발역, StationResponse 도착역) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", 출발역.getId(), 도착역.getId())
                .then().log().all()
                .extract();
    }

    private void 최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단경로_지하철역_목록과_거리_요금_조회됨(ExtractableResponse<Response> response, PathResponse expectedPath) {
        PathResponse path = response.as(PathResponse.class);

        List<Long> stationIds = path.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedPath.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(stationIds).isEqualTo(expectedStationIds),
                () -> assertThat(path.getDistance()).isEqualTo(expectedPath.getDistance()),
                () -> assertThat(path.getFare()).isEqualTo(expectedPath.getFare())
        );
    }

    private void 최단경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
