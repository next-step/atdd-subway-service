package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineServiceTest {
    private LineService lineService = new LineService(null, null);

    @Test
    @DisplayName("getStations는 정렬된 순서로 반환한다")
    void getStations는_정렬된_순서로_반환한다() {
        // given
        Station 강남역 = new Station("강남");
        Station 양재역 = new Station("양재역");
        Station 청계산 = new Station("청계산");
        Station 판교 = new Station("판교");

        Line line = new Line("신분당선", "RED", 양재역, 청계산, 2);
        line.getSections().add(new Section(line, 청계산,  판교, 5));
        line.getSections().add(new Section(line, 강남역, 양재역, 5));

        // when
        List<Station> stations = lineService.getStations(line);

        // then
        assertThat(stations)
                .containsExactly(강남역, 양재역, 청계산, 판교);
    }
}