package study.unit;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.AdditionalCost;
import nextstep.subway.line.domain.CostType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("지하철 경로 조회 테스트")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class PathTest {
    @MockBean
    private LineService lineService;
    @MockBean
    private MemberService memberService;

    private static Station 강남역;
    private static Station 교대역;
    private static Station 양재역;
    private static Station 수서역;
    private static Station 서초역;
    private static Station 사당역;
    private static Station 남부터미널역;
    private static Line 신분당선;
    private static Line 이호선;
    private static Line 삼호선;

    static {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        수서역 = new Station("수서역");
        서초역 = new Station("서초역");
        사당역 = new Station("사당역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10, new AdditionalCost(600));
        이호선 = new Line("2호선", "green lighten-1", 서초역, 사당역, 10);
        삼호선 = new Line("3호선", "orange darken-1", 남부터미널역, 교대역, 60, new AdditionalCost(300));
        신분당선.addLineStation(교대역, 강남역, 9);
    }

    @DisplayName("지하철 경로 조회 실패")
    @ParameterizedTest
    @MethodSource("arguments")
    void findPathFail(Station s1, Station s2, Class<Exception> e) {
        when(lineService.findAllLines()).thenReturn(asList(신분당선, 이호선, 삼호선));
        when(memberService.findMember(1L)).thenReturn(new MemberResponse(1L, "baek@github.com", 17));

        PathFinder pathFinder = new PathFinder(lineService.findAllLines());

        assertThatThrownBy(() -> pathFinder.findPath(CostType.getCostType(memberService.findMember(1L).getAge()), s1, s2)).isInstanceOf(e);
    }

    @DisplayName("지하철 경로 조회")
    @Test
    void findPath() {
        when(lineService.findAllLines()).thenReturn(asList(신분당선, 이호선, 삼호선));
        when(memberService.findMember(1L)).thenReturn(new MemberResponse(1L, "baek@github.com", 17));

        PathFinder pathFinder = new PathFinder(lineService.findAllLines());

        assertThat(pathFinder.findPath(CostType.getCostType(memberService.findMember(1L).getAge()), 남부터미널역, 강남역).getStations())
                .extracting(StationResponse::getName).containsExactly("남부터미널역", "교대역", "강남역");
        assertThat(pathFinder.findPath(CostType.getCostType(memberService.findMember(1L).getAge()), 남부터미널역, 강남역).getDistance()).isEqualTo(69);
        assertThat(pathFinder.findPath(CostType.getCostType(memberService.findMember(1L).getAge()), 남부터미널역, 강남역).getFare()).isEqualTo(2420);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(Arguments.of(강남역, 강남역, RuntimeException.class),
                Arguments.of(수서역, 강남역, NoSuchElementException.class),
                Arguments.of(서초역, 양재역, IllegalStateException.class));
    }
}
