package nextstep.subway.station.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    private Station 강남역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
    }

    @Test
    void 두개의_지하철역_아이디갑_일치여부_확인() {
        assertThat(강남역.isSameId(new Station(1L, "강남역"))).isTrue();
        assertThat(강남역.isSameId(new Station(2L, "양재역"))).isFalse();
    }
}
