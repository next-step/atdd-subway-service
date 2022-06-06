package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class StationsTest {
    private List<Section> sections;

    @BeforeEach
    void setUp() {
        sections = new ArrayList<>();

        sections.add(new Section(new Station("동천역"), new Station("광교역"), 10));
        sections.add(new Section(new Station("판교역"), new Station("정자역"), 10));
        sections.add(new Section(new Station("정자역"), new Station("동천역"), 10));
        sections.add(new Section(new Station("강남역"), new Station("판교역"), 10));
    }

    @Test
    void 역을_순서대로_연결한다() {
        // given
        Stations stations = new Stations();
        stations.connectStation(sections);

        // when
        List<Station> result = stations.getElements();

        // then
        assertAll(
                () -> assertThat(result)
                        .hasSize(5),
                () -> assertThat(result.get(0))
                        .isEqualTo(new Station("강남역")),
                () -> assertThat(result.get(result.size() - 1))
                        .isEqualTo(new Station("광교역"))
        );
    }

    @Test
    void 역_연결에_대한_유효성_검사() {
        // given
        Stations stations = new Stations();

        // when & then
        assertThatThrownBy(() ->
                stations.connectStation(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
