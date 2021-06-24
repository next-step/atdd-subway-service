package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

    private Line 신분당선 = new Line("신분당선", "red");
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 청계산역 = new Station("청계산역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    @DisplayName("구간 추가 테스트")
    @Test
    void add() {
        // given
        Sections sections = new Sections();
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        Section 양재_판교 = new Section(신분당선, 양재역, 판교역, 20);
        sections.add(강남_양재);
        sections.add(양재_판교);

        // when
        Section 양재_청계산 = new Section(신분당선, 양재역, 청계산역, 15);
        sections.add(양재_청계산);

        // then
        List<Station> stations = sections.stations();
        assertThat(stations.size()).isEqualTo(4);
        assertThat(stations).isEqualTo(Arrays.asList(강남역, 양재역, 청계산역, 판교역));
    }

    @DisplayName("구간추가 테스트 - 기존 노선에 존재하지 않는 구간 추가")
    @Test
    void add_anyStationsAreNotExist() {
        // given
        Sections sections = new Sections();
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        Section 양재_판교 = new Section(신분당선, 양재역, 판교역, 20);
        sections.add(강남_양재);
        sections.add(양재_판교);

        // when then
        Section 정자_청계산 = new Section(신분당선, 정자역, 청계산역, 15);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.add(정자_청계산))
                .withMessageMatching("구간 추가를 위해서는 기존 구간과의 연결점이 필요합니다. 역 정보를 확인해주세요.");
    }

    @DisplayName("역 제거 테스트")
    @Test
    void remove() {
        // given
        Sections sections = new Sections();
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        Section 양재_판교 = new Section(신분당선, 양재역, 판교역, 20);
        Section 양재_청계산 = new Section(신분당선, 양재역, 청계산역, 15);
        sections.add(강남_양재);
        sections.add(양재_판교);
        sections.add(양재_청계산);

        // when
        sections.remove(신분당선, 청계산역);

        // then
        List<Station> stations = sections.stations();
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations).isEqualTo(Arrays.asList(강남역, 양재역, 판교역));
    }

    @DisplayName("역 제거 테스트 - 빈 구간 또는 구간이 하나일 때는 역 제거 불가")
    @Test
    void remove_emptyOrJustOneSection() {
        // given
        Sections 빈구간 = new Sections();
        Sections 단일구간 = new Sections();
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        단일구간.add(강남_양재);

        // when then
        assertAll(
                () -> assertThatIllegalStateException()
                        .isThrownBy(() -> 빈구간.remove(신분당선, 정자역))
                        .withMessageMatching("구간의 갯수가 0 입니다. 삭제할 수 없습니다."),
                () -> assertThatIllegalStateException()
                        .isThrownBy(() -> 단일구간.remove(신분당선, 강남역))
                        .withMessageMatching("구간의 갯수가 1 입니다. 삭제할 수 없습니다.")
        );
    }

    @DisplayName("구간에 존재하지 않는 역은 삭제불가")
    @Test
    void removeStationIsNotExists() {
        // given
        Sections sections = new Sections();
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        Section 양재_판교 = new Section(신분당선, 양재역, 판교역, 20);
        sections.add(강남_양재);
        sections.add(양재_판교);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.remove(신분당선, 정자역))
                .withMessageMatching("구간에 존재하지 않는 역입니다. 삭제하고자 하는 역 정보를 확인해 주세요.");
    }
}