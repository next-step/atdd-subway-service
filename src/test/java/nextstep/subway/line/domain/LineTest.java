package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import nextstep.subway.path.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("Line 유닛 테스트")
class LineTest {

    @DisplayName("조회 응답")
    @Test
    void toResponse() {
        //given
        Line line = new Line("신분당선"
                , "빨간색"
                , new Station("강남역")
                , new Station("선릉역")
                , new Distance(5)
                , new Fare(900));
        //when
        List<StationResponse> stationResponse = line.stationResponses();
        //then
        assertThat(stationResponse.size()).isEqualTo(2);
    }
}