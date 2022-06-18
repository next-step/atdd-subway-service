package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    Station 강남역;
    Station 광교역;
    Station 판교역;
    Station 성수역;
    Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        판교역 = new Station("판교역");
        성수역 = new Station("성수역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        final Section section = new Section(신분당선, 광교역, 판교역, 10);
        신분당선.addSection(section);
    }

    @Test
    @DisplayName("순서에 맞게 역을 조회한다.")
    void searchStation() {
        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역, 판교역);
    }

    @Test
    @DisplayName("이미 추가할 구간에 상행역과 하행역이 존재하면 추가를 할 수 없다.")
    void addSectionValid() {
        final Section section = new Section(신분당선, 강남역, 판교역, 10);
        assertThatIllegalArgumentException().isThrownBy(
                () -> 신분당선.addSection(section)
        );
    }

    @Test
    @DisplayName("이미 추가할 구간에 상행역과 하행역이 존재하지 않으면 추가를 할 수 없다.")
    void addSectionExistValid() {
        final Section section = new Section(신분당선, 성수역, new Station("가로숲역"), 10);
        assertThatIllegalArgumentException().isThrownBy(
                () -> 신분당선.addSection(section)
        );
    }

    @Test
    @DisplayName("이미 추가할 구간에 거리보다 추가할 거리가 크면 추가 할수 없다.")
    void addSectionDistanceOver() {
        final Section section = new Section(신분당선, 성수역, 판교역, 10);


        assertThatIllegalArgumentException().isThrownBy(
                () -> 신분당선.addSection(section)
        );
    }

    @Test
    @DisplayName("가운대 구간이 추가된다")
    void addSection() {
        final Section section = new Section(신분당선, 성수역, 판교역, 2);
        신분당선.addSection(section);
        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역, 성수역, 판교역);
    }

    @Test
    @DisplayName("하행역 구간이 추가된다")
    void addDownSection() {
        final Section section = new Section(신분당선, 판교역, 성수역, 2);
        신분당선.addSection(section);
        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역, 판교역, 성수역);
    }

    @Test
    @DisplayName("상행역 구간이 추가된다")
    void addUpSection() {
        final Section section = new Section(신분당선, 성수역, 강남역, 2);
        신분당선.addSection(section);
        assertThat(신분당선.getStations()).containsExactly(성수역, 강남역, 광교역, 판교역);
    }


    @Test
    @DisplayName("역이 삭제된다.")
    void removeStation() {
        신분당선.removeStation(판교역);
        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }
}
