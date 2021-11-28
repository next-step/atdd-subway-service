package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    private static final Line 이호선 = new Line("2호선", "green");
    private static final Station 강남역 = new Station("강남역");
    private static final Station 역삼역 = new Station("역삼역");
    private static final Station 삼성역 = new Station("삼성역");

    @Test
    void merge() {
        // given
        Section 하행구간 = new Section(이호선, 강남역, 역삼역, 20);
        Section 상행구간 = new Section(이호선, 역삼역, 삼성역, 10);

        // when
        Section section = Section.merge(이호선, 상행구간, 하행구간);

        // then
        assertThat(section).isEqualTo(new Section(이호선, 강남역, 삼성역, 30));
    }
}