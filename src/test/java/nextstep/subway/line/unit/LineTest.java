package nextstep.subway.line.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    Station 청량리역;
    Station 왕십리역;
    Station 서울숲역;
    Station 선릉역;
    Station 도곡역;
    Line 분당선;
    int 왕십리_선릉_길이;

    @BeforeEach
    void setup() {
        청량리역 = new Station("청량리역");
        왕십리역 = new Station("왕십리역");
        서울숲역 = new Station("서울숲역");
        선릉역 = new Station("선릉역");
        도곡역 = new Station("도곡역");

        왕십리_선릉_길이 = 7;

        분당선 = new Line("분당선", "bg-yellow-600", 왕십리역, 선릉역, 왕십리_선릉_길이);
    }

    @Test
    void 역_목록() {
        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(왕십리역, 선릉역);
    }

    @Test
    void 중간_역_추가() {
        분당선.addSection(왕십리역, 서울숲역, 왕십리_선릉_길이 - 5);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(왕십리역, 서울숲역, 선릉역);
    }

    @Test
    void 상행_종점_추가() {
        분당선.addSection(청량리역, 왕십리역, 3);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(청량리역, 왕십리역, 선릉역);
    }

    @Test
    void 하행_종점_추가() {
        분당선.addSection(선릉역, 도곡역, 3);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(왕십리역, 선릉역, 도곡역);
    }

    @Test
    void 중복_구간_추가_예외() {
        assertThatThrownBy(() -> 분당선.addSection(왕십리역, 선릉역, 왕십리_선릉_길이 - 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거리_초과_구간_추가_예외() {
        assertThatThrownBy(() -> 분당선.addSection(왕십리역, 서울숲역, 왕십리_선릉_길이))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 역_미일치_구간_추가_예외() {
        assertThatThrownBy(() -> 분당선.addSection(청량리역, 서울숲역, 3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 중간_역_제거() {
        분당선.addSection(청량리역, 왕십리역, 3);

        분당선.removeSection(왕십리역);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(청량리역, 선릉역);
    }

    @Test
    void 상행_종점_제거() {
        분당선.addSection(왕십리역, 서울숲역, 3);

        분당선.removeSection(왕십리역);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(서울숲역, 선릉역);
    }

    @Test
    void 하행_종점_제거() {
        분당선.addSection(왕십리역, 서울숲역, 3);

        분당선.removeSection(선릉역);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(왕십리역, 서울숲역);
    }

    @Test
    void 미등록_역_제거() {
        분당선.addSection(왕십리역, 서울숲역, 2);

        분당선.removeSection(도곡역);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).containsExactly(왕십리역, 서울숲역, 선릉역);
    }

    @Test
    void 역_제거_예외() {
        assertThatThrownBy(() -> 분당선.removeSection(왕십리역))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> 분당선.removeSection(선릉역))
                .isInstanceOf(IllegalStateException.class);
    }
}
