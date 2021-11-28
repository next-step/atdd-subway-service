package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import nextstep.subway.common.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("지하철 역들")
class StationsTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Stations.from(
                Collections.singletonList(Station.from(Name.from("강남역"))))
            );
    }

    @Test
    @DisplayName("초기 구간은 반드시 필수")
    void instance_nullList_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Stations.from(null))
            .withMessage("지하철 역 목록은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] {0} 은 강남역만 존재하는 지하철 역들의 크기보다 작은 것이 {1}")
    @DisplayName("크기가 더 작은지 판단")
    @CsvSource({"1,false", "2,true"})
    void sizeLessThan(int target, boolean expected) {
        // given
        Stations 강남역만_존재하는_지하철_역들 = Stations.from(
            Collections.singletonList(Station.from(Name.from("강남역"))));

        // when
        boolean sizeLessThan = 강남역만_존재하는_지하철_역들.sizeLessThan(target);

        // then
        assertThat(sizeLessThan)
            .isEqualTo(expected);
    }

}
