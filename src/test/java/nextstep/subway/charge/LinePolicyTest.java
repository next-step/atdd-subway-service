package nextstep.subway.charge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nextstep.subway.charge.domain.Charge;
import nextstep.subway.charge.domain.LinePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinePolicyTest {

    private List<Section> 구간목록;
    Station 세번째역;

    @BeforeEach
    void setUp() {
        Line 일호선 = new Line("일호선", "blue", 0);
        Station 첫번째역 = new Station("강남역");
        Station 두번째역 = new Station("신사역");
        세번째역 = new Station("부산역");
        Section 첫번째구간 = new Section(일호선, 첫번째역, 두번째역, 10);
        Section 두번째구간 = new Section(일호선, 두번째역, 세번째역, 14);

        구간목록 = new ArrayList<>();
        구간목록.add(첫번째구간);
        구간목록.add(두번째구간);
    }

    @DisplayName("최대 노선 추가요금이 0인 경우")
    @Test
    void applyPolicy() {
        //given
        LinePolicy linePolicy = new LinePolicy(구간목록);
        Charge charge = new Charge(1250);

        //when
        linePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(1250);
    }

    @DisplayName("최대 노선 추가요금이 0미만인 경우")
    @Test
    void applyPolicyNegativeExtraCharge() {
        //given
        Station 네번째역 = new Station("사당역");
        Line 이호선 = new Line("이호선", "blue", -400);
        Section 세번째구간 = new Section(이호선, 세번째역, 네번째역, 11);

        //when & then
        assertThatThrownBy(() -> new LinePolicy(Collections.singletonList(세번째구간)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최대 노선 추가요금이 300인 경우")
    @Test
    void applyPolicyWithExtraCharge() {
        //given
        Station 네번째역 = new Station("사당역");
        Line 이호선 = new Line("이호선", "blue", 300);
        Section 세번째구간 = new Section(이호선, 세번째역, 네번째역, 11);
        구간목록.add(세번째구간);

        LinePolicy linePolicy = new LinePolicy(구간목록);
        Charge charge = new Charge(1250);

        //when
        linePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(1550);
    }
}
