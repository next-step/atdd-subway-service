package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LineTest {

    @DisplayName("Line 은 Section 을 추가 할수 있다.")
    @Test
    void addSectionTest() {
        final Line 신분당선 = new Line("신분당선", "bg-blue-200");
        final Station 수원역 = new Station("수원역");
        final Station 병점역 = new Station("병점역");
        신분당선.addSection(new Section(신분당선, 수원역, 병점역, 10));
        assertThat(신분당선).isEqualTo(new Line("신분당선", "bg-blue-200", 수원역, 병점역, 10));
    }


    @DisplayName("Section 추가시 같은 노선이 아닌 경우 에러를 발생한다.")
    @Test
    void invalidAddSectionTest() {
        final Line 신분당선 = new Line("신분당선", "bg-blue-200");

        final Line 일호선 = new Line("일호선", "bg-red-200");
        final Station 수원역 = new Station("수원역");
        final Station 병점역 = new Station("병점역");

        assertThatThrownBy(() -> 신분당선.addSection(new Section(일호선, 수원역, 병점역, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

}