package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    private Station gangnam;
    private Station yangjae;
    private Station gyodae;
    private Station hongdae;
    private Line line;

    @BeforeEach
    public void setUp() {
        gangnam = new Station("강남역");
        yangjae = new Station("양재역");
        gyodae = new Station("교대역");
        hongdae = new Station("홍대역");
        line = new Line("2호선", "green", gangnam, yangjae, 10);
    }

    @Test
    void getStations() {
        line.addStation(yangjae, gyodae, 5);

        List<Station> stations = line.getStations();

        assertThat(stations).isNotNull();
        assertThat(stations).containsExactly(gangnam, yangjae, gyodae);
    }

    @DisplayName("예외처리 테스트: 이미 등록된 구간")
    @Test
    void alreadyRegisterSection() {
        assertThatThrownBy(() -> {
            line.addStation(gangnam, yangjae, 5);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageMatching("이미 등록된 구간 입니다.");
    }

    @DisplayName("예외처리 테스트: 등록되지않은 구간")
    @Test
    void notYetRegisterSection() {
        assertThatThrownBy(() -> {
            line.addStation(hongdae, gyodae, 5);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageMatching("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간 등록 test: 기존상행-새로운하행")
    @Test
    void addToFront() {
        line.addStation(gangnam, gyodae, 5);

        List<Station> stations = line.getStations();
        assertThat(stations).isNotNull();
        assertThat(stations).containsExactly(gangnam, gyodae, yangjae);
    }

    @DisplayName("구간 등록 test: 새로운상행-기존하행")
    @Test
    void addToEnd() {
        line.addStation(gyodae, yangjae, 5);

        List<Station> stations = line.getStations();
        assertThat(stations).isNotNull();
        assertThat(stations).containsExactly(gangnam, gyodae, yangjae);
    }

    @DisplayName("역 제거 test")
    @Test
    void removeStation() {
        line.addStation(gyodae, yangjae, 5);

        line.removeStation(yangjae);

        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(gangnam, gyodae);
    }
}
