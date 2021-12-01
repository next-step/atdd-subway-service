package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.LineTest.신분당선;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Sections sections = new Sections();

    @BeforeEach
    void beforeEach() {
        sections.add(new Section(신분당선, 역삼역, 양재역, 10));
        sections.add(new Section(신분당선, 양재역, 사당역, 5));
    }

    @DisplayName("구간 추가")
    @Test
    void addSection() {
        Sections actual = new Sections();
        actual.add(new Section(신분당선, 역삼역, 양재역, 10));
        actual.add(new Section(신분당선, 양재역, 사당역, 5));

        assertThat(actual).isEqualTo(sections);
    }

    @DisplayName("두 지하철역 중 하나는 등록 되어 있어야 합니다.")
    @Test
    void notIncludeOneStationExceptionTest() {
        Station 잠실새내 = new Station("잠실새내");
        Section section = new Section(신분당선, 잠실역, 잠실새내, 3);

        assertThatThrownBy(() -> sections.add(section)).hasMessage("두 지하철역 중 하나는 등록 되어 있어야 합니다.");
    }

    @DisplayName("이미 등록된 구간 입니다.")
    @Test
    void alreadyAddSectionExceptionTest() {
        Section section = new Section(신분당선, 역삼역, 사당역, 3);

        assertThatThrownBy(() -> sections.add(section)).hasMessage("이미 등록된 구간 입니다.");
    }
}