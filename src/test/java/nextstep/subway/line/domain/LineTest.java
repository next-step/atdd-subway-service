package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @DisplayName("상행역을 조회하자.")
    @Test
    void getUpStation() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색", 강남역, 잠실역, 200);

        assertThat(지하철2호선.getUpStation()).isEqualTo(강남역);
    }

    @DisplayName("하행역을 조회하자.")
    @Test
    void getDownStation() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색", 강남역, 잠실역, 200);

        assertThat(지하철2호선.getDownStation()).isEqualTo(잠실역);
    }

    @DisplayName("구간정보가 비어있는지 판단하자")
    @Test
    void isExist() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색", 강남역, 잠실역, 200);

        assertFalse(지하철2호선.isEmptySection());
    }

    @DisplayName("구간이 1개 이하일 경우 삭제불가능하다.")
    @Test
    void isUnableRemoveStatus() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색", 강남역, 잠실역, 200);

        assertTrue(지하철2호선.isUnableRemoveStatus());
    }

    @DisplayName("구간을 추가하자")
    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색", 강남역, 잠실역, 200);

        지하철2호선.addSection(new Section(지하철2호선, 강남역, 삼성역, 100));
        assertThat(지하철2호선.getSections()).hasSize(2);
    }
}