package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청_및_확인;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest
                .지하철_노선_생성_요청_및_검증(new LineRequest("신분당선", "빨간색", 강남역.getId(), 양재역.getId(), 3))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest
                .지하철_노선_생성_요청_및_검증(new LineRequest("2호선", "초록색", 강남역.getId(), 정자역.getId(), 7))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest
                .지하철_노선_생성_요청_및_검증(new LineRequest("3호선", "주황색", 강남역.getId(), 광교역.getId(), 2))
                .as(LineResponse.class);
    }

    @TestFactory
    @DisplayName("최단거리를 검색한다")
    Stream<DynamicTest> 최단거리를_검색한다() {
        return Stream.of(
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("신분당선에 정자역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(신분당선, 강남역, 양재역, 정자역)),
                dynamicTest("2호선에 양재역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(이호선, 양재역, 정자역, 5)),
                dynamicTest("2호선에 양재역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(이호선, 강남역, 양재역, 정자역)),
                dynamicTest("3호선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(삼호선, 광교역, 정자역, 1)),
                dynamicTest("3호선에 정자역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(삼호선, 강남역, 광교역, 정자역)),
                dynamicTest("강남역과 정자역 최단거리를 확인한다", 지하철_최단거리_요청_및_확인(강남역, 정자역, Arrays.asList(강남역, 광교역), 3))
        );
    }

    private Executable 지하철_최단거리_요청_및_확인(StationResponse source, StationResponse target, List<StationResponse> exceptStations, int exceptDistance) {
        return () -> {
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .param("source", source.getId())
                    .param("target", target.getId())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when().log().all()
                    .get("/paths")
                    .then().log().all()
                    .extract();

            지하철_최단거리_응답됨(response);

            지하철_최단거리_검증(response, exceptStations, exceptDistance);
        };
    }

    private void 지하철_최단거리_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LinePathResponse.class)).isNotNull();
    }

    private void 지하철_최단거리_검증(ExtractableResponse<Response> response, List<StationResponse> exceptStations, int exceptDistance) {
        LinePathResponse pathResponse = response.as(LinePathResponse.class);

        List<Long> exceptStationIds = exceptStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(pathResponse.getStations())
                .map(StationResponse::getId)
                .containsExactlyInAnyOrderElementsOf(exceptStationIds);

        assertThat(pathResponse.getDistance())
                .isEqualTo(exceptDistance);
    }
}
