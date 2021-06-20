package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LineSectionsTest {

    private Station 강남역;
    private Station 판교역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        광교역 = new Station("광교역");
    }

    @Test
    @DisplayName("구간 사이 새로운 구간 추가")
    void add() {
        // given
        LineSections lineSections = new LineSections();

        // when
        lineSections.add(new Section(null, 강남역, 광교역, 10));
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // then
        assertThat(lineSections.toStations()).containsExactly(강남역, 판교역, 광교역);
    }
}
