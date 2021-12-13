package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathMockitoTest {

    @Mock
    private PathService pathService;

    private Station dangsanStation;
    private Station hapJeongStation;
    private Station ehwaStation;
    private Station sunyudoStation;
    private Station shinMokDongStation;

    private Line twoLine;
    private Line nineLine;

    @BeforeEach
    void setUp() {
        dangsanStation = new Station("당산역");
        hapJeongStation = new Station("합정역");
        ehwaStation = new Station("이대역");

        twoLine = new Line("2호선", "green", dangsanStation, ehwaStation, 10);
        Section section = new Section(twoLine, dangsanStation, hapJeongStation, 3);
        twoLine.addSection(section);

        sunyudoStation = new Station("선유도역");
        shinMokDongStation = new Station("신목동역");

        nineLine = new Line("9호선", "gold", dangsanStation, sunyudoStation, 20);
        Section nineSection = new Section(nineLine, sunyudoStation, shinMokDongStation, 3);
        nineLine.addSection(nineSection);
    }


    @Test
    @DisplayName("거리 찾는 테스트")
    void findPath() {
        //given
        List<Station> stations = Arrays.asList(dangsanStation, sunyudoStation);
        List<StationResponse> stationResponses = stations.stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());
        LoginMember loginMember = new LoginMember(1L, "test@test.com", 12);

        //when
        when(pathService.findPath(dangsanStation.getId(), sunyudoStation.getId(), loginMember))
                .thenReturn(new PathResponse(stationResponses, new Distance(10)));

        PathResponse pathResponse = pathService.findPath(dangsanStation.getId(), sunyudoStation.getId(), loginMember);

        List<StationResponse> stationBasket = pathResponse.getStations();
        //then
        assertThat(stationBasket.size()).isEqualTo(2);
        assertThat(stationBasket.stream()
                .map(it -> it.getName()).collect(Collectors.toList())).contains("당산역", "선유도역");
        assertThat(pathResponse.getDistance()).isEqualTo(10);
    }
}
