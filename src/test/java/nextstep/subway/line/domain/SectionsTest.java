package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    //lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
    Station 강남역 = new Station("강남역");
    Station 광교역 = new Station("광교역");
    Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("노선과 함께 구간을 생성한다.")
    void createSections() {
        assertThat(신분당선.getSections().size()).isEqualTo(1);
        assertThat(신분당선.getSections2().size()).isEqualTo(1);
    }


}
