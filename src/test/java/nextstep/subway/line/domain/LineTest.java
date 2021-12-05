package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineTest {

    @DisplayName("노선에 속한 역 목록 조회")
    @Test
    void findAllStationInLine() {
        //given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        //when
        final List<Station> stations = line.getStations();

        //then
        assertThat(stations).extracting("name").containsExactly("강남역","광교역");
    }


    @DisplayName("역 사이에 새로운 역 추가")
    @Test
    void addStationBetweenStations() {
        // given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        line.addSection(upStation, new Station("양재역"), 5);

        // then
        final List<Station> stations = line.getStations();
        assertThat(stations).extracting("name").containsExactly("강남역","양재역","광교역");
    }

    @DisplayName("새로운 역을 상행 종점으로 등록")
    @Test
    void addUpStation() {
        // given
        final Station upStation = new Station("양재역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        line.addSection(new Station("강남역"), upStation, 5);

        // then
        final List<Station> stations = line.getStations();
        assertThat(stations).extracting("name").containsExactly("강남역","양재역","광교역");
    }

    @DisplayName("새로운 역을 하행 종점으로 등록")
    @Test
    void addDownStation() {
        // given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("양재역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        line.addSection(downStation, new Station("광교역"), 5);

        // then
        final List<Station> stations = line.getStations();
        assertThat(stations).extracting("name").containsExactly("강남역","양재역","광교역");
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록 불가")
    @Test
    void distanceExcessException() {
        // given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        assertThrows(RuntimeException.class, () -> {
            line.addSection(upStation, new Station("양재역"), 10);
        });
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 등록 불가")
    @Test
    void upStationAndDownStationExistException() {
        // given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        assertThrows(RuntimeException.class, () -> {
            line.addSection(upStation, downStation, 5);
        });
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어 있지 않다면 추가 불가")
    @Test
    void upStationOrDownStationNotExistException() {
        // given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        assertThrows(RuntimeException.class, () -> {
            line.addSection(new Station("판교"), new Station("정자"), 5);
        });
    }
}