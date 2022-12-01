package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.JpaEntityTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PathTest extends JpaEntityTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;
    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10);
        이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));
        flushAndClear();
    }

    @DisplayName("최단경로 찾기 테스트")
    @Test
    void pathFinderTest() {
        // when
        Path path = new PathFinder().find(new Sections(sectionRepository.findAll()), 교대역, 양재역);

        // then
        assertThat(path.getStationPaths()).containsExactlyElementsOf(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(path.getDistance()).isEqualTo(5);
    }
}
