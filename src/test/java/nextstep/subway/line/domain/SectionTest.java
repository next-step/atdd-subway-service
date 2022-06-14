package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.generic.domain.Distance;
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
    @DisplayName("상행역이 같은지 확인 - 역을 대입")
    void hasUpStation_station() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);

        // when & then
        assertAll(
            () -> assertThat(기존구간.hasUpStation(강남역)).isTrue(),
            () -> assertThat(기존구간.hasUpStation(광교역)).isFalse()
        );
    }

    @Test
    @DisplayName("하행역이 같은지 확인 - 역을 대입")
    void hasDownStation_station() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);

        // when & then
        assertAll(
                () -> assertThat(기존구간.hasDownStation(광교역)).isTrue(),
                () -> assertThat(기존구간.hasDownStation(강남역)).isFalse()
        );
    }

    @Test
    @DisplayName("구간이 인접한 구간인지 확인")
    void intersects() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");
        final Section 상행역이_같은구간 = new Section(신분당선, 강남역, 동천역, 8);
        final Station 미금역 = new Station("미금역");
        final Section 하행역이_같은구간 = new Section(신분당선, 미금역, 광교역, 8);
        final Section 인접하지_않은구간 = new Section(신분당선, 동천역, 미금역, 4);

        // when & then
        assertAll(
            () -> assertThat(기존구간.intersects(상행역이_같은구간)).isTrue(),
            () -> assertThat(기존구간.intersects(하행역이_같은구간)).isTrue(),
            () -> assertThat(기존구간.intersects(인접하지_않은구간)).isFalse()
        );
    }

    @Test
    @DisplayName("구간의 역이 같은지 확인")
    void equalsStations() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Section 상행_하행역이_같은구간 = new Section(신분당선, 강남역, 광교역, 8);
        final Station 동천역 = new Station("동천역");
        final Section 상행역만_같은구간 = new Section(신분당선, 강남역, 동천역, 4);
        final Section 하행역만_같은구간 = new Section(신분당선, 동천역, 광교역, 4);

        // when & then
        assertAll(
            () -> assertThat(기존구간.equalsStations(상행_하행역이_같은구간)).isTrue(),
            () -> assertThat(기존구간.equalsStations(상행역만_같은구간)).isFalse(),
            () -> assertThat(기존구간.equalsStations(하행역만_같은구간)).isFalse()
        );
    }

    @Test
    @DisplayName("상행역이 같은지 확인 - 구간을 대입")
    void hasUpStation_section() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");
        final Section 상행역이_같은구간 = new Section(신분당선, 강남역, 동천역, 4);
        final Section 상행역이_다른구간 = new Section(신분당선, 동천역, 광교역, 4);

        // when & then
        assertAll(
                () -> assertThat(기존구간.hasUpStation(상행역이_같은구간)).isTrue(),
                () -> assertThat(기존구간.hasUpStation(상행역이_다른구간)).isFalse()
        );
    }

    @Test
    @DisplayName("구간 끼리 종점이 같은지 확인")
    void hasDownStation_section() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");
        final Section 하행역이_같은_추가구간 = new Section(신분당선, 동천역, 광교역, 4);
        final Section 상행역이_같은_추가구간 = new Section(신분당선, 강남역, 동천역, 4);

        // when & then
        assertAll(
                () -> assertThat(기존구간.hasDownStation(하행역이_같은_추가구간)).isTrue(),
                () -> assertThat(기존구간.hasDownStation(상행역이_같은_추가구간)).isFalse()
        );
    }

    @Test
    @DisplayName("구간 재배열 테스트 - 역 과 역 사이에 역이 추가되는 경우(상행역이 같을때)")
    void rearrange_upStation() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");

        // when
        final Section 새로운구간 = new Section(신분당선, 강남역, 동천역, 8);
        기존구간.rearrange(새로운구간);

        // then
        assertThat(기존구간).isEqualTo(new Section(신분당선, 동천역, 광교역, 2));
    }

    @Test
    @DisplayName("구간 재배열 테스트 - 역 과 역 사이에 역이 추가되는 경우(하행역이 같을때)")
    void rearrange_downStation() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 광교역, 10);
        final Station 동천역 = new Station("동천역");

        // when
        final Section 새로운구간 = new Section(신분당선, 동천역, 광교역, 8);
        기존구간.rearrange(새로운구간);

        // then
        assertThat(기존구간).isEqualTo(new Section(신분당선, 강남역, 동천역, 2));
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

    @Test
    @DisplayName("구간 병합시 생성되는 구간 테스트")
    void mergeSection() {
        // given
        final Section 기존구간 = new Section(신분당선, 강남역, 판교역, 10);
        final Section 다음구간 = new Section(신분당선, 판교역, 광교역, 5);

        // when & then
        assertThat(Section.mergeSection(기존구간, 다음구간)).isEqualTo(new Section(신분당선, 강남역, 광교역, 15));
    }

}
