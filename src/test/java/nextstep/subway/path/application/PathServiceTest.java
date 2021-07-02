package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.collect.Lists;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@ExtendWith(SpringExtension.class)
class PathServiceTest {

    @Test
    @DisplayName("경로 찾기 기능")
    void findPath() {
        // given
        StationRepository stationRepository = mock(StationRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);

        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);

        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(양재역));
        when(stationRepository.findAll()).thenReturn(Lists.newArrayList(강남역, 양재역));
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(신분당선));

        // when
        PathService pathService = new PathService(stationRepository, lineRepository);

        // then
        assertThat(pathService.findPath(1L, 2L).getDistance()).isEqualTo(10);
    }
}