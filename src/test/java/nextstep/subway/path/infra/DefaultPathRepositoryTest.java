package nextstep.subway.path.infra;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathRepository;
import nextstep.subway.path.domain.PathSection;
import nextstep.subway.path.domain.PathSections;
import nextstep.subway.path.domain.PathStation;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultPathRepositoryTest {

    private PathRepository pathRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        pathRepository = new DefaultPathRepository(lineRepository, stationRepository);
    }

    @DisplayName("모든 경로 구간들을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        Line 일호선 = new Line("일호선", "blue", new Station("청량리역"), new Station("신창역"), 100);
        Line 이호선 = new Line("이호선", "green", new Station("강남역"), new Station("교대역"), 10);
        Line 삼호선 = new Line("신분당선", "red", new Station("강남역"), new Station("정자역"), 30);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 삼호선));

        // when
        PathSections allSections = pathRepository.findAllSections();

        // then
        List<String> stationNames = allSections.getPathSections()
                .stream()
                .flatMap(pathSection -> pathSection.getStations().stream())
                .map(PathStation::getName)
                .collect(Collectors.toList());
        Integer distanceSum = allSections.getPathSections()
                .stream()
                .map(PathSection::getDistance)
                .reduce(0, Integer::sum);

        assertThat(stationNames).contains("청량리역", "신창역", "강남역", "교대역", "정자역");
        assertThat(distanceSum).isEqualTo(140);
    }

    @DisplayName("경로 지하철역 id로 경로 지하철역을 조회할 수 있다.")
    @Test
    void findById() {
        // given
        String 강남역 = "강남역";
        when(stationRepository.findById(any())).thenReturn(Optional.of(new Station(강남역)));

        // when
        PathStation actual = pathRepository.findById(1L).get();

        // then
        assertThat(actual.getName()).isEqualTo(강남역);
    }
}
