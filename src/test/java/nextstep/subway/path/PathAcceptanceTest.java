package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
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

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_인증토큰_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어있음;
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
    private StationResponse 수서역;
    private StationResponse 판교역;
    private String 청소년;
    private String 어린이;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                            |
     * *3호선*                   *신분당선*(추가 운임비 : 800원)
     * |                            |
     * 남부터미널역  --- *3호선* --- 양재역  --- *3호선* ---  수서역
     *                              |
     *                          *신분당선*(추가 운임비 : 800원)
     *                              |
     *                            판교역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        수서역 = StationAcceptanceTest.지하철역_등록되어_있음("수서역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 800)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 15)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 10);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 양재역, 수서역, 40);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 판교역, 20);

        회원_등록되어있음("청소년@email.com", "me", 15);
        회원_등록되어있음("어린이@email.com", "me", 8);
        청소년 = 로그인_인증토큰_요청("청소년@email.com", "me").as(TokenResponse.class).getAccessToken();
        어린이 = 로그인_인증토큰_요청("어린이@email.com", "me").as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("여러가지 최단 거리 경로 조회하면서 최단 경로와 요금 응답 성공을 검증")
    @Test
    void shortestPath() {
        // when
        ExtractableResponse<Response> response1 = 최단_거리_경로_조회_요청(교대역, 남부터미널역);

        // then
        최단_거리_경로_응답_성공(response1);
        최단_경로와_총_소요_거리와_적절한_요금을_응답(response1, Arrays.asList(교대역, 남부터미널역), 10, 1250);

        // when
        ExtractableResponse<Response> response2 = 최단_거리_경로_조회_요청(교대역, 양재역);

        // then
        최단_거리_경로_응답_성공(response2);
        최단_경로와_총_소요_거리와_적절한_요금을_응답(response2, Arrays.asList(교대역, 남부터미널역, 양재역), 15, 1350);

        // when
        ExtractableResponse<Response> response3 = 최단_거리_경로_조회_요청(교대역, 수서역);

        // then
        최단_거리_경로_응답_성공(response3);
        최단_경로와_총_소요_거리와_적절한_요금을_응답(response3, Arrays.asList(교대역, 남부터미널역, 양재역, 수서역), 55, 2050);

        ExtractableResponse<Response> response4 = 최단_거리_경로_조회_요청(교대역, 판교역);

        // then
        최단_거리_경로_응답_성공(response4);
        최단_경로와_총_소요_거리와_적절한_요금을_응답(response4, Arrays.asList(교대역, 남부터미널역, 양재역, 판교역), 35, 2550);
    }

    @DisplayName("청소년과 어린이 사용자가 최단 경로와 요금 조회")
    @Test
    void shortestPath2() {
        // when
        ExtractableResponse<Response> response1 = 로그인_사용자_최단_거리_경로_조회_요청(청소년, 교대역, 수서역);

        // then
        최단_거리_경로_응답_성공(response1);
        최단_경로와_총_소요_거리와_적절한_요금을_응답(response1, Arrays.asList(교대역, 남부터미널역, 양재역, 수서역), 55, 1360);

        ExtractableResponse<Response> response2 = 로그인_사용자_최단_거리_경로_조회_요청(어린이, 교대역, 판교역);

        // then
        최단_거리_경로_응답_성공(response2);
        최단_경로와_총_소요_거리와_적절한_요금을_응답(response2, Arrays.asList(교대역, 남부터미널역, 양재역, 판교역), 35, 1100);
    }

    private void 최단_경로와_총_소요_거리와_적절한_요금을_응답(ExtractableResponse<Response> response, List<StationResponse> stations, int totalDistance, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> resultIds = pathResponse.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        List<Long> expectedIds = stations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultIds).isEqualTo(expectedIds);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    private void 최단_거리_경로_응답_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 최단_거리_경로_조회_요청(StationResponse upStation, StationResponse downStation) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body("false")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", upStation.getId(), downStation.getId())
                .then().log().all().extract();
        return response;
    }

    private ExtractableResponse<Response> 로그인_사용자_최단_거리_경로_조회_요청(String accessToken, StationResponse upStation, StationResponse downStation) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", upStation.getId(), downStation.getId())
                .then().log().all().extract();
        return response;
    }
}
