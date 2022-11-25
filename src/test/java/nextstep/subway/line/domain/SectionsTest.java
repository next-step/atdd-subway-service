package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionsTest {
    private Station 서초역;
    private Station 강남역;
    private Sections sections;

    @BeforeEach
    void setUp() {
        서초역 = new Station("서초역");
        강남역 = new Station("강남역");
        sections = new Sections(Collections.singletonList(new Section(서초역, 강남역, 10)));
    }

    @DisplayName("구간 목록에 할당된 지하철 역을 조회할 수 있다.")
    @Test
    void assignedStations() {
        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 강남역);
    }

    @DisplayName("역 사이에 새로운 역을 등록하고 할당된 지하철 역을 조회 시 추가한 역이 조회된다.")
    @Test
    void addSectionBetweenStations() {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, 5);

        sections.add(newSection);

        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 교대역, 강남역);
    }

    @DisplayName("새로운 역을 상행 종점으로 등록하고 할당된 지하철 역을 조회 시 추가한 역이 조회된다.")
    @Test
    void addSectionHeadOfLine() {
        Station 방배역 = new Station("방배역");
        Section newSection = new Section(방배역, 서초역, 5);

        sections.add(newSection);

        assertThat(sections.assignedOrderedStation()).containsExactly(방배역, 서초역, 강남역);
    }

    @DisplayName("새로운 역을 하행 종점으로 등록하고 할당된 지하철 역을 조회 시 추가한 역이 조회된다.")
    @Test
    void addSectionEndOfLine() {
        Station 역삼역 = new Station("역삼역");
        Section newSection = new Section(강남역, 역삼역, 5);

        sections.add(newSection);

        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 강남역, 역삼역);
    }

    @DisplayName("이미 노선에 포함된 상행역과 하행역을 등록할 경우 예외가 발생한다.")
    @Test
    void addSectionExistException() {
        Section newSection = new Section(서초역, 강남역, 5);

        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 노선에 등록되어 있습니다.");
    }

    @DisplayName("구간의 길이가 기존 구간의 길이보다 크거나 같은 구간을 등록할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void addSectionDistanceException(int distance) {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, distance);

        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0이하의 값일 수 없습니다.");
    }

    @DisplayName("상행역과 하행역 모두 노선에 포함되지 않은 구간을 등록할 경우 예외가 발생한다.")
    @Test
    void addSectionNotIncludeException() {
        Station 뚝섬역 = new Station("뚝섬역");
        Station 성수역 = new Station("뚝섬역");
        Section newSection = new Section(뚝섬역, 성수역, 5);

        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역 모두 노선에 포함되어 있지 않습니다.");
    }

    @DisplayName("구간의 가운데 역을 제거한 뒤 지하철 역 조회 시 제거된 역이 조회되지 않는다.")
    @Test
    void deleteMiddleStation() {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, 5);
        sections.add(newSection);
        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 교대역, 강남역);

        sections.delete(교대역);

        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 강남역);
    }

    @DisplayName("노선의 상행 종점을 제거한 뒤 지하철 역 조회 시 제거한 역이 조회되지 않는다.")
    @Test
    void deleteStationHeadOfLine() {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, 5);
        sections.add(newSection);
        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 교대역, 강남역);

        sections.delete(서초역);

        assertThat(sections.assignedOrderedStation()).containsExactly(교대역, 강남역);
    }

    @DisplayName("노선의 하행 종점을 제거한 뒤 지하철 역 조회 시 제거한 역이 조회되지 않는다.")
    @Test
    void deleteStationEndOfLine() {
        Station 교대역 = new Station("교대역");
        Section newSection = new Section(서초역, 교대역, 5);
        sections.add(newSection);
        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 교대역, 강남역);

        sections.delete(강남역);

        assertThat(sections.assignedOrderedStation()).containsExactly(서초역, 교대역);
    }

    @DisplayName("하나의 구간만 존재하는 노선의 역을 제거 할 경우 예외가 발생한다.")
    @Test
    void deleteStationOneSectionException() {
        assertThatThrownBy(() -> sections.delete(서초역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 마지막 구간은 삭제할 수 없습니다.");
    }

    @DisplayName("노선에 포함되지 않은 역을 제거하려는 경우 예외가 발생한다.")
    @Test
    void deleteStationNotIncludeLine() {
        Station 성수역 = new Station("성수역");

        assertThatThrownBy(() -> sections.delete(성수역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 포함되지 않은 지하철 역은 삭제할 수 없습니다.");
    }
}
