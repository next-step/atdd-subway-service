package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LinesTest {


    @DisplayName("모든 노선에서 구간정보를 가져오자")
    @Test
    void getSections() {
        Station 강남역 = new Station("강남역");
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");
        Station 정자역 = new Station("정자역");
        Section 삼성_잠실 = new Section(삼성역, 잠실역, 50);
        Section 강남_삼성 = new Section(강남역, 삼성역, 100);
        Section 강남_정자 = new Section(강남역, 정자역, 100);

        Line 지하철2호선 = new Line("2호선", "초록색");
        지하철2호선.addSection(삼성_잠실);
        지하철2호선.addSection(강남_삼성);
        Line 지하철신분당선 = new Line("신분당선", "빨간색");
        지하철신분당선.addSection(강남_정자);

        Lines lines = new Lines(Arrays.asList(지하철2호선, 지하철신분당선));

        assertThat(lines.getSections()).hasSize(3);
        assertThat(lines.getSections()).contains(삼성_잠실, 강남_삼성, 강남_정자);
    }
}