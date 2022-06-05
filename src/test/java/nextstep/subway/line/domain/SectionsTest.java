package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간목록에 대한 단위 테스트")
class SectionsTest {
    private Station 대림역;
    private Station 신대방역;
    private Station 신도림역;

    @BeforeEach
    void setUp() {
        대림역 = new Station("대림");
        신대방역 = new Station("신대방");
        신도림역 = new Station("신도림");
    }


    @DisplayName("구간목록에 구간을 저장하면 정상적으로 저장되어야 한다")
    @Test
    void sections_add_test() {
        // given
        Section 구간 = new Section(new Line(), 대림역, 신대방역, 10);
        Section 구간_2 = new Section(new Line(), 신도림역, 대림역, 5);
        Sections sections = new Sections();

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
        // given
        Section 구간 = new Section(new Line(), 대림역, 신대방역, 10);
        Section 구간_2 = new Section(new Line(), 신도림역, 대림역, 5);

        Sections sections = new Sections();
        sections.add(구간);
        sections.add(구간_2);

        // then
        List<Station> result = sections.getOrderedStations();
        assertThat(result).containsExactly(신도림역, 대림역, 신대방역);
    }
}
