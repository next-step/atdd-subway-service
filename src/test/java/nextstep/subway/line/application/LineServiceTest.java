package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    private LineService lineService;
    private Line 신분당선;
    private Station 정자역;
    private Station 양재역;
    private Station 판교역;
    private Station 강남역;

    @BeforeEach
    void setUp() {
        정자역 = new Station("정자역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        강남역 = new Station("강남역");
        신분당선 = new Line("신분당선", "red", 양재역, 정자역, 10);

        lineService = new LineService(lineRepository, stationService);
    }

    @DisplayName("지하철 노선을 등록하고 반환하는 역의 순서가 맞는지 확인한다.")
    @Test
    void createLine() {
        // given
        when(lineRepository.save(any())).thenReturn(신분당선);

        //when
        LineResponse lineResponse = lineService.saveLine(new LineRequest());

        //then
        assertThat(lineResponse.getStations())
                .extracting("name")
                .containsExactly("양재역", "정자역");
    }

    @DisplayName("지하철 구간을 등록하고 반환하는 역의 순서가 맞는지 확인한다.")
    @Test
    void addBetweenSection() {
        // given
        givenLineByLineRepository(신분당선);

        // when
        addLineStationByLineService(판교역, 정자역);

        // then
        assertThat(신분당선.getStations()).extracting("name")
                .containsExactly("양재역", "판교역", "정자역");
    }

    @DisplayName("구간 등록 예외 처리")
    @Test
    void addSectionExpectedException() {
        // given
        givenLineByLineRepository(신분당선);

        //when then
        assertThatThrownBy(() -> addLineStationByLineService(정자역, 양재역)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");

        assertThatThrownBy(() -> addLineStationByLineService(판교역, 강남역)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간 삭제")
    @Test
    void deleteSection() {
        // given
        givenLineByLineRepository(신분당선);
        addLineStationByLineService(판교역, 정자역);
        givenStationByStationService(정자역);

        // when
        lineService.removeLineStation(1L, 1L);

        // then
        assertThat(신분당선.getStations()).extracting("name")
                .containsExactly("양재역", "판교역");
    }

    @DisplayName("구간 삭제 예외 처리")
    @Test
    void deleteSectionExpectedException() {
        // given
        givenLineByLineRepository(신분당선);
        givenStationByStationService(정자역);

        // when then
        assertThatThrownBy(() -> lineService.removeLineStation(1L, 1L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("구간 삭제 실패됨");
    }

    private void givenLineByLineRepository(Line line) {
        when(lineRepository.findById(any())).thenReturn(Optional.of(line));
    }

    private void givenStationByStationService(Station targetStation) {
        when(stationService.findStationById(any())).thenReturn(targetStation);
    }

    private void addLineStationByLineService(Station upStation, Station downStation) {
        // given
        when(stationService.findStationById(any())).thenReturn(upStation).thenReturn(downStation);
        // when
        lineService.addLineStation(1L, new SectionRequest());
    }
}