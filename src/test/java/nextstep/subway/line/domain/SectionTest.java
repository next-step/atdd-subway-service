package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SectionTest {
    private Line 호선;
    private Station 강남역;
    private Station 잠실역;
    private Section 구간;

    @BeforeEach
    void setUp() {
        호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        구간 = new Section(강남역, 잠실역, new Distance(5));
    }

    @Test
    void 구간의_상행역을_변경한다() {
//        // when
//        Station 선릉역 = new Station("선릉역");
//        Section 새로운구간 = new Section(호선, 강남역, 선릉역, 2);
//        구간.updateStation(새로운구간);
//
//        Station upStation = 구간.getUpStation();
//        Station 선릉역1 = 선릉역;
//
//        // then
//        assertThat(구간.getUpStation()).isEqualTo(선릉역);
    }

    @Test
    void 구간의_하행역을_변경한다() {
//        // when
//        Station 종합운동장역 = new Station("종합운동장역");
//        Section 새로운구간 = new Section(호선, 종합운동장역, 잠실역, 2);
//        구간.updateStation(새로운구간);
//
//        // then
//        assertThat(구간.getDownStation()).isEqualTo(종합운동장역);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6})
    void 구간의_역_변경시_거리를_뺀_결과가_0이하일_경우_예외를_발생한다(int distance) {
//        // when
//        Station 종합운동장역 = new Station("종합운동장역");
//        Section 새로운구간 = new Section(호선, 종합운동장역, 잠실역, distance);
//
//        // then
//        assertThatThrownBy( () -> 구간.updateStation(새로운구간))
//                .isInstanceOf(IllegalArgumentException.class);
    }
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_0이하일_경우_예외를_발생한다(int distance) {
//        assertThatThrownBy( () -> new Section(호선, 강남역, 잠실역, distance))
//                .isInstanceOf(IllegalArgumentException.class);
    }

}
