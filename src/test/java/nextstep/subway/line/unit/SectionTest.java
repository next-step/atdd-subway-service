package nextstep.subway.line.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    private Line 신분당선 = new Line("신분당선", "빨간색");
    private Station 강남역= new Station("강남역");
    private Station 미금역 = new Station("미금역");
    private Station 광교역 = new Station("광교역");


    @DisplayName("상행역 기준으로 구간 추가시 기존 구간의 상행역과 거리가 변경된다.")
    @Test
    void updateUpStationWhenAdd_test() {
        // given
        Section 구간 = new Section(신분당선, 강남역, 광교역, 10);
        // when

        구간.updateUpStation(미금역, 5);

        // then
        assertEquals(미금역, 구간.getUpStation());
        assertEquals(5, 구간.getDistance().value());
    }

    @DisplayName("하행역 기준으로 구간 추가시 기존 구간의 하행역과 거리가 변경된다.")
    @Test
    void updateDownStationWhenAdd_test() {
        // given
        Section 구간 = new Section(신분당선, 강남역, 광교역, 10);
        // when

        구간.updateDownStation(미금역, 5);

        // then
        assertEquals(미금역, 구간.getDownStation());
        assertEquals(5, 구간.getDistance().value());
    }

    @DisplayName("역 삭제로 기존 구간이 변경될때 기존 구간의 하행역이 변경되고 거리가 추가된다.")
    @Test
    void updateDownStationWhenDelete_test() {
        // given
        Section 구간 = new Section(신분당선, 강남역, 미금역, 5);
        // when

        구간.updateDownStationDelete(미금역, 10);

        // then
        assertEquals(미금역, 구간.getDownStation());
        assertEquals(15, 구간.getDistance().value());
    }


}
