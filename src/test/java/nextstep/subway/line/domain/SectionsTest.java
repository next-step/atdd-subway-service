package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.exception.CannotRegisterException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간목록에 대한 단위 테스트")
class SectionsTest {

    private Station 대림역;
    private Station 신대방역;
    private Station 신도림역;
    private Section 구간;
    private Section 구간_2;
    private Sections sections;

    @BeforeEach
    void setUp() {
        대림역 = new Station("대림");
        신대방역 = new Station("신대방");
        신도림역 = new Station("신도림");
        구간 = new Section(new Line(), 대림역, 신대방역, 10);
        구간_2 = new Section(new Line(), 신도림역, 대림역, 5);
        sections = new Sections();
    }


    @DisplayName("구간목록에 구간을 저장하면 정상적으로 저장되어야 한다")
    @Test
    void sections_add_test() {
        // when
        sections.add(구간);
        sections.add(구간_2);

        // then
        List<Section> result = sections.getItems();
        assertThat(result).hasSize(2);
    }

    @DisplayName("구간을 조회하면 역이 정상적으로 정렬되어 조회되어야 한다")
    @Test
    void sections_find_test() {
        Sections sections = new Sections();
        sections.add(구간);
        sections.add(구간_2);

        // then
        List<Station> result = sections.getOrderedStations();
        assertThat(result).containsExactly(신도림역, 대림역, 신대방역);
    }

    @DisplayName("구간사이에 상행이 같은 구간인 역을 추가하면 정상적으로 추가되어야 한다")
    @Test
    void sections_save_test2() {
        Section new_구간 = new Section(new Line(), 대림역, 신도림역, 5);

        Sections sections = new Sections();
        sections.add(구간);
        sections.add(new_구간);

        // then
        List<Station> result = sections.getOrderedStations();

        assertAll(
            () -> assertThat(result).containsExactly(대림역, 신도림역, 신대방역),
            () -> assertThat(구간.getDistance()).isEqualTo(5),
            () -> assertThat(구간.getUpStation()).isEqualTo(new_구간.getDownStation())
        );
    }

    @DisplayName("등록하려는 구간의 두 역이 이미 노선에 이미 노선에 존재한다면 예외가 발생한다")
    @Test
    void sections_add_exception() {
        sections.add(구간);
        sections.add(구간_2);

        Station station = 대림역;
        Station station2 = 신대방역;
        Section section = new Section(new Line(), station, station2, 10);

        assertThatThrownBy(() -> {
            sections.add(section);
        }).isInstanceOf(CannotRegisterException.class)
            .hasMessageContaining(ExceptionType.IS_EXIST_BOTH_STATIONS.getMessage());
    }

    @DisplayName("등록하려는 구간의 두 역이 노선에 모두 존재하지 않는다면 예외가 발생한다")
    @Test
    void sections_add_exception2() {
        sections.add(구간);
        sections.add(구간_2);

        Station station = new Station("가리봉역");
        Station station2 = new Station("남구로역");
        Section section = new Section(new Line(), station, station2, 10);

        assertThatThrownBy(() -> {
            sections.add(section);
        }).isInstanceOf(CannotRegisterException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_REGISTER_SECTION.getMessage());
    }
}
