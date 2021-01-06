package nextstep.subway.line.domain;

import nextstep.subway.line.exceptions.SectionsException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간을 관리한다.")
class SectionsTest {
    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;
    private Station 양재역;
    private Station 청계산입구역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "pink");
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
        청계산입구역 = new Station("청계산입구역");
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void add() {
        // when
        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Section section2 = new Section(신분당선, 양재역, 광교역, 5);
        Sections sections = new Sections();
        sections.add(section1);
        sections.add(section2);

        // then
        assertThat(sections.getOrderedStations())
                .extracting("name")
                .containsExactly("강남역", "양재역", "광교역");
    }

    @DisplayName("구간 추가 예외 - 이미 등록된 구간")
    @Test
    void add_exception1() {
        // given
        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Sections sections = new Sections();
        sections.add(section1);

        // when, then
        assertThatThrownBy(() -> {
            Section section2 = new Section(신분당선, 강남역, 광교역, 5);
            sections.add(section2);
        }).isInstanceOf(SectionsException.class).hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("구간 추가 예외 - 상행/하행 어디에도 속하지 않는 역이 포함된 경우 ")
    @Test
    void add_exception2() {
        // given
        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Sections sections = new Sections();
        sections.add(section1);

        // when, then
        assertThatThrownBy(() -> {
            Section section2 = new Section(신분당선, 양재역, 청계산입구역, 5);
            sections.add(section2);
        }).isInstanceOf(SectionsException.class).hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간을 제거한다.")
    @Test
    void remove() {
        // given
        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Section section2 = new Section(신분당선, 양재역, 광교역, 5);
        Sections sections = new Sections();
        sections.add(section1);
        sections.add(section2);

        // when
        sections.remove(양재역);

        // then
        assertThat(sections.getOrderedStations())
                .extracting("name")
                .containsExactly("강남역", "광교역");
    }

    @DisplayName("구간 제거 예외 - 마지막 구간이어서 더이상 삭제 불가")
    @Test
    void remove_exception1() {
        // given
        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Sections sections = new Sections();
        sections.add(section1);

        // when, then
        assertThatThrownBy(() -> {
            sections.remove(강남역);
        }).isInstanceOf(SectionsException.class).hasMessage("마지막 구간입니다.");
    }

}