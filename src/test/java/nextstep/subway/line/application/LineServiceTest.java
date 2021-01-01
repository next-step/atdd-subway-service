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

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);
    }

    @DisplayName("지하철 노선을 등록하고 반환하는 역의 순서가 맞는지 확인한다.")
    @Test
    void createLine() {
        // given
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Line line = new Line("신분당선", "red", 양재역, 정자역, 10);
        when(lineRepository.save(any())).thenReturn(line);

        //when
        LineResponse lineResponse = lineService.saveLine(new LineRequest());

        //then
        assertThat(lineResponse.getStations())
                .extracting("name")
                .containsExactly("양재역", "정자역");
    }

    @DisplayName("지하철 구간 등록")
    @Test
    void addBetweenSection() {
        // given
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Station 판교역 = new Station("판교역");
        Line line = new Line("신분당선", "red", 양재역, 정자역, 10);
        setLine(line);

        // when
        addSection(판교역, 정자역);
        // then
        assertThat(line.getStations()).extracting("name")
                .containsExactly("양재역", "판교역", "정자역");
    }

    private void setLine(Line line) {
        when(lineRepository.findById(any())).thenReturn(Optional.of(line));
    }

    @DisplayName("구간 등록 예외 처리")
    @Test
    void addSectionExpectedException() {
        // given
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Line line = new Line("신분당선", "red", 양재역, 정자역, 10);
        setLine(line);

        //when then
        assertThatThrownBy(() -> addSection(정자역, 양재역)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");

        assertThatThrownBy(() -> addSection(판교역, 강남역)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    private void addSection(Station upStation, Station downStation) {
        when(stationService.findStationById(any())).thenReturn(upStation).thenReturn(downStation);
        // when
        lineService.addLineStation(1L, new SectionRequest());
    }
}