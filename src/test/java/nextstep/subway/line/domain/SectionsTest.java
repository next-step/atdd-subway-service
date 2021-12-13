package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 도메인 관련 기능")
public class SectionsTest {

    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Line 신분당선;
    private Section 광교_판교_구간;

    @BeforeEach
    public void setUp() {

        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 판교역, 20);
        광교_판교_구간 = new Section(신분당선, 광교역, 판교역, 10);
    }

    @DisplayName("노선에 구간을 추가한다.")
    @Test
    void addLineStation() {
        // when
        Sections 신분당선_구간_목록 = 신분당선.getSections();
        신분당선_구간_목록.addLineStation(광교_판교_구간);

        // then
        assertThat(신분당선_구간_목록.getSections().size()).isEqualTo(2);
        assertThat(신분당선_구간_목록.getStations()).containsExactly(강남역, 광교역, 판교역);
    }

    @DisplayName("구간 목록으로부터 상행 역을 가져온다.")
    @Test
    void findUpStation() {
        // given
        Sections 신분당선_구간_목록 = 신분당선.getSections();
        신분당선_구간_목록.addLineStation(광교_판교_구간);

        // when, then
        assertThat(신분당선_구간_목록.findUpStation()).isEqualTo(강남역);
    }

    @DisplayName("구간 목록으로부터 정렬된 역 목록을 가져온다. up -> down")
    @Test
    void getSortedStation() {
        // given
        Sections 신분당선_구간_목록 = 신분당선.getSections();
        신분당선_구간_목록.addLineStation(광교_판교_구간);

        // when, then
        assertThat(신분당선_구간_목록.getStations()).containsExactly(강남역, 광교역, 판교역);
    }

    @DisplayName("구간 목록내에 역이 포함되는지 확인한다.")
    @Test
    void checkIfContainsStation() {
        // given
        Sections 신분당선_구간_목록 = 신분당선.getSections();

        // when, then
        assertThat(신분당선_구간_목록.containsStation(강남역)).isTrue();
        assertThat(신분당선_구간_목록.containsStation(광교역)).isFalse();
    }

    @DisplayName("노선에서 역을 제거한다.")
    @Test
    void removeLineStation() {
        // given
        Sections 신분당선_구간_목록 = 신분당선.getSections();
        신분당선_구간_목록.addLineStation(광교_판교_구간);

        // when
        신분당선_구간_목록.removeLineStation(광교역);

        // then
        assertThat(신분당선_구간_목록.getSections().size()).isEqualTo(1);
        assertThat(신분당선_구간_목록.getStations()).containsExactly(강남역, 판교역);
    }

}
