package nextstep.subway.path.dto;

import nextstep.subway.exception.NoPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationGraphPathTest {


    @Autowired
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L,"강남역");
        양재역 = new Station(2L,"양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 남부터미널역, 교대역, 10);
        신분당선 = new Line("신분당선", "pink", 강남역, 양재역, 10);
        삼호선.addStation(양재역,남부터미널역,10);
    }

    @DisplayName("출발역과 도착역, 노선으로 stationGraphPath 객체를 생성한다")
    @Test
    void createStationGrapthPathTest() {
        //when
        List<Line> lines = Arrays.asList(이호선,삼호선,신분당선);
        List<Section> allSectionList =  lines.stream()
                .flatMap(line -> line.getSections().values().stream())
                .collect(Collectors.toList());
        StationGraphPath stationPath = new StationGraphPath(강남역, 남부터미널역, allSectionList);

        //then
        assertThat(stationPath).isNotNull();
    }


    @DisplayName("경로 객체의 최단 경로 역들과 거리를 가져온다")
    @Test
    void generateShortestPathTest() {
        //when
        List<Line> lines = Arrays.asList(이호선,삼호선,신분당선);
        List<Section> allSectionList =  lines.stream()
                .flatMap(line -> line.getSections().values().stream())
                .collect(Collectors.toList());
        StationGraphPath stationPath = new StationGraphPath(강남역, 남부터미널역, allSectionList);

        //then
        assertThat(stationPath.getPathStations()).containsExactly(강남역,교대역,남부터미널역);
        assertThat(stationPath.getDistance()).isEqualTo(20);

    }

    @DisplayName("거리가 같을때는 생성 시 입력 값 우선순위가 된다")
    @Test
    void generateShortestPathWhenSameDistanceTest() {

        //when
        List<Line> lines = Arrays.asList(이호선,삼호선,신분당선);
        List<Section> allSectionList =  lines.stream()
                .flatMap(line -> line.getSections().values().stream())
                .collect(Collectors.toList());
        StationGraphPath stationPath = new StationGraphPath(강남역, 남부터미널역, allSectionList);

        //then
        assertThat(stationPath.getPathStations()).containsExactly(강남역,교대역,남부터미널역);
        assertThat(stationPath.getDistance()).isEqualTo(20);

        //when
        List<Line> linesDiff = Arrays.asList(신분당선,삼호선,이호선);
        List<Section> allSectionListDiff =  linesDiff.stream()
                .flatMap(line -> line.getSections().values().stream())
                .collect(Collectors.toList());
        StationGraphPath stationPathDifferentway = new StationGraphPath(강남역, 남부터미널역, allSectionListDiff);

        //then
        assertThat(stationPathDifferentway.getPathStations()).containsExactly(강남역,양재역,남부터미널역);
        assertThat(stationPathDifferentway.getDistance()).isEqualTo(20);
    }


    @DisplayName("경로가 존재하지 않을때 실패")
    @Test
    void generateShortestPathFailedBecauseOfNotExistPathTest() {
        //give
        Station 종로역 = new Station(6L, "종로역");
        Station 동묘역 = new Station(7L, "동묘역");
        Line 일호선 = new Line("일호선", "blue", 종로역, 동묘역, 1);

        //when && then
        List<Line> lines = Arrays.asList(신분당선,삼호선,이호선,일호선);
        List<Section> allSectionList =  lines.stream()
                .flatMap(line -> line.getSections().values().stream())
                .collect(Collectors.toList());
        assertThatThrownBy(() -> new StationGraphPath(강남역, 종로역, allSectionList))
                .isInstanceOf(NoPathException.class)
                .hasMessageContaining("연결된 경로가 없습니다.");
    }


}