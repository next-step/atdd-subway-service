package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private Station 강남역;
    private Station 선릉역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 교대역;
    private Section section;
    private Line line;

    private Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections();

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");

        line = new Line("2호선", "red");
        section = new Section(line, 강남역, 선릉역, 10);

        line.addLineStation(section);
        sections.add(section);
    }

    @DisplayName("상행 종점 구간 추가")
    @Test
    void 상행종점_생성_검증() {
        // given
        Section newSection = new Section(line, 교대역, 강남역, 5);
        // when
        sections.add(newSection);
        // then
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
                "교대역", "강남역", "선릉역");
    }

    @DisplayName("하행 종점 구간 추가")
    @Test
    void 하행종점_생성_검증() {
        // given
        Section newSection = new Section(line, 선릉역, 삼성역, 5);
        // when
        sections.add(newSection);
        // then
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
                "강남역", "선릉역", "삼성역");
    }

    @DisplayName("중간 구간 추가")
    @Test
    void 중간구간_생성_검증() {
        // given
        Section newSection = new Section(line, 강남역, 역삼역, 5);
        // when
        line.addLineStation(newSection);
        // then
        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
                "강남역", "역삼역", "선릉역");
    }

    @DisplayName("상행 삭제 검증")
    @Test
    void 상행구간_삭제_검증() {
        // given
        Section newSection = new Section(line, 교대역, 강남역, 5);
        sections.add(newSection);
        // when
        sections.removeLineStation(line, 교대역);
        // then
        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
                "강남역", "선릉역");
    }

    @DisplayName("하행 삭제 검증")
    @Test
    void 하행구간_삭제_검증() {
        // given
        Section newSection = new Section(line, 선릉역, 삼성역, 5);
        sections.add(newSection);
        // when
        sections.removeLineStation(line, 삼성역);
        // then
        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
                "강남역", "선릉역");
    }

    @DisplayName("중간 구간 삭제")
    @Test
    void 중간구간_삭제_검증() {
        // given
        Section newSection = new Section(line, 강남역, 역삼역, 5);
        line.addLineStation(newSection);
        // when
        line.removeLineStation(line, 역삼역);
        // then
        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
                "강남역", "선릉역");
    }

}