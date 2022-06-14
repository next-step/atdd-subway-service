package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 일호선;
    private LineResponse 구호선;
    private LineResponse 오호선;
    private StationResponse 노량진역;
    private StationResponse 대방역;
    private StationResponse 신길역;
    private StationResponse 여의도역;
    private StationResponse 샛강역;
    private StationResponse 여의나루역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        노량진역 = StationAcceptanceTest.지하철역_등록되어_있음("노량진역").as(StationResponse.class);
        대방역 = StationAcceptanceTest.지하철역_등록되어_있음("대방역").as(StationResponse.class);
        신길역 = StationAcceptanceTest.지하철역_등록되어_있음("신길역").as(StationResponse.class);
        여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        샛강역 = StationAcceptanceTest.지하철역_등록되어_있음("샛강역").as(StationResponse.class);
        여의나루역 = StationAcceptanceTest.지하철역_등록되어_있음("여의나루역").as(StationResponse.class);

        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-blue-600", 노량진역, 신길역, 10);
        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-yellow-600", 노량진역, 샛강역, 5);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-purple-600", 신길역, 여의나루역, 12);

        지하철_노선에_지하철역_등록_요청(일호선, 노량진역, 대방역, 7);
        지하철_노선에_지하철역_등록_요청(구호선, 샛강역, 여의도역, 8);
        지하철_노선에_지하철역_등록_요청(오호선, 신길역, 여의도역, 10);
    }
}
