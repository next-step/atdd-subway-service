package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {

    Section section;
    Station upStation;
    Station downStation;

    @BeforeEach
    void setup() {
        Line line = new Line("신분당선"
                , "빨간색"
                , new Station("강남역")
                , new Station("선릉역")
                , new Distance(5));

        upStation = new Station("선릉역");
        downStation = new Station("한티역");

        section = new Section(line, upStation, downStation, new Distance(5));
    }

    @DisplayName("상행역 비교")
    @Test
    void isUpStation() {
        //given
        //when
        //then
        assertThat(section.isUpStation(upStation)).isTrue();
    }

    @DisplayName("하행역 비교")
    @Test
    void isDownStation() {
        //given
        //when
        //then
        assertThat(section.isDownStation(downStation)).isTrue();
    }

    @DisplayName("존재하는 역인지 확인")
    @Test
    void isExistsStation() {
        //given
        //when
        //then
        assertThat(section.isExistsStation(upStation)).isTrue();
    }

    @DisplayName("상행역 수정")
    @Test
    void updateUpStation() {
        //given
        Station station = new Station("임시역");
        //when
        section.updateUpStation(station, new Distance(3));
        //then
        assertThat(section.isUpStation(station)).isTrue();
    }

    @DisplayName("하행역 수정")
    @Test
    void updateDownStation() {
        //given
        Station station = new Station("임시역");
        //when
        section.updateDownStation(station, new Distance(3));
        //then
        assertThat(section.isDownStation(station)).isTrue();
    }
}