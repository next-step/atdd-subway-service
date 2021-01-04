package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDistanceException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {
    private Station 양재역;
    private Section section;

    @BeforeEach
    void setUp() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        양재역 = new Station("양재역");

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        section = new Section(신분당선, 강남역, 광교역, new Distance(10));
    }

    @DisplayName("상행 Station과 거리(기존 거리 - 새로운 구간 길이)를 Update한다.")
    @Test
    void updateUpStation() {
        // when
        section.updateUpStation(양재역, new Distance(5));

        // then
        assertThat(section.getUpStation()).isEqualTo(양재역);
        assertThat(section.getDistance()).isEqualTo(new Distance(5));
    }

    @DisplayName("하행 Station과 거리(기존 거리 - 새로운 구간 길이)를 Update한다.")
    @Test
    void updateDownStation() {
        // when
        section.updateDownStation(양재역, new Distance(2));

        // then
        assertThat(section.getDownStation()).isEqualTo(양재역);
        assertThat(section.getDistance()).isEqualTo(new Distance(8));
    }

    @DisplayName("기존 구간 길이와 새로운 구간 길이가 같을 경우 update를 하지 못한다.")
    @Test
    void updateDistanceException1() {
        assertThatThrownBy(() -> section.updateDownStation(양재역, new Distance(10)))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("기존 구간 길이보다 새로운 구간 길이가 클 경우 update를 하지 못한다.")
    @Test
    void updateDistanceException2() {
        assertThatThrownBy(() -> section.updateUpStation(양재역, new Distance(20)))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
