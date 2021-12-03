package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    @Test
    @DisplayName("라인에 포함된 역목록 정렬되어 조회")
    void getStations() {
        //given
        Station 서울역 = new Station("서울역");
        Station 용산역 = new Station("용산역");
        Line line = Line.of("1호선", "blue", 서울역, 용산역, 3);

        //when
        List<Station> stations = line.getStations();

        //then
        assertThat(stations).extracting(Station::getName).containsExactly("서울역", "용산역");
    }


}
