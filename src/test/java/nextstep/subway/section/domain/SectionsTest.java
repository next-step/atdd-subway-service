package nextstep.subway.section.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
public class SectionsTest {

    private final Line line = new Line("2호선", "green");
    private final Station upStation = new Station("강남역");
    private final Station downStation = new Station("역삼역");
    private final Sections sections = new Sections();

    @DisplayName("새로 추가한 역 목록이 순서대로  반환되는지 테스트")
    @Test
    void given_Sections_when_GetListOfStation_then_ReturnListOfStation() {
        // given
        final Section firstSection = new Section(line, upStation, downStation, 100);
        final Station newStation = new Station("시청역");
        final Section secondSection = new Section(line, upStation, newStation, 10);
        sections.add(firstSection);
        sections.add(secondSection);

        // when
        List<Station> actual = sections.stations();

        // then
        assertThat(actual).isEqualTo(Arrays.asList(upStation, newStation, downStation));
    }

    @DisplayName("Section 을 추가했을 때 정상적으로 저장되는지 테스트")
    @Test
    void given_Station_when_AddSection_then_AddedToGivenSections() {
        // given
        final Section firstSection = new Section(line, upStation, downStation, 100);
        final Station newStation = new Station("신림역");
        final Section secondSection = new Section(line, upStation, newStation, 10);

        // when
        sections.add(firstSection);
        sections.add(secondSection);

        // then
        assertThat(sections.toList()).isEqualTo(Arrays.asList(firstSection, secondSection));
    }

    @DisplayName("존재하는 역을 추가할 때 예외가 발생하는지 테스트")
    @Test
    void given_ExistStation_when_AddSection_then_ThrownException() {
        // given
        final Section firstSection = new Section(line, upStation, downStation, 100);
        sections.add(firstSection);

        // when
        final Throwable throwable = catchThrowable(() -> sections.add(firstSection));

        // then
        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("존재하지 않은 역을 추가할 때 예외가 발생하는지 테스트")
    @Test
    void given_NotExistStation_when_AddSection_then_ThrownException() {
        // given
        final Section firstSection = new Section(line, upStation, downStation, 100);
        sections.add(firstSection);
        final Station 시청역 = new Station("시청역");
        final Station 당산역 = new Station("당산역");
        final Section section = new Section(line, 시청역, 당산역, 5);

        // when
        final Throwable throwable = catchThrowable(() -> sections.add(section));

        // then
        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }
}
