package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.acceptance.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남구청역, 선정릉역, 선릉역, 연주역, 삼성중앙역, 봉은사역, 종합운동장역, 교대역, 강남역, 삼성역;
    private LineResponse 수인분당선, 구호선, 이호선;

    /**
     *                                    강남구청역
     *                                        |
     *                                    (수인분당, 4)
     *                                        |
     *                  연주역--(9호선, 3)--선정릉역--(9호선, 3)--삼성중앙역--(9호선, 3)--봉은사역--(9호선, 2)
     *                                        |                                                 |
     *                                    (수인분당, 4)                                      종합운동장역
     *                                        |                                                 |
     * 교대역--(2호선, 3)--강남역--(2호선, 3)--선릉역--(2호선, 6)-------------삼성역--------------(2호선, 7)
     */

    @BeforeEach
    void setUpTest() {
        super.setUp();

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

        수인분당선 = 지하철_노선_등록되어_있음("수인분당선", "bg-yellow-600", 선릉역, 선정릉역, 4);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(수인분당선, 선정릉역, 강남구청역, 4);

        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-partridge-600", 연주역, 선정릉역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 선정릉역, 삼성중앙역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 삼성중앙역, 봉은사역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 봉은사역, 종합운동장역, 2);

        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 선릉역, 2);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 선릉역, 삼성역, 6);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 종합운동장역, 7);
    }

    /**
     * given 여러역이 포함된 하나의 노선에
     * when 해당 노선에 포함된 두개의 역의 경로를 조회하면
     * then 두개의 역의 최소 경로와 거리가 조회되어야 한다
     */
    @DisplayName("두개이상 역이 포함된 노선에 경로를 검색하면 정상 조회되어야 한다")
    @Test
    void findPathByOneLine() {

    }

    /**
     * given 여러역이 포함된 여러 노선에
     * when 연결된 각각 다른 노선에 포함된 2개의 역의 경로를 조회하면
     * then 해당 역의 최소 거리의 경로가 조회되어야 한다
     */
    @DisplayName("여러역이 포함된 여러 노선에 환승이 가능한 각각 다른 노선에 포함된 역의 경로를 조회하면 정상 조회되어야 한다")
    @Test
    void findPathByMultipleLine() {

    }

    /**
     * given 출발역과 도착역이 같은 역의 경로를 조회하면
     * then 예외가 발생해야 한다
     */
    @DisplayName("출발역과 도착역이 같은 역의 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findPathBySameStartAndEndStation() {

    }

    /**
     * given 노선에 출발역 또는 도착역이 등록되지 않은 역의 경로를 조회하면
     * then 예외가 발생해야 한다
     */
    @DisplayName("출발역 또는 도착역이 등록되지 않은 역의 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findPathByUnregisteredStartOrEndStation() {

    }

    /**
     * given 하나의 노선 또는 환승이 불가능한 두개의 역의 경로를 조회하면
     * then 예외가 발생해야 한다
     */
    @DisplayName("연결되지 않은 역의 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findPathByNotConnectedStations() {

    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }
}
