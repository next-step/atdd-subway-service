package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    private Line line;
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 군자역;

    @BeforeEach
    void setUp() {
        강남역 = Station.of("강남역");
        역삼역 = Station.of("역삼역");
        삼성역 = Station.of("삼성역");
        군자역 = Station.of("군자역");
        line = new Line("1호선", "파란색");
    }

    @DisplayName("라인에서 역을 지운다.")
    @Test
    void removeTest() {
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        Section section2 = new Section(line, 역삼역, 군자역, 10);
        line.addSection(section1);
        line.addSection(section2);

        line.remove(역삼역);

        assertThat(line.getStations()).containsExactly(강남역, 군자역);
    }

    @DisplayName("라인에서 역을 지우는 것에 실패한다.")
    @Test
    void removeFailTest() {
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        line.addSection(section1);

        assertThatThrownBy(() -> {
            line.remove(역삼역);
        }).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> {
            line.remove(삼성역);
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("라인에 순서 섞인 구간들이 들어가도 정렬된 역들을 정상 반환한다.")
    @Test
    void getStationsTest() {
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        Section section2 = new Section(line, 역삼역, 군자역, 10);
        Section section3 = new Section(line, 삼성역, 강남역, 5);
        line.addSection(section1);
        line.addSection(section2);
        line.addSection(section3);

        List<Station> stations = line.getStations();

        assertThat(stations).containsExactly(삼성역, 강남역, 역삼역, 군자역);
    }

    @DisplayName("라인에서 가장 최상행 역을 찾는다.")
    @Test
    void findMostTopStationTest() {
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        Section section2 = new Section(line, 역삼역, 군자역, 10);
        Section section3 = new Section(line, 삼성역, 강남역, 5);
        line.addSection(section1);
        line.addSection(section2);
        line.addSection(section3);

        Station topMostStation = line.findTopMostStation();

        assertThat(topMostStation).isEqualTo(삼성역);
    }

    @DisplayName("라인에 구간을 추가한다.")
    @Test
    void addSectionTest() {
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        Section section2 = new Section(line, 강남역, 군자역, 5);

        line.addSection(section1);
        assertThat(line.getSections().get(0)).isEqualTo(section1);

        line.addSection(section2);
        assertThat(line.getStations()).containsExactly(강남역, 군자역, 역삼역);
    }

    @DisplayName("라인에 구간을 추가 실패한다.")
    @Test
    void addSectionFailTest() {
        Section section = new Section(line, 강남역, 역삼역, 10);

        line.addSection(section);

        assertThatThrownBy(() -> {
            line.addSection(new Section(line, 강남역, 역삼역, 10));
        }).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> {
            line.addSection(new Section(line, 군자역, 삼성역, 10));
        }).isInstanceOf(RuntimeException.class);
    }
}
