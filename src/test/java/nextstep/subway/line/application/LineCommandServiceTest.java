package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineCommandServiceTest {
    private LineCommandService lineCommandService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private LineQueryService lineQueryService;

    @Mock
    private StationService stationService;

    private LineRequest lineRequest1;
    private Line 일호선;
    private Station 용산역;
    private Station 남영역;
    private Station 서울역;
    private Station 시청역;

    @BeforeEach
    void setUp() {
        lineCommandService = new LineCommandService(lineQueryService, stationService, lineRepository);
        lineRequest1 = new LineRequest("1호선", "blue", 1L, 2L, 10);
        용산역 = new Station("용산역");
        ReflectionTestUtils.setField(용산역, "id", 1L);
        남영역 = new Station("남영역");
        ReflectionTestUtils.setField(남영역, "id", 2L);
        서울역 = new Station("서울역");
        ReflectionTestUtils.setField(서울역, "id", 3L);
        시청역 = new Station("시청역");
        ReflectionTestUtils.setField(시청역, "id", 4L);
        일호선 = new Line("1호선", "blue");
        ReflectionTestUtils.setField(일호선, "id", 1L);
        ReflectionTestUtils.setField(일호선, "sections", new Sections(new ArrayList<>(Arrays.asList(new Section(일호선, 용산역, 남영역, 10)))));
    }

    @Test
    void saveLine() {
        //given
        when(stationService.findById(용산역.getId())).thenReturn(용산역);
        when(stationService.findById(남영역.getId())).thenReturn(남영역);
        when(lineRepository.save(any())).thenReturn(일호선);

        //when
        LineResponse actual = lineCommandService.saveLine(lineRequest1);

        //then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getName()).isEqualTo("1호선");
            assertThat(actual.getColor()).isEqualTo("blue");
            assertThat(actual.getStations()).containsAll(Arrays.asList(StationResponse.of(용산역), StationResponse.of(남영역)));
        });
    }

    @Test
    void addLineStation() {
        //given
        when(lineQueryService.findLineById(any())).thenReturn(일호선);
        when(stationService.findById(서울역.getId())).thenReturn(서울역);
        when(stationService.findById(남영역.getId())).thenReturn(남영역);

        //when
        lineCommandService.addLineStation(일호선.getId(), new SectionRequest(서울역.getId(), 남영역.getId(), 1));

        //then
        assertThat(일호선.getStations()).containsAll(Arrays.asList(용산역, 서울역, 남영역));
    }

    @Test
    void removeLineStation() {
        //given
        when(lineQueryService.findLineById(any())).thenReturn(일호선);
        when(stationService.findById(서울역.getId())).thenReturn(서울역);
        when(stationService.findById(남영역.getId())).thenReturn(남영역);
        lineCommandService.addLineStation(일호선.getId(), new SectionRequest(서울역.getId(), 남영역.getId(), 1));

        //when
        lineCommandService.removeLineStation(일호선.getId(), 서울역.getId());

        //then
        assertAll(() -> {
            assertThat(일호선.getStations().size()).isEqualTo(2);
            assertThat(일호선.getStations()).containsAll(Arrays.asList(용산역, 남영역));
        });
    }
}
