package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 서초역 = new Station("서초역");
    private static final Station 반포역 = new Station("반포역");

    @DisplayName("노선을 변경한다.")
    @Test
    void update() {
        //given
        String 변경이름 = "신분당선";
        String 변경색 = "bg-red-500";
        Line line = new Line("분당선", "bg-green-600");

        //when
        line.update(변경이름, 변경색);

        //then
        assertAll(
                () -> assertEquals(변경이름, line.getName()),
                () -> assertEquals(변경색, line.getColor())
        );
    }

    @DisplayName("노선에 구간을 추가한다.")
    @Test
    void addSection() {
        //given
        Line line = new Line("신분당선", "bg-red-600");
        line.addSection(강남역, 서초역, 3);
        //when
        line.addSection(서초역, 반포역, 3);
        //then
        assertThat(line.getStations()).containsExactly(강남역, 서초역, 반포역);
    }

    @DisplayName("노선의 역을 구한다.")
    @Test
    void getStation() {
        //given
        Line line = new Line("신분당선", "bg-red-600");
        line.addSection(강남역, 서초역, 3);
        //when
        List<Station> stations = line.getStations();
        //then
        assertThat(stations).containsExactly(강남역, 서초역);
    }
}