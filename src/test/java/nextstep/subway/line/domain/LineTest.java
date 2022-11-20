package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    @DisplayName("노선을 생성하면, 해당 노선 내에 존재하는 지하철역들을 조회할 수 있다.")
    @Test
    void createNewLine() {
        //given
        Station 양재역 = createStation("양재역");

        //when
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);

        //then
        assertThat(line.findStations()).hasSize(2);
    }
}
