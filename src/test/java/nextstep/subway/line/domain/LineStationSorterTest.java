package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선에 포함된 역 목록을 정렬하는 클래스 테스트")
class LineStationSorterTest {

    private Station 서초역;
    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        서초역 = new Station("서초역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
    }

    @Test
    void 역_목록을_정렬한다() {
        Line line = new Line("2호선", "bg-green-600", 0, 서초역, 교대역, 10);
        line.addSection(new Section(line, 역삼역, 선릉역, 10));
        line.addSection(new Section(line, 교대역, 강남역, 10));
        line.addSection(new Section(line, 강남역, 역삼역, 10));

        assertThat(line.getSortedStations())
                .containsExactlyElementsOf(Arrays.asList(서초역, 교대역, 강남역, 역삼역, 선릉역));
    }
}
