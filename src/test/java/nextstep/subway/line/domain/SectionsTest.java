package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Sections")
class SectionsTest {

    private Sections 구간_목록;
    private Station 논현역;
    private Station 정자역;

    @BeforeEach
    void setUp() {
        구간_목록 = new Sections();
        논현역 = new Station("논현역");
        정자역 = new Station("정자역");
        구간_목록.addSection(Section.of(null, 논현역, 정자역, 100));
    }

    @Test
    @DisplayName("구간 추가")
    public void addSection() {
        // When
        Station 강남역 = new Station("강남역");
        구간_목록.addSection(Section.of(null, 논현역, 강남역, 50));

        // Then
        assertAll(
            () -> assertThat(구간_목록.getElements()).hasSize(2),
            () -> assertThat(구간_목록.getAllStation()).hasSize(3)
                .containsExactly(논현역, 강남역, 정자역)
        );
    }

    @Test
    @DisplayName("노선에 존재하는 역으로만 구성된 구간 추가 시 예외")
    public void throwException_WhenAlreadyExistStations() {
        // Given
        Station 강남역 = new Station("강남역");
        구간_목록.addSection(Section.of(null, 논현역, 강남역, 50));

        // When & Then
        Section 노선에_존재하는_역으로만_구성된_구간 = Section.of(null, 강남역, 정자역, 5);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> 구간_목록.addSection(노선에_존재하는_역으로만_구성된_구간));
    }

    @Test
    @DisplayName("추가하는 구간의 거리가 전체 구간의 총 길이 보다 큰 경우")
    public void throwException_WhenAddingSectionsDistanceIsOverThanLinesTotalDistance() {
        // Given
        Station 강남역 = new Station("강남역");
        Section 전체_구간의_총_길이보다_긴_구간 = Section.of(null, 논현역, 강남역, 100);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> 구간_목록.addSection(전체_구간의_총_길이보다_긴_구간));
    }

    @Test
    @DisplayName("추가하는 구간의 거리가 추가되는 역과 역 사이의 거리 보다 큰 경우")
    public void throwException_WhenAddingSectionsDistanceIsOverThanConnectedStationsDistance() {
        // Given
        Station 강남역 = new Station("강남역");
        Station 신논현역 = new Station("신논현역");
        구간_목록.addSection(Section.of(null, 논현역, 신논현역, 5));
        Section 역과_역_사이의_거리보다_큰_구간 = Section.of(null, 논현역, 강남역, 5);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> 구간_목록.addSection(역과_역_사이의_거리보다_큰_구간));
    }

    @Test
    @DisplayName("구간에 포함된 역과 점점이 없는 역으로 구성된 구간 추가 시 예외")
    public void throwException_WhenSectionIsNotConnectedAnyStations() {
        // Given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section 구간에_포함된_역과_접점이_없는_구간 = Section.of(null, 강남역, 양재역, 100);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> 구간_목록.addSection(구간에_포함된_역과_접점이_없는_구간));
    }

    @Test
    @DisplayName("구간 제거")
    public void remove() {
        // Given
        Station 강남역 = new Station("강남역");
        구간_목록.addSection(Section.of(null, 강남역, 정자역, 50));

        // When
        구간_목록.remove(논현역);

        // Then
        assertThat(구간_목록.getElements()).hasSize(1);
        assertThat(구간_목록.getAllStation()).hasSize(2)
            .containsExactly(강남역, 정자역);
    }

    @Test
    @DisplayName("단일 구간인 노선의 구간 제거 시 예외")
    public void throwException() {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> 구간_목록.remove(논현역));
    }

    @Test
    @DisplayName("구간에 존재하지 않는 역 삭제 시 예외")
    public void throwException_WhenRemoveTargetStationIsNotContains() {
        // Given
        Station 천호역 = new Station("천호역");

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> 구간_목록.remove(천호역));
    }
}
