package nextstep.subway.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;

public class PathTestUtils {

    protected Station 교대역;
    protected Station 남부터미널역;
    protected Station 강남역;
    protected Station 양재역;

    protected Line 신분당선;
    protected Line 이호선;
    protected Line 삼호선;

    @BeforeEach
    public void setUp() {
        createStation();
        createLine();
    }

    protected void createStation() {
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
    }

    protected void createLine() {
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 3, 900);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 1, 1000);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5, 300);
        addSection();
    }

    protected void addSection() {
        삼호선.addSection(교대역, 남부터미널역, 3);
    }
}
