package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    private Line line;
    private Station upStation;
    private Station downStation;
    private Station middleStation;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행역");
        downStation = new Station("하행역");
        middleStation = new Station("중앙역");
        line = new Line("노선","색상",upStation,downStation,10);
    }
    @Test
    @DisplayName("구간이 0개일 찾을 수 없다.")
    void test0() {
        Sections sections = new Sections();

        List<StationResponse> stationResponses = sections.getStationResponse();

        assertThat(stationResponses.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("구간이 1개일 때 상행역/하행역을 찾을 수 있다.")
    void test1() {
        Sections sections = new Sections();
        sections.add(new Section(line,upStation,downStation,10));

        List<StationResponse> stationResponses = sections.getStationResponse();

        assertThat(stationResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간이 2개일 때 모든 역을 찾을 수 있다.")
    void test2() {
        Sections sections = new Sections();
        sections.add(new Section(line,upStation,middleStation,4));
        sections.add(new Section(line,middleStation,downStation,6));

        List<StationResponse> stationResponses = sections.getStationResponse();

        assertThat(stationResponses.size()).isEqualTo(3);
    }
}