package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {
    public static final Line twoLine = new Line("2호선", "yellow");
    public static final Station gangNam = new Station("강남역");
    public static final Station yangJae = new Station("양재역");
    public static final Station panGyo = new Station("판교역");
    public static final Distance distance = Distance.of(10);

    @Test
    @DisplayName("Section 객체가 같은지 검증")
    void verifySameSection() {
        Section section = new Section(twoLine, gangNam, yangJae, distance);

        assertThat(section).isEqualTo(new Section(twoLine, gangNam, yangJae, distance));
    }
}
