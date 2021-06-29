package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.path.dto.PathRequest;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    private Station 강남역;
    private Station 선릉역;

    @BeforeEach
    void setup() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
    }

    @DisplayName("경로 조회 테스트")
    @Test
    void findPath() {
        //given
        pathService = new PathService(lineRepository, stationRepository);
        //when
        when(pathService.findPath(new LoginMember(), new PathRequest(1L, 2L))).thenReturn(new PathResponse());
        when(stationRepository.findById(1L)).thenReturn(Optional.of(new Station()));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(new Station()));
        //then
        verify(pathService, times(1)).findPath(new LoginMember(), new PathRequest(강남역.getId(), 선릉역.getId()));
    }

    @DisplayName("역이 없을 경우")
    @Test
    void findPathFailed() {
        //given
        //when
        pathService = new PathService(lineRepository, stationRepository);
        //then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> pathService.findPath(new LoginMember(), new PathRequest(9L, 10L))
        ).withMessage(Sections.NOT_FOUND_SECTION);
    }
}