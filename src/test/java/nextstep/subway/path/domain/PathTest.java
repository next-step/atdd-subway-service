package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class PathTest {

    private Path sut;
    private Station 강남;
    private Station 광교;
    private List<Line> lines;

    @Mock
    PathNavigation pathNavigation;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        광교 = new Station(2L, "광교");
        lines = new ArrayList<>();
        lines.add(new Line("신분당선", "gs-1123", 강남, 광교, 100));
        PathNavigation pn = PathNavigation.by(lines);
        sut = pn.findShortestPath(강남, 광교);
    }

    @Test
    void create() {
        Path.of(new ArrayList<>(), 10);


    }
}