package nextstep.subway.section.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionsTest {

    private final Line line = new Line("2호선", "green");
    private final Station upStation = new Station("강남역");
    private final Station downStation = new Station("역삼역");
    private final Sections sections = new Sections();

    @DisplayName("새로 추가한 2개의 역 목록이 순서대로  반환되는지 테스트")
    @Test
    void stations() {
        // given
        final Section firstSection = new Section(line, upStation, downStation, 100);
        sections.add(firstSection);

        // when
        List<Station> actual = sections.stations();

        // then
        assertThat(actual).isEqualTo(Arrays.asList(upStation, downStation));
    }
}
