package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionTest {

    @Test
    @DisplayName("Section 상행역 update Test")
    void updateSectionUpStation() {
        // given
        Station 상행역 = new Station("상행역");
        Station 하행역 = new Station("하행역");
        Station 새로운역 = new Station("새로운역");
        Section originSection = new Section(null, 상행역, 하행역,10);

        // when
        originSection.updateUpStation(새로운역, 8);

        // then
        assertThat(originSection.getUpStation()).isEqualTo(새로운역);
        assertThat(originSection.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("Section 하행역 update Test")
    void updateSectionDownStation() {
        // given
        Station 상행역 = new Station("상행역");
        Station 하행역 = new Station("하행역");
        Station 새로운역 = new Station("새로운역");
        Section originSection = new Section(null, 상행역, 하행역,10);

        // when
        originSection.updateDownStation(새로운역, 8);

        // then
        assertThat(originSection.getDownStation()).isEqualTo(새로운역);
        assertThat(originSection.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("Section 상행역 update시 기존 거리보다 크거나 같으면 error 발생")
    void error_updateSectionUpStation() {
        Station 상행역 = new Station("상행역");
        Station 하행역 = new Station("하행역");
        Station 새로운역 = new Station("새로운역");
        Section originSection = new Section(null, 상행역, 하행역,10);

        assertThrows(RuntimeException.class, () -> originSection.updateUpStation(새로운역, 10));
    }

    @Test
    @DisplayName("Section 하행역 update시 기존 거리보다 크거나 같으면 error 발생")
    void error_updateSectionDownStation() {
        Station 상행역 = new Station("상행역");
        Station 하행역 = new Station("하행역");
        Station 새로운역 = new Station("새로운역");
        Section originSection = new Section(null, 상행역, 하행역,10);

        assertThrows(RuntimeException.class, () -> originSection.updateDownStation(새로운역, 10));

    }
}
