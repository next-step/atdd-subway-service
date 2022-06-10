package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Station 잠실역;
    private Station 강남역;
    private Station 역삼역;
    private Sections sections = Sections.newInstance();

    @BeforeEach
    void before() {
        잠실역 = new Station("잠실역");
        역삼역 = new Station("역삼역");
        강남역 = new Station("강남역");
        sections.add(new Section(잠실역, 역삼역, 10));
        sections.add(new Section(역삼역, 강남역, 5));
    }

    @Test
    void getStations() {
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(잠실역, 역삼역, 강남역);
    }

    @Test
    void addSectionTest() {
        // 잠실역 -2- 선릉역 -8- 역삼역 -5- 강남역
        Station 선릉역 = new Station("선릉역");
        Section newSection = new Section(잠실역, 선릉역, 2);
        sections.add(newSection);
        assertThat(sections.getStations()).containsExactly(잠실역, 선릉역, 역삼역, 강남역);
    }

    @Test
    void removeStationTest() {
        //삭제전: 잠실역 -5- 신천역 -3- 선릉역 -2- 역삼역 -5- 강남역
        //삭제후: 잠실역 -5- 신천역 -5- 역삼역 -5- 강남역

        Station 선릉역 = new Station("선릉역");
        Station 신천역 = new Station("신천역");
        Section newSection = new Section(잠실역, 신천역, 5);
        sections.add(newSection);
        Section newSection2 = new Section(신천역, 선릉역, 3);
        sections.add(newSection2);
        sections.delete(선릉역);
        assertThat(sections.getStations()).containsExactly(잠실역, 신천역, 역삼역, 강남역);
    }
}
