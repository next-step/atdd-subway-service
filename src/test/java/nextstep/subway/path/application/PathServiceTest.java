package nextstep.subway.path.application;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    StationService stationService;
    @Mock
    LineService lineService;

    @Test
    void findShortestPath(){
        //given
        PathService pathService = new PathService(stationService, lineService);
        when(lineService.findAllLines()).thenReturn(Lists.newArrayList(new Line()));
        when(stationService.findStationById(1L)).thenReturn(Station.from("출발역"));
        when(stationService.findStationById(2L)).thenReturn(Station.from("도착역"));

        //when
        PathResponse pathResponse = pathService.findShortestPath(1L, 2L);

        //then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(pathResponse).isNotNull(),
                () -> assertThat(pathResponse.getStationResponses()).isNotNull()
        );
    }
}