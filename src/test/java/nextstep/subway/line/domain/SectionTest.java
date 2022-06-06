package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간에 대한 단위 테스트")
class SectionTest {

    @DisplayName("구간에 대해 상행역과 같다면 ture, 아니면 false 를 리턴해야 한다")
    @Test
    void section_equals_upStation_test() {
        // given
        Station upStation = new Station("테스트");
        Station downStation = new Station("테스트2");
        Section 구간 = new Section(new Line(), upStation, downStation, 10);

        // when
        boolean 상행역과_같음 = 구간.isEqualsUpStation(upStation);
        boolean 상행역과_다름 = 구간.isEqualsUpStation(downStation);

        // then
        assertAll(
            () -> assertTrue(상행역과_같음),
            () -> assertFalse(상행역과_다름)
        );
    }

    @DisplayName("구간에 대해 하행역과 같다면 ture, 아니면 false 를 리턴해야 한다")
    @Test
    void section_equals_downStation_test() {
        // given
        Station upStation = new Station("테스트");
        Station downStation = new Station("테스트2");
        Section 구간 = new Section(new Line(), upStation, downStation, 10);

        // when
        boolean 하행역과_같음 = 구간.isEqualsDownStation(downStation);
        boolean 하행역과_다름 = 구간.isEqualsDownStation(upStation);

        // then
        assertAll(
            () -> assertTrue(하행역과_같음),
            () -> assertFalse(하행역과_다름)
        );
    }

    @DisplayName("하행역이 포함된 구간이 추가되면 역의 상행이"
        + " 추가되는 역으로 변경되며 거리도 차감된다")
    @Test
    void update_upStation_test() {
        // given
        Station 문래역 = new Station("문래");
        Station 대림역 = new Station("대림");

        Section section = new Section(new Line(), 문래역, 대림역, 10);
        Station 신도림역 = new Station("신도림");

        // when
        section.updateUpStation(신도림역, new Distance(5));

        // then
        assertAll(
            () -> assertThat(section.getUpStation()).isEqualTo(신도림역),
            () -> assertThat(section.getDownStation()).isEqualTo(대림역),
            () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("상행역이 포함된 구간이 추가되면 역의 상행이"
        + " 추가되는 역으로 변경되며 거리도 차감된다")
    @Test
    void update_downStation_test() {
        // given
        Station 문래역 = new Station("문래");
        Station 대림역 = new Station("대림");

        Section section = new Section(new Line(), 문래역, 대림역, 10);
        Station 신도림역 = new Station("신도림");

        // when
        section.updateDownStation(신도림역, new Distance(5));

        // then
        assertAll(
            () -> assertThat(section.getUpStation()).isEqualTo(문래역),
            () -> assertThat(section.getDownStation()).isEqualTo(신도림역),
            () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }
}
