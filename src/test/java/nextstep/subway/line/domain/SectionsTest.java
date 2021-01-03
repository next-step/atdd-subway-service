package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    void setup() {
        line = new Line("2호선", "초록색", BigDecimal.ZERO);
        station1 = new Station("강남역");
        station2 = new Station("서초역");
        station3 = new Station("잠실역");
        station4 = new Station("신도림역");
    }

    @DisplayName("Sections에서 역 이름이 한번만 나온 역들을 찾아낼 수 있다.")
    @Test
    void findEndStationsInSections() {
        List<Section> sections = Arrays.asList(
                new Section(line, station1, station2, 5),
                new Section(line, station2, station3, 5),
                new Section(line, station3, station4, 5)
        );

        List<Station> stations = sections.stream()
                .flatMap(it -> it.getStations().stream())
                .collect(Collectors.toList());

        List<Station> justOnceStations = stations.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(it -> it.getValue() == 1L)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        assertThat(justOnceStations).contains(station1, station4);
        assertThat(justOnceStations).doesNotContain(station2, station3);
    }
}
