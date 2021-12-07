package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class SectionsTest {

    @DisplayName("노선 역 리스트 조회")
    @Test
    void getStations() {
        // Given
        Line line = new Line("2호선", "green");
        Station upStation = new Station("강남역");
        Station downStation = new Station("잠실역");
        Sections sections = new Sections(new Section(line, upStation, downStation, 10));

        // When
        List<Station> stations =  sections.getStations();

        // Then
        Assertions.assertThat(stations).containsAll(Arrays.asList(upStation, downStation));
    }
}
