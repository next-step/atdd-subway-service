package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {
    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Station 성수역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");
        성수역 = new Station("성수역");
        신분당선 = new Line.Builder().name("신분당선").color("bg-red-600").downStation(광교역).distance(10).build();
    }


    @DisplayName("구간의 상행역인지 판단한다.")
    @Test
    void isUpStation() {
        //given
        Section section = new Section(신분당선, 광교역, 판교역, 5);

        //when & then
        assertAll(
                () -> assertThat(section.isUpStation(판교역)).isFalse(),
                () -> assertThat(section.isUpStation(광교역)).isTrue()
        );
    }

    @DisplayName("구간의 하행역인지 판단한다.")
    @Test
    void isDownStation() {
        //given
        Section section = new Section(신분당선, 광교역, 판교역, 5);

        //when & then
        assertAll(
                () -> assertThat(section.isDownStation(판교역)).isTrue(),
                () -> assertThat(section.isDownStation(광교역)).isFalse()
        );
    }

    @DisplayName("구간의 역이 포함되어 있는지 판단한다.")
    @Test
    void isContainStation() {
        //given
        Section section = new Section(신분당선, 광교역, 판교역, 5);

        //when
        final boolean isContainStation = section.isContainStation(판교역);

        //then
        assertThat(isContainStation).isTrue();
    }
}