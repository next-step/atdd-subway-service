package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    private static final Line 이호선 = new Line("2호선", "green");
    private static final Station 강남역 = new Station("강남역");
    private static final Station 교대역 = new Station("교대역");
    private static final Station 역삼역 = new Station("역삼역");
    private static final Station 삼성역 = new Station("삼성역");
    private static final Station 잠실역 = new Station("잠실역");

    @Test
    void getStations_역들을_조회한다() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(이호선, 삼성역, 잠실역, 20));
        sections.add(new Section(이호선, 교대역, 강남역, 10));
        sections.add(new Section(이호선, 역삼역, 삼성역, 30));
        sections.add(new Section(이호선, 강남역, 역삼역, 40));

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(교대역, 강남역, 역삼역, 삼성역, 잠실역);
    }

    @Test
    void updateUpStation_상행역을_업데이트한다() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(이호선, 강남역, 삼성역, 10));

        // when
        sections.updateUpStation(강남역, 역삼역, 3);

        // then
        assertThat(sections.getSections()).contains(new Section(이호선, 역삼역, 삼성역, 7));
    }
}
