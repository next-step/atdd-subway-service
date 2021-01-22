package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidAddSectionException;
import nextstep.subway.line.exception.InvalidRemoveSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Station 강남역;
    private Station 광교역;
    private Station 양재역;
    private Station 정자역;

    private Line 신분당선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
        정자역 = new Station("정자역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10, 0);
        sections = 신분당선.getSections();
    }

    @DisplayName("역과 역 사이에 새로운 역을 추가한다.")
    @Test
    void addSection1() {
        // when
        sections.addSection(new Section(신분당선, 양재역, 광교역, new Distance(3)));

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("새로운 종점을 추가한다.")
    @Test
    void addSection2() {
        // when
        sections.addSection(new Section(신분당선, 광교역, 양재역, new Distance(10)));

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 광교역, 양재역));
    }

    @DisplayName("이미 등록되어 있는 구간은 추가하지 못한다.")
    @Test
    void addSection3() {
        assertThatThrownBy(() -> sections.addSection(new Section(신분당선, 강남역, 광교역, new Distance(5))))
                .isInstanceOf(InvalidAddSectionException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("등록하려는 구간에서 둘 중 하나 지하철역이 등록되어 있지 않으면 추가하지 못한다.")
    @Test
    void addSection4() {
        assertThatThrownBy(() -> sections.addSection(new Section(신분당선, 양재역, 정자역, new Distance(5))))
                .isInstanceOf(InvalidAddSectionException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("등록되어 있는 지하철역을 제거한다.")
    @Test
    void removeSection1() {
        // given
        sections.addSection(new Section(신분당선, 양재역, 광교역, new Distance(3)));

        // when
        sections.removeSection(광교역);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역));
    }

    @DisplayName("구간이 한개(등록된 지하철역 2개)있을 경우 지하철역을 제거하지 못한다.")
    @Test
    void removeSection2() {
        assertThatThrownBy(() -> sections.removeSection(강남역))
                .isInstanceOf(InvalidRemoveSectionException.class)
                .hasMessage("지하철역이 2개 등록되어 있어서 역을 제거할 수 없습니다.");
    }
}
