package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    private static Line 신분당선;
    private static Station 강남;
    private static Station 양재시민의숲;
    private static Station 양재;

    @BeforeAll
    static void setUp() {
        신분당선 = new Line("신분당선", "green");
        강남 = new Station("강남");
        양재시민의숲 = new Station("양재시민의숲");
        양재 = new Station("양재");
    }

    @DisplayName("지하철 구간 추가")
    @Test
    void add() {
        // given
        Sections sections = new Sections();
        Section section = new Section(신분당선, 강남, 양재시민의숲, 10);

        // when
        sections.add(section);

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("지하철 노선 지하철역 조회 시 순서 정렬됨")
    @Test
    void getStationsRelativeOrder() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남, 양재시민의숲, 10));
        sections.add(new Section(신분당선, 강남, 양재, 5));

        // when
        Stations stations = sections.getStationsRelativeOrder();

        // then
        assertThat(stations).containsExactly(강남, 양재, 양재시민의숲);
    }

    @DisplayName("지하철 노선 지하철역 조회")
    @Test
    void getStations() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남, 양재시민의숲, 10));
        sections.add(new Section(신분당선, 강남, 양재, 5));

        // when
        Stations stations = sections.getStationsRelativeOrder();

        // then
        assertThat(stations).contains(강남, 양재, 양재시민의숲);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void removeSection() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남, 양재시민의숲, 10));
        sections.add(new Section(신분당선, 강남, 양재, 5));

        // when
        sections.removeSection(강남);

        // then
        assertThat(sections.getStations()).contains(양재, 양재시민의숲);
    }
}
