package nextstep.subway.line.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    Station 왕십리역;
    Station 서울숲역;
    Station 선릉역;
    Station 도곡역;
    Line 분당선;

    @BeforeEach
    void setup() {
        왕십리역 = new Station("왕십리역");
        서울숲역 = new Station("서울숲역");
        선릉역 = new Station("선릉역");
        도곡역 = new Station("도곡역");

        분당선 = new Line("분당선", "bg-yellow-600", 왕십리역, 선릉역, 7);
    }

    @Test
    void 상행_종점_찾기() {
        Station 상행종점 = 분당선.findUpStation();
        assertThat(상행종점).isSameAs(왕십리역);
    }

    @Test
    void 역_목록() {
        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(왕십리역, 선릉역);
    }
}
