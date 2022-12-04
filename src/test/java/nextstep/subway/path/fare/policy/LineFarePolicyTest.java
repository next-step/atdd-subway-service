package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdgeWeight;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineFarePolicyTest {

    static final Fare ONE = Fare.valueOf(100);
    static final Fare THREE = Fare.valueOf(300);

    LineFarePolicy farePolicy;

    @BeforeEach
    void setUp() {
        farePolicy = new LineFarePolicy();
    }

    @Test
    void testCalculateLineFare() {
        Station station1 = new Station("station1");
        Station station2 = new Station("station2");
        Station station3 = new Station("station3");

        Line line1 = new Line("line1", "", ONE);
        Line line2 = new Line("line2", "", THREE);

        Section section1 = new Section(line1, station1, station2, 10);
        Section section2 = new Section(line2, station1, station3, 10);

        Path path = new Path(
                Lists.newArrayList(new SectionEdgeWeight(section1), new SectionEdgeWeight(section2)),
                Lists.newArrayList(station1, station2, station3),
                Distance.valueOf(10));

        Fare fare = farePolicy.calculate(path);

        assertThat(fare).isEqualTo(THREE);
    }
}
