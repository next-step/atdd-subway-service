package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    private Station 강남역;
    private Station 광교역;
    private Station 정자역;
    private Station 양재역;
    private Line 신분당선;

    @BeforeEach
    public void setup() {
        강남역 = new Station("강남역");
        정자역 = new Station("정자역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 500);
    }

    @DisplayName("라인의 상행 종점 역 찾기")
    @Test
    public void findUpStation() {
        //when
        Station findStation = 신분당선.findUpStation();

        //then
        assertThat(findStation.equals(강남역)).isTrue();
    }

}
