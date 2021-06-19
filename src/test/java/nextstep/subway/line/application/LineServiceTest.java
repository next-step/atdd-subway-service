package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private Line line1;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);
        lineRequest1 = new LineRequest("1호선", "blue", 1L, 2L, 10);
        lineRequest2 = new LineRequest("2호선", "green", 1L, 2L, 20);
        upStation = new Station("용산역");
        ReflectionTestUtils.setField(upStation, "id", 1L);
        downStation = new Station("서울역");
        ReflectionTestUtils.setField(downStation, "id", 2L);
        line1 = new Line("1호선", "blue");
        ReflectionTestUtils.setField(line1, "id", 1L);
        ReflectionTestUtils.setField(line1, "sections2", new Sections(Arrays.asList(new Section(line1, upStation, downStation, 1))));
    }

    @Test
    void saveLine() {
        //given
        when(stationService.findById(upStation.getId())).thenReturn(upStation);
        when(stationService.findById(downStation.getId())).thenReturn(downStation);
        when(lineRepository.save(any())).thenReturn(line1);

        //when
        LineResponse actual = lineService.saveLine(lineRequest1);

        //then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getName()).isEqualTo("1호선");
            assertThat(actual.getColor()).isEqualTo("blue");
            assertThat(actual.getStations()).containsAll(Arrays.asList(StationResponse.of(upStation), StationResponse.of(upStation)));
        });
    }

    @Test
    void findLines() {
    }

    @Test
    void findLineById() {
    }

    @Test
    void findLineResponseById() {
    }

    @Test
    void updateLine() {
    }

    @Test
    void deleteLineById() {
    }

    @Test
    void addLineStation() {
    }

    @Test
    void removeLineStation() {
    }
}