package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.일호선_구간_강남역_신촌역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_신촌역_역삼역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_역삼역_서울역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_용산역_역삼역;
import static nextstep.subway.line.domain.SectionTest.일호선_존재하지않는_용문역_양평역;
import static nextstep.subway.line.domain.StationTest.강남역;

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
    void 이미_등록된_구간_입니다() {
        // then
        Assertions.assertThatThrownBy(() -> {
                일호선_구간들.addLineStation(일호선_구간_신촌역_역삼역);
            }).isInstanceOf(RuntimeException.class)
            .hasMessageStartingWith("이미 등록된 구간 입니다.");
    }

    @Test
    void 등록할_수_없는_구간_입니다() {
        // then
        Assertions.assertThatThrownBy(() -> {
                일호선_구간들.addLineStation(일호선_존재하지않는_용문역_양평역);
            }).isInstanceOf(RuntimeException.class)
            .hasMessageStartingWith("등록할 수 없는 구간 입니다.");
    }

}