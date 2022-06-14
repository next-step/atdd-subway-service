package nextstep.subway.line.unit;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    Station 청량리역;
    Station 왕십리역;
    Station 서울숲역;
    Station 선릉역;
    Station 도곡역;

    Sections sections;

    @BeforeEach
    void setup() {
        청량리역 = new Station("청량리역");
        왕십리역 = new Station("왕십리역");
        서울숲역 = new Station("서울숲역");
        선릉역 = new Station("선릉역");
        도곡역 = new Station("도곡역");

        sections = new Sections();
        sections.add(new Section(null, 왕십리역, 선릉역, 7));
    }

    @Test
    void 중간_역_삽입_구간_추가_1() {
        sections.add(new Section(null, 왕십리역, 서울숲역, 3));

        assertThat(sections.getElements()).containsExactlyInAnyOrder(
                new Section(null, 왕십리역, 서울숲역, 3),
                new Section(null, 서울숲역, 선릉역, 4));
    }

    @Test
    void 중간_역_삽입_구간_추가_2() {
        sections.add(new Section(null, 서울숲역, 선릉역, 4));

        assertThat(sections.getElements()).containsExactlyInAnyOrder(
                new Section(null, 왕십리역, 서울숲역, 3),
                new Section(null, 서울숲역, 선릉역, 4));
    }

    @Test
    void 새로운_상행_종점_구간_추가() {
        sections.add(new Section(null, 청량리역, 왕십리역, 3));

        assertThat(sections.getElements()).containsExactlyInAnyOrder(
                new Section(null, 청량리역, 왕십리역, 3),
                new Section(null, 왕십리역, 선릉역, 7));
    }

    @Test
    void 새로운_하행_종점_구간_추가() {
        sections.add(new Section(null, 선릉역, 도곡역, 3));

        assertThat(sections.getElements()).containsExactlyInAnyOrder(
                new Section(null, 왕십리역, 선릉역, 7),
                new Section(null, 선릉역, 도곡역, 3));
    }

    @Test
    void 중복_구간_추가_예외() {
        assertThatThrownBy(() -> sections.add(new Section(null, 왕십리역, 선릉역, 3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 길이_초과_구간_추가_예외() {
        assertThatThrownBy(() -> sections.add(new Section(null, 왕십리역, 서울숲역, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 역_미일치_구간_추가_예외() {
        assertThatThrownBy(() -> sections.add(new Section(null, 청량리역, 서울숲역, 3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상행_종점_찾기() {
        sections.add(new Section(null, 청량리역, 왕십리역, 3));
        sections.add(new Section(null, 선릉역, 도곡역, 3));
        sections.add(new Section(null, 왕십리역, 서울숲역, 2));

        Section firstSection = sections.findFirstSection();
        assertThat(firstSection.getUpStation()).isSameAs(청량리역);
        assertThat(firstSection.getDownStation()).isSameAs(왕십리역);
        assertThat(firstSection.getDistance()).isEqualTo(3);
    }

    @Test
    void 역_목록_조회() {
        sections.add(new Section(null, 청량리역, 왕십리역, 3));
        sections.add(new Section(null, 왕십리역, 서울숲역, 3));
        sections.add(new Section(null, 선릉역, 도곡역, 3));

        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(청량리역, 왕십리역, 서울숲역, 선릉역, 도곡역);
    }

    @Test
    void 역_제거() {
        sections.add(new Section(null, 청량리역, 왕십리역, 3));

        sections.removeStation(왕십리역);

        assertThat(sections.getElements()).containsExactlyInAnyOrder(
                new Section(null, 청량리역, 선릉역, 10)
        );
    }

    @Test
    void 미등록_역_제거() {
        sections.add(new Section(null, 청량리역, 왕십리역, 3));

        sections.removeStation(서울숲역);

        assertThat(sections.getElements()).containsExactlyInAnyOrder(
                new Section(null, 청량리역, 왕십리역, 3),
                new Section(null, 왕십리역, 선릉역, 7)
        );
    }

    @Test
    void 역_제거_예외() {
        assertThatThrownBy(() -> sections.removeStation(왕십리역))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> sections.removeStation(선릉역))
                .isInstanceOf(IllegalStateException.class);
    }
}
