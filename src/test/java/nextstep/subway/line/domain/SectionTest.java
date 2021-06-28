package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {

    Line line;
    Section section;
    Station upStation;
    Station downStation;

    @BeforeEach
    void setup() {
        line = new Line("신분당선"
                , "빨간색"
                , new Station("강남역")
                , new Station("선릉역")
                , new Distance(5)
                , new Fee(900));

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

    @DisplayName("기존 구간에 하행을 새로운 구간의 상행과 연결")
    @Test
    void connectSection() {
        //given
        Station station = new Station("임시역");
        Section newSection = new Section(line, downStation, station, new Distance(3));
        //when
        section.connectUpStation(newSection);
        //then
        assertThat(section.getDistance()).isEqualTo(new Distance(2));
    }
}