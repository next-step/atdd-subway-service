package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    PathService pathService;

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
        //when
        when(pathService.findPath(any())).thenReturn(new ArrayList<>(Arrays.asList(강남역.toResponse(), 선릉역.toResponse())));
        List<StationResponse> path = pathService.findPath(new PathRequest(강남역.getId(), 선릉역.getId()));
        //then
        assertThat(path).isNotNull();
        assertThat(path.get(0).getName()).isEqualTo(강남역.getName());
        assertThat(path.get(1).getName()).isEqualTo(선릉역.getName());
    }

    @DisplayName("역이 없을 경우")
    @Test
    void findPathFailed() {
        //given
        //when
        pathService = new PathService(stationRepository);
        //then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> pathService.findPath(new PathRequest(9L, 10L))
        ).withMessage(Sections.NOT_FOUND_SECTION);
    }
}