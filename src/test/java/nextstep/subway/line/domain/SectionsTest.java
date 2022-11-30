package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Set;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SectionsTest {
    private Station 강남역;
    private Station 종합운동장역;
    private Station 잠실역;
    private Sections 구간;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        종합운동장역 = new Station("종합운동장역");
        구간 = new Sections(Collections.singletonList(new Section(강남역, 잠실역, 5)));
    }

    @Test
    void 지하철역_구간_조회() {
        assertThat(구간.orderedStations()).containsExactly(강남역, 잠실역);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_0이하일_경우_예외를_발생한다(int distance) {
        assertThatThrownBy(() -> new Section(강남역, 잠실역, distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선에_등록된_중간역을_삭제하면_해당_역이_조회되지_않는다() {
        // given
        구간.add(new Section(강남역, 종합운동장역, 3));
        assertThat(구간.orderedStations()).containsExactly(강남역, 종합운동장역, 잠실역);

        // when
        구간.delete(종합운동장역);

        // then
        assertThat(구간.orderedStations()).containsExactly(강남역, 잠실역);
    }

    @Test
    void 노선에_등록된_상행역_종점을_삭제하면_해당_역이_조회되지_않는다() {
        // given
        Station 종운역 = new Station("종운역");
        Section newSection = new Section(강남역, 종운역, 3);
        구간.add(newSection);
        assertThat(구간.orderedStations()).containsExactly(강남역, 종운역, 잠실역);

        // when
        구간.delete(강남역); // 삭제가 되지 않음

        // then
        assertThat(구간.orderedStations()).containsExactly(종운역, 잠실역);
    }
    @Test
    void 노선에_등록된_하행역_종점을_삭제하면_해당_역이_조회되지_않는다() {
        // given
        구간.add(new Section(잠실역, 종합운동장역, 3));
        assertThat(구간.orderedStations()).containsExactly(강남역, 잠실역, 종합운동장역);

        // when
        구간.delete(종합운동장역);

        // then
        assertThat(구간.orderedStations()).containsExactly(강남역, 잠실역);
    }

    @Test
    void 노선에_등록되지_않은_역_제거시_예외발생() {

    }

}
