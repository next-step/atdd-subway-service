package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {
    private Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections();

        Station upStation = new Station("판교역");
        Station downStation = new Station("정자역");

        Station upStation2 = new Station("강남역");
        Station downStation2 = new Station("판교역");

        sections.add(new Section(upStation, downStation, 10));
        sections.add(new Section(upStation2, downStation2, 10));
    }

    @Test
    void 상행역에서_하행역까지_모든_역을_가져온다() {
        // given
        Station upStation = new Station("강남역");
        Station downStation = new Station("정자역");
        // when
        List<Station> stations = sections.getStations();
        // then
        assertAll(
                () -> assertThat(stations.get(0)).isEqualTo(upStation),
                () -> assertThat(stations.get(stations.size() - 1)).isEqualTo(downStation)
        );
    }
}
