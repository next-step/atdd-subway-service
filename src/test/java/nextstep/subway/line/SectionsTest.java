package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.domain.TempDistance;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("섹션 관련 기능")
public class SectionsTest {

    private Sections sections;
    private static final String station1Name = "강남역";
    private static final String station2Name = "양재역";
    private static final String station3Name = "광교역";
    private static final Station station1 = new Station(station1Name);;
    private static final Station station2 = new Station(station2Name);
    private static final Station station3 = new Station(station3Name);
    private static final Line line = new Line("분당선","bg-red-600");

    @BeforeEach
    public void setUp() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(line, station1, station2, new TempDistance(10)));
        sections.add(new Section(line, station2, station3, new TempDistance(10)));
        this.sections = new Sections(sections);
    }

    @DisplayName("이미 구간에 등록된 경우 오류가 발생한다.")
    @Test
    void already_registered_station_section_add_exception_test() {
        assertThatThrownBy(()->sections.checkValidSection(station1, station2))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("모든역이 구간에 등록되어 있지 않은 경우 오류가 발생한다.")
    @Test
    void no_registered_station_section_add_exception_test() {
        //given
        Station station3 = new Station("새로운역");
        Station station4 = new Station("또다른새로운역");

        //when
        //then
        assertThatThrownBy(()->sections.checkValidSection(station3, station4))
                .isInstanceOf(RuntimeException.class);
    }

    static Stream<Arguments> station_exist_test() {
        return Stream.of(
                Arguments.of(station1, true),
                Arguments.of(station2, true),
                Arguments.of(station3, true),
                Arguments.of(new Station("새로운역"), false)
        );
    }

    @DisplayName("구간에 등록되어 있는 역인지 체크 테스트")
    @ParameterizedTest
    @MethodSource
    void station_exist_test(Station input, boolean expected) {
        assertThat(sections.isStationExisted(input)).isEqualTo(expected);
    }

    @DisplayName("첫번째 역 찾기 테스트")
    @Test
    void find_first_station_test() {
        assertThat(sections.findUpStation()).isEqualTo(station1);
    }

    @DisplayName("지하철역 목록조회 테스트")
    @Test
    void get_station_list_test() {
        assertThat(sections.getStations()).containsExactly(station1, station2, station3);
    }

    @DisplayName("맨앞 구간 추가 테스트")
    @Test
    void add_first_section_test() {
        //given
        String newStationName = "새로운역";
        Station newStation = new Station(newStationName);

        //when
        Section newSection = new Section(line, newStation, station1, new TempDistance(5));
        sections.add(newSection);

        //then
        assertThat(sections.findUpStation()).isEqualTo(newStation);
    }

    @DisplayName("맨끝 구간 추가 테스트")
    @Test
    void add_last_section_test() {
        //given
        String newStationName = "새로운역";
        Station newStation = new Station(newStationName);

        //when
        Section newSection = new Section(line, station1, newStation, new TempDistance(5));
        sections.add(newSection);

        //then
        assertThat(sections.getStations()).contains(newStation);
    }

    @DisplayName("중간 구간 추가 상위역 추가 테스트")
    @Test
    void add_no_terminated_section_add_up_station_test() {
        //given
        String newStationName = "새로운역";
        Station newStation = new Station(newStationName);

        //when
        Section newSection = new Section(line, newStation, station2, new TempDistance(5));
        sections.add(newSection);

        //then
        assertThat(sections.getStations()).contains(newStation);
    }

    @DisplayName("중간 구간 추가 하위역 추가 테스트")
    @Test
    void add_no_terminated_section_add_down_station_test() {
        //given
        String newStationName = "새로운역";
        Station newStation = new Station(newStationName);

        //when
        Section newSection = new Section(line, station2, newStation, new TempDistance(5));
        sections.add(newSection);

        //then
        assertThat(sections.getStations()).contains(newStation);
    }
}
