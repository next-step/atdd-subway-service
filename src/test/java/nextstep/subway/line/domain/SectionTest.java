package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionTest {

    private Section 강남_판교_구간1;
    private Section 강남_판교_구간2;
    private Section 강남_정자_구간;
    private Section 판교_정자_구간;
    private Section 정자_광교_구간;

    @BeforeEach
    void setUp() {
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");
        Station 정자역 = new Station("정자역");
        Station 광교역 = new Station("광교역");

        강남_판교_구간1 = Section.of(null, 강남역, 판교역, 5);
        강남_판교_구간2 = Section.of(null, 강남역, 판교역, 7);
        강남_정자_구간 = Section.of(null, 강남역, 정자역, 10);
        판교_정자_구간 = Section.of(null, 판교역, 정자역, 10);
        정자_광교_구간 = Section.of(null, 정자역, 광교역, 10);
    }

    @Test
    void test_상하행_종점역이_같으면_같은_구간() {
        assertAll(
            () -> assertThat(강남_판교_구간1.hasSameStations(강남_판교_구간2)).isTrue(),
            () -> assertThat(강남_판교_구간1.hasSameStations(강남_정자_구간)).isFalse()
        );
    }

    @Test
    void test_상행_종점_일치() {
        assertAll(
            () -> assertThat(강남_판교_구간1.hasSameUpStation(강남_정자_구간)).isTrue(),
            () -> assertThat(강남_정자_구간.hasSameUpStation(판교_정자_구간)).isFalse()
        );
    }

    @Test
    void test_하행_종점_일치() {
        assertAll(
            () -> assertThat(강남_정자_구간.hasSameDownStation(판교_정자_구간)).isTrue(),
            () -> assertThat(강남_판교_구간1.hasSameDownStation(강남_정자_구간)).isFalse()
        );
    }
}