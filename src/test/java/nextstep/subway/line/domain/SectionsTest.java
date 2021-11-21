package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간들")
class SectionsTest {

    private Section 강남_광교_구간;

    @BeforeEach
    void setUp() {
        강남_광교_구간 = Section.of(
            Station.from(Name.from("강남")),
            Station.from(Name.from("광교")),
            Distance.from(Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Sections.from(강남_광교_구간));
    }

    @Test
    @DisplayName("구간이 null인 상태로 객체화")
    void instance_nullSection_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Sections.from(null))
            .withMessage("section must not be null");
    }

}
