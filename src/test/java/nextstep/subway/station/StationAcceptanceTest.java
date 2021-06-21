package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.station.step.StationAcceptanceStep.삭제한_지하철_역이_반영된_역_목록이_조회됨;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_목록_응답됨;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_목록_조회_요청;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_목록_포함됨;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_삭제됨;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_생성_요청;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_생성됨;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_제거_요청;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    /**
     * Scenario: 지하철역을_관리
     * When 지하철역_생성_요청
     * Then 지하철역_생성됨
     * When 지하철역_목록_조회_요청
     * Then 지하철역_목록_포함됨
     * When 지하철역_제거_요청
     * Then 지하철역_삭제됨
     * When 지하철역_목록_조회_요청
     * Then 삭제한 지하철 역이 반영된 역 목록이 조회됨
     **/
    @Test
    void 지하철역을_관리() {
        ExtractableResponse<Response> 지하철역_생성_결과1 = 지하철역_생성_요청(강남역);
        ExtractableResponse<Response> 지하철역_생성_결과2 = 지하철역_생성_요청(역삼역);
        지하철역_생성됨(지하철역_생성_결과1);
        지하철역_생성됨(지하철역_생성_결과2);

        ExtractableResponse<Response> 지하철역_목록_조회_결과 = 지하철역_목록_조회_요청();
        지하철역_목록_응답됨(지하철역_목록_조회_결과);
        지하철역_목록_포함됨(지하철역_목록_조회_결과, Arrays.asList(지하철역_생성_결과1, 지하철역_생성_결과2));

        ExtractableResponse<Response> 지하철역_삭제_결과 = 지하철역_제거_요청(지하철역_생성_결과1);
        지하철역_삭제됨(지하철역_삭제_결과);

        ExtractableResponse<Response> 지하철역_목록_조회_결과2 = 지하철역_목록_조회_요청();
        지하철역_목록_응답됨(지하철역_목록_조회_결과2);
        삭제한_지하철_역이_반영된_역_목록이_조회됨(지하철역_목록_조회_결과, Arrays.asList(지하철역_생성_결과1.as(StationResponse.class)));
    }
}
