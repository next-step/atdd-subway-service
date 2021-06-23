package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sections 단위 테스트")
class SectionsTest {

    Sections 강남_광교;

    @Test
    @DisplayName("구간들의 역을 가져온다")
    void getStations() {
        강남_광교 = new Sections();
        강남_광교.add(강남_광교_100);
        assertThat(강남_광교.getStations())
            .containsExactly(강남역, 광교역);
    }
}
