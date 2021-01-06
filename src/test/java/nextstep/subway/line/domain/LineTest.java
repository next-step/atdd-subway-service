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

class LineTest {
    private Station 강남역;
    private Station 광교역;
    private Station 양재역;

    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("역과 역 사이에 새로운 역을 추가한다.")
    @Test
    void addSection1() {
        // when
        신분당선.addSection(new Section(신분당선, 양재역, 광교역, new Distance(3)));

        //then
        assertThat(신분당선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("새로운 종점을 추가한다.")
    @Test
    void addSection2() {
        // when
        신분당선.addSection(new Section(신분당선, 광교역, 양재역, new Distance(3)));

        //then
        assertThat(신분당선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 광교역, 양재역));
    }

    @DisplayName("이미 등록되어 있는 역은 추가하지 못한다.")
    @Test
    void addSection3() {
        assertThatThrownBy(() -> 신분당선.addSection(new Section(신분당선, 강남역, 광교역, new Distance(3))))
                .isInstanceOf(InvalidAddSectionException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("등록되어 있는 지하철역을 제거한다.")
    @Test
    void removeSection1() {
        // given
        신분당선.addSection(new Section(신분당선, 양재역, 광교역, new Distance(3)));

        // when
        신분당선.removeSection(양재역);

        //then
        assertThat(신분당선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 광교역));
    }

    @DisplayName("등록되어 있는 지하철역이 2개일 경우, 지하철역을 제거할 수 없다.")
    @Test
    void removeSection2() {
        assertThatThrownBy(() -> 신분당선.removeSection(강남역))
                .isInstanceOf(InvalidRemoveSectionException.class)
                .hasMessage("지하철역이 2개 등록되어 있어서 역을 제거할 수 없습니다.");
    }
}
