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
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("경로 관련 도메인 테스트")
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

    // 미금 - 정자 구간 확인용도
    private Station 오리역;
    private Station 수내역;
    private Station 동천역;
    private Station 미금역;
    private Station 정자역;
    private Station 판교역;

    private Line 신분당선;
    private Line 분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 비이상 지하철역 생성
        사당역 = new Station("사당역");
        stationRepository.save(사당역);

        // 신분당선
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

    /**
     * Given : 동일한 상하행을 가진 구간이 각각 다른 노선에 포함되어 있는 경우
     * When : 최단경로 조회 시
     * Then : 어떤 노선이 최단경로에 포함되는 지 확인
     */
    @DisplayName("미금 - 정자 구간 테스트(동일구간 다른 노선의 경우)")
    @Test
    void sameSectionPathTest() {
        // 새로운 노선 / 구간이 필요함
        super.setUp();

        // given
        미금_정자_구간_생성();

        // when
        Path path = new PathFinder().find(new Sections(sectionRepository.findAll()), 오리역, 수내역);

        // then : 분당선만 타야함
        Set<Line> lines = path.getSectionEdges().stream()
                .map(it -> it.getLine())
                .collect(Collectors.toSet());
        assertThat(lines).containsExactly(분당선);
    }

    private void 미금_정자_구간_생성() {
        // 신분당선
        동천역 = new Station("동천역");
        미금역 = new Station("미금역");
        정자역 = new Station("정자역");
        판교역 = new Station("판교역");

        // 분당선
        오리역 = new Station("오리역");
        수내역 = new Station("수내역");

        신분당선 = new Line("신분당선", "빨간색", 미금역, 정자역, 10, 900);
        분당선 = new Line("분당선", "노란색", 미금역, 정자역, 9, 500);

        신분당선.addSection(동천역, 미금역, 2);
        신분당선.addSection(정자역, 판교역, 2);

        분당선.addSection(오리역, 미금역, 2);
        분당선.addSection(정자역, 수내역, 2);
        lineRepository.saveAll(Lists.newArrayList(신분당선, 분당선));
        flushAndClear();
    }
}
