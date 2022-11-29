package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SectionTest {
    private Station 강남역;
    private Station 잠실역;
    private Section 구간;
    private Sections 구간_목록;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        구간 = new Section(강남역, 잠실역, 5);
        구간_목록 = new Sections(Collections.singletonList(new Section(강남역, 잠실역, 5)));
    }

    @Test
    void 구간의_상행역을_변경한다() {
        // when
        Station 선릉역 = new Station("선릉역");
        Section 새로운구간 = new Section(강남역, 선릉역, 2);
        구간.updateStation(새로운구간);

        // then
        assertThat(구간.getUpStation()).isEqualTo(선릉역);
    }

    @Test
    void 구간의_하행역을_변경한다() {
        // when
        Station 종합운동장역 = new Station("종합운동장역");
        Section 새로운구간 = new Section(종합운동장역, 잠실역, 2);
        구간.updateStation(새로운구간);

        // then
        assertThat(구간.getDownStation()).isEqualTo(종합운동장역);
    }

    @Test
    void 구간_병합을_통해_하행역을_변경한다() {
        // given
        Station 종합운동장역 = new Station("종합운동장역");
        Section 새로운구간 = new Section(잠실역, 종합운동장역, 2);

        // when
        구간.merge(새로운구간);

        // then
        assertThat(구간.getDownStation()).isEqualTo(종합운동장역);
    }

    @Test
    void 이미_노선에_포함된_상행역과_하행역을_등록할_경우_예외가_발생한다() {
        // given
        Section newSection = new Section(강남역, 잠실역, 2);

        // then
        assertThatThrownBy(() -> 구간_목록.add(newSection))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6})
    void 구간의_역_변경시_거리를_뺀_결과가_0이하일_경우_예외를_발생한다(int distance) {
        // when
        Station 종합운동장역 = new Station("종합운동장역");
        Section 새로운구간 = new Section(종합운동장역, 잠실역, distance);

        // then
        assertThatThrownBy( () -> 구간.updateStation(새로운구간))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_0이하일_경우_예외를_발생한다(int distance) {
        // then
        assertThatThrownBy( () -> new Section(강남역, 잠실역, distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
