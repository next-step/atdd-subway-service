package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    private Station testUpStation;
    private Station testDownStation;
    private Section testSection;

    @BeforeEach
    void makeDefaultLine() {
        testUpStation = new Station("서울역");
        testDownStation = new Station("금정역");
        testSection = new Section(testUpStation, testDownStation, 10);
    }

    @DisplayName("Section 생성시 0 이하의 거리 입력")
    @Test
    void constructSectionWithNegative() {
        assertThatThrownBy(() -> new Section(testUpStation, testDownStation, -10))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Section 생성시 역을 null 로 입력")
    @Test
    void constructSectionWithNull() {
        assertThatThrownBy(() -> new Section(testUpStation, null, 5))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Section 상행역 업데이트")
    @Test
    void updateUpStation() {
        Station upStation = new Station("소요산역");
        testSection.updateUpStation(upStation, 4);

        assertThat(testSection.getUpStation()).isEqualTo(upStation);
        assertThat(testSection.getDistance()).isEqualTo(6);
    }

    @DisplayName("Section 상행역 업데이트 시 distance 를 기존 구간 길이보다 길게 입력")
    @Test
    void updateUpStationWithOverDistance() {
        Station upStation = new Station("소요산역");

        assertThatThrownBy(() -> testSection.updateUpStation(upStation, 11))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Section 하행역 업데이트")
    @Test
    void updateDownStation() {
        Station downStation = new Station("동탄역");
        testSection.updateDownStation(downStation, 3);

        assertThat(testSection.getDownStation()).isEqualTo(downStation);
        assertThat(testSection.getDistance()).isEqualTo(7);
    }

    @DisplayName("Section 하행역 업데이트 시 distance 를 기존 구간 길이보다 길게 입력")
    @Test
    void updateDownStationWithOverDistance() {
        Station downStation = new Station("동탄역");

        assertThatThrownBy(() -> testSection.updateDownStation(downStation, 12))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Section 에 특정 Station 이 포함되어 있는지 확인")
    @Test
    void hasStation() {
        assertThat(testSection.hasStation(testUpStation)).isTrue();
        assertThat(testSection.hasStation(new Station("ABC"))).isFalse();
    }

    @DisplayName("Section 의 상행역이 특정 역과 동일역인지 확인")
    @Test
    void equalUpStation() {
        assertThat(testSection.equalUpStation(testUpStation)).isTrue();
        assertThat(testSection.equalUpStation(testDownStation)).isFalse();
    }

    @DisplayName("Section 의 하행역이 특정 역과 동일역인지 확인")
    @Test
    void equalDownStation() {
        assertThat(testSection.equalDownStation(testDownStation)).isTrue();
        assertThat(testSection.equalDownStation(testUpStation)).isFalse();
    }

}
