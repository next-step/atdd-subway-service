package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class SectionTest {

    private Station 강남역;
    private Station 판교역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        광교역 = new Station("광교역");
    }

    @Test
    @DisplayName("구간의 상행선 변경")
    void updateUpStation() {
        // given
        Section section = new Section(null, 강남역, 광교역, 5);

        // when
        section.updateUpStation(판교역, 3);

        // then
        assertThat(section.getUpStation()).isEqualTo(판교역);
        assertThat(section.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간의 상행선 변경 시, 기존 구간의 거리보다 같거나 긴 경우 변경 실패")
    void updateUpStation_expteion() {
        // given
        Section section = new Section(null, 강남역, 광교역, 5);

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> section.updateUpStation(판교역, 5));
        Assertions.assertThrows(RuntimeException.class, () -> section.updateUpStation(판교역, 10));
    }

    @Test
    @DisplayName("구간의 하행선 변경")
    void updateDownStation() {
        // given
        Section section = new Section(null, 강남역, 판교역, 5);

        // when
        section.updateDownStation(광교역, 3);

        // then
        assertThat(section.getDownStation()).isEqualTo(광교역);
        assertThat(section.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간의 하행선 변경 시, 기존 구간의 거리보다 같거나 긴 경우 변경 실패")
    void updateDownStation_expteion() {
        // given
        Section section = new Section(null, 강남역, 판교역, 5);

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> section.updateDownStation(광교역, 5));
        Assertions.assertThrows(RuntimeException.class, () -> section.updateDownStation(광교역, 10));
    }
}
