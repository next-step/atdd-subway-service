package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 기능")
class LineTest {

    private Line line;

    /**
     * 강남-(5)-양재-(5)-양재시민의숲-(5)-청계산입구-(5)-판교
     */
    @BeforeEach
    void setUp() {
        Map<String, Station> stations = createStations("강남", "양재", "양재시민의숲", "청계산입구", "판교");

        line = new Line("신분당선", "bg-red-600");
        line.addSection(new Section(stations.get("양재시민의숲"), stations.get("청계산입구"), 5));
        line.addSection(new Section(stations.get("양재"), stations.get("양재시민의숲"), 5));
        line.addSection(new Section(stations.get("강남"), stations.get("양재"), 5));
        line.addSection(new Section(stations.get("청계산입구"), stations.get("판교"), 5));
    }

    @Test
    @DisplayName("노선에 속한 지하철 역이 상행역 부터 하행역 순으로 정렬된다.")
    void sortLineStations() {
        // given // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations)
                .extracting("name")
                .containsExactly("강남", "양재", "양재시민의숲", "청계산입구", "판교");
    }

    @Test
    @DisplayName("노선에 속한 지하철 역이 없으면 빈 목록이 조회된다.")
    void emptyStations() {
        // given
        Line line = new Line("신분당선", "bg-red-600");

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).isEmpty();
    }

    private Map<String, Station> createStations(String... stationName) {
        return Arrays.stream(stationName)
                .map(Station::new)
                .collect(Collectors.toMap(Station::getName, Function.identity()));
    }
}
