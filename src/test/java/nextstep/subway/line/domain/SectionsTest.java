package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {
    private Line 이호선;
    private Station 강남역;
    private Station 삼성역;
    private Station 선릉역;
    private Station 잠실역;
    private Station 잠실나루역;
    private Station 강변역;
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
        강변역 = new Station("강변역");
        ReflectionTestUtils.setField(강변역, "id", 6L);
    }

    @DisplayName("노선에 해당하는 모든 구간의 역은 상행역-하행역 순으로 정렬한다.")
    @Test
    void getStations() {
        //when
        List<Station> actual = sections.getStations();

        //then
        assertThat(actual).containsAll(Arrays.asList(강남역, 삼성역, 선릉역, 잠실역));
    }

    @DisplayName("노선의 구간을 추가한다.")
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

    @DisplayName("이미 등록된 구간을 등록하면 예외를 발생시킨다.")
    @Test
    void addExistsException() {
        //given
        Section existsSection = new Section(이호선, 강남역, 삼성역, 1);

        //when
        assertThatThrownBy(() -> sections.add(existsSection))
                .isInstanceOf(IllegalArgumentException.class) // then
                .hasMessage(Sections.EXISTS_SECTION_EXCEPTION_MESSAGE);
    }

    @DisplayName("노선에 등록되지 않은 역의 구간을 추가할 때 예외를 발생시킨다.")
    @Test
    void addNotExistsException() {
        //given
        Section notExistsSection = new Section(이호선, 잠실나루역, 강변역, 1);

        //when
        assertThatThrownBy(() -> sections.add(notExistsSection))
                .isInstanceOf(IllegalArgumentException.class) // then
                .hasMessage(Sections.NOT_EXISTS_ALL_STATIONS_EXCEPTION_MESSAGE);
    }

    @DisplayName("노선의 구간을 삭제한다.")
    @Test
    void removeLineStation() {
        //when
        sections.removeSection(이호선, 삼성역);
        List<Station> actual = sections.getStations();

        //then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(3);
            assertThat(actual.get(0)).isEqualTo(강남역);
            assertThat(actual.get(1)).isEqualTo(선릉역);
            assertThat(actual.get(2)).isEqualTo(잠실역);
        });
    }

    @DisplayName("1개의 구간만이 존재하는 노선의 역을 제거할 때 예외를 발생시킨다.")
    @Test
    void removeLineStationAtLeastOneException() {
        //given
        sections.removeSection(이호선, 삼성역);
        sections.removeSection(이호선, 선릉역);

        //when
        assertThatThrownBy(() -> sections.removeSection(이호선, 잠실역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Sections.AT_LEAST_ONE_SECTION_EXCEPTION_MESSAGE);

    }
}
