package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionsTest {

    @DisplayName("구간에 포함 된 역 목록 조회")
    @Test
    void getStations() {
        Line line = new Line("2호선", "bg-green-600");
        Station upStation = new Station("신도림역");
        Station middleStation = new Station("신정네거리역");
        Station downStation = new Station("까치산역");

        Sections sections = new Sections(Arrays.asList(new Section(line, upStation, middleStation, 3), new Section(line, middleStation, downStation, 3)) );
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(upStation, middleStation, downStation);
    }
}
