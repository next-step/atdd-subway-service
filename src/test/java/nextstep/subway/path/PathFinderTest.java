package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PathFinderTest {
    @Autowired
    LineRepository lineRepository;
    @Autowired
    StationRepository stationRepository;
    private final Line 신분당선 = Line.of("신분당선", "red");
    private final Line 수인분당선 = Line.of("수인분당선", "yellow");
    private final Station 강남역 = new Station("강남역");
    private final Station 판교역 = new Station("판교역");
    private final Station 광교역 = new Station("광교역");
    private final Station 선릉역 = new Station("선릉역");
    private final Station 수원역 = new Station("수원역");
    private final Distance TEN = Distance.from(10);
    private final Distance FIVE = Distance.from(5);
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        stationRepository.save(강남역);
        stationRepository.save(판교역);
        stationRepository.save(광교역);
        stationRepository.save(선릉역);
        stationRepository.save(수원역);
        신분당선.addSection(강남역, 광교역, FIVE);
        수인분당선.addSection(선릉역, 수원역, TEN);
        pathFinder = new PathFinder(Arrays.asList(신분당선, 수인분당선));
    }

    @Test
    void 최단_경로_조회() {
        Path path = pathFinder.findPath(강남역.getId(), 광교역.getId());
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @Test
    void 출발역과_도착역이_같은_경우() {
        assertThatThrownBy(() -> pathFinder.findPath(강남역.getId(), 강남역.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선에_없는_역_조회() {
        assertThatThrownBy(() -> pathFinder.findPath(강남역.getId(), 판교역.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출발역과_도착역이_연결되어_있지_않은_경우() {
        assertThatThrownBy(() -> pathFinder.findPath(강남역.getId(), 수원역.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}
