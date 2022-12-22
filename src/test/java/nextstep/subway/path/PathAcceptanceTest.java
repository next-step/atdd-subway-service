package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends PathAcceptanceTestFixture {

    /**
     * Given 한 개의 노선에 2개 이상 역이 등록되어 있고
     * When 두 역을 출발지와 도착지로 지정하여 경로를 조회하면
     * Then 경로가 조회된다
     * And 총 거리가 조회된다
     * And 지하철 이용 요금이 조회된다
     */
    @DisplayName("환승하지 않는 지하철 경로 조회")
    @Test
    void 환승_불필요_경로_조회() {
        // Given
        //3호선 교대역 -3- 남부터미널역 -2- 양재역

        // When
        ExtractableResponse<Response> response = 경로_조회_요청(교대역.getId(), 양재역.getId());

        // Then
        경로_조회됨(response);

        // Then
        PathResponse pathResponse = 경로응답(response);
        assertThat(pathResponse.getStations()).containsExactlyElementsOf(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(pathResponse.getPathDistance()).isEqualTo(5);
        assertThat(pathResponse.getFare()).isEqualTo(1250 + 500); // 거리에 따른 기본요금 + 노선별 추가요금
    }

    /**
     * Given 환승역이 존재하는 2개의 노선에 환승역이 아닌 역이 각각 등록되어있고
     * When 환승역이 아닌 두 역을 출발역과 도착역으로 지정하여 경로를 조회하면
     * Then 경로가 조회된다
     * And 총 거리가 조회된다
     * And 지하철 이용 요금이 조회된다
     */
    @DisplayName("환승하는 지하철 경로 조회")
    @Test
    void 환승_필요_경로_조회() {
        // Given
        //남부터미널역(3호선) -2- 양재역(환승) -10- 강남역(2호선)

        // When
        ExtractableResponse<Response> response = 경로_조회_요청(남부터미널역.getId(), 강남역.getId());

        // Then
        경로_조회됨(response);

        // Then
        PathResponse pathResponse = 경로응답(response);
        assertThat(pathResponse.getStations()).containsExactlyElementsOf(Arrays.asList(남부터미널역, 양재역, 강남역));
        assertThat(pathResponse.getPathDistance()).isEqualTo(12);
        assertThat(pathResponse.getFare()).isEqualTo(1350 + 900); // 거리에 따른 기본요금 + 노선별 추가요금
    }

    /**
     * 예외 케이스
     * When 출발역과 도착역을 같게 지정하고 경로를 조회하면
     * Then 경로 조회에 실패한다
     */
    @DisplayName("출발역과 도착역이 같은 경로 조회 요청")
    @Test
    void 출발역과_도착역이_같은_경로_조회() {
        // When
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 강남역.getId());

        // Then
        경로_조회_실패함(response);
    }

    /**
     * 예외 케이스
     * Given 연결되지 않은 두 역이 있고
     * When  두 역을 각각 출발지와 도착지로 지정하고 경로를 조회하면
     * Then 경로 조회에 실패한다
     */
    @DisplayName("연결되지 않은 두 역의 경로 조회 요청")
    @Test
    void 연결되지_않은_두_역_사이_경로_조회() {
        // Given
        StationResponse 고속터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);
        StationResponse 봉은사역 = StationAcceptanceTest.지하철역_등록되어_있음("봉은사역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("9호선", "bg-brown-600", 고속터미널역.getId(), 봉은사역.getId(), 60);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // When
        ExtractableResponse<Response> response = 경로_조회_요청(남부터미널역.getId(), 봉은사역.getId());

        // Then
        경로_조회_실패함(response);
    }

    /**
     * 예외 케이스
     * When  존재하지 않는 역을 출발지 또는 도착지로 지정하고 경로를 조회하면
     * Then 경로 조회에 실패한다
     */
    @DisplayName("존재하지 않는 역을 출발지/도착지로 지정한 경로 조회 요청")
    @Test
    void 존재하지_않는_역_경로_조회() {
        // Given
        // 지하철역 생성 후 노선에 등록하지 않은 상태
        StationResponse 고속터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);

        // When
        ExtractableResponse<Response> response = 경로_조회_요청(고속터미널역.getId(), 강남역.getId());

        // Then
        경로_조회_실패함(response);
    }
}
