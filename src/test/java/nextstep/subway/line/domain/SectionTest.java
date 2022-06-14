package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionTest {

    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line.Builder("2호선", "color").build();
    }

    @Test
    void 상행역과_상행역_비교() {
        Station upStation = new Station("상행역");
        Station downStation = new Station("하행역");
        Section section = new Section(line, upStation, downStation, Distance.from(5));

        assertAll(
                () -> assertThat(section.isSameUpStation(
                        new Section(line, upStation, new Station("역"), Distance.from(5)))).isEqualTo(
                        true),
                () -> assertThat(
                        section.isSameUpStation(
                                new Section(line, new Station("역"), downStation, Distance.from(5)))).isEqualTo(false)
        );
    }

    @Test
    void 상행역과_역을_비교() {
        Station upStation = new Station("상행역");
        Station downStation = new Station("하행역");
        Section section = new Section(line, upStation, downStation, Distance.from(5));

        assertAll(
                () -> assertThat(section.isSameUpStation(upStation)).isEqualTo(true),
                () -> assertThat(section.isSameUpStation(downStation)).isEqualTo(false)
        );
    }

    @Test
    void 하행역과_하행역_비교() {
        Station upStation = new Station("상행역");
        Station downStation = new Station("하행역");
        Section section = new Section(line, upStation, downStation, Distance.from(5));

        assertAll(
                () -> assertThat(
                        section.isSameDownStation(
                                new Section(line, new Station("역"), downStation, Distance.from(5)))).isEqualTo(true),
                () -> assertThat(
                        section.isSameDownStation(
                                new Section(line, upStation, new Station("역"), Distance.from(5)))).isEqualTo(false)
        );

    }

    @Test
    void 하행역과_역을_비교() {
        Station upStation = new Station("상행역");
        Station downStation = new Station("하행역");
        Section section = new Section(line, upStation, downStation, Distance.from(5));

        assertAll(
                () -> assertThat(section.isSameDownStation(downStation)).isEqualTo(true),
                () -> assertThat(section.isSameDownStation(upStation)).isEqualTo(false)
        );
    }
}