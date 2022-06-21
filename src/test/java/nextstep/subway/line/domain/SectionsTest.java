package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionsTest {
    private Station 판교역;
    private Station 곤지암역;
    private Station 여주역;
    private Line 경강선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        판교역 = new Station("판교역");
        곤지암역 = new Station("곤지암역");
        여주역 = new Station("여주역");
        경강선 = new Line("경강선", "blue");
        sections = new Sections();
    }

    @Test
    @DisplayName("구간을 추가한다. (역 사이에 새로운 역을 등록)")
    void addSection_between() {
        // given
        Section 판교역_여주역_구간 = new Section(경강선, 판교역, 여주역, 11);
        Section 곤지암역_여주역_구간 = new Section(경강선, 곤지암역, 여주역, 5);
        sections.add(판교역_여주역_구간);

        // when
        sections.add(곤지암역_여주역_구간);

        // then
        assertAll(
                () -> assertThat(sections.findAllStations()).containsExactly(판교역, 곤지암역, 여주역),
                () -> assertThat(sections.getSections().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("상행역이 같은 구간을 추가한다.")
    void addSection_up() {
        // given
        Section 판교역_여주역_구간 = new Section(경강선, 판교역, 여주역, 11);
        Section 판교역_곤지암역_구간 = new Section(경강선, 판교역, 곤지암역, 6);
        sections.add(판교역_여주역_구간);

        // when
        sections.add(판교역_곤지암역_구간);

        // then
        assertAll(
                () -> assertThat(sections.findAllStations()).contains(판교역, 곤지암역, 여주역),
                () -> assertThat(sections.getSections().stream()
                        .filter(x -> x.getUpStation().equals(곤지암역) && x.getDownStation().equals(여주역))
                        .findFirst()
                        .get()
                        .getDistance()).isEqualTo(5),
                () -> assertThat(sections.getSections().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("하행역이 같은 구간을 추가한다.")
    void addSection_down() {
        // given
        Section 판교역_여주역_구간 = new Section(경강선, 판교역, 여주역, 11);
        Section 곤지암역_여주역_구간 = new Section(경강선, 곤지암역, 여주역, 5);
        sections.add(판교역_여주역_구간);

        // when
        sections.add(곤지암역_여주역_구간);

        // then
        assertAll(
                () -> assertThat(sections.findAllStations()).contains(판교역, 곤지암역, 여주역),
                () -> assertThat(sections.getSections().stream()
                        .filter(x -> x.getUpStation().equals(판교역) && x.getDownStation().equals(곤지암역))
                        .findFirst()
                        .get()
                        .getDistance()).isEqualTo(6),
                () -> assertThat(sections.getSections().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("구간 추가 예외 - 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    public void addSection_fail_distance_size() {
        // given
        Section 판교역_곤지암역_구간 = new Section(경강선, 판교역, 곤지암역, 6);
        Section 판교역_여주역_구간 = new Section(경강선, 판교역, 여주역, 11);
        sections.add(판교역_곤지암역_구간);

        // then
        assertThatThrownBy(() -> {
            sections.add(판교역_여주역_구간);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
