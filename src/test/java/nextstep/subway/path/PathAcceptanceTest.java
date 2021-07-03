package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.PageController.URIMapping.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    public static final RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate(PATH);

    private long 강남역ID, 양재역ID, 교대역ID, 남부터미널역ID, 없는역ID;

    @BeforeEach
    public void register() {
        //역 추가
        강남역ID = StationAcceptanceTest.requestCreateStation("강남역").as(StationResponse.class).getId();
        양재역ID = StationAcceptanceTest.requestCreateStation("양재역").as(StationResponse.class).getId();
        교대역ID = StationAcceptanceTest.requestCreateStation("교대역").as(StationResponse.class).getId();
        남부터미널역ID = StationAcceptanceTest.requestCreateStation("남부터미널역").as(StationResponse.class).getId();

        없는역ID = StationAcceptanceTest.requestCreateStation("없는역").as(StationResponse.class).getId();

        //노선 추가
        long 신분당선ID = createLine("신분당선", 강남역ID, 양재역ID, 10).as(LineResponse.class).getId();
        long 이호선ID = createLine("이호선", 교대역ID, 강남역ID, 10).as(LineResponse.class).getId();
        long 삼호선ID = createLine("삼호선", 교대역ID, 양재역ID, 5).as(LineResponse.class).getId();

        //경로 추가
        appendSection(삼호선ID, 교대역ID, 남부터미널역ID, 3);
    }

    @Test
    @DisplayName("지하철 최단경로를 조회한다.")
    public void requestPath() {
        // when
        ExtractableResponse<Response> response = requestPath(getDefaultParam(강남역ID, 양재역ID));

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(PathResponse.class).getStationList()).isNotNull(),
            () -> assertThat(response.as(PathResponse.class).getDistance()).isNotNull()
        );
    }

    @Test
    @DisplayName("지하철 최단경로를 조회시 잘못된 출발역과 도착역일 경우 예외가 발생한다.")
    public void requestPathException() {
        assertAll(
            () -> assertThat(requestPath(getDefaultParam(강남역ID, 강남역ID)).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(requestPath(getDefaultParam(강남역ID, 없는역ID)).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    private Map<String, Long> getDefaultParam(long source, long target) {
        return new HashMap<String, Long>() {
            {
                put("source",source);
                put("target", target);
            }
        };
    }

    private ExtractableResponse<Response> appendSection(final long lineId, final long upStationId, final long downStationId, final int distance) {
        RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate(String.format("%s/%s%s", LINE, lineId, SECTION));

        SectionRequest sectionRequest = SectionRequest.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();

        return restAssuredTemplate.post(sectionRequest);
    }

    private ExtractableResponse<Response> createLine(final String name, final long upStationId, final long downStationId, final int distance) {
        LineRequest lineRequest = LineRequest.builder()
                .name(name).upStationId(upStationId).downStationId(downStationId).distance(distance)
                .build();

        return LineAcceptanceTest.requestCreatedLine(lineRequest);
    }


    /**
     * @see nextstep.subway.path.ui.PathController#findLineById
     */
    public static ExtractableResponse<Response> requestPath(Map<String, Long> query) {
        return restAssuredTemplate.get(query);
    }
}
