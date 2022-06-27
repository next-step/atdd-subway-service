package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.acceptance.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.신규_회원가입_후_로그인_토큰_추출;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/paths";
    private static final String MY_EMAIL = "test@test.com";
    private static final String MY_PASSWORD = "1234";
    private static final int MY_AGE = 20;

    private StationResponse 강남구청역, 선정릉역, 선릉역, 연주역, 삼성중앙역, 봉은사역, 종합운동장역, 교대역, 강남역, 삼성역, 광교역, 광교중앙역, 상현역, 성복역;
    private LineResponse 수인분당선, 구호선, 이호선, 신분당선;

    private String myToken;

    /**
     *                                     선정릉역--(9호선, 3)--삼성중앙역--(9호선, 3)--봉은사역--(9호선, 2)
     *                                        |                                                 |
     *                                    (수인분당, 4)                                      종합운동장역
     *                                        |                                                 |
     * 교대역--(2호선, 3)--강남역--(2호선, 3)--선릉역--(2호선, 6)-------------삼성역--------------(2호선, 7)
     *
     * 광교역--(신분당, 10)--광교중앙역--(신분당, 40)--상현역--(신분당, 1)--성복역
     */

    @BeforeEach
    void setUpTest() {
        super.setUp();

        myToken = 신규_회원가입_후_로그인_토큰_추출(MY_EMAIL, MY_PASSWORD, MY_AGE);

        강남구청역 = StationAcceptanceTest.지하철역_등록되어_있음("강남구청역").as(StationResponse.class);
        선정릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선정릉역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        연주역 = StationAcceptanceTest.지하철역_등록되어_있음("연주역").as(StationResponse.class);
        삼성중앙역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성중앙역").as(StationResponse.class);
        봉은사역 = StationAcceptanceTest.지하철역_등록되어_있음("봉은사역").as(StationResponse.class);
        종합운동장역 = StationAcceptanceTest.지하철역_등록되어_있음("종합운동장역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        광교중앙역 = StationAcceptanceTest.지하철역_등록되어_있음("광교중앙역").as(StationResponse.class);
        상현역 = StationAcceptanceTest.지하철역_등록되어_있음("상현역").as(StationResponse.class);
        성복역 = StationAcceptanceTest.지하철역_등록되어_있음("성복역").as(StationResponse.class);

        수인분당선 = 지하철_노선_등록되어_있음("수인분당선", "bg-yellow-600", 선릉역, 선정릉역, 4);

        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-partridge-600", 선정릉역, 삼성중앙역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 삼성중앙역, 봉은사역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 봉은사역, 종합운동장역, 2);

        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 선릉역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 선릉역, 삼성역, 6);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 종합운동장역, 7);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-green-600", 광교역, 광교중앙역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 광교중앙역, 상현역, 40);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 상현역, 성복역, 1);
    }

    /**
     * given 여러역이 포함된 하나의 노선에
     * when 해당 노선에 포함된 두개의 역의 경로를 조회하면
     * then 두개의 역의 최소 경로와 거리가 조회되어야 한다
     */
    @DisplayName("두개이상 역이 포함된 노선에 경로를 검색하면 정상 조회되어야 한다")
    @Test
    void findPathByOneLine() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(교대역, 선릉역, myToken);

        // then
        노선_경로_조회_성공됨(response);
        노선_경로와_거리가_최단으로_정상_조회됨(response, 6, 교대역, 강남역, 선릉역);
    }

    /**
     * given 여러역이 포함된 여러 노선에
     * when 연결된 각각 다른 노선에 포함된 2개의 역의 경로를 조회하면
     * then 해당 역의 최소 거리의 경로가 조회되어야 한다
     */
    @DisplayName("여러역이 포함된 여러 노선에 환승이 가능한 각각 다른 노선에 포함된 역의 경로를 조회하면 정상 조회되어야 한다")
    @Test
    void findPathByMultipleLine() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(교대역, 종합운동장역, myToken);

        // then
        노선_경로_조회_성공됨(response);
        노선_경로와_거리가_최단으로_정상_조회됨(response, 18, 교대역, 강남역, 선릉역, 선정릉역, 삼성중앙역, 봉은사역, 종합운동장역);
    }

    /**
     * given 출발역과 도착역이 같은 역의 경로를 조회하면
     * then 예외가 발생해야 한다
     */
    @DisplayName("출발역과 도착역이 같은 역의 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findPathBySameStartAndEndStation() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(교대역, 교대역, myToken);

        // then
        노선_경로_조회_실패됨(response);
    }

    /**
     * given 노선에 출발역 또는 도착역이 등록되지 않은 역의 경로를 조회하면
     * then 예외가 발생해야 한다
     */
    @DisplayName("출발역 또는 도착역이 등록되지 않은 역의 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findPathByUnregisteredStartOrEndStation() {
        // when
        ExtractableResponse<Response> failByUnregisteredStartStation = 노선_경로_조회(연주역, 선정릉역, myToken);
        ExtractableResponse<Response> failByUnregisteredEndStation = 노선_경로_조회(선정릉역, 강남구청역, myToken);

        // then
        노선_경로_조회_실패됨(failByUnregisteredStartStation);
        노선_경로_조회_실패됨(failByUnregisteredEndStation);
    }

    /**
     * given 하나의 노선 또는 환승이 불가능한 두개의 역의 경로를 조회하면
     * then 예외가 발생해야 한다
     */
    @DisplayName("연결되지 않은 역의 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findPathByNotConnectedStations() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(광교역, 강남역, myToken);

        // then
        노선_경로_조회_실패됨(response);
    }

    /**
     * given 여러역이 포함된 노선에
     * when 최소 운임 거리(10km 이하)에 해당하는 경로를 조회하면
     * then 운임 비용은 해당 구간의 비용으로 정상 계산되어야 한다
     */
    @DisplayName("최소 운임 거리의 경로를 조회하면 기본 비용으로 요금이 조회되어야 한다")
    @Test
    void findPathMinimumDistanceTest() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(광교역, 광교중앙역, myToken);

        // then
        노선_경로_비용_정상_조회됨(response, 1_250);
    }

    /**
     * given 여러역이 포함된 노선에
     * when 추가 운임 거리(10km 초과 50km 이하)에 해당하는 경로를 조회하면
     * then 운임 비용은 해당 구간의 비용으로 정상 계산되어야 한다
     */
    @DisplayName("10km ~ 50km 미만의 거리의 경로를 조회하면 해당 구간의 비용으로 계산되어야 한다")
    @Test
    void findDefaultAdditionalDistanceTest() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(광교역, 상현역, myToken);

        // then
        노선_경로_비용_정상_조회됨(response, 2_050);
    }

    /**
     * given 여러역이 포함된 노선에
     * when 추가 운임 거리가 최대거리 이상 해당하는 경로를 조회하면
     * then 운임 비용은 해당 구간의 비용으로 정상 계산되어야 한다
     */
    @DisplayName("50km 초과 거리의 경로를 조회하면 해당 구간의 비용으로 계산되어야 한다")
    @Test
    void findLongerDistanceTest() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(광교역, 성복역, myToken);

        // then
       노선_경로_비용_정상_조회됨(response, 2_150);
    }

    /**
     * given 여러역이 포함된 노선에
     * when 추가 운임 거리가 최대거리 이상 해당하는 경로를 로그인 없이 조회하면
     * then 운임 비용은 해당 구간의 비용으로 할인 없이 정상 계산되어야 한다 (어른 요금으로 계산되어야 한다)
     */
    @DisplayName("로그인 없이 구간 검색을 하면 할인 없이 어른 요금으로 정상 조회되어야 한다")
    @Test
    void findDistanceWithoutLoginTest() {
        // when
        ExtractableResponse<Response> response = 노선_경로_조회(광교역, 성복역, null);

        // then
        노선_경로_비용_정상_조회됨(response, 2_150);
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private ExtractableResponse<Response> 노선_경로_조회(StationResponse source, StationResponse target, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source.getId());
        params.put("target", target.getId());

        return RestAssuredRequest.getRequest(PATH, params, token);
    }

    private void 노선_경로_조회_성공됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    private void 노선_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    private void 노선_경로_비용_정상_조회됨(ExtractableResponse<Response> response, int expectedFare) {
        노선_경로_조회_성공됨(response);
        int totalFare = response.as(ShortestPathResponse.class).getTotalFare();

        assertThat(totalFare).isEqualTo(expectedFare);
    }

    private void 노선_경로와_거리가_최단으로_정상_조회됨(ExtractableResponse<Response> response, int expectDistance, StationResponse... expectStations) {
        ShortestPathResponse pathResponse = response.as(ShortestPathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(expectDistance);
        for (int idx = 0; idx < expectStations.length; idx++) {
            PathStation pathStation = pathResponse.getStations().get(idx);
            StationResponse compareStation = expectStations[idx];

            assertThat(pathStation.getId()).isEqualTo(compareStation.getId());
            assertThat(pathStation.getName()).isEqualTo(compareStation.getName());
            assertThat(pathStation.getCreatedAt()).isEqualTo(compareStation.getCreatedDate());
        }
    }
}
