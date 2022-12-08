package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    Station station1;
    Station station2;
    Line line;

    @BeforeEach
    void beforeEach(){
        station1 = new Station("강남역");
        station2 = new Station("잠실역");
        line = new Line("2호선", "green", station1, station2, 20, 1000);
    }

    @Test
    @DisplayName("중간 구간 추가 성공 테스트")
    void addSection(){
        // given - beforeEach

        // when
        Station newStation = new Station("종합운동장역");
        line.addSection(station1, newStation, 10);

        // then
        assertThat(line.getStations()).containsExactly(
                station1, newStation, station2
        );
    }

    @Test
    @DisplayName("상행 종점 추가 성공 테스트")
    void addSection_upStation(){
        // given - beforeEach

        // when
        Station newStation = new Station("사당역");
        line.addSection(newStation, station1, 30);

        // then
        assertThat(line.getStations()).containsExactly(
                newStation, station1, station2
        );
    }

    @Test
    @DisplayName("하행 종점 추가 성공 테스트")
    void addSection_downStation(){
        // given - beforeEach

        // when
        Station newStation = new Station("성수역");
        line.addSection(station2, newStation, 30);

        // then
        assertThat(line.getStations()).containsExactly(
                station1, station2, newStation
        );
    }

    @Test
    @DisplayName("구간 추가 실패 - 이미 존재하는 역들로는 구간을 추가할 수 없다.")
    void addSection_allStationExistsException(){
        // given - beforeEach

        // when

        // then
        assertThatThrownBy(
                () -> line.addSection(station1, station2, 30)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구간 추가 실패 - 존재하지 않는 역들로는 구간을 추가할 수 없다.")
    void addSection_stationNotExistsException(){
        // given - beforeEach

        // when
        Station newStation1 = new Station("시청역");
        Station newStation2 = new Station("왕십리역");

        // then
        assertThatThrownBy(
                () -> line.addSection(newStation1, newStation2, 30)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("중간 구간 제거 성공 테스트")
    void removeSection(){
        // given - beforeEach
        Station newStation = new Station("왕십리역");
        line.addSection(station2, newStation, 30);

        // when
        line.removeSection(station2);

        // then
        assertThat(line.getStations()).containsExactly(
                station1, newStation
        );
    }

    @Test
    @DisplayName("상행 종점 제거 성공 테스트")
    void removeSection_upStation(){
        // given - beforeEach
        Station newStation = new Station("왕십리역");
        line.addSection(station2, newStation, 30);

        // when
        line.removeSection(station1);

        // then
        assertThat(line.getStations()).containsExactly(
                station2, newStation
        );
    }

    @Test
    @DisplayName("하행 종점 제거 성공 테스트")
    void removeSection_downStation(){
        // given - beforeEach
        Station newStation = new Station("왕십리역");
        line.addSection(station2, newStation, 30);

        // when
        line.removeSection(newStation);

        // then
        assertThat(line.getStations()).containsExactly(
                station1, station2
        );
    }

    @Test
    @DisplayName("구간 제거 실패 테스트 - 마지막 구간만 남은 경우 해당 구간을 제거할 수 없다.")
    void removeSectionException_lastSection(){
        // given - beforeEach

        // when

        // then
        assertThatThrownBy(
                () -> line.removeSection(station2)
        ).isInstanceOf(RuntimeException.class);
    }
}
