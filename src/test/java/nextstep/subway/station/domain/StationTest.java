package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

class StationTest {
    Station 왕십리;
    Station 신당;
    Station DDP;
    Station 을지로;

    Sections sections;

    @BeforeEach
    void setup() {
        왕십리 = Station.of(1L, "왕십리");
        신당 = Station.of(2L, "신당");
        DDP = Station.of(3L, "DDP");
        을지로 = Station.of(4L, "을지로");

        sections = Sections.from(Arrays.asList(
            Section.of(1L, 신당, DDP),
            Section.of(2L, 왕십리, 신당),
            Section.of(3L, DDP, 을지로)
        ));
    }

    @Test
    void 동등성() {
        assertThat(Station.of(1L, "왕십리")).isEqualTo(Station.of(1L, "상왕십리"));
    }
}
