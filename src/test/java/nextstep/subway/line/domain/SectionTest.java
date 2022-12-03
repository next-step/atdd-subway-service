package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionTest {
    private Station upStation;
    private Station downStation;
    private Line line;
    private Section section;

    @BeforeEach
    void setUp() {
        upStation = new Station("강남역");
        downStation = new Station("판교역");
        line = Line.builder()
                .name("신분당선")
                .color("red")
                .upStation(upStation)
                .downStation(downStation)
                .distance(5)
                .build();
        section = new Section(line, upStation, downStation, 15);
    }

    @DisplayName("거리가 10인 구간을 생성한다.")
    @Test
    void create() {
        assertAll(() -> {
            assertThat(section.getUpStation()).isEqualTo(new Station("강남역"));
            assertThat(section.getDownStation()).isEqualTo(new Station("판교역"));
            assertThat(section.getDistance().value()).isEqualTo(15);
        });
    }

    @DisplayName("구간에 상행역과 거리를 수정한다.")
    @Test
    void updateUpStation() {
        section.updateUpStation(new Station("판교역"), 7);
        assertThat(section.getUpStation()).isEqualTo(new Station("판교역"));
        assertThat(section.getDistance()).isEqualTo(new Distance(8));
    }

    @DisplayName("구간에 하행역과 거리를 수정한다.")
    @Test
    void updateDownStation() {
        section.updateDownStation(new Station("판교역"), 7);
        assertThat(section.getUpStation()).isEqualTo(new Station("강남역"));
        assertThat(section.getDistance()).isEqualTo(new Distance(8));
    }

    @DisplayName("구간 거리를 더한다.")
    @Test
    void plusDistance() {
        assertThat(section.plus(new Distance(5))).isEqualTo(20);
    }
}
