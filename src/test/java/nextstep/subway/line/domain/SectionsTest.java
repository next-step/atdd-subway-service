package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    private Line 이호선;
    private Station 강남역;
    private Station 삼성역;
    private Station 선릉역;
    private Station 잠실역;
    private Station 잠실나루역;
    private Sections sections;

    @BeforeEach
    void setUp() {
        //given
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        삼성역 = new Station("삼성역");
        ReflectionTestUtils.setField(삼성역, "id", 2L);
        선릉역 = new Station("선릉역");
        ReflectionTestUtils.setField(선릉역, "id", 3L);
        잠실역 = new Station("잠실역");
        ReflectionTestUtils.setField(잠실역, "id", 4L);
        이호선 = new Line("2호선", "green");
        ReflectionTestUtils.setField(이호선, "id", 1L);
        sections = new Sections(new ArrayList<>(Arrays.asList(
                new Section(이호선, 삼성역, 선릉역, 10),
                new Section(이호선, 강남역, 삼성역, 10),
                new Section(이호선, 선릉역, 잠실역, 10)
        )));
        ReflectionTestUtils.setField(이호선, "sections", sections);

        //and
        잠실나루역 = new Station("잠실나루역");
        ReflectionTestUtils.setField(잠실나루역, "id", 5L);
    }

    @Test
    void getStations() {
        //when
        List<Station> actual = sections.getStations();

        //then
        assertThat(actual).containsAll(Arrays.asList(강남역, 삼성역, 선릉역, 잠실역));
    }

    @Test
    void add() {
        //when
        sections.add(new Section(이호선, 잠실나루역, 잠실역, 1));
        List<Station> actual = sections.getStations();

        //then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(5);
            assertThat(actual.get(0)).isEqualTo(강남역);
            assertThat(actual.get(1)).isEqualTo(삼성역);
            assertThat(actual.get(2)).isEqualTo(선릉역);
            assertThat(actual.get(3)).isEqualTo(잠실나루역);
            assertThat(actual.get(4)).isEqualTo(잠실역);
        });
    }
}
