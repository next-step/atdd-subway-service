package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철구간 테스트")
class SectionTest {

    @DisplayName("같은 구간은 설정할 수 없다.")
    @Test
    void error_same_subway_section() {
        // given
        Station 강남역 = new Station("강남역");

        // when & then
        Assertions.assertThatThrownBy(() -> new Section(강남역, 강남역, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상행선과 하행선이 동일할 수 없습니다.");
    }

}
