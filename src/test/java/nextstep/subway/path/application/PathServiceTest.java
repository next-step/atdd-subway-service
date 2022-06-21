package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PathServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;

    private SectionService sectionService;
    private StationService stationService;
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 여의도역;
    private Station 샛강역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;

    final int 강남역_양재역_간_거리 = 20;
    final int 교대역_강남역_간_거리 = 10;
    final int 교대역_남부터미널역_간_거리 = 10;
    final int 남부터미널역_양재역_간_거리 = 5;
    final int 여의도역_샛강역_간_거리 = 15;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionRepository);
        stationService = new StationService(stationRepository);
        pathService = new PathService(sectionService, stationService);

        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        교대역 = stationRepository.save(new Station("교대역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        여의도역 = stationRepository.save(new Station("여의도역"));
        샛강역 = stationRepository.save(new Station("샛강역"));

        신분당선 = lineRepository.save(new Line("신분당선", "red", 강남역, 양재역, 강남역_양재역_간_거리));
        이호선 = lineRepository.save(new Line("이호선", "green", 교대역, 강남역, 교대역_강남역_간_거리));
        삼호선 = lineRepository.save(new Line("삼호선", "orange", 교대역, 양재역, 교대역_남부터미널역_간_거리 + 남부터미널역_양재역_간_거리));
        구호선 = lineRepository.save(new Line("구호선", "brown", 여의도역, 샛강역, 여의도역_샛강역_간_거리));

        삼호선.addSection(교대역, 남부터미널역, 교대역_남부터미널역_간_거리);
        lineRepository.save(삼호선);
    }

    @Test
    void 최단_경로를_구할_수_있다() {
        // when
        final PathResponse path = pathService.findShortestPath(강남역.getId(), 남부터미널역.getId());

        // then
        assertThat(path.getDistance()).isEqualTo(교대역_강남역_간_거리 + 교대역_남부터미널역_간_거리);
    }

    @Test
    void 출발역과_도착역이_같으면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 강남역.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 경로가_없으면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 여의도역.getId()))
                .isInstanceOf(RuntimeException.class);
    }
}
