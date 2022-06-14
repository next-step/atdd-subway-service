package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    private Station upStation;
    private Station downStation;
    Line line;

    @BeforeEach
    public void setUp() {
        upStation = new Station("상행 종점");
        downStation = new Station("하행 종점");
        line = new Line("2호선", "color", upStation, downStation, Distance.from(5));

    }

    @Test
    void 노선의_역_목록() {
        // then
        assertThat(line.getStations()).containsExactly(upStation, downStation);
    }

    @Test
    void 구간_등록() {
        // when
        Station lineUpStation = upStation;
        Station lineDownStation = new Station("구간역");
        line.addSection(lineUpStation, lineDownStation, Distance.from(2));

        // then
        assertThat(line.getStations()).containsExactly(upStation, lineDownStation, downStation);
    }

    @Test
    void 구간_등록_중복_예외() {
        // when
        Station lineUpStation = upStation;
        Station lineDownStation = downStation;

        // then
        assertThatThrownBy(
                () -> line.addSection(lineUpStation, lineDownStation, Distance.from(2))
        ).isInstanceOf(RuntimeException.class);

    }

    @Test
    void 잘못된_구간_등록_예외() {
        // when
        Station lineUpStation = new Station("구간역1");
        Station lineDownStation = new Station("구간역2");

        // then
        assertThatThrownBy(
                () -> line.addSection(lineUpStation, lineDownStation, Distance.from(2))
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 역_제거() {
        // when
        Station station = new Station("구간역");
        line.addSection(upStation, station, Distance.from(2));
        line.removeStation(station);

        // then
        assertThat(line.getStations()).doesNotContain(station);
    }

    @Test
    void 역_제거_예외() {
        // then
        assertThatThrownBy(
                () -> line.removeStation(upStation)
        ).isInstanceOf(RuntimeException.class);
    }
}