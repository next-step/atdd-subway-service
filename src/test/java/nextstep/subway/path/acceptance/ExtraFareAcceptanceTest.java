package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTestFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestFixture.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.ExtraFareAcceptanceTestFixture.지하철_최단경로_요금_확인됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestFixture.지하철_최단경로_조회_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTestFixture.지하철_최단경로_조회_요청_응답됨;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExtraFareAcceptanceTest extends AcceptanceTest {

    private LineResponse 이호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 영등포구청역;
    private StationResponse 신당역;
    private StationResponse 마곡역;
    private StationResponse 종로3가역;
    private int 이호선_추가요금;
    private int 오호선_추가요금;

    /**
     * 2호선 추가요금 0원 5호선 추가요금 900원
     * <p>
     * *2호선* 강남역 --- 영등포구청역 --- 신당 | | *5호선* 마곡역 --- 영등포구청역 --- 종로3가역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        이호선_추가요금 = 0;
        오호선_추가요금 = 900;

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        영등포구청역 = 지하철역_등록되어_있음("영등포구청역").as(StationResponse.class);
        신당역 = 지하철역_등록되어_있음("신당역").as(StationResponse.class);
        마곡역 = 지하철역_등록되어_있음("마곡역").as(StationResponse.class);
        종로3가역 = 지하철역_등록되어_있음("종로3가역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(
            new LineRequest("이호선", "bg-red-600", 강남역.getId(), 영등포구청역.getId(), 8, 이호선_추가요금)).as(
            LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 영등포구청역, 신당역, 49);
        오호선 = 지하철_노선_등록되어_있음(
            new LineRequest("오호선", "bg-red-600", 마곡역.getId(), 영등포구청역.getId(), 9, 오호선_추가요금)).as(
            LineResponse.class);
        지하철_노선에_지하철역_등록_요청(오호선, 영등포구청역, 종로3가역, 70);
    }

    @DisplayName("거리가 0KM ~ 10KM 사이인 경우 기본 요금을 조회한다.")
    @Test
    void basicExtraFare() {
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(강남역, 영등포구청역);

        지하철_최단경로_조회_요청_응답됨(response);
        지하철_최단경로_요금_확인됨(response, 이호선_추가요금 + ExtraFare.BASIC);
    }

    @DisplayName("노선의 추가요금을 더한 값을 계산한다.")
    @Test
    void SurchargeAddBasicExtraFare() {
        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(마곡역, 영등포구청역);

        지하철_최단경로_조회_요청_응답됨(response);
        지하철_최단경로_요금_확인됨(response, 오호선_추가요금 + ExtraFare.BASIC);
    }

    @DisplayName("거리가 11KM ~ 50KM 사이인 경우 5KM당 100원의 추가 요금이 발생한다.")
    @Test
    void middleExtraFare() {
        int 추가요금 = 800;

        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(영등포구청역, 신당역);

        지하철_최단경로_조회_요청_응답됨(response);
        지하철_최단경로_요금_확인됨(response, 이호선_추가요금 + ExtraFare.BASIC + 추가요금);
    }

    @DisplayName("거리가 51KM ~ 178KM 사이인 경우 8KM당 100원의 추가 요금이 발생한다.")
    @Test
    void longExtraFare() {
        int 추가요금 = 1100;

        ExtractableResponse<Response> response = 지하철_최단경로_조회_요청(영등포구청역, 종로3가역);

        지하철_최단경로_조회_요청_응답됨(response);
        지하철_최단경로_요금_확인됨(response, 오호선_추가요금 + ExtraFare.BASIC + 추가요금);
    }
}

