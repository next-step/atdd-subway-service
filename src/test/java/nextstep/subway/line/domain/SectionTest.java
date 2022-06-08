package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Section 클래스 테스트")
class SectionTest {

    @DisplayName("구간의 상행역과 입력 받은 역이 동등한지 확인한다")
    @ParameterizedTest
    @CsvSource({
            "강남역, true",
            "역삼역, false"
    })
    void matchesUpStation(String stationName, boolean expected) {
        Station station = new Station(stationName);
        Section section = new Section(new Line(), new Station("강남역"), new Station(), 10);
        assertThat(section.matchesUpStation(station)).isEqualTo(expected);
    }

    @DisplayName("구간의 하행역과 입력 받은 역이 동등한지 확인한다")
    @ParameterizedTest
    @CsvSource({
            "강남역, true",
            "역삼역, false"
    })
    void matchesDownStation(String stationName, boolean expected) {
        Station station = new Station(stationName);
        Section section = new Section(new Line(), new Station(), new Station("강남역"), 10);
        assertThat(section.matchesDownStation(station)).isEqualTo(expected);
    }

}
