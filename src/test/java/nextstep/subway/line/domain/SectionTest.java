package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.line.domain.LineTest.신분당선;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionTest {

    private Section section;

    @BeforeEach
    void setUp() {
        section = new Section(신분당선, 양재역, 역삼역, 10);
    }

    @DisplayName("입력받은 구간이 두 역 사이에 포함되어있다.")
    @Test
    void isIncludeOneStationTest() {
        Section actual = new Section(신분당선, 양재역, 잠실역, 10);
        assertThat(section.isIncludeSection(actual)).isTrue();
    }

    @DisplayName("입력받은 구간이 종점을 포함하고 있다.")
    @Test
    void isIncludeOneEndStationTest() {
        Section actual = new Section(신분당선, 역삼역, 잠실역, 10);
        assertThat(section.isIncludeSection(actual)).isFalse();
    }

    @DisplayName("하행역이 구간 사이에 있을 경우")
    @ParameterizedTest
    @ValueSource(ints = {
            1, 9
    })
    void downStationBetweenSection(int distance) {
        Section actual = new Section(신분당선, 양재역, 잠실역, distance);
        section.updateStationByAddSection(actual);
        assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(잠실역),
                () -> assertThat(section.getDistance()).isEqualTo(10 - actual.getDistance())
        );
    }

    @DisplayName("상행역이 구간 사이에 있을 경우")
    @ParameterizedTest
    @ValueSource(ints = {
            1, 9
    })
    void upStationBetweenSection(int distance) {
        Section actual = new Section(신분당선, 잠실역, 역삼역, distance);
        section.updateStationByAddSection(actual);
        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(잠실역),
                () -> assertThat(section.getDistance()).isEqualTo(10 - actual.getDistance())
        );
    }

    @DisplayName("기존 구간의 길이보다 크거나 같으면 등록 할 수 없다.")
    @Test
    void distanceOver() {
        Section actual = new Section(신분당선, 양재역, 잠실역, 10);
        assertThatThrownBy(() -> section.updateStationByAddSection(actual)).hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
