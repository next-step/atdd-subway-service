package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SectionsTest {
    private Station 강남역;
    private Station 잠실역;
    private Sections 구간;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        구간 = new Sections(Collections.singletonList(new Section(강남역, 잠실역, new Distance(5))));
    }

    @Test
    void 지하철역_구간_조회() {
        assertThat(구간.orderedStations()).containsExactly(강남역, 잠실역);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_0이하일_경우_예외를_발생한다(int distance) {
        assertThatThrownBy(() -> new Section(강남역, 잠실역, new Distance(distance)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
