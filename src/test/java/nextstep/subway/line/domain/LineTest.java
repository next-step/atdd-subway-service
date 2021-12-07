package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test
    @DisplayName("노선에 있는 역 조회 테스트")
    void getStationsTest() {
        Station dangsanStation = makeStation("당산역");
        Station hongdaeStation = makeStation("홍대역");

        Line line = makeLine("2호선", "green", dangsanStation, hongdaeStation, 10);
        checkValidStations(line, "당산역", "홍대역");
    }

    @Test
    @DisplayName("노선에 역 추가하는 테스트")
    void addSectionTest() {
        Station dangsanStation = makeStation("당산역");
        Station hongdaeStation = makeStation("홍대역");
        Station ewhaStation = makeStation("이대역");

        Line line = makeLine("2호선", "green", dangsanStation, hongdaeStation, 10);

        Section ewhaSection = makeSection(line, dangsanStation, ewhaStation, 3);

        line.addSection(ewhaSection);
        checkValidStations(line, "당산역", "이대역", "홍대역");
    }

    @Test
    @DisplayName("지하철역 삭제하는 테스트")
    void removeStationTest() {
        Station dangsanStation = makeStation("당산역");
        Station hongdaeStation = makeStation("홍대역");
        Station ewhaStation = makeStation("이대역");
        Station hapJeoungStation = makeStation("합정역");

        Line line = makeLine("2호선", "green", dangsanStation, hongdaeStation, 10);

        Section ewhaSection = makeSection(line, dangsanStation, ewhaStation, 3);
        line.addSection(ewhaSection);

        Section hapJeongSection = makeSection(line, dangsanStation, hapJeoungStation, 1);
        line.addSection(hapJeongSection);

        line.removeStation(ewhaStation);

        checkValidStations(line, "당산역", "합정역", "홍대역");
    }

    private void checkValidStations(Line line, String... stationNames) {
        List<Station> stations = line.getStations();
        Assertions.assertThat(stations.stream()
                .map(it -> it.getName())
                .collect(Collectors.toList())).containsExactly(stationNames);
    }

    private Station makeStation(String stationName) {
        return new Station(stationName);
    }

    private Section makeSection(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, new Distance(distance));
    }

    private Line makeLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }
}
