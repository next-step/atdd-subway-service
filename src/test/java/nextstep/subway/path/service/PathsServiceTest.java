package nextstep.subway.path.service;

import nextstep.subway.component.PathFinder;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathsServiceTest {

    @Mock
    LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private PathFinder pathFinder;

    @Test
    void findPath() {
        // PathResponse 객첵를 리턴
        // given
        PathsService patchService = new PathsService(lineRepository, stationRepository, pathFinder);
        Station 강남역 = new Station(1L, "강남역");
        Station 광교역 = new Station(2L, "광교역");
        Station 교대역 = new Station(3L, "교대역");
        Long source = 광교역.getId();
        Long target = 교대역.getId();

        Line 신분당선 = new Line(1L, "신분당선", 강남역, 광교역, 10);
        Line 삼호선 = new Line(2L, "삼호선", 교대역, 강남역, 5);
        List<Line> lines = Arrays.asList(신분당선, 삼호선);
        // 전체 지하철 노선을 조회
        when(lineRepository.findAll()).thenReturn(lines);
        // source 기준 지하철역 조회
        Optional<Station> sourceStation = Optional.of(광교역);
        when(stationRepository.findById(source)).thenReturn(sourceStation);

        // target 기준으로 지하철역 조회
        Optional<Station> targetStation = Optional.of(교대역);
        when(stationRepository.findById(target)).thenReturn(targetStation);

        // 노선, source, target 기준으로 경로 조회
        SubwayPath subwayPath = new SubwayPath(Arrays.asList(광교역, 강남역, 교대역), 15);
        when(pathFinder.shortestPath(lines, sourceStation, targetStation)).thenReturn(subwayPath);

        // when
        PathResponse pathResponse = patchService.findPath(source, target);

        // then
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(15);
        assertThat(pathResponse.getStations().size()).isEqualTo(3);
        List<Long> stationNames = pathResponse.getStations().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(stationNames).containsExactly(광교역.getId(), 강남역.getId(), 교대역.getId());
    }
}
