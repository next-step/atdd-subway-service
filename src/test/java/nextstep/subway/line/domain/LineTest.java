package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DuplicatedSectionException;
import nextstep.subway.line.exception.NoneMatchSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    Line _2호선;
    Station 강남역;
    Station 역삼역;
    Station 선릉역;
    Station 양재역;
    Station 사당역;

    @BeforeEach
    public void setUp() {
        _2호선 = new Line();
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        양재역 = new Station("양재역");
        사당역 = new Station("사당역");
        Section section1 = new Section(_2호선, 강남역, 역삼역, 10);
        Section section2 = new Section(_2호선, 역삼역, 선릉역, 10);
        _2호선.getSections().add(section1);
        _2호선.getSections().add(section2);
    }

    @DisplayName("가장 상행역을 구한다")
    @Test
    void findUpStationTest() {
        assertThat(_2호선.findUpStation()).isEqualTo(강남역);
    }

    @DisplayName("역을 순서대로 구한다")
    @Test
    void getStationsTest() {
        assertThat(_2호선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 역삼역, 선릉역));
    }

    @DisplayName("역을 제거한다")
    @Test
    void removeLineStationTest() {
        _2호선.removeLineStation(역삼역);
        assertThat(_2호선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 선릉역));
        assertThat(getDistances(_2호선)
                .containsAll(Arrays.asList(20)));
    }

    @DisplayName("구간을 추가한다")
    @Test
    void addStationTest() {
        _2호선.addSection(new Section(양재역, 강남역, 4));
        assertThat(_2호선.getStations()).containsExactlyElementsOf(Arrays.asList(양재역, 강남역, 역삼역, 선릉역));
        assertThat(getDistances(_2호선)
                .containsAll(Arrays.asList(4, 10, 10)));
    }

    @DisplayName("상행이 같은 구간을 추가한다")
    @Test
    void addStationTest2() {
        _2호선.addSection(new Section(강남역, 양재역, 4));
        assertThat(_2호선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 역삼역, 선릉역));
        assertThat(getDistances(_2호선)
                .containsAll(Arrays.asList(6, 4, 10)));
    }

    @DisplayName("이미 등록된 구간을 추가한다")
    @Test
    void addDuplicatedSectionTest() {
        assertThatThrownBy(() ->
                _2호선.addSection(new Section(강남역, 역삼역, 4)))
                .isInstanceOf(DuplicatedSectionException.class);
    }

    @DisplayName("역이 없는 구간을 추가한다")
    @Test
    void addNoneMatchStationTest() {
        assertThatThrownBy(() ->
                _2호선.addSection(new Section(사당역, 양재역, 4)))
                .isInstanceOf(NoneMatchSectionException.class);
    }

    private List<Integer> getDistances(Line line) {
        return line.getSections().getDistances();
    }
}