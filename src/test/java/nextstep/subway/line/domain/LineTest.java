package nextstep.subway.line.domain;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest extends AcceptanceTest {

    @Test
    void findUpStationTest() {
        Line line = new Line();
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        Section section2 = new Section(line, 역삼역, 선릉역, 10);
        line.getSections().add(section1);
        line.getSections().add(section2);

        assertThat(line.findUpStation()).isEqualTo(강남역);
    }
}