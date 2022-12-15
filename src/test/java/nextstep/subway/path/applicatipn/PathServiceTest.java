package nextstep.subway.path.applicatipn;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.SourceAndTargetStationDto;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.exception.type.ValidExceptionType.IS_TARGET_ANS_SOURCE_SAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    @InjectMocks
    private PathService pathService;

    private Station 신도림역 = Station.from("신도림역");
    private Station 가디역 = Station.from("가디역");
    private Station 대림역 = Station.from("대림역");

    private Station 수원역 = Station.from("수원역");

    private Line 일호선 = Line.of("1호선", "blue", 가디역, 신도림역, Distance.from(30), 0);
    private Line 이호선 = Line.of("2호선", "green", 신도림역, 대림역, Distance.from(10), 0);
    private Line 칠호선 = Line.of("7호선", "deep-green", 가디역, 대림역, Distance.from(10), 0);

    private LoginMember loginMember = LoginMember.ofLogin(1L, "test@test.com", Age.from(10));

    private LoginMember notLoginMember = LoginMember.ofNotLogin();

    /**
     * 지하철 노선도
     * <p>
     * 구로역    --- *1호선* ---   신도림역
     * |                        |
     * *1호선*                   *2호선*
     * |                        |
     * 가디역  --- *7호선* ---   대림역
     */

    @Test
    @DisplayName("출발지와 도착지를 이용하여 최단 거리를 구한다")
    void getPath() {
        Long sourceId = 1L;
        Long targetId = 2L;

        when(stationService.findStationById(sourceId, targetId)).thenReturn(SourceAndTargetStationDto.of(신도림역, 가디역));
        when(lineService.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 칠호선));

        PathResponse result = pathService.getPath(sourceId, targetId, loginMember);

        List<Long> ids = result.getStations().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(result.getDistance()).isEqualTo(20);
        assertThat(ids).containsAll(Arrays.asList(신도림역.getId(), 대림역.getId(), 가디역.getId()));
    }

    @Test
    @DisplayName("출발지와 도착지가 같으면 예외를 발생시킨다")
    void startAndEndSameException() {
        Long sourceId = 1L;
        Long targetId = 1L;

        when(stationService.findStationById(sourceId, targetId)).thenReturn(SourceAndTargetStationDto.of(신도림역, 신도림역));

        assertThatThrownBy(() -> {
            pathService.getPath(sourceId, targetId, loginMember);
        }).isInstanceOf(NotValidDataException.class)
                .hasMessageContaining(IS_TARGET_ANS_SOURCE_SAME.getMessage());
    }
}
