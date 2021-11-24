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

    @DisplayName("라인에 구간을 추가한다.")
    @Test
    void add_section() {
        // given
        Section newSection = new Section(신분당선, 강남역, 양재역, 40);

        // when
        신분당선.addSection(newSection);

        // then
        Sections expectedSection = Sections.of(new Section(신분당선, 양재역, 광교역, 60),
                                                new Section(신분당선, 강남역, 양재역, 40)
                                            );
        Assertions.assertThat(신분당선.getSections()).isEqualTo(expectedSection);
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

    @DisplayName("라인의 구간에 있는 특정역을 삭제한다.")
    @Test
    void delete_stationAtSection() {
        // given
        Section newSection = new Section(신분당선, 강남역, 양재역, 40);
        신분당선.addSection(newSection);

        // when
        신분당선.deleteStation(양재역);

        // then
        List<Station> stations = 신분당선.findStations();
        Assertions.assertThat(stations).isEqualTo(Arrays.asList(강남역, 광교역));
    }
}
