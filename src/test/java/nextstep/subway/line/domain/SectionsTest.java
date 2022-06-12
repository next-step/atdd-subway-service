package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    private final static Line 이호선 = new Line("2호선", "green");
    private final static Station 강남역 = new Station("강남역");
    private final static Station 삼성역 = new Station("삼성역");
    private final static Station 잠실역 = new Station("잠실역");
    private final static Station 시청역 = new Station("시청역");
    private final static Station 왕십리역 = new Station("왕십리역");
    private final static Section 강남역_삼성역 = new Section(이호선, 강남역, 삼성역, 5);
    private final static Section 삼성역_잠실역 = new Section(이호선, 삼성역, 잠실역, 5);

    @Test
    @DisplayName("특정 노선의 역 정보들을 상행종착역 부터 하행종착역 순서대로 출력한다")
    void getStations() {
        // given
        final Sections 이호선구간 = new Sections();
        이호선구간.addSection(강남역_삼성역);
        이호선구간.addSection(삼성역_잠실역);

        // when
        List<Station> actual = 이호선구간.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 삼성역, 잠실역);
    }

    @Test
    @DisplayName("특정 노선의 구간 정보들을 상행종착역 부터 하행종착역 순서대로 출력한다")
    void getSections() {
        // given
        final Sections sections = new Sections();
        sections.addSection(강남역_삼성역);
        sections.addSection(삼성역_잠실역);

        // when
        final List<Section> sectionList = sections.getSections();

        // then
        assertThat(sectionList).containsExactly(강남역_삼성역, 삼성역_잠실역);
    }

    @Test
    @DisplayName("이미 등록된 역 사이에 구간 등록시 구간의 길이와 같거나 긴 구간을 추가할 수 없다")
    void addSectionInvalidDistanceError() {
        // given
        final Sections 이호선_구간 = new Sections();
        final Section 강남역_잠실역 = new Section(이호선, 강남역, 잠실역, 5);
        이호선_구간.addSection(강남역_잠실역);
        final Section 삼성역_잠실역 = new Section(이호선, 삼성역, 잠실역, 5);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> 이호선_구간.addSection(삼성역_잠실역)
        );
    }

    @Test
    @DisplayName("동일한 구간을 다시 추가할 수 없다")
    void addSectionSameSectionError() {
        // given
        final Sections 이호선_구간 = new Sections();
        이호선_구간.addSection(삼성역_잠실역);
        final Section 삼성역_잠실역 = new Section(이호선, 삼성역, 잠실역, 5);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> 이호선_구간.addSection(삼성역_잠실역)
        );
    }

    @Test
    @DisplayName("기존 구간에 어떤 역도 속하지 않는 구간은 추가할 수 없다")
    void addSectionNonExistSectionError() {
        // given
        final Sections 이호선_구간 = new Sections();
        이호선_구간.addSection(강남역_삼성역);
        이호선_구간.addSection(삼성역_잠실역);

        final Section 시청역_왕십리역 = new Section(이호선, 시청역, 왕십리역, 5);

        // when& then
        assertThatIllegalArgumentException().isThrownBy(
                () -> 이호선_구간.addSection(시청역_왕십리역)
        );
    }

    @Test
    @DisplayName("기존 구간에서 시작역을 삭제할 수 있다")
    void deleteFirstStation() {
        // given
        final Sections 이호선_구간 = new Sections();
        이호선_구간.addSection(강남역_삼성역);
        이호선_구간.addSection(삼성역_잠실역);

        // when
        이호선_구간.deleteSection(강남역, 이호선);

        // then
        assertThat(이호선_구간.getStations()).containsExactly(삼성역, 잠실역);
    }

    @Test
    @DisplayName("기존 구간에서 종착역을 삭제할 수 있다")
    void deleteLastStation() {
        // given
        final Sections 이호선_구간 = new Sections();
        이호선_구간.addSection(강남역_삼성역);
        이호선_구간.addSection(삼성역_잠실역);

        // when
        이호선_구간.deleteSection(잠실역, 이호선);

        // then
        assertThat(이호선_구간.getStations()).containsExactly(강남역, 삼성역);
    }

    @Test
    @DisplayName("기존 구간에서 중간역을 삭제할 수 있다")
    void deleteMiddleStation() {
        // given
        final Sections 이호선_구간 = new Sections();
        이호선_구간.addSection(강남역_삼성역);
        이호선_구간.addSection(삼성역_잠실역);

        // when
        이호선_구간.deleteSection(삼성역, 이호선);

        // then
        assertThat(이호선_구간.getStations()).containsExactly(강남역, 잠실역);
    }

    @Test
    @DisplayName("현재 남아있는 구간이 하나인 경우 삭제할 수 없다")
    void lastSectionDeleteError() {
        // given
        final Sections 이호선_구간 = new Sections();
        이호선_구간.addSection(강남역_삼성역);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> 이호선_구간.deleteSection(강남역, 이호선)
        );
    }
}
