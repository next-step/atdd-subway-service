package nextstep.subway.line.unit;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 기능")
public class SectionTest {

    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Section 강남_광교_구간;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");

        강남_광교_구간 = new Section(null, 강남역, 광교역, 10);
    }

    @Test
    @DisplayName("상행역 수정")
   void updateUpStation() {
        강남_광교_구간.updateUpStation(판교역, 5);
        assertThat(강남_광교_구간.getUpStation().getName()).isEqualTo(판교역.getName());
        assertThat(강남_광교_구간.getDistance()).isEqualTo(5);
    }

    @Test
   @DisplayName("하행역 수정")
    void updateDownStation() {
        강남_광교_구간.updateDownStation(판교역, 5);
        assertThat(강남_광교_구간.getDownStation().getName()).isEqualTo(판교역.getName());
        assertThat(강남_광교_구간.getDistance()).isEqualTo(5);
    }

}
