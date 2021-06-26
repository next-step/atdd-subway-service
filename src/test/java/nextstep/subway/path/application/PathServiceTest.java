package nextstep.subway.path.application;

import static nextstep.subway.station.domain.StationFixtures.광화문역;
import static nextstep.subway.station.domain.StationFixtures.서대문역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("지하철 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private PathService pathService;

    @DisplayName("지하철 경로 조회")
    @Test
    void findPath() {

        // Given
        Station 아현역 = new Station(4L, "아현역");
        Station 충정로역 = new Station(5L, "충정로역");
        Station 시청역 = new Station(6L, "시청역");
        Station 애오개역 = new Station(1L, "애오개역");
        Station 서대문역 = new Station(2L, "서대문역");
        Station 광화문역 = new Station(3L, "광화문역");

        Line 이호선 = new Line(2L, "이호선", "green");
        이호선.addSection(new Section(아현역, 충정로역, 10));
        이호선.addSection(new Section(충정로역, 시청역, 10));
        Line 오호선 = new Line(5L, "오호선", "purple");
        오호선.addSection(new Section(애오개역, 충정로역, 10));
        오호선.addSection(new Section(충정로역, 서대문역, 10));
        오호선.addSection(new Section(서대문역, 광화문역, 10));
        List<Line> allLines = new ArrayList<>(Arrays.asList(이호선, 오호선));

        when(lineRepository.findAll()).thenReturn(allLines);

        Map<Long, Station> stationMap = new HashMap<>();
        stationMap.put(서대문역.getId(), 서대문역);
        stationMap.put(시청역.getId(), 시청역);
        given(stationService.findMapByIds(서대문역.getId(), 시청역.getId())).willReturn(stationMap);

        // When
        PathResponse pathResponse = pathService.findPath(서대문역.getId(), 시청역.getId());

        // Then
        지하철역_순서_정렬됨(pathResponse.getStations(), 서대문역, 충정로역, 시청역);
    }

    public static void 지하철역_순서_정렬됨(List<StationResponse> stationResponses, Station... expectedStations) {
        List<Long> stationIds = stationResponses.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = Arrays.stream(expectedStations)
            .map(Station::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

}
