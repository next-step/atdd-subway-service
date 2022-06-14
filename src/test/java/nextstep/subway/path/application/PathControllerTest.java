package nextstep.subway.path.application;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.behaviors.SubwayBehaviors.로그인정보없이_최단경로_및_요금을_조회한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import nextstep.subway.RestAssuredTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.SubwayFare;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

class PathControllerTest extends RestAssuredTest {

    @MockBean
    PathService pathService;

    private StationResponse 강남역 = new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now());
    private StationResponse 양재역 = new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now());
    private StationResponse 교대역 = new StationResponse(3L, "교대역", LocalDateTime.now(), LocalDateTime.now());
    private LineResponse 삼호선 = new LineResponse();

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 최단경로요청시_정상응답여부_확인() {
        Long startStationId = 강남역.getId();
        Long endStationId = 교대역.getId();

        // Given
        when(pathService.findShortestPath(eq(startStationId), eq(endStationId), any()))
                .thenReturn(new PathResponse(Lists.newArrayList(강남역, 양재역, 교대역), Lists.newArrayList(삼호선), 10,
                        SubwayFare.of(0)));
        // When
        ExtractableResponse<Response> response = 로그인정보없이_최단경로_및_요금을_조회한다(
                String.valueOf(startStationId),
                String.valueOf(endStationId)
        );

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(10);
        List<StationResponse> stations = pathResponse.getStations();
        List<String> stationNames = stations.stream().map((StationResponse::getName)).collect(toList());
        assertThat(stationNames)
                .hasSize(3)
                .containsExactly("강남역", "양재역", "교대역");
    }
}
