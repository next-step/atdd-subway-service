package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PathGraphTest {

    Line 이호선;
    Line 삼호선;
    Line 신분당선;


    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;


    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        교대역 = stationRepository.save(new Station("교대역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        신분당선 = lineRepository.save(new Line("신분당선", "bg-red-600", 강남역, 양재역, 10));
        이호선 = lineRepository.save(new Line("이호선", "bg-red-600", 교대역, 강남역, 10));
        삼호선 = lineRepository.save(new Line("삼호선", "bg-red-600", 교대역, 양재역, 5));
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
    }

    @Test
    @DisplayName("최단_구간의_경로를_구한다")
    void shortestStations() {
        //given
        List<Section> sections = sectionRepository.findAll();
        ShortestPathFinder shortestPathFinder = new JgraphShortestPathFinder(sections);
        PathGraph pathGraph = new PathGraph(shortestPathFinder);

        //when
        List<Station> shortestPath = pathGraph.findShortestPath(교대역.getId(), 양재역.getId());

        //then
        assertThat(shortestPath).containsExactly(교대역, 남부터미널역, 양재역);
    }

}
