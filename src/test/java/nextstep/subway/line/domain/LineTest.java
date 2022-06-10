package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    @Test
    void 노선의_역_목록() {
        // when
        Station upStation = new Station("상행 종점");
        Station downStation = new Station("하행 종점");
        Line line = new Line("2호선", "color", upStation, downStation, 5);

        // then
        assertThat(line.getStations()).containsExactly(upStation, downStation);
    }
}