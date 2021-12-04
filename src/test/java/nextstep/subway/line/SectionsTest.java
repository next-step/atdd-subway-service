package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.*;

class SectionsTest {
    private final Line line = new Line("신분당선", "bg-red-600");
    private final Station 강남역 = Station.from("강남역");
    private final Station 광교역 = Station.from("광교역");
    private final Station 양재역 = Station.from("양재역");

    private final Distance oneDistance = Distance.from(1);
    private final Distance distance = Distance.from(10);
    private final Section section1 = Section.of(line, 강남역, 광교역, distance);
    private final Section section2 = Section.of(line, 양재역, 광교역, oneDistance);

    @DisplayName("Section객체의 배열(콜렉션)을 인자로 하는 정적팩터리 메서드를 호출하면 Sections 객체를 반환한다.")
    @Test
    void staticFactoryMethodTest() {
        assertThat(Sections.from(Arrays.asList(section1, section2))).isInstanceOf(Sections.class);
    }

    @DisplayName("Section객체 추가하는 메서드를 호출하면 Section이 추가된다.")
    @Test
    void addSectionTest() {
        line.addSection(section1.getUpStation(), section1.getDownStation(), section1.getDistance());
        line.addSection(section2.getUpStation(), section2.getDownStation(), section2.getDistance());
        Sections sections = line.sections();
        assertThat(sections.sections().size()).isEqualTo(2);
    }

    @DisplayName("stations메서드를 호출하면 역 목록이 담긴 콜렉션 객체를 반환한다.")
    @Test
    void removeTest() {
        line.addSection(강남역, 광교역, distance);
        line.addSection(양재역, 광교역, oneDistance);
        Sections sections = line.sections();
        sections.removeLineStation(line, 양재역);
        assertThat(sections.stations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("stations메서드를 호출하면 역 목록이 담긴 콜렉션 객체를 반환한다.")
    @Test
    void stationsTest() {
        line.addSection(강남역, 광교역, distance);
        line.addSection(양재역, 광교역, oneDistance);
        Sections sections = line.sections();
        assertThat(sections.stations()).containsExactly(강남역, 양재역, 광교역);
    }
}
