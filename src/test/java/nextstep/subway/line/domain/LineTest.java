package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.line.domain.Line.LINE_NEGATIVE_CHARGE_EXCEPTION_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    private Station 강남역;
    private Station 청계산입구역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        청계산입구역 = new Station("청계산입구역");
        ReflectionTestUtils.setField(청계산입구역, "id", 2L);
    }

    @Test
    void create() {
        //when
        assertDoesNotThrow(() -> new Line("신분당선", "bg-red-600", 강남역, 청계산입구역, 20, 900)); //then
    }

    @Test
    void chargeException() {
        //when
        assertThatThrownBy(() -> new Line("신분당선", "bg-red-600", 강남역, 청계산입구역, 20, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(LINE_NEGATIVE_CHARGE_EXCEPTION_MESSAGE);
    }
}
