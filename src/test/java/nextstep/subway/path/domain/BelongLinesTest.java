package nextstep.subway.path.domain;

import com.google.common.collect.ImmutableList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BelongLinesTest {

    @DisplayName("belongLine은 Station이 소속된 Line만을 데이터로 갖는다.")
    @Test
    void belongTest() {

        // given
        List<Station> stations = ImmutableList.of(new Station("name1"), new Station("name2"));
        Line notExpect = new Line("line1", "color", new Station("up"), new Station("down"), 1);
        Line expect = new Line("line1", "color", stations.get(0), stations.get(1), 1);

        List<Line> lines = ImmutableList.of(notExpect, expect);

        BelongLines actual = new BelongLines(lines, stations);

        assertThat(actual.getLines()).contains(expect)
                                     .doesNotContain(notExpect);
    }
}
