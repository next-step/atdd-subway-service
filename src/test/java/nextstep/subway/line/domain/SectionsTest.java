package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Station 신림역;
    private Line 신분당선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");
        신림역 = new Station("신림역");
        신분당선 = new Line("신분당선", "bg-red-600");
        sections = new Sections();
    }

    @DisplayName("구간 등록")
    @Test
    void addSections() {
        // given
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 광교역, 판교역, 1);
        sections.add(section);
        sections.add(section2);

        // when
        List<Section> result = sections.getSections();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("동일 상행역 구간 등록")
    @Test
    void addSectionsSameUpStation() {
        // given
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 강남역, 판교역, 1);
        sections.add(section);
        sections.add(section2);

        // when
        List<Section> result = sections.getSections();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.stream().filter(x -> x.getUpStation().equals(강남역)).findFirst().get().getDistance()).isEqualTo(1)
        );
    }

    @DisplayName("동일 하행역 구간 등록")
    @Test
    void addSectionsSameDownStation() {
        // given
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 판교역, 광교역, 1);
        sections.add(section);
        sections.add(section2);

        // when
        List<Section> result = sections.getSections();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.stream().filter(x -> x.getUpStation().equals(강남역)).findFirst().get().getDistance()).isEqualTo(9)
        );
    }

    @DisplayName("구간 조회")
    @Test
    void findSections() {
        // given
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 판교역, 광교역, 1);
        sections.add(section);
        sections.add(section2);

        // when
        List<Section> result = sections.getSections();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.stream().filter(x -> x.getUpStation().equals(강남역)).findFirst().get().getDistance()).isEqualTo(9)
        );
    }

    @DisplayName("동일 구간 등록 시 예외 발생")
    @Test
    void 예외_addSameSection() {
        // given, when
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        sections.add(section);

        // then
        assertThatThrownBy(() -> sections.add(section)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에 존재하지 않는 역으로 구간 등록 시 예외 발생")
    @Test
    void 예외_addSectionAndNotContainsStation() {
        // given, when
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        sections.add(section);

        // then
        Section section2 = new Section(new Line(), 판교역, 신림역, 10);
        assertThatThrownBy(() -> sections.add(section2)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("종점역 삭제")
    @Test
    void deleteStation() {
        // given
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 판교역, 광교역, 1);
        sections.add(section);
        sections.add(section2);

        // when
        sections.delete(광교역);

        // then
        List<Long> list = sections.getStations().stream().map(Station::getId).collect(Collectors.toList());
        List<Long> expectList = Arrays.asList(강남역.getId(), 광교역.getId());
        assertAll(
                () -> assertThat(sections.getSections()).hasSize(1),
                () -> assertThat(sections.getSections().stream()
                        .filter(x -> x.getUpStation().getName().equals("강남역"))
                        .findFirst().get().getDistance()).isEqualTo(9),
                () -> 지하철_노선_정렬_확인(list, expectList)
        );
    }

    @DisplayName("중간역 삭제")
    @Test
    void deleteMiddleStation() {
        // given
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 판교역, 광교역, 1);
        sections.add(section);
        sections.add(section2);

        // when
        sections.delete(판교역);

        // then
        List<Long> list = sections.getStations().stream().map(Station::getId).collect(Collectors.toList());
        List<Long> expectList = Arrays.asList(강남역.getId(), 광교역.getId());
        assertAll(
                () -> assertThat(sections.getSections()).hasSize(1),
                () -> assertThat(sections.getSections().stream()
                        .filter(x -> x.getUpStation().getName().equals("강남역"))
                        .findFirst().get().getDistance()).isEqualTo(10),
                () -> 지하철_노선_정렬_확인(list, expectList)
        );
    }

    @DisplayName("구간이 하나인 경우 예외 발생")
    @Test
    void 예외_deleteStation() {
        // given, when
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        sections.add(section);

        // then
        assertThatThrownBy(() -> sections.delete(강남역)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 구간인 경우 예외 발생")
    @Test
    void 예외_deleteNotContainsStation() {
        // given, when
        Section section = new Section(new Line(), 강남역, 광교역, 10);
        Section section2 = new Section(new Line(), 판교역, 광교역, 1);
        sections.add(section);
        sections.add(section2);

        // then
        assertThatThrownBy(() -> sections.delete(신림역)).isInstanceOf(IllegalArgumentException.class);
    }

    public void 지하철_노선_정렬_확인(List<Long> list, List<Long> expectList) {
        assertThat(list).containsExactlyElementsOf(expectList);
    }
}
