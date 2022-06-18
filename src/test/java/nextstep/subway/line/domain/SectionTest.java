package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    Station 강남역;
    Station 판교역;
    Station 정자역;
    Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
        신분당선 = new Line("신분당선", "bg-red-600");
    }

    @Test
    @DisplayName("구간을 생성하고 상행, 하행역이 같은지 확인한다.")
    void hasProperStations() {
        // given
        Section savedSection = Section.of(신분당선, 강남역, 정자역, Distance.from(10));

        // when & then
        assertAll(
            () -> assertThat(savedSection.equalsUpStation(강남역)).isTrue(),
            () -> assertThat(savedSection.equalsDownStation(정자역)).isTrue()
        );
    }

    @Test
    @DisplayName("구간의 역이 동일한지 확인한다.")
    void hasSameStations() {
        // given
        Section saved = Section.of(신분당선, 강남역, 정자역, Distance.from(10));
        Section sameSection = Section.of(신분당선, 강남역, 정자역, Distance.from(7));
        Station 광교역 = new Station("광교역");
        Section sectionWithSameUpStation = Section.of(신분당선, 강남역, 광교역, Distance.from(5));
        Section sectionWithSameDownStation = Section.of(신분당선, 광교역, 정자역, Distance.from(5));

        // when & then
        assertAll(
            () -> assertThat(saved.isSameStationPair(sameSection)).isTrue(),
            () -> assertThat(saved.isSameStationPair(sectionWithSameUpStation)).isFalse(),
            () -> assertThat(saved.isSameStationPair(sectionWithSameDownStation)).isFalse()
        );
    }

    @Test
    @DisplayName("기준 구간과 상행역이 동일한 역 추가로 인한 신규구간이 추가된다.")
    void combineWhenAddingStationWithSameUpStation() {
        // given
        Section saved = Section.of(신분당선, 강남역, 정자역, Distance.from(10));

        // when
        Section added = Section.of(신분당선, 강남역, 판교역, Distance.from(7));
        saved.update(added);

        // then
        assertThat(saved).isEqualTo(Section.of(신분당선, 판교역, 정자역, Distance.from(3)));
    }

    @Test
    @DisplayName("기준 구간과 하행역이 동일한 역 추가로 인한 신규구간이 추가된다.")
    void combineWhenAddingStationWithSameDownStation() {
        // given
        Section saved = Section.of(신분당선, 강남역, 정자역, Distance.from(10));

        // when
        Section added = Section.of(신분당선, 판교역, 정자역, Distance.from(3));
        saved.update(added);

        // then
        assertThat(saved).isEqualTo(Section.of(신분당선, 강남역, 판교역, Distance.from(7)));
    }

}
