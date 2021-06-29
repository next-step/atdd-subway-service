package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.errorMessage.ErrorEnum;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.path.dto.PathRequest;


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
        List<Line> lines = new ArrayList<>(Arrays.asList(new Line("2호선", "녹색", 강남역, 선릉역, new Distance(10), new Fare(0))));
        pathService = new PathService(lineRepository, stationRepository);

        //when
        doReturn(Optional.of(강남역)).when(stationRepository).findById(1L);
        doReturn(Optional.of(선릉역)).when(stationRepository).findById(2L);
        doReturn(lines).when(lineRepository).findAll();

        PathResponse path = pathService.findPath(new LoginMember(), new PathRequest(1L, 2L));
        //then
        assertThat(path).isNotNull();
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
        ).withMessage(ErrorEnum.NOT_FOUND_STATION.message());
    }
}