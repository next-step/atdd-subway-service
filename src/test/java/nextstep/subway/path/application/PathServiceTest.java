package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.collection.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private Station 문래역;
    private Station 신도림역;
    private Station 영등포구청역;
    private Station 합정역;
    private Station 홍대역;
    private Line 이호선;

    private PathService pathService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        pathService = new PathService(sectionRepository, stationRepository);

        // given
        // 지하철역_등록_되어_있음
        신도림역 = new Station(1L, "신도림역");
        문래역 = new Station(2L, "문래역");
        영등포구청역 = new Station(3L, "영등포구청역");
        합정역 = new Station(4L, "합정역");
        홍대역 = new Station(5L, "홍대역");

        when(sectionRepository.findAll()).thenReturn(
                Arrays.asList(new Section(신도림역, 문래역, new Distance(10)),
                        new Section(문래역, 영등포구청역, new Distance(5)),
                        new Section(문래역, 합정역, new Distance(5)),
                        new Section(영등포구청역, 홍대역, new Distance(10)),
                        new Section(합정역, 홍대역, new Distance(11))
        ));
    }

    @Test
    @DisplayName("지하철 경로 조회")
    void 지하철_경로_조회() {
        // when
        // 신도림에서 홍대역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(홍대역));

        // then
        Path path = pathService.findOptimalPath(1L, 5L);
        List<Station> stations = path.getStations();
        List<Station> expectedStations = Arrays.asList(
                신도림역, 문래역, 영등포구청역, 홍대역
        );

        List<String> response = stations.stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        List<String> expected = expectedStations.stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertThat(response).containsExactlyElementsOf(expected);
    }
}
