package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestFixture.회원_생성을_요청;
import static nextstep.subway.path.PathAcceptanceTestFixture.이용_요금_조회됨;
import static nextstep.subway.path.PathAcceptanceTestFixture.총_거리_조회됨;
import static nextstep.subway.path.PathAcceptanceTestFixture.최단_경로_조회_실패됨_400;
import static nextstep.subway.path.PathAcceptanceTestFixture.최단_경로_조회_실패됨_404;
import static nextstep.subway.path.PathAcceptanceTestFixture.최단_경로_조회_요청;
import static nextstep.subway.path.PathAcceptanceTestFixture.최단_경로_조회됨;
import static nextstep.subway.station.StationAcceptanceTestFixture.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 공항선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 김포공항역;
    private StationResponse 마곡나루역;
    private String 사용자;

    /**
     * 김포공항    --- *공항선* --- 마곡나루
     * 교대역    --- *2호선* ---   강남역
     * |                            |
     * *3호선*                   *신분당선*
     * |                           |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        김포공항역 = 지하철역_등록되어_있음("김포공항역").as(StationResponse.class);
        마곡나루역 = 지하철역_등록되어_있음("마곡나루역").as(StationResponse.class);

        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 5, 100);
        신분당선 = 지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10, 200);
        이호선 = 지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-blue-600", 교대역.getId(), 양재역.getId(), 20, 300);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);
        LineRequest 공항선_요청 = new LineRequest("공항선", "bg-purple-600", 김포공항역.getId(), 마곡나루역.getId(), 30, 500);
        공항선 = 지하철_노선_등록되어_있음(공항선_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 15);

        회원_생성을_요청("valid@email.com", "valid_password", 26);
        사용자 = 로그인_됨("valid@email.com", "valid_password");
    }

    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최단 거리 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     And 로그인 되어 있음
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리도 함께 응답함
     *     And ** 지하철 이용 요금도 함께 응답함 **
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findShortestPathWithFare() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사용자, 남부터미널역, 강남역);

        최단_경로_조회됨(response, Arrays.asList(남부터미널역, 양재역, 강남역));
        총_거리_조회됨(response, 10);
        이용_요금_조회됨(response, 1550);
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 동일한 출발역과 도착역으로 최단 경로를 조회하면
     *  Then 최단 경로 조회에 실패한다.
     */
    @DisplayName("출발역과 도착역이 같을 경우 최단 경로를 조회할 수 없다.")
    @Test
    void findShortestPathWithSameStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사용자, 강남역, 강남역);

        최단_경로_조회_실패됨_400(response);
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 연결되어 있지 않은 출발역과 도착역의 최단 경로를 조회하면
     *  Then 최단 경로 조회에 실패한다.
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 최단 경로를 조회할 수 없다.")
    @Test
    void findShortestPathWithNotConnectStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사용자, 김포공항역, 강남역);

        최단_경로_조회_실패됨_400(response);
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 존재하지 않는 출발역이나 도착역으로 최단 경로를 조회하면
     *  Then 최단 경로 조회에 실패한다.
     */
    @DisplayName("존재하지 않는 출발역이나 도착역으로 최단 경로를 조회할 수 없다.")
    @Test
    void findShortestPathWithNotExistStation() {
        StationResponse 존재하지_않는_역 = new StationResponse(Long.MAX_VALUE, "구글역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사용자, 김포공항역, 존재하지_않는_역);

        최단_경로_조회_실패됨_404(response);
    }
}
