package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    Station 강남역;
    Station 판교역;
    Station 광교역;
    Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600");
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThat(new Section(신분당선, 강남역, 광교역, 10)).isEqualTo(new Section(신분당선, 강남역, 광교역, 10));
    }

    @Test
    @DisplayName("생성 테스트")
    void hasUpStation_station() {
    }

    @Test
    @DisplayName("생성 테스트")
    void intersects() {

    }

    @Test
    @DisplayName("생성 테스트")
    void equalsStations() {
    }

    @Test
    @DisplayName("생성 테스트")
    void hasUpStation_section() {
    }

    @Test
    @DisplayName("구간 끼리 종점이 같은지 확인")
    void hasDownStation() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");
        final Section 종점이_같은_추가구간 = new Section(신분당선, 동천역, 광교역, 4);
        final Section 시점이_같은_추가구간 = new Section(신분당선, 강남역, 동천역, 4);

        // when & then
        assertAll(
                () -> assertThat(기존구간.hasDownStation(종점이_같은_추가구간)).isTrue(),
                () -> assertThat(기존구간.hasDownStation(시점이_같은_추가구간)).isFalse()
        );
    }

    @Test
    @DisplayName("구간 재배열 테스트")
    void rearrange() {
    }

    @Test
    @DisplayName("구간에 역이 포함 되는지 확인")
    void hasStation() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");

        // when & then
        assertAll(
            () -> assertThat(기존구간.hasStation(강남역)).isTrue(),
            () -> assertThat(기존구간.hasStation(광교역)).isTrue(),
            () -> assertThat(기존구간.hasStation(동천역)).isFalse()
        );
    }
}
