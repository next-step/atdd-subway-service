package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.JpaEntityTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PathTest extends JpaEntityTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;

    private Station 사당역;
    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;

    private Station 신사역;
    private Station 양재역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 비이상 지하철역 생성
        사당역 = new Station("사당역");
        stationRepository.save(사당역);

        // 신분당
        신사역 = new Station("신사역");
        양재역 = new Station("양재역");

        // 2호선
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");

        // 3호선
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red lighten-1", 신사역, 강남역, 10);
        이호선 = new Line("2호선", "green lighten-1", 교대역, 역삼역, 10);
        삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 7);

        신분당선.addSection(강남역, 양재역, 5);
        이호선.addSection(교대역, 강남역, 5);
        삼호선.addSection(교대역, 남부터미널역, 4);

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));
        flushAndClear();
    }

    @DisplayName("최단경로 찾기 테스트 - 환승없음")
    @Test
    void pathFinderTest1() {
        // when
        Path path = new PathFinder().find(new Sections(sectionRepository.findAll()), 교대역, 양재역);

        // then
        assertThat(path.getStationPaths()).containsExactlyElementsOf(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(path.getDistance()).isEqualTo(7);
    }

    @DisplayName("최단경로 찾기 테스트 - 환승 1회")
    @Test
    void pathFinderTest2() {
        // when
        Path path = new PathFinder().find(new Sections(sectionRepository.findAll()), 교대역, 신사역);

        // then
        assertThat(path.getStationPaths()).containsExactlyElementsOf(Arrays.asList(교대역, 강남역, 신사역));
        assertThat(path.getDistance()).isEqualTo(15);
    }

    @DisplayName("최단경로 찾기 테스트 - 환승 2회")
    @Test
    void pathFinderTest3() {
        // when
        Path path = new PathFinder().find(new Sections(sectionRepository.findAll()), 남부터미널역, 신사역);

        // then
        assertThat(path.getStationPaths()).containsExactlyElementsOf(Arrays.asList(남부터미널역, 양재역, 강남역, 신사역));
        assertThat(path.getDistance()).isEqualTo(18);
    }

    @DisplayName("최단경로 찾기 테스트 - 환승 2회")
    @Test
    void pathFinderTest4() {
        // when
        Path path = new PathFinder().find(new Sections(sectionRepository.findAll()), 역삼역, 남부터미널역);

        // then
        assertThat(path.getStationPaths()).containsExactlyElementsOf(Arrays.asList(역삼역, 강남역, 양재역, 남부터미널역));
        assertThat(path.getDistance()).isEqualTo(13);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    @Test
    void pathFinderSameStationExceptionTest() {
        // when / then
        assertThatThrownBy(() -> new PathFinder().find(new Sections(sectionRepository.findAll()), 강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우 예외 발생")
    @Test
    void pathFinderUnconnectedExceptionTest() {
        // when / then
        assertThatThrownBy(() -> new PathFinder().find(new Sections(sectionRepository.findAll()), 사당역, 신사역))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
