package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FakePathServiceTest {
    private final Line 이호선 = Line.ofNameAndColor("2호선", "초록색");
    private final Line 신분당선 = Line.ofNameAndColor("신분당선", "빨간색");
    private final Line 삼호선 = Line.ofNameAndColor("3호선", "주황색");
    private final Station 강남역 = new Station("강남역");
    private final Station 양재역 = new Station("양재역");
    private final Station 교대역 = new Station("교대역");
    private final Station 남부터미널역 = new Station("남부터미널역");
    private final Section 강남역_양재역 = Section.of(신분당선, 강남역, 양재역, 10);
    private final Section 교대역_강남역 = Section.of(이호선, 교대역, 강남역, 10);
    private final Section 교대역_남부터미널역 = Section.of(삼호선, 교대역, 남부터미널역, 3);
    private final Section 남부터미널역_양재역 = Section.of(삼호선, 남부터미널역, 양재역, 5);
    public class FakeSectionRepository implements SectionRepository{
        private final Map<Long, Section> sectionMap = new HashMap<>();

        public FakeSectionRepository() {
            sectionMap.put(1L, 강남역_양재역);
            sectionMap.put(2L, 교대역_강남역);
            sectionMap.put(3L, 교대역_남부터미널역);
            sectionMap.put(4L, 남부터미널역_양재역);
        }

        @Override
        public List<Section> findAll() {
            return sectionMap.values().stream().collect(Collectors.toList());
        }
    }

    public class FakeStationRepository implements StationRepository {
        private final Map<Long, Station> stationMap = new HashMap<>();
        public FakeStationRepository() {
            stationMap.put(1L, 강남역);
            stationMap.put(2L, 남부터미널역);
        }



        @Override
        public Optional<Station> findById(Long id) {
            return Optional.of(stationMap.get(id));
        }

        @Override
        public List<Station> findAll() {
            return null;
        }

        @Override
        public Station save(Station toStation) {
            return null;
        }

        @Override
        public void deleteById(Long id) {

        }
    }

    private PathFinderService pathFinderService;
    private StationService stationService;
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new FakeStationRepository();
        sectionRepository = new FakeSectionRepository();
        stationService = new StationService(stationRepository);
        pathFinderService = new PathFinderService(sectionRepository, stationService);
    }
    @DisplayName("최단거리 경로 조회")
    @Test
    void findShortestPath() {
        // given
        Long 강남역_ID = 1L;
        Long 남부터미널역_ID = 2L;
        // when
        PathResponse 경로_조회_결과 = pathFinderService.getShortestPath(강남역_ID, 남부터미널역_ID, LoginMember.GUEST);

        // then
        assertAll(
                ()->assertThat(경로_조회_결과.getStations().stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("강남역", "교대역", "남부터미널역"),
                ()->assertThat(경로_조회_결과.getDistance()).isEqualTo(13)
        );
    }
}
