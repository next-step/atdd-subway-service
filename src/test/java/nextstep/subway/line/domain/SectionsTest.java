package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Sections 도메인 테스트")
class SectionsTest {

    private Station 양평역;
    private Station 영등포구청역;
    private Station 영등포시장역;
    private Station 신길역;
    private Station 오목교역;

    private Line 오호선;
    private Section 구간_영등포구청역_신길역;
    private Section 구간_양평역_영등포구청역;
    private Section 구간_영등포구청역_영등포시장역;
    private Section 구간_오목교역_영등포구청역;

    private Distance 기본_구간_거리_30 = new Distance(30);
    private Distance 절반_구간_거리_15 = new Distance(15);

    Sections sections = new Sections();

    @BeforeEach
    void 구간_생성() {
        양평역 = new Station(1L, "양평역");
        영등포구청역 = new Station(2L, "영등포구청역");
        영등포시장역 = new Station(3L, "영등포시장역");
        신길역 = new Station(4L, "신길역");
        오목교역 = new Station(5L, "오목교역");

        오호선 = new Line(1L, "5호선", "보라색", 영등포구청역, 신길역, 기본_구간_거리_30);

        구간_영등포구청역_신길역 = new Section(1L, 오호선, 영등포구청역, 신길역, 기본_구간_거리_30);
        구간_영등포구청역_영등포시장역 = new Section(2L, 오호선, 영등포구청역, 영등포시장역, 절반_구간_거리_15);

        구간_오목교역_영등포구청역 = new Section(3L, 오호선, 오목교역, 영등포구청역, 기본_구간_거리_30);
        구간_양평역_영등포구청역 = new Section(4L, 오호선, 양평역, 영등포구청역, 기본_구간_거리_30);
        sections = new Sections();
    }

    @Test
    void add_성공_케이스1() {
        // when
        assertDoesNotThrow(() -> sections.add(구간_영등포구청역_신길역));
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void add_성공_케이스2() {
        // when
        assertDoesNotThrow(() -> sections.add(구간_영등포구청역_신길역));
        assertDoesNotThrow(() -> sections.add(구간_영등포구청역_영등포시장역));
        assertDoesNotThrow(() -> sections.add(구간_양평역_영등포구청역));
        assertThat(sections.size()).isEqualTo(3);
    }
}