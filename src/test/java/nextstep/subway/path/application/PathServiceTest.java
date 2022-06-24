package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    Line 일호선;
    Line 신분당선;

    Station 판교역;
    Station 성남역;
    Station 광교역;
    Station 인천역;
    Station 동인천역;
    Station 주안역;

    @BeforeEach
    void setUp() {

        판교역 = new Station("판교역");
        성남역 = new Station("성남역");
        광교역 = new Station("광교역");

        인천역 = new Station("인천역");
        동인천역 = new Station("동인천역");
        주안역 = new Station("주안역");

        신분당선 = new Line("신분당선", "레드", 판교역, 성남역, 5);
        일호선 = new Line("일호선", "블루", 인천역, 동인천역, 5);
        신분당선.addSection(new Section(신분당선, 판교역, 주안역, 2));
        일호선.addSection(new Section(일호선, 동인천역, 주안역, 10));

    }

    @Test
    @DisplayName("최적 경로 찾기")
    void findShortPath() {
        SectionRepository sectionRepository = mock(SectionRepository.class);
        StationRepository stationRepository = mock(StationRepository.class);

        PathService pathService = new PathService(sectionRepository, stationRepository);
        PathRequest pathRequest = new PathRequest(1L, 2L);

        when(sectionRepository.findAll()).thenReturn(stubSections());
        when(stationRepository.getById(pathRequest.getSource())).thenReturn(판교역);
        when(stationRepository.getById(pathRequest.getTarget())).thenReturn(주안역);

        final PathResponse shortPath = pathService.findShortPath(new PathRequest(1L, 2L));

        assertThat(shortPath.getStations()).containsExactly(StationResponse.of(판교역), StationResponse.of(주안역));
    }

    private List<Section> stubSections() {
        List<Section> stubSections = new ArrayList<>();

        final Sections 신분당선구간 = 신분당선.getSections();
        final Sections 일호선구간 = 일호선.getSections();
        stubSections.addAll(신분당선구간.getSectionElements());
        stubSections.addAll(일호선구간.getSectionElements());

        return stubSections;
    }




}
