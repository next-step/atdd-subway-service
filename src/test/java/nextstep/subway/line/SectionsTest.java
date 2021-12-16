package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@DataJpaTest
public class SectionsTest {
    @DisplayName("노선 생성 후 지하철 목록 조회")
    @Test
    void getStations() {
        // given
        Station upStation = new Station(1L, "1번역");
        Station downStation = new Station(2L, "2번역");
        Line line = new Line("신분당선", "red", upStation, downStation, 10);
        List<Station> stations = line.getSortedStations();

        // when
        Station firstStation = stations.get(0);
        Station lastStation = stations.get(stations.size() - 1);

        // then
        assertThat(firstStation).isEqualTo(upStation);
        assertThat(lastStation).isEqualTo(downStation);
    }

    @DisplayName("상행역 구간 정보 추가")
    @Test
    void addUpStation() {
        // given
        Station firstStation = new Station(1L, "1번역");
        Station secondStation = new Station(2L, "2번역");
        Station addStation = new Station(3L, "추가역");
        Line line = new Line("신분당선", "red", firstStation, secondStation, 10);
        line.addSection(addStation, firstStation, 10);

        // when
        Station result = line.getSortedStations().get(0);

        // then
        assertThat(result).isEqualTo(addStation);
    }

    @DisplayName("하행역 구간 정보 추가")
    @Test
    void addDownStation() {
        // given
        Station firstStation = new Station(1L, "1번역");
        Station secondStation = new Station(2L, "2번역");
        Station addStation = new Station(3L, "추가역");
        Line line = new Line("신분당선", "red", firstStation, secondStation, 10);
        line.addSection(secondStation, addStation, 10);

        // when
        List<Station> stations = line.getSortedStations();
        Station result = line.getSortedStations().get(stations.size() - 1);

        // then
        assertThat(result).isEqualTo(addStation);
    }

    @DisplayName("상행역 하행역 사이의 지하철 역 추가")
    @Test
    void addBetweenStation() {
        // given
        Station firstStation = new Station(1L, "1번역");
        Station secondStation = new Station(2L, "2번역");
        Station addStation = new Station(3L, "추가역");
        Line line = new Line("신분당선", "red", firstStation, secondStation, 10);
        line.addSection(firstStation, addStation, 5);

        // when
        List<Station> stations = line.getSortedStations();

        // then
        assertThat(stations).contains(addStation);
        assertThat(stations.get(0)).isNotEqualTo(addStation);
        assertThat(stations.get(stations.size() - 1)).isNotEqualTo(addStation);
    }

    @DisplayName("구간에 존재하지 않는 지하철 역 추가")
    @Test
    void invalidAddNotFound() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // given
                Station firstStation = new Station(1L, "1번역");
                Station secondStation = new Station(2L, "2번역");
                Station thirdSection = new Station(3L, "3번역");
                Station fourthStation = new Station(4L, "4번역");
                Line line = new Line("신분당선", "red", firstStation, secondStation, 10);

                //when
                line.addSection(thirdSection, fourthStation, 5);
            }).withMessageMatching("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("이미 등록된 구간 추가")
    @Test
    void invalidAlreadyAdded() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // given
                Station firstStation = new Station(1L, "1번역");
                Station secondStation = new Station(2L, "2번역");
                Line line = new Line("신분당선", "red", firstStation, secondStation, 10);

                //when
                line.addSection(firstStation, secondStation, 5);
            }).withMessageMatching("이미 등록된 구간 입니다.");
    }

    @DisplayName("구간의 지하철 역 삭제")
    @Test
    void remove() {
        // given
        Station firstStation = new Station(1L, "1번역");
        Station secondStation = new Station(2L, "2번역");
        Station thirdStation = new Station(3L, "3번역");
        Line line = new Line("신분당선", "red", firstStation, secondStation, 10);
        line.addSection(secondStation, thirdStation, 5);
        line.removeLineStation(secondStation);

        // when
        List<Station> stations = line.getSortedStations();

        // then
        assertThat(stations).isNotIn(secondStation);
    }

    @DisplayName("더 이상 삭제할 구간이 없는 경우")
    @Test
    void invalidSectionEmpty() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // given
                Station firstStation = new Station(1L, "1번역");
                Station secondStation = new Station(2L, "2번역");
                Line line = new Line("신분당선", "red", firstStation, secondStation, 10);

                //when
                line.removeLineStation(firstStation);
            }).withMessageMatching("더 이상 삭제할 수 없습니다.");
    }

    @DisplayName("삭제할 지하철 역이 없는 경우")
    @Test
    void invalidRemoveNotFound() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // given
                Station firstStation = new Station(1L, "1번역");
                Station secondStation = new Station(2L, "2번역");
                Station thirdStation = new Station(3L, "3번역");
                Station removeStation = new Station(-999L, "없는역");
                Line line = new Line("신분당선", "red", firstStation, secondStation, 10);
                line.addSection(thirdStation, firstStation, 10);
                //when
                line.removeLineStation(removeStation);
            }).withMessageMatching("해당 역은 포함되어 있지 않습니다.");
    }

}
