package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {
    @DisplayName("PathFinder 생성")
    @Test
    void create() {
        // when
        PathFinder pathFinder = new PathFinder(createLines());

        // then
        assertThat(pathFinder).isNotNull();
    }

    private List<Line> createLines() {
        Station 종합운동장 = new Station("종합운동장");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실 = new Station("잠실");
        Station 석촌 = new Station("석촌");

        Line 이호선 = new Line("2호선", "green", 종합운동장, 잠실새내, 10);
        이호선.addSection(new Section(잠실새내, 잠실, 10));

        Line 팔호선 = new Line("8호선", "pink", 잠실, 석촌, 10);

        return Arrays.asList(이호선, 팔호선);
    }
}
