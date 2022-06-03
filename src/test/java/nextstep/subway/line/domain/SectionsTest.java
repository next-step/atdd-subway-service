package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private static final int DISTANCE = 5;

    private Line 신분당선;
    private Station 강남역;
    private Station 판교역;
    private Station 정자역;
    private Station 광교역;
    private Section 강남역_판교역_구간;
    private Section 정자역_광교역_구간;

    @BeforeEach
    void setUp() {
        신분당선 = Line.of("신분당선", "RED");
        강남역 = Station.from("강남역");
        판교역 = Station.from("판교역");
        정자역 = Station.from("정자역");
        광교역 = Station.from("광교역");
        강남역_판교역_구간 = Section.of(신분당선, 강남역, 판교역, DISTANCE);
        정자역_광교역_구간 = Section.of(신분당선, 정자역, 광교역, DISTANCE);
    }

    @DisplayName("지하철역 구간 Section을 목록으로 Sections를 생성한다.")
    @Test
    void generate01() {
        assertThatNoException().isThrownBy(() ->
            Sections.from(Arrays.asList(강남역_판교역_구간, 정자역_광교역_구간))
        );
    }
}