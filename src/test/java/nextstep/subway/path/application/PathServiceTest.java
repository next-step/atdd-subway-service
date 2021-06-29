package nextstep.subway.path.application;

import static nextstep.subway.path.domain.FarePolicy.ADULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.impl.ShortestPath;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
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
    @Mock
    private PathFinder pathFinder;
    @Mock
    private ShortestPath shortestPath;
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

        Map<Long, Station> findStations = new HashMap<>();
        findStations.put(서대문역.getId(), 서대문역);
        findStations.put(시청역.getId(), 시청역);
        given(stationService.findStationsByIds(any(), any())).willReturn(findStations);
        given(shortestPath.getDistance()).willReturn(20);
        given(shortestPath.getStations()).willReturn(new ArrayList<>(Arrays.asList(서대문역, 충정로역, 시청역)));
        given(pathFinder.findPath(any(), any(), any())).willReturn(shortestPath);

        LoginMember loginMember = new LoginMember(ADULT.getMaxAge());

        // When
        PathResponse pathResponse = pathService.findPath(loginMember, new PathRequest(서대문역.getId(), 시청역.getId()));

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
