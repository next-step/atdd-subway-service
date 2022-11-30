package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PathGraphTest {
    @Test
    void path(){
        // given
        Station station1 = new Station(1L, "암사역");
        Station station2 = new Station(2L, "천호역");
        Station station3 = new Station(3L, "몽촌토성역");
        Line line = new Line("8호선", "bg-color-555", station1, station2, 10);
        line.addSection(station2, station3, 10);

        // when
        GraphPath path = PathGraph.findPath(station1.getId(), station3.getId(), Arrays.asList(line));

        // then
        assertThat(path.getVertexList()).containsExactly(
                station1.getId().toString(), station2.getId().toString(), station3.getId().toString()
        );
        assertThat((int)path.getWeight()).isEqualTo(20);

    }
}
