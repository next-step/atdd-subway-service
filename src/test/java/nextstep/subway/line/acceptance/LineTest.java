package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.MapKeyColumn;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LineTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @DisplayName("지하철 노선의 상행을 찾는다.")
    @Test
    void findUpStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");

        //when
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        Station upStation = line.findUpStation();

        //then
        assertThat(upStation.getName()).isEqualTo("강남역");

    }

    @DisplayName("지하철 노선의 모든 지하철을 상행부터 하행 순으로 조회한다.")
    @Test
    void getStationsTest() {

        //given

        //when

        //then
    }

    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void addLineStationTest() {

        //given

        //when

        //then
    }

    @DisplayName("지하철 노선에 구간을 제외한다.")
    @Test
    void removeLineStationTest() {

        //given

        //when

        //then
    }
}
