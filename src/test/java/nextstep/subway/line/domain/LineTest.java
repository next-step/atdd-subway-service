package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Line 도메인 테스트")
class LineTest {

    private Distance 기본_구간_거리_30 = new Distance(30);
    private Station 영등포구청역 = new Station(2L, "영등포구청역");
    private Station 신길역 = new Station(4L, "신길역");

    private Line 오호선 = new Line(1L, "5호선", "보라색", 영등포구청역, 신길역, 기본_구간_거리_30);

    @Test
    void update_성공() {
        // given
        final String CHANGED_NAME = "죠르디선";
        final String CHANGED_COLOR = "초록색";

       // when
        오호선.update(CHANGED_NAME, CHANGED_COLOR);;

        // then
        assertThat(오호선.getName()).isEqualTo(CHANGED_NAME);
        assertThat(오호선.getColor()).isEqualTo(CHANGED_COLOR);
    }

}