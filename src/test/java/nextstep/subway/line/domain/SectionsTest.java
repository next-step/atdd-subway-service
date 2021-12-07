package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 목록 클래스 테스트")
public class SectionsTest {

    private static final Station 서울역 = mock(Station.class);
    private static final Station 남영역 = mock(Station.class);
    private static final Station 용산역 = mock(Station.class);
    private static final Station 노량진역 = mock(Station.class);
    static {
        when(서울역.getId()).thenReturn(1L);
        when(서울역.getName()).thenReturn("서울역");

        when(남영역.getId()).thenReturn(2L);
        when(남영역.getName()).thenReturn("남영역");

        when(용산역.getId()).thenReturn(3L);
        when(용산역.getName()).thenReturn("용산역");

        when(노량진역.getId()).thenReturn(4L);
        when(노량진역.getName()).thenReturn("노량진역");
    }

    @Test
    @DisplayName("서울역-남영역-용산역-노량진역 으로 정렬된 목록 조회")
    void sortedSections() {
        // given
        Sections sections = new Sections();
        sections.add(Section.create(용산역, 노량진역, Distance.valueOf(10)));
        sections.add(Section.create(서울역, 남영역, Distance.valueOf(5)));
        sections.add(Section.create(남영역, 용산역, Distance.valueOf(5)));

        //when
        List<Station> stations = sections.getSortedStations();

        //then
        assertAll(() -> {
            assertThat(stations.size()).isEqualTo(4);
            assertThat(stations).extracting(Station::getName)
                .containsExactly("서울역", "남영역", "용산역", "노량진역");
        });
    }

    @Test
    @DisplayName("이미 존재하는 구간 체크")
    void validateForAddedAlreadyExists() {

        final Line line = mock(Line.class);

        Sections sections = new Sections();
        sections.add(Section.create(line, 서울역, 남영역, Distance.valueOf(5)));

        //when&then
        assertTrue(sections.isAlreadySection(Section.create(line, 서울역, 남영역, Distance.valueOf(10))));
        assertFalse(sections.isAlreadySection(Section.create(line, 서울역, 용산역, Distance.valueOf(5))));
    }

    @Test
    @DisplayName("포함된 구간 체크")
    void validateForAddedNotInclude() {
        final Line line = mock(Line.class);
        Sections sections = new Sections();
        sections.add(Section.create(line, 서울역, 남영역, Distance.valueOf(5)));

        //when&then
        assertFalse(
            sections.isIncludeStationOfSection(Section.create(line, 용산역, 노량진역, Distance.valueOf(10))));
        assertTrue(
            sections.isIncludeStationOfSection(Section.create(line, 서울역, 용산역, Distance.valueOf(10))));
    }

    @Test
    @DisplayName("구간 목록에서 기존 구간이 있는 경우 상행역 변경")
    void updateUpStationBySection() {
        Sections sections = new Sections();
        sections.add(Section.create(서울역, 용산역, Distance.valueOf(10)));

        //when
        sections.updateOriginSectionByAdded(Section.create(서울역, 남영역, Distance.valueOf(5)));

        List<Station> stations = sections.getSortedStations();
        //then
        assertThat(stations).extracting(Station::getName)
            .containsExactly("남영역", "용산역");
    }

    @Test
    @DisplayName("구간 목록에서 기존 구간이 있는 경우 하행역 변경")
    void updateDownStationBySection() {
        Sections sections = new Sections();
        sections.add(Section.create(서울역, 용산역, Distance.valueOf(10)));

        //when
        sections.updateOriginSectionByAdded(Section.create(남영역, 용산역, Distance.valueOf(5)));

        List<Station> stations = sections.getSortedStations();
        //then
        assertThat(stations).extracting(Station::getName)
            .containsExactly("서울역", "남영역");
    }

    @Test
    @DisplayName("구간 삭제")
    void removeLineStation() {
        // given
        Sections sections = new Sections();
        sections.add(Section.create(서울역, 남영역, Distance.valueOf(5)));
        sections.add(Section.create(남영역, 용산역, Distance.valueOf(5)));

        sections.removeLineStation(남영역);

        List<Station> stations = sections.getSortedStations();

        //then
        assertAll(() -> {
            assertThat(stations.size()).isEqualTo(2);
            assertThat(stations).extracting(Station::getName)
                .containsExactly("서울역", "용산역");
        });
    }

    @Test
    @DisplayName("구간 삭제 시 포함된 구간이 없는 경우 NotFoundException 발생")
    void validateForRemoveNotInclude() {
        // given
        Sections sections = new Sections();
        sections.add(Section.create(서울역, 남영역, Distance.valueOf(5)));
        sections.add(Section.create(남영역, 용산역, Distance.valueOf(5)));

        assertThatThrownBy(() -> sections.removeLineStation(노량진역))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("역이 포함된 구간이 없습니다.");
    }

    @Test
    @DisplayName("구간 삭제 시 구간이 하나밖에 없는 경우 CannotDeleteException 발생")
    void validateForRemoveMinSize() {
        // given
        Sections sections = new Sections();
        sections.add(Section.create(서울역, 남영역, Distance.valueOf(5)));

        assertThatThrownBy(() -> sections.removeLineStation(서울역))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("구간이 하나는 존재해야 합니다.");
    }
}
