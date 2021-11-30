package nextstep.subway.path;

import com.google.common.collect.Lists;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.section.Money;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.ui.PathController;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 조회 - mockito를 활용한 가짜 협력 객체 사용")
public class PathMockitoTest {

    private Station 선릉역 = new Station("선릉역");
    private Station 역삼역 = new Station("역삼역");
    private Station 강남역 = new Station("강남역");
    private Station 판교역 = new Station("판교역");
    private Station 수지역 = new Station("수지역");
    private Station 광교역 = new Station("광교역");


    @Test
    @DisplayName("지하철 노선에 등록된 [역삼 - 수지] 최단 거리 조회 (선릉 --(20)> [ 역삼 --(10)> 강남 --(4)> 판교 --(7)> 수지 ] --(9)> 광교)")
    void findAllLines() {
        // given
        PathService pathService = mock(PathService.class);
        StationRepository stationRepository = mock(StationRepository.class);

        when(stationRepository.findAll()).thenReturn(Lists.newArrayList(선릉역, 역삼역, 강남역, 판교역, 수지역, 광교역));
        when(pathService.findPath(LoginMember.ofGuest(), 선릉역.getId(), 수지역.getId()))
                .thenReturn(PathResponse.of(Lists.newArrayList(역삼역, 강남역, 판교역, 수지역), 21, Money.ofZero()));
        PathController pathController = new PathController(pathService);

        // when
        ResponseEntity<PathResponse> response = pathController.findPath(LoginMember.ofGuest(), 선릉역.getId(), 수지역.getId());

        // then
        assertThat(response.getBody().getDistance()).isEqualTo(21);
    }

}
