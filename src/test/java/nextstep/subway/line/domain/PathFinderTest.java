package nextstep.subway.line.domain;

import java.util.Arrays;
import nextstep.subway.line.application.LineService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathFinderTest {

    @Mock
    private LineService lineService;
    private PathFinder pathFinder;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    Station 강남역;
    Station 교대역;
    Station 남부터미널역;
    Station 양재역;
    /**
     * 교대역    --- *2호선*(10) ---   강남역
     * |                        |
     * *3호선*(7)                   *신분당선*(9)
     * |                        |
     * 남부터미널역  --- *3호선*(8) ---   양재
    */
    @BeforeEach
    public void init(){
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        이호선 = new Line("이호선", "녹색", 강남역, 교대역, 10);
        신분당선 = new Line("신분당선", "빨강색", 강남역, 양재역, 9);
        삼호선 = new Line("삼호선", "주황색", 남부터미널역, 양재역, 8);
        삼호선.addStation(교대역, 남부터미널역, 7);

        when(lineService.findAllLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
        pathFinder = new PathFinder(lineService.findAllLines());
    }

    @Test
    public void 정상_경로찾기_테스트(){
        //when
        GraphPath path = pathFinder.findPath(교대역, 양재역);

        //then
        assertAll(
            () -> assertThat(path.getVertexList()).containsExactlyElementsOf(
                Arrays.asList(교대역, 남부터미널역, 양재역)),
            () -> assertThat(path.getWeight()).isEqualTo(15)
        );
    }

    @Test
    public void 동일_정거장_경로찾기_실패(){
        assertThatThrownBy(() -> pathFinder.findPath(강남역, 강남역)).isInstanceOf(
            RuntimeException.class);
    }

    @Test
    public void 찾을수없는_정거장_경로찾기_실패(){
        Station 없는역 = new Station("없는역");
        assertThatThrownBy(() -> pathFinder.findPath(없는역, 강남역)).isInstanceOf(
            RuntimeException.class);
    }
}
