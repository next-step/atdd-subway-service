package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    private Station 강남역;
    private Station 판교역;
    private Station 양재역;
    private Station 교대역;
    private Line 신분당선;

    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);

        // given
        // 지하철 & 노선 등록되어 있음
        강남역 = new Station(1L, "강남역");
        판교역 = new Station(2L, "판교역");
        양재역 = new Station(3L, "양재역");
        교대역 = new Station(4L, "교대역");
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(양재역);

        신분당선 = new Line("2호선", "BLUE", 강남역, 판교역, 10);
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(신분당선));
    }

    @Test
    void addLineStation() {
        // when
        // 추가 구간을 등록
        boolean isAddSection = lineService.addLineStation(1L, new SectionRequest(1L, 3L, 5));

        // then
        // 구간 추가등록 완료
        assertThat(isAddSection).isTrue();
    }

    @Test
    void exception_distance() {
        // when then
        // 기존 거리보다 긴 거리면 오류 발생
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 3L, 15)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception_duplicate_section() {
        // when then
        // 기존 구간과 동일하면 오류 발생
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 2L, 8)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception_not_registered_station() {
        // when then
        // 기존에 역과 일치하는 역이 없으면 오류 발생
        추가_역_등로됨();
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(4L, 5L, 8)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void removeLineStation() {
        // when
        // 추가역 과 구간 등록됨
        추가_역_등로됨();
        추가_구간_등록됨();

        // then
        // 구간 등록 됨
        List<Station> stations = lineService.getStations(신분당선);
        assertThat(stations.size()).isEqualTo(4);

        // when
        boolean isRemoveStation = lineService.removeLineStationNew(1L, 2L);

        // then
        List<Station> expected = lineService.getStations(신분당선);
        assertThat(isRemoveStation).isTrue();
        assertThat(expected.size()).isEqualTo(3);
        assertThat(expected.contains(2L)).isFalse();
    }

    @Test
    void exception_remove_not_registered_station() {
        // when then
        // 기존에 역과 일치하는 역이 없으면 오류 발생
        추가_역_등로됨();
        추가_구간_등록됨();
        assertThatThrownBy(() -> lineService.removeLineStationNew(1L, 5L))
                .isInstanceOf(RuntimeException.class);
    }

    private void 추가_구간_등록됨() {
        lineService.addLineStation(1L, new SectionRequest(1L, 3L, 5));
        lineService.addLineStation(1L, new SectionRequest(2L, 4L, 5));
    }

    private void 추가_역_등로됨() {
        when(stationService.findStationById(2L)).thenReturn(판교역);
        when(stationService.findStationById(4L)).thenReturn(교대역);
    }
}
