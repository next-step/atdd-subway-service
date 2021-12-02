package nextstep.subway.line;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest extends AcceptanceTest {
    private final Line line = new Line("신분당선", "bg-red-600");
    private final Station 강남역 = Station.from("강남역");
    private final Station 광교역 = Station.from("광교역");
    private final Station 양재역 = Station.from("양재역");
    private final int distance = 10;
    private final Section section = Section.of(line, 강남역, 광교역, distance);

    @DisplayName("디폴트 생성자를 호출하면 Section 객체를 반환한다.")
    @Test
    void constructTest1() {
        assertThat(new Section()).isInstanceOf(Section.class);
    }

    @DisplayName("라인, 상행역, 하행역, 거리를 인자로 하는 생성자를 호출하면 Section 객체를 반환한다.")
    @Test
    void constructTest2() {
        assertThat(new Section(line, 강남역, 광교역, distance)).isInstanceOf(Section.class);
    }

    @DisplayName("라인, 상행역, 하행역, 거리를 인자로 하는 정적팩터리 메서드를 호출하면 Section 객체를 반환한다.")
    @Test
    void staticFactoryMethodTest1() {
        assertThat(Section.of(line, 강남역, 광교역, distance)).isInstanceOf(Section.class);
    }

    @DisplayName("구간의 상행역을 수정하는 updateUpStation함수를 호출하면 상행역을 변경하며, 기존 거리에서 새로운 거리를 제외한 거리로 변경한다.")
    @Test
    void updateUpStationTest() {
        section.updateUpStation(양재역, 1);

        assertThat(section.getUpStation()).isEqualTo(양재역);
        assertThat(section.getDownStation()).isEqualTo(광교역);
        assertThat(section.getDistance()).isEqualTo(distance - 1);
    }

    @DisplayName("구간의 상행역을 수정하는 updateDownStation함수를 호출하면 상행역을 변경하며, 기존 거리에서 새로운 거리를 제외한 거리로 변경한다.")
    @Test
    void updateDownStationTest() {
        section.updateDownStation(양재역, 1);

        assertThat(section.getUpStation()).isEqualTo(강남역);
        assertThat(section.getDownStation()).isEqualTo(양재역);
        assertThat(section.getDistance()).isEqualTo(distance - 1);
    }

}
