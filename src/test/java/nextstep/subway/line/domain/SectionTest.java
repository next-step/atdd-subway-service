package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {
    private Station 방화역;
    private Station 하남검단산역;
    private Section 오호선생성구간;

    @BeforeEach
    public void setUp() {
        방화역 = new Station(1L, "방화역");
        하남검단산역 = new Station(2L, "하남검단산역");
        오호선생성구간 = new Section(1L, 방화역, 하남검단산역, 10);
    }

    @DisplayName("상행역 변경")
    @Test
    public void 상행역_변경_확인() throws Exception {
        //given
        Station 새로운역 = new Station(3L, "새로운역");

        //when
        오호선생성구간.updateUpStation(새로운역, 3);

        //then
        assertThat(오호선생성구간.getUpStation()).isEqualTo(새로운역);
    }

    @DisplayName("하행역 변경")
    @Test
    public void 하행역_변경_확인() throws Exception {
        //given
        Station 새로운역 = new Station(3L, "새로운역");

        //when
        오호선생성구간.updateDownStation(새로운역, 3);

        //then
        assertThat(오호선생성구간.getDownStation()).isEqualTo(새로운역);
    }
}
