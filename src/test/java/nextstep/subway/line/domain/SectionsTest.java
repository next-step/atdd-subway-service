package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SectionsTest {
    Line line;
    Sections sections;
    Station upStation;
    Station downStation;

    @BeforeEach
    void setUp() {
        line = new Line("2호선", "green");
        upStation = new Station("강남역");
        downStation = new Station("잠실역");
        sections = new Sections(new Section(line, upStation, downStation, 10));
    }

    @DisplayName("노선 역 리스트 조회")
    @Test
    void getStations() {
        // When
        List<Station> stations =  sections.getStations();

        // Then
        assertThat(stations).containsAll(Arrays.asList(upStation, downStation));
    }

    @DisplayName("노선 역 추가")
    @Test
    void addLineStation() {
        // Given
        Station newStation = new Station("역삼역");

        // When
        sections.addLineStations(new Section(line, upStation, newStation, 3));

        // Then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsAll(Arrays.asList(upStation, newStation, downStation));
    }

    @DisplayName("노선 역 제거")
    @Test
    void removeLineStation() {
        // Given
        Station newStation = new Station("역삼역");
        sections.addLineStations(new Section(line, upStation, newStation, 3));

        // When
        sections.removeLineStation(newStation);

        // Then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsAll(Arrays.asList(upStation, downStation));
    }
}
