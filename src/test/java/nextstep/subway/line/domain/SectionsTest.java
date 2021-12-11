package nextstep.subway.line.domain;

import nextstep.subway.common.exception.*;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    private List<Section> 구간목록 = new ArrayList<>();
    private Line 이호선 = null;
    private static Section 첫번째구간 = null;
    private static Station 잠실역 = null;
    private static Station 강변역 = null;
    private static Distance 거리10 = null;
    private static Distance 거리5 = null;

    @BeforeEach
    void setUp() {
        거리5 = Distance.of(5);
        거리10 = Distance.of(10);
        잠실역 = Station.from("잠실역");
        강변역 = Station.from("강변역");
        첫번째구간 = Section.of(이호선, 잠실역, 강변역, 거리10);
        구간목록.add(첫번째구간);
        이호선 = Line.of("2호선", "green", Sections.from(구간목록));
    }

    @DisplayName("상행역을 추가한다.")
    @Test
    void add_상행역() {
        final Station 잠실새내역 = Station.from("잠실새내역");
        final Section 두번째구간 = Section.of(이호선, 잠실새내역, 잠실역, 거리10);

        final Sections sections = Sections.from(이호선.getSectionList());

        sections.add(두번째구간, 이호선);

        assertAll(
                () -> assertThat(sections.getSections()).hasSize(2),
                () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역)
        );
    }

    @DisplayName("하행역을 추가한다.")
    @Test
    void add_하행역() {
        final Station 잠실나루역 = Station.from("잠실나루역");
        final Section 두번째구간 = Section.of(이호선, 강변역, 잠실나루역, 거리10);

        final Sections sections = Sections.from(이호선.getSectionList());

        sections.add(두번째구간, 이호선);

        assertAll(
                () -> assertThat(sections.getSections()).hasSize(2),
                () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(잠실나루역)
        );
    }

    @DisplayName("상행역과 하행역 사이를 추가한다.")
    @Test
    void add_중간역() {
        final Station 잠실나루역 = Station.from("잠실나루역");
        final Section 두번째구간 = Section.of(이호선, 잠실역, 잠실나루역, 거리5);

        final Sections sections = Sections.from(이호선.getSectionList());

        sections.add(두번째구간, 이호선);

        assertAll(
                () -> assertThat(sections.getSections()).hasSize(2),
                () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실나루역),
                () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(Distance.of(5))
        );
    }

    @DisplayName("등록된 구간의 상행역과 추가할 하행역이 동일하다.")
    @Test
    void add_예외1() {
        final Section 두번째구간 = Section.of(이호선, 잠실역, 강변역, 거리5);

        final Sections sections = Sections.from(이호선.getSectionList());

        assertThatThrownBy(() -> sections.add(두번째구간, 이호선))
                .isInstanceOf(RegisterAllIncludeException.class)
                .hasMessage("상행, 하행 역 모두가 포함되어 있어 등록할 수 없습니다.");
    }

    @DisplayName("등록된 구간의 상행역과 추가할 하행역이 동일하다.")
    @Test
    void add_예외2() {
        final Station 잠실나루역 = Station.from("잠실나루역");
        final Station 잠실새내역 = Station.from("잠실새내역");
        final Section 두번째구간 = Section.of(이호선, 잠실나루역, 잠실새내역, 거리5);

        final Sections sections = Sections.from(이호선.getSectionList());

        assertThatThrownBy(() -> sections.add(두번째구간, 이호선))
                .isInstanceOf(RegisterNotAllIncludeException.class)
                .hasMessage("상행, 하행 역 모두가 포함되지 않아서 등록할 수 없습니다.");
    }

    @DisplayName("등록된 구간의 길이와 추가할 구간의 길이가 같거나 크면 실패한다.")
    @Test
    void add_예외3() {
        final Station 잠실나루역 = Station.from("잠실나루역");
        final Section 두번째구간 = Section.of(이호선, 잠실역, 잠실나루역, 거리10);

        final Sections sections = Sections.from(이호선.getSectionList());

        assertThatThrownBy(() -> sections.add(두번째구간, 이호선))
                .isInstanceOf(RegisterDistanceException.class)
                .hasMessage("등록할 수 없는 구간입니다.");
    }

    @DisplayName("노선에 속한 구간의 2개인 경우 중간역을 제거하면 구간이 하나로 합쳐지면서 다음으로 오던 역이 종점이 된다.")
    @Test
    void mergeSection() {
        final Station 구의역 = Station.from("구의역");
        final Section 두번째구간 = Section.of(이호선, 강변역, 구의역, 거리10);
        final Sections 이호선구간목록 = Sections.from(이호선.getSectionList());
        이호선구간목록.add(두번째구간, 이호선);

        assertThat(이호선구간목록.getSections()).hasSize(2);

        이호선구간목록.merge(강변역);

        assertAll(
                () -> assertThat(이호선구간목록.getSections()).hasSize(1),
                () -> assertThat(이호선구간목록.getSections().get(0).getUpStation()).isEqualTo(잠실역),
                () -> assertThat(이호선구간목록.getSections().get(0).getDownStation()).isEqualTo(구의역),
                () -> assertThat(이호선구간목록.getSections().get(0).getDistance()).isEqualTo(Distance.of(20))
        );
    }

    @DisplayName("노선에 구간이 하나인 경우 삭제가 불가능하다.")
    @Test
    void mergeSection_예외() {
        final Sections sections = Sections.from(이호선.getSectionList());

        assertThat(sections.getSections()).hasSize(1);

        assertThatThrownBy(() -> sections.merge(강변역))
                .isInstanceOf(OneSectionDeleteException.class)
                .hasMessage("구간이 하나인 경우 삭제할 수 없습니다.");
    }

    @DisplayName("노선에 구간이 하나도 없으면 삭제가 불가능하다.")
    @Test
    void mergeSection_예외2() {
        final List<Section> 다른목록 = new ArrayList<>();
        final Line 다른이호선 = Line.of("8호선", "pink", Sections.from(다른목록));

        final Sections sections = Sections.from(다른이호선.getSectionList());

        assertThatThrownBy(() -> sections.merge(잠실역))
                .isInstanceOf(NoSectionDeleteException.class)
                .hasMessage("등록된 구간이 없어서 삭제할 수 없습니다.");
    }
}
