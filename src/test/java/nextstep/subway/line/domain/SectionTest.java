package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {
    @DisplayName("상행선을 바꿀 수 있다.")
    @Test
    void updateUpStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));

        Station newStation = new Station("양재역");
        Section newSection = new Section(upStation, newStation, new Distance(5));

        // when
        section.update(newSection);

        // then
        assertThat(section.getUpStation()).isEqualTo(newStation);
    }

    @DisplayName("지하철 역이 구간의 상행선과 동일한지 알 수 있다")
    @Test
    void isEqualUpStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));

        // when
        boolean result = section.isEqualUpStation(upStation);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("지하철 역이 구간의 하행선과 동일한지 알 수 있다")
    @Test
    void isEqualDownStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));

        // when
        boolean result = section.isEqualDownStation(downStation);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("하행선을 바꿀 수 있다.")
    @Test
    void updateDownStation() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));

        Station newStation = new Station("양재역");
        Section newSection = new Section(newStation, downStation, new Distance(5));

        // when
        section.update(newSection);

        // then
        assertThat(section.getDownStation()).isEqualTo(newStation);
    }

    @DisplayName("두 개의 구간을 병합 할 수 있다")
    @Test
    void merge() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));

        Station newStation = new Station("양재역");
        Section newSection = new Section(downStation, newStation, new Distance(5));

        // when
        Section result = section.merge(newSection);

        // then
        assertThat(result.getDownStation()).isEqualTo(newStation);
    }
}
