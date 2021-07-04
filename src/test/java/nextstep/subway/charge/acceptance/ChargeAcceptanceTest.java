package nextstep.subway.charge.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.acceptance.PathAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 조회 기능을 테스트")
public class ChargeAcceptanceTest extends AcceptanceTest {
    private StationResponse 신림역;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 잠실역;
    private StationResponse 삼성역;
    private StationResponse 화곡역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        // 지하철역_등록되어_있음
        신림역 = 지하철역_등록("신림역");
        강남역 = 지하철역_등록("강남역");
        교대역 = 지하철역_등록("교대역");
        잠실역 = 지하철역_등록("잠실역");
        삼성역 = 지하철역_등록("삼성역");
        화곡역 = 지하철역_등록("화곡역");

        // and
        // 지하철_노선_등록되어_있음
        LineRequest lineRequest = new LineRequest("이호선", "bg-red-600", 강남역.getId(), 교대역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // and
        // 노선_지하철역_구간_추가_등록되어_있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회")
    @Test
    void 요금_포함된_지하철_경로_조회() {
        // when
        // 출발역에서_도착역까지_최단_거리_경로_조회_요청
        ExtractableResponse<Response> response = PathAcceptanceTest.지하철_경로_조회(강남역, 삼성역);

        // than
        // 최단_거리_경로_조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 잠실역, 삼성역));

        // and
        // 최단_거리_응답함
        지하철_경로_거리_응답됨(pathResponse, 30);

        // and
        // 지하철_이용_요금도_함께_응답됨 30Km -> 1,250 + (100 * 4) = 1,650
        지하철_요금_응답됨(pathResponse, 1650);
    }

    private void 지하철_요금_응답됨(PathResponse pathResponse, int charge) {
        assertThat(pathResponse.getCharge()).isEqualTo(charge);
    }

    private void 지하철_경로_거리_응답됨(PathResponse pathResponse, int distance) {
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private StationResponse 지하철역_등록(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    private void 지하철_경로_조회됨(PathResponse pathResponse, List<StationResponse> expectedStations) {
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);

    }
}
