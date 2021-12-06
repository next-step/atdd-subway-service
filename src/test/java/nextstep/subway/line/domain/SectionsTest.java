package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {


    @Test
    @DisplayName("서울역-남영역-용산역-노량진역 으로 정렬된 목록 조회")
    void sortedSections() {
        // given
        Station 서울역 = new Station("서울역");
        Station 남영역 = new Station("남영역");
        Station 용산역 = new Station("용산역");
        Station 노량진역 = new Station("노량진역");

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
}
