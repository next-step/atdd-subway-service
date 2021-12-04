package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_강남역_신촌역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_신촌역_역삼역;
import static nextstep.subway.station.domain.StationFixture.강남역;
import static nextstep.subway.station.domain.StationFixture.역삼역;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SectionTest {

    /**
     * 강남역 - 역삼역 연결 확인
     */
    @Test
    void 다음역을_연결한다() {
        // given
        Section 일호선_구간_강남역_신촌역 = 일호선_구간_강남역_신촌역();
        Section 일호선_구간_신촌역_역삼역 = 일호선_구간_신촌역_역삼역();
        int sumDistance = 일호선_구간_강남역_신촌역.getDistance() + 일호선_구간_신촌역_역삼역.getDistance();

        // when
        일호선_구간_강남역_신촌역.conCateSection(일호선_구간_신촌역_역삼역);

        // then
        assertAll(() -> {
            assertThat(일호선_구간_강남역_신촌역.getUpStation()).isEqualTo(강남역());
            assertThat(일호선_구간_강남역_신촌역.getDownStation()).isEqualTo(역삼역());
            assertThat(일호선_구간_강남역_신촌역.getDistance()).isEqualTo(sumDistance);
        });
    }
}
