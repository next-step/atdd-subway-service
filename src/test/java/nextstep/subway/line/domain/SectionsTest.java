package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    @DisplayName("구간들 길이(합) 테스트")
    @Test
    void getDistance() {
        List<Section> sections = new ArrayList<>();
        sections.add(createSection(Line.createEmpty(), createStation(1L, "강남역"), createStation(2L, "양재역"), Distance.valueOf(10)));
        sections.add(createSection(Line.createEmpty(), createStation(2L, "양재역"), createStation(4L, "양재시민의숲역"), Distance.valueOf(5)));
        assertThat(Sections.valueOf(sections).distance()).isEqualTo(Distance.valueOf(15));
    }

    Station createStation(long id, String name) {
        return Station.builder(name)
                .id(id)
                .build();
    }

    Section createSection(Line line, Station upStation, Station downStation, Distance distance) {
        return Section.builder(line, upStation, downStation, distance)
                .build();
    }
}
