package nextstep.subway.line.domain;

import nextstep.subway.line.domain.dummy.LineDummy;
import nextstep.subway.line.domain.dummy.SectionDummy;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.dummy.StationDummy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class LineTest {

    @DisplayName("Station 가지고 오기")
    @Test
    void getStationsTest() {
        // given
        Line 신분당선 = LineDummy.신분당선;
        지하철_노선에_역을_등록(신분당선);

        // when
        List<Station> expectedStations = 신분당선.getStations();
        List<Station> actualStations = Arrays.asList(
                StationDummy.강남역,
                StationDummy.양재역,
                StationDummy.양재시민의숲역,
                StationDummy.청계산입구역,
                StationDummy.판교역,
                StationDummy.정자역,
                StationDummy.미금역,
                StationDummy.동천역,
                StationDummy.광교역
        );

        // then
        지하철_노선에_역이_정렬됨(expectedStations, actualStations);
    }

    void 지하철_노선에_역을_등록(Line line) {
        line.getSections().add(SectionDummy.동천역_광교역);
        line.getSections().add(SectionDummy.강남역_양재역);
        line.getSections().add(SectionDummy.양재시민의숲역_청계산입구역);
        line.getSections().add(SectionDummy.미금역_동천역);
        line.getSections().add(SectionDummy.양재역_양재시민의숲역);
        line.getSections().add(SectionDummy.정자역_미금역);
        line.getSections().add(SectionDummy.청계산입구역_판교역);
        line.getSections().add(SectionDummy.판교역_정자역);
    }

    void 지하철_노선에_역이_정렬됨(List<Station> expectedStations, List<Station> actualStations) {

        List<String> expectedStationNames = expectedStations.stream().map(Station::getName).collect(Collectors.toList());
        List<String> actualStationNames = actualStations.stream().map(Station::getName).collect(Collectors.toList());

        Assertions.assertThat(expectedStationNames).containsExactlyElementsOf(actualStationNames);
    }
}
