package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.일호선;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_강남역_신촌역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_신촌역_역삼역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_역삼역_서울역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_용산역_역삼역;
import static nextstep.subway.line.domain.StationTest.강남역;
import static nextstep.subway.line.domain.StationTest.신촌역;
import static nextstep.subway.line.domain.StationTest.역삼역;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SectionsTest {

    public final Sections 일호선_구간들 = new Sections(일호선_구간_강남역_신촌역, 일호선_구간_신촌역_역삼역, 일호선_구간_역삼역_서울역,
        일호선_구간_용산역_역삼역);


    @Test
    void 첫번째역을_찾는다() {
        // when
        Station upStation = 일호선_구간들.findUpStation();

        // then
        Assertions.assertThat(upStation).isEqualTo(강남역);
    }

    @Test
    void 정렬된_역을_확인한다() {
        // given
        일호선.addSection(일호선_구간_강남역_신촌역);
        일호선.addSection(일호선_구간_신촌역_역삼역);

        // when
        List<Station> stations = 일호선.getStations();

        // then
        Assertions.assertThat(stations).containsExactly(강남역, 신촌역, 역삼역);
    }

    @Test
    void 이미_포함된구간은_추가되지_않는다() {
        // given
        일호선.addSection(일호선_구간_강남역_신촌역);
        일호선.addSection(일호선_구간_강남역_신촌역);

        // when
        List<Section> sections = 일호선.getSections();

        // then
        Assertions.assertThat(sections.size()).isEqualTo(1);
    }

}