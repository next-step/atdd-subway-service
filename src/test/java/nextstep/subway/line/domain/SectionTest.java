package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {
    private final Line line = new Line("1호선", "dark-blue");
    private final Station 구로역 = new Station("구로역");
    private final Station 신도림역 = new Station("신도림역");
    private final Station 영등포역 = new Station("영등포역");
    private final Station 신길역 = new Station("신길역");
    private final Distance 거리_50 = new Distance(50);
    private final Distance 거리_100 = new Distance(100);
    private Section section = null;

    @BeforeEach
    void setUp() {
        section = new Section(line, 신도림역, 영등포역, 거리_100);
    }

    @Test
    @DisplayName("구간의 상행역을 변경한다.")
    void 상행역_변경() {
        assertThat(section.getUpStation()).isEqualTo(신도림역);
        section.updateUpStation(구로역, 거리_50);
        assertThat(section.getUpStation()).isEqualTo(구로역);
    }

    @Test
    @DisplayName("구간의 하행역을 변경한다.")
    void 하행역_변경() {
        assertThat(section.getDownStation()).isEqualTo(영등포역);
        section.updateDownStation(신길역, 거리_50);
        assertThat(section.getDownStation()).isEqualTo(신길역);
    }

    @Test
    @DisplayName("구간의 상행역을 비교한다.")
    void 상행역_비교() {
        assertThat(section.equalsUpStation(신도림역)).isTrue();
        assertThat(section.equalsUpStation(신길역)).isFalse();
    }

    @Test
    @DisplayName("구간의 하행역을 비교한다.")
    void 하행역_비교() {
        assertThat(section.equalsDownStation(영등포역)).isTrue();
        assertThat(section.equalsDownStation(구로역)).isFalse();
    }
}
