package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("노량진역 -> 여의나루역 최단거리는 15이며 경로는 노량진 - 샛강 - 여의도 - 여의나루다.")
    @Test
    void shortestPathNoryangjinToYeouinaru() {

        //when
        final PathResponse 최단_경로 = 최단_경로_요청(노량진역.getId(), 여의나루역.getId()).as(PathResponse.class);
        final List<StationResponse> 역_이동_목록 = 최단_경로.getStations();
        final int 이동_거리 = 최단_경로.getDistance();

        //then
        역_이동_목록_개수_확인(역_이동_목록, 4);
        N번째_역_이름_확인(역_이동_목록, 0, 노량진역);
        N번째_역_이름_확인(역_이동_목록, 1, 샛강역);
        N번째_역_이름_확인(역_이동_목록, 2, 여의도역);
        N번째_역_이름_확인(역_이동_목록, 3, 여의나루역);
        이동_거리_확인(이동_거리, 15);

    }

    public static ExtractableResponse<Response> 최단_경로_요청(final Long source, final Long target) {
        return RestAssured
                .given().log().all()
                .param("source", source)
                .param("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 역_이동_목록_개수_확인(final List<StationResponse> stations, final int expected) {
        assertThat(stations.size()).isEqualTo(expected);
    }

    private void N번째_역_이름_확인(final List<StationResponse> stations, final int n, final StationResponse station) {
        assertThat(stations.get(n).getName()).isEqualTo(station.getName());
    }

    private void 이동_거리_확인(final int distance, final int expected) {
        assertThat(distance).isEqualTo(expected);
    }
}
