package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private Line 이호선;
    private Station 강남역;
    private Station 삼성역;
    private Station 선릉역;
    private Station 잠실역;
    private Sections sections;

    @BeforeEach
    void setUp() {
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
        ReflectionTestUtils.setField(이호선, "sections2", sections);
    }

    @Test
    void getStations() {
        //when
        List<Station> actual = sections.getStations();

        //then
        assertThat(actual).containsAll(Arrays.asList(강남역, 삼성역, 선릉역, 잠실역));
    }
}
