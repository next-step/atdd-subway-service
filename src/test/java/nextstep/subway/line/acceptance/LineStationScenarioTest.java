package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineStationScenarioTest extends AcceptanceTest {

    private StationResponse 강남구청역;
    private StationResponse 청담역;
    private StationResponse 뚝섬유원지역;
    private StationResponse 건대입구역;
    private StationResponse 어린이대공원역;

    private LineRequest lineRequest;
    private LineResponse 칠호선;

    /**
     * Given 지하철역 등록되어 있음
     *      And 지하철 노선 등록되어 있음
     *      And 지하철 노선에 지하철역 등록되어 있음
     */
    @BeforeEach
    void init() {
        강남구청역 = StationAcceptanceTest.지하철역_등록되어_있음("강남구청역").as(StationResponse.class);
        청담역 = StationAcceptanceTest.지하철역_등록되어_있음("청담역").as(StationResponse.class);
        뚝섬유원지역 = StationAcceptanceTest.지하철역_등록되어_있음("뚝섬유원지역").as(StationResponse.class);
        건대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("건대입구역").as(StationResponse.class);
        어린이대공원역 = StationAcceptanceTest.지하철역_등록되어_있음("어린이대공원역").as(StationResponse.class);

        lineRequest = new LineRequest("칠호선", "yellow", 청담역.getId(), 뚝섬유원지역.getId(), 15);
        칠호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    /**
     *  Scenario: 지하철 구간을 관리
     *      When 지하철 구간 등록 요청
     *      Then 지하철 구간 등록됨
     *      When 지하철 노선에 등록된 역 목록 조회 요청
     *      Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
     *      When 지하철 구간 삭제 요청
     *      Then 지하철 구간 삭제됨
     *      When 지하철 노선에 등록된 역 목록 조회 요청
     *      Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @Test
    @DisplayName("지하철 구간 관련 기능 시나리오 테스트")
    void 지하철_구간_관련_기능_시나리오_테스트 () {
        //When 지하철 구간 등록 요청
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(칠호선, 뚝섬유원지역, 건대입구역, 10);

        //Then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(response);

        //When 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> response_line = 지하철_노선_조회_요청(칠호선);
        LineResponse 칠호선_response = response_line.as(LineResponse.class);

        //Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        assertThat(칠호선_response.getStations()
                                .stream()
                                .map(StationResponse::getId))
                                .containsExactly(청담역.getId(), 뚝섬유원지역.getId(), 건대입구역.getId());

        지하철_노선_목록_응답됨(response);

        //When 지하철 구간 삭제 요청
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(칠호선, 청담역);

        //Then 지하철 구간 삭제됨
        지하철_노선에_지하철역_제외됨(removeResponse);

        //When 지하철 노선에 등록된 역 목록 조회 요청
        response_line = 지하철_노선_조회_요청(칠호선);
        칠호선_response = response_line.as(LineResponse.class);

        //Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        assertThat(칠호선_response.getStations()
                .stream()
                .map(StationResponse::getId))
                .containsExactly(뚝섬유원지역.getId(), 건대입구역.getId());
    }
}
