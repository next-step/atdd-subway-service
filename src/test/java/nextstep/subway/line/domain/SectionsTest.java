package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @DisplayName("구간에서 라인들을 추출한다.")
    @Test
    void extractLines() {
        //given
        Line 일호선 = new Line("1호선", "blue");
        Line 이호선 = new Line("2호선", "green");

        Station 영등포역 = new Station("영등포역");
        Station 신길역 = new Station("신길역");
        Station 뚝섬역 = new Station("뚝섬역");
        Station 건대역 = new Station("건대역");

        Section 첫번째_구간 = new Section(일호선, 영등포역, 신길역, 10);
        Section 두번째_구간 = new Section(이호선, 뚝섬역, 건대역, 20);
        Sections 구간들 = new Sections(List.of(첫번째_구간, 두번째_구간));

        //when
        List<Line> lines = 구간들.extractLines();

        //then
        assertThat(lines).containsExactly(
                일호선,
                이호선
        );
    }
}
