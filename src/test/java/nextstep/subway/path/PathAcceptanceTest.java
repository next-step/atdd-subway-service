package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.fare.FareAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.springframework.http.*;

import io.restassured.*;
import io.restassured.response.*;
import nextstep.subway.*;
import nextstep.subway.auth.dto.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.path.dto.*;
import nextstep.subway.station.domain.*;
import nextstep.subway.station.dto.*;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 일반토큰;

    /**
     *                 10
     * 교대역   ----- *2호선* -----  강남역
     * |                            |
     * *3호선*  3                  *신분당선*    10
     * |                            |
     * 남부터미널역 ----- *3호선* -----   양재
     *                   5
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        // Scenario: 두 역의 최단 거리 경로를 조회
        // Given 지하철역이 등록되어있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // And 지하철 노선이 등록되어있음
        신분당선 = 지하철_노선_등록되어_있음(
            LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
            LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
            LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 5))
            .as(LineResponse.class);

        // And 지하철 노선에 지하철역이 등록되어있음
        지하철_노선에_지하철_구간_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성됨(회원_생성을_요청(일반_아이디_생성_요청));
        일반토큰 = 로그인_요청(일반_아이디_생성_요청).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("지하철 최단 경로를 조회한다.")
    @Test
    void shortestPathTest() {
        // When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 남부터미널역.getId(), 일반토큰);

        // Then 최단 거리 경로를 응답
        정답_응답_확인(response);

        // And 총 거리도 함께 응답함
        최단_경로_거리_확인(response);
        최단_경로_역_확인(response, Arrays.asList("강남역", "교대역", "남부터미널역"));

        // And ** 지하철 이용 요금도 함께 응답함 **
        지하철_이용_요금_응답_확인(response);
    }

    public static ExtractableResponse<Response> 최단_경로_조회(Long source, Long target, String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .get("/paths?source={source}&target={target}", source, target)
            .then().log().all()
            .extract()
            ;
    }

    private void 지하철_이용_요금_응답_확인(ExtractableResponse<Response> response) {
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(1350);
    }

    private void 정답_응답_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_거리_확인(ExtractableResponse<Response> response) {
        assertThat(response.as(PathResponse.class).getTotalDistance()).isEqualTo(13);
    }

    private void 최단_경로_역_확인(ExtractableResponse<Response> response, List<String> stationNames) {
        List<String> orderedStationNames = response.as(PathResponse.class)
            .getStations()
            .stream()
            .map(Station::getName)
            .collect(Collectors.toList());

        assertThat(orderedStationNames).isEqualTo(stationNames);
    }
}
