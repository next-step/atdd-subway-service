package nextstep.subway.line.domain;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest extends AcceptanceTest {

    Line _2호선;
    Station 강남역;
    Station 역삼역;
    Station 선릉역;
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        _2호선 = new Line();
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        Section section1 = new Section(_2호선, 강남역, 역삼역, 10);
        Section section2 = new Section(_2호선, 역삼역, 선릉역, 10);
        _2호선.getSections().add(section1);
        _2호선.getSections().add(section2);
    }

    @DisplayName("가장 상행역을 구한다")
    @Test
    void findUpStationTest() {
        assertThat(_2호선.findUpStation()).isEqualTo(강남역);
    }

    @DisplayName("역을 순서대로 구한다")
    @Test
    void getStationsTest() {
        assertThat(_2호선.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 역삼역, 선릉역));
    }

}