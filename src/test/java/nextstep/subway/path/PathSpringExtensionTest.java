package nextstep.subway.path;

import nextstep.subway.auth.domain.Stranger;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathEdge;
import nextstep.subway.path.infrastructure.JGraphPathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * packageName : nextstep.subway.path
 * fileName : PathSpringExtensionTest
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class PathSpringExtensionTest {
    @MockBean
    private JGraphPathFinder pathFinder;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private LineRepository lineRepository;

    @MockBean
    private Stranger 비로그인;

    @MockBean(name = "강남역")
    private Station 강남역;

    @MockBean(name = "역삼역")
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        when(강남역.getName()).thenReturn("강남역");
        when(강남역.getId()).thenReturn(1L);
        when(역삼역.getName()).thenReturn("역삼역");
        when(역삼역.getId()).thenReturn(2L);
        when(강남역.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(강남역.getModifiedDate()).thenReturn(LocalDateTime.now());
        when(역삼역.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(역삼역.getModifiedDate()).thenReturn(LocalDateTime.now());
    }

    @Test
    @DisplayName("경로 조회")
    public void findPath() {
        // given
        final Line line1 = Line.of("1호선", "남색", 강남역, 역삼역, 5);
        List<Line> lines = Lists.newArrayList(line1);
        List<Station> stations = Lists.newArrayList(강남역, 역삼역);
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAll()).thenReturn(stations);
        when(pathFinder.getShortestPath(anyList(), anyList(), anyLong(), anyLong()))
                .thenReturn(Path.of(Lists.newArrayList(PathEdge.of(line1.sections().getList().get(0))), stations, 강남역, 역삼역, Distance.of(5)));
        when(비로그인.isStranger()).thenReturn(true);
        SubwayFare fare = new SeoulMetroFare();
        PathService pathService = new PathService(pathFinder, fare, stationRepository, lineRepository);

        //when
        Path path = pathService.getShortestPath(강남역.getId(), 역삼역.getId());

        //then
        assertThat(path.stations()).hasSize(2);
        assertThat(path.distance()).isEqualTo(Distance.of(5));
        assertThat(fare.rateInquiry(path, 비로그인)).isEqualTo(Money.of(SeoulMetroType.BASE_RATE));
    }
}
