package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixtures.강변;
import static nextstep.subway.station.StationFixtures.구의;
import static nextstep.subway.station.StationFixtures.도곡;
import static nextstep.subway.station.StationFixtures.잠실;
import static nextstep.subway.station.StationFixtures.잠실나루;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Sections sections;
    private Line line;

    @BeforeEach
    void setUp() {
        // given
        sections = Sections.of();
        line = new Line("2호선", "RED");
    }

    @Test
    @DisplayName("구간 추가 후 구간 갯수 검증")
    void add() {
        // given
        Section section = Section.of(line, 잠실, 잠실나루, 100);

        // when
        sections.add(section);

        // then
        assertThat(sections.getStationsInOrder()).extracting("name")
            .containsExactly(잠실.getName(), 잠실나루.getName());
    }

    @Test
    @DisplayName("이미 등록된 구간 입니다.")
    void addSection_duplicate() {
        // given
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 잠실, 잠실나루, 50);

        // when
        sections.add(section1);

        // then
        assertThrows(InvalidParameterException.class, () -> sections.add(section2));
    }

    @Test
    @DisplayName("추가구간 길이가 기존 구간길이보다 클 경우 에러")
    void addSection_distance_over() {
        // given
        Line line = new Line("2호선", "RED");
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 잠실, 강변, 200);

        // when
        sections.add(section1);

        // then
        assertThrows(InvalidParameterException.class, () -> sections.add(section2));
    }

    @Test
    @DisplayName("상행, 하행 어디에도 속하지 않는 역은 추가 할 수 없음")
    void addSection_add_not_position() {
        // when
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 강변, 구의, 100);
        sections.add(section1);

        // then
        assertThrows(InvalidParameterException.class, () -> sections.add(section2));
    }

    @Test
    @DisplayName("구간 중간에 역 추가")
    void addSection_add_inside() {
        // when
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 잠실, 구의, 50);
        sections.add(section1);
        sections.add(section2);

        // then

        List<Station> stations = sections.getStationsInOrder();
        assertThat(stations).extracting("name")
            .containsExactly(잠실.getName(), 구의.getName(), 잠실나루.getName());
    }

    @Test
    @DisplayName("상행 종점 이전 역 추가")
    void addSection_add_first_pre() {
        // when
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 구의, 잠실, 50);
        sections.add(section1);
        sections.add(section2);

        // then

        List<Station> stations = sections.getStationsInOrder();
        assertThat(stations).extracting("name")
            .containsExactly(구의.getName(), 잠실.getName(), 잠실나루.getName());
    }

    @Test
    @DisplayName("하행 종점 다음 역 추가")
    void addSection_add_last_next() {
        // given
        // when
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 잠실나루, 구의, 100);
        sections.add(section1);
        sections.add(section2);

        // then

        List<Station> stations = sections.getStationsInOrder();
        assertThat(stations).extracting("name")
            .containsExactly(잠실.getName(), 잠실나루.getName(), 구의.getName());
    }

    @Test
    @DisplayName("구간 하나일때 제거 실패")
    void remove_fail() {
        // given
        Section section = Section.of(line, 잠실, 잠실나루, 100);
        sections.add(section);

        // when
        // then
        assertThrows(InvalidParameterException.class, () -> sections.remove(잠실));
    }

    @Test
    @DisplayName("구간에서 역 한개 제거")
    void remove() {
        // given
        Section section1 = Section.of(line, 잠실, 잠실나루, 100);
        Section section2 = Section.of(line, 잠실나루, 구의, 50);
        Section section3 = Section.of(line, 구의, 도곡, 5);
        sections.add(section1);
        sections.add(section2);
        sections.add(section3);

        // then
        assertThat(sections.getStationsInOrder()).hasSize(4);

        // when
        sections.remove(잠실나루);

        // then
        List<Station> as = sections.getStationsInOrder();
        assertThat(sections.getStationsInOrder()).hasSize(3);
    }
}
