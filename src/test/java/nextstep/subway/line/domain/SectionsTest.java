package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("노선에 등록된 지하철역을 상하행 순서로 출력한다.")
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Line 신분당선 = new Line("신분당선", "red", 강남역, 판교역, 10);
        신분당선.getSections().add(new Section(신분당선, 양재시민의숲역, 판교역, 5));

        assertThat(신분당선.getSectionsObj().getStations()).containsExactly(강남역, 양재시민의숲역, 판교역);
    }

}
