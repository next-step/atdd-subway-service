package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    private Station 마곡역;
    private Station 마포역;
    private Station 김포공항역;
    private Station 공덕역;
    private Line 지하철5호선;

    @BeforeEach
    void setUp() {
        마곡역 = new Station("마곡역");
        마포역 = new Station("마포역");
        공덕역 = new Station("공덕역");
        김포공항역 = new Station("김포공항역");
        지하철5호선 = new Line("5호선", "bg-purple", 마곡역, 마포역, 10);
    }

    @DisplayName("상행선부터 하행선으로 정렬된 지하철 역을 조회한다.")
    @Test
    void getStations() {
        Section section = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Sections sections = Sections.from(Collections.singletonList(section));

        assertThat(sections.getStations()).hasSize(2)
            .containsExactly(마곡역, 마포역);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void addSection() {
        Section section1 = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Section section2 = Section.of(지하철5호선, 김포공항역, 마곡역, 10);
        Sections sections = Sections.from(Collections.singletonList(section1));

        sections.add(section2);

        assertThat(sections.getStations()).hasSize(3)
            .containsExactly(김포공항역, 마곡역, 마포역);
    }

    @DisplayName("동일한 구간을 추가하면 에러가 발생한다.")
    @Test
    void validateDuplicateException() {
        Section section = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Sections sections = Sections.from(Collections.singletonList(section));

        assertThatThrownBy(() -> sections.add(section))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간에 상행선, 하행선 둘다 존재하지 않는 역이면 에러가 발생한다.")
    @Test
    void validateNotExistException() {
        Section section1 = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Section section2 = Section.of(지하철5호선, 김포공항역, 공덕역, 10);
        Sections sections = Sections.from(Collections.singletonList(section1));

        assertThatThrownBy(() -> sections.add(section2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간의 지하철 역을 삭제할 수 있다.")
    @Test
    void remove() {
        Section section1 = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Section section2 = Section.of(지하철5호선, 김포공항역, 마곡역, 10);
        Sections sections = Sections.from(Arrays.asList(section1, section2));

        sections.remove(마곡역);

        assertThat(sections.getStations()).hasSize(2)
            .containsExactly(김포공항역, 마포역);
    }

    @DisplayName("하나의 구간만 존재하는 경우 지하철 역을 삭제하면 에러가 발생한다")
    @Test
    void validateOneSectionException() {
        Section section = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Sections sections = Sections.from(Collections.singletonList(section));

        assertThatThrownBy(() -> sections.remove(마곡역))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 지하철 역을 삭제하면 에러가 발생한다")
    @Test
    void validateNotExistStationException() {
        Section section = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Sections sections = Sections.from(Collections.singletonList(section));

        assertThatThrownBy(() -> sections.remove(김포공항역))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
