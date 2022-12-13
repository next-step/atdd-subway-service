package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("지하철역 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private StationService stationService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
    }

    @DisplayName("지하철역 생성에 성공한다.")
    @Test
    void 지하철역_생성에_성공한다() {
        // given
        when(stationRepository.save(any())).thenReturn(강남역);

        // when
        StationResponse 저장된_지하철역 = stationService.saveStation(new StationRequest("강남역"));

        // then
        assertThat(저장된_지하철역.getName()).isEqualTo("강남역");
    }

    @DisplayName("지하철역 목록 조회에 성공한다.")
    @Test
    void 지하철역_목록_조회에_성공한다() {
        // given
        when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역));

        // when
        List<StationResponse> 지하철역_전체_목록 = stationService.findAllStations();

        // then
        assertThat(지하철역_전체_목록).hasSize(3);
    }

    @DisplayName("지하철역 조회에 실패한다.")
    @Test
    void 지하철역_조회에_실패한다() {
        // given
        when(stationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> stationService.findStationById(1L));
    }

    @DisplayName("지하철역 조회에 성공한다.")
    @Test
    void 지하철역_조회에_성공한다() {
        // given
        when(stationRepository.findById(anyLong())).thenReturn(Optional.of(강남역));

        // when
        Station 조회된_지하철역 = stationService.findStationById(1L);

        // then
        assertThat(조회된_지하철역.getName()).isEqualTo("강남역");
    }
}
