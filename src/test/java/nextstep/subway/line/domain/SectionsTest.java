package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Station 상행역;
    private Station 하행역;
    private Line 신규라인;
    private Section 상행_하행_구간;
    private Sections sections;

    @BeforeEach
    void setUp() {
        상행역 = new Station("상행역");
        하행역 = new Station("하행역");
        신규라인 = new Line("신규라인", "파랑", 상행역, 하행역, 10);
        상행_하행_구간 = new Section(신규라인, 상행역, 하행역, 10);
        sections = new Sections();
        sections.add(상행_하행_구간);
    }

    @Test
    void 구간_중간에_등록() {
        Station 신규역 = new Station("신규역");
        Section 상행_신규역구간 = new Section(신규라인, 상행역, 신규역, 5);

        sections.add(상행_신규역구간);

        assertThat(sections.getStations()).contains(신규역);
    }

    @Test
    void 상행구간_구간연장() {
        Station 상행연장역 = new Station("상행연장역");
        Section 상행_연장_구간 = new Section(신규라인, 상행연장역, 상행역, 5);

        sections.add(상행_연장_구간);

        assertThat(this.sections.getSections()).hasSize(2);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(15);
    }

    @Test
    void 하행구간_구간연장() {
        Station 하행연장역 = new Station("하행연장역");
        Section 하행_연장_구간 = new Section(신규라인, 하행역, 하행연장역, 5);

        sections.add(하행_연장_구간);

        assertThat(this.sections.getSections()).hasSize(2);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(15);
    }

    @Test
    void 구간_등록_실패_구간이_존재하는_경우() {
        assertThatThrownBy(
                () -> sections.add(new Section(신규라인, 상행역, 하행역, 5))
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @Test
    void 구간_등록_실패_신규구간에_상행하행둘다없는_경우() {
        Station 신규역2 = new Station("신규역2");
        Station 신규역3 = new Station("신규역3");

        assertThatThrownBy(
                () -> sections.add(new Section(신규라인, 신규역2, 신규역3, 5))
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @Test
    void 구간_등록_실패_역간격보다_거리가큰경우() {
        Station 신규역2 = new Station("신규역2");

        assertThatThrownBy(
                () -> sections.add(new Section(신규라인, 상행역, 신규역2, 20))
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    void 상행역_삭제() {
        Station 상행연장역 = new Station("상행연장역");
        Section 상행_연장_구간 = new Section(신규라인, 상행연장역, 상행역, 5);
        sections.add(상행_연장_구간);

        sections.remove(신규라인, 상행연장역);

        assertThat(this.sections.getSections()).hasSize(1);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(10);
    }

    @Test
    void 하행역_삭제() {
        Station 하행연장역 = new Station("하행연장역");
        Section 하행_연장_구간 = new Section(신규라인, 하행역, 하행연장역, 5);
        sections.add(하행_연장_구간);

        sections.remove(신규라인, 하행연장역);

        assertThat(this.sections.getSections()).hasSize(1);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(10);
    }

    @Test
    void 중간역_삭제() {
        Station 중간역 = new Station("중간역");
        Section 상행_중간_구간 = new Section(신규라인, 상행역, 중간역, 5);
        sections.add(상행_중간_구간);

        sections.remove(신규라인, 중간역);

        assertThat(this.sections.getSections()).hasSize(1);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(10);
    }
}
