package nextstep.subway.line.unit;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("지하철 노선 기능")
public class LineTest {

    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Line 신분당선;
    private Line 영역이없는호선;
    private Section 상행종점역;
    private Section 하행종점역;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");

        신분당선 = new Line("신분당선", "빨간색");
        영역이없는호선 = new Line("영역이없는호선", "노란색");

        상행종점역 = new Section(신분당선, 강남역, 광교역, 10);
        하행종점역 = new Section(신분당선, 광교역, 판교역, 7);

        신분당선.addSection(상행종점역);
        신분당선.addSection(하행종점역);
    }

    @Test
    @DisplayName("자히철 노선 내의 역 리스트 조회")
    void getStation() {
        assertThat(신분당선.getStations().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("지하철 노선에 일부 영역 추가")
    void addStation() {
        신분당선.addStation(광교역, new Station("수원역"), 5);

        assertAll(
                () -> assertThat(신분당선.getStations().size()).isEqualTo(4),
                () -> assertThrows(
                        InvalidRequestException.class,
                        () -> 신분당선.addStation(강남역, new Station("역삼역"), 15)),
                () -> assertThrows(
                        InvalidRequestException.class,
                        () -> 신분당선.addStation(new Station("신논현역"), new Station("역삼역"), 15)),
                () -> assertThrows(
                        InvalidRequestException.class,
                        () -> 신분당선.addStation(강남역, 판교역, 5))
        );
    }

    @Test
    @DisplayName("지하철 노선에 지하철 역 삭제")
    void removeStation() {
        신분당선.removeStation(광교역);
        assertAll(
                () -> assertThat(신분당선.getStations().size()).isEqualTo(2),
                () -> assertThrows(
                        InvalidRequestException.class,
                        () -> 영역이없는호선.removeStation(강남역))
        );
    }

}
