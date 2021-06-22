package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    private Line 사호선;
    Station 회현역;
    Station 서울역;
    Station 명동역;

    @BeforeEach
    void setUp() {
        회현역 = new Station("회현역");
        서울역 = new Station("서울역");
        명동역 = new Station("명동역");
    }

    @DisplayName("입력한 순서대로 지하철역 반환")
    @Test
    void 입력_순서대로_지하철역_반환() {

        Section firstSection = new Section(사호선, 회현역, 명동역, 30);
        Section secondSection = new Section(사호선, 서울역, 회현역, 30);

        사호선.getSections().add(firstSection);
        사호선.getSections().add(secondSection);

        //When
        List<Station> expectedStations = 사호선.getSortedStation();

        //Then
        assertThat(expectedStations).containsExactly(서울역, 회현역, 명동역);
    }

    @DisplayName("신규 노선에 구간 추가")
    @Test
    void 신규_노선에_구간_추가() {

        //Given
        Section firstSection = new Section(사호선, 회현역, 명동역, 30);

        //When
        사호선.getSections().add(firstSection);

        //Then
        assertThat(사호선.getSections()).containsExactly(firstSection);

    }
}
