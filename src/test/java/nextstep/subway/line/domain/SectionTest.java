package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    @DisplayName("구역에 존재하는 지하철역들을 조회한다.")
    @Test
    void findStationInSection() {
        //given
        Station 양재역 = createStation("양재역");
        Station 양재시민의숲역 = createStation("양재시민의숲역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section = createSection(line, 양재역, 양재시민의숲역, 15);

        //when
        List<Station> stations = section.stations();

        //then
        assertThat(stations).contains(양재역, 양재시민의숲역);
    }
}
