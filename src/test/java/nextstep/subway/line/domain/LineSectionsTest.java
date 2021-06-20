package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
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
        lineSections.add(new Section(null, 강남역, 광교역, 10));

        // when
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // then
        assertThat(lineSections.toStations()).containsExactly(강남역, 판교역, 광교역);
    }

    @Test
    @DisplayName("상행 종점으로 구간 추가")
    void add2() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // when
        lineSections.add(new Section(null, 강남역, 판교역, 5));

        // then
        assertThat(lineSections.toStations()).containsExactly(강남역, 판교역, 광교역);
    }

    @Test
    @DisplayName("하행 종점으로 구간 추가")
    void add3() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 판교역, 5));

        // when
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // then
        assertThat(lineSections.toStations()).containsExactly(강남역, 판교역, 광교역);
    }

    @Test
    @DisplayName("구간 사이 새로운 구간 추가 시, 동일한 구간(상행선/하행선)의 경우 구간 등록 실패")
    void add_exception() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> lineSections.add(new Section(null, 강남역, 광교역, 10)));
    }

    @Test
    @DisplayName("구간 사이 새로운 구간 추가 시, 상행선 또는 하행선 포함하지 않은 경우 구간 등록 실패")
    void add_exception2() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));
        Station 당산역 = new Station("당산역");
        Station 신도림역 = new Station("신도림역");

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> lineSections.add(new Section(null, 당산역, 신도림역, 10)));
    }

    @Test
    @DisplayName("구간 사이 새로운 구간 추가 시, 기존 구간의 거리 보다 거리가 더 긴 경우 구간 등록 실패")
    void add_exception3() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> lineSections.add(new Section(null, 판교역, 광교역, 10)));
    }

    @DisplayName("구간의 중간역 삭제")
    @Test
    void removeLineSection() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // when
        lineSections.removeSection(null, 판교역);

        // then
        assertThat(lineSections.toStations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("구간의 하행 종점역 삭제")
    @Test
    void removeLineSection2() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // when
        lineSections.removeSection(null, 광교역);

        // then
        assertThat(lineSections.toStations()).containsExactly(강남역, 판교역);
    }

    @DisplayName("구간의 상행 종점역 삭제")
    @Test
    void removeLineSection3() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));
        lineSections.add(new Section(null, 판교역, 광교역, 5));

        // when
        lineSections.removeSection(null, 강남역);

        // then
        assertThat(lineSections.toStations()).containsExactly(판교역, 광교역);
    }

    @DisplayName("구간이 하나인 경우, 삭제 불가능")
    @Test
    void removeLineSection_exception() {
        // given
        LineSections lineSections = new LineSections();
        lineSections.add(new Section(null, 강남역, 광교역, 10));

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> lineSections.removeSection(null, 판교역));
    }
}
