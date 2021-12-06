package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    @DisplayName("라인을 생성시 구간을 일급 콜렉션으로 넣어줌")
    @Test
    void createSections() {
        Station upStation = new Station("건대역");
        Station downStation = new Station("용마산역");

        Line line = new Line("7호선", "bg-red-600", upStation, downStation, 10);

        assertThat(line.getSections()).isNotNull();
    }


}
