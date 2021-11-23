package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("지하철 라인 관련 기능")
public class LineTest {
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;

    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");

        신분당선 = new Line("신분당선", "red lighten-1", 강남역, 광교역, 100);
    }


    @DisplayName("구간의 개수를 조회한다.")
    @Test
    void find_sectionCount() {
        // when
        int sectionCount = 신분당선.sectionCount();

        // then
        Assertions.assertThat(sectionCount).isEqualTo(1);
    }

    @DisplayName("기등록된 구간을 삭제한다.")
    @Test
    void delete_section() {
        // given
        Section section = new Section(신분당선, 강남역, 광교역, 100);

        // when
        신분당선.removeSection(section);

        // then
        Assertions.assertThat(신분당선.sectionCount()).isZero();
    }

    @DisplayName("등록된 구간이 없는것을 검사한다.")
    @Test
    void check_emptySections() {
        // given
        Section section = new Section(신분당선, 강남역, 광교역, 100);

        // when
        신분당선.removeSection(section);

        // then
        Assertions.assertThat(신분당선.isSectionEmpty()).isTrue();
    }

    @DisplayName("라인의 상행종점역을 조회한다.")
    @Test
    void find_upTerminalStation() {
        // when
        Station findedStation = 신분당선.findUpTerminalStation();

        // then
        Assertions.assertThat(findedStation).isEqualTo(강남역);
    }

    @DisplayName("라인에 구간을 추가한다.")
    @Test
    void add_section() {
        // given
        Section newSection = new Section(신분당선, 강남역, 양재역, 40);

        // when
        boolean addResult = 신분당선.addSection(newSection);

        // then
        Assertions.assertThat(addResult).isTrue();
        Assertions.assertThat(신분당선.getSections()).isEqualTo(Arrays.asList(
            new Section(신분당선, 양재역, 광교역, 60),
            new Section(신분당선, 강남역, 양재역, 40)
        ));
    }

    @DisplayName("라인에있는 역들을 조회한다.")
    @Test
    void search_stations() {
        // given
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 40));

        // when
        List<Station> stations = 신분당선.findStations();

        // then
        Assertions.assertThat(stations).isEqualTo(Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("라인에 특정역이 있는지 확인한다.")
    @Test
    void check_existStation() {
        // when
        boolean isFindStation = 신분당선.hasStation(강남역);

        // then
        Assertions.assertThat(isFindStation).isTrue();
    }

    @DisplayName("라인의 구간에 있는 특정역을 삭제한다.")
    @Test
    void delete_stationAtSection() {
        // given
        Section newSection = new Section(신분당선, 강남역, 양재역, 40);
        boolean addResult = 신분당선.addSection(newSection);

        // when
        신분당선.deleteStation(양재역);

        // then
        List<Station> stations = 신분당선.findStations();
        Assertions.assertThat(stations).isEqualTo(Arrays.asList(강남역, 광교역));
    }
}
