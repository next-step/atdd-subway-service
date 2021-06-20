package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LineTest {
    @Test
    @DisplayName("상행역에서 하행역 순으로 정렬된 역 목록 조회")
    void getStations() {
        // given
        Station 강남역 = new Station(1L, "강남역");
        Station 판교역 = new Station(2L, "판교역");
        Station 정자역 = new Station(3L, "정자역");
        Station 광교역 = new Station(4L, "광교역");

        Line line = new Line("신논현선", "red", 강남역, 판교역, 5);
        line.getSections().add(new Section(line, 판교역, 정자역, 2));
        line.getSections().add(new Section(line, 정자역, 광교역, 5));

        // when
        List<Station> stations = line.getStations();

        // then
        List<Station> orderedStations = Arrays.asList(강남역, 판교역, 정자역, 광교역);
        assertThat(stations).isEqualTo(orderedStations);
    }
}
