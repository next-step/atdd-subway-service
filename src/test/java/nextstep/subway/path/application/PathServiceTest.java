package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.fixture.ServiceLayerSubwayFixture;
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

    private ServiceLayerSubwayFixture 지하철;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionRepository);
        stationService = new StationService(stationRepository);
        pathService = new PathService(sectionService, stationService);

        지하철 = new ServiceLayerSubwayFixture(stationRepository, lineRepository);

        지하철.삼호선.addSection(지하철.교대역, 지하철.남부터미널역, 지하철.교대역_남부터미널역_간_거리);
        lineRepository.save(지하철.삼호선);
    }

    @Test
    void 최단_경로를_구할_수_있다() {
        // when
        final PathResponse path = pathService.findShortestPath(지하철.강남역.getId(), 지하철.남부터미널역.getId());

        // then
        assertThat(path.getDistance()).isEqualTo(지하철.교대역_강남역_간_거리 + 지하철.교대역_남부터미널역_간_거리);
    }

    @Test
    void 출발역과_도착역이_같으면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> pathService.findShortestPath(지하철.강남역.getId(), 지하철.강남역.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 경로가_없으면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> pathService.findShortestPath(지하철.강남역.getId(), 지하철.여의도역.getId()))
                .isInstanceOf(RuntimeException.class);
    }
}
