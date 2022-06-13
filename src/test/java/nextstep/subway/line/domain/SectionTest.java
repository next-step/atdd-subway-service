package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {
    private Line line;
    private Station upStation;
    private Station downStation;
    private Section section;

    @BeforeEach
    void setUp() {
        upStation = new Station("공덕역");
        downStation = new Station("마포역");
        line = new Line("오호선", "purple");
        section = Section.of(line, upStation, downStation, Distance.from(10));
    }

    @Test
    @DisplayName("상행역과 하행역을 반환한다.")
    void findStations() {
        List<Station> stations = section.findStations();
        List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactly("공덕역", "마포역");
    }

    @Test
    @DisplayName("상행역 정보가 업데이트 된다.")
    void updateUpStation() {
        Station otherDownStation = new Station("애오개역");
        Section otherSection = Section.of(line, upStation, otherDownStation, Distance.from(5));

        section.update(otherSection);
        Distance distance = section.getDistance();

        assertThat(section.getUpStation()).isEqualTo(otherDownStation);
        assertThat(distance.getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("하행역 정보가 업데이트 된다.")
    void updateDownStation() {
        Station otherUpStation = new Station("애오개역");
        Section otherSection = Section.of(line, otherUpStation, downStation, Distance.from(5));

        section.update(otherSection);
        Distance distance = section.getDistance();

        assertThat(section.getDownStation()).isEqualTo(otherUpStation);
        assertThat(distance.getDistance()).isEqualTo(5);
    }
}
