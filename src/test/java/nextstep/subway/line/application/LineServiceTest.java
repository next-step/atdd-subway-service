package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({LineService.class, StationService.class})
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("구간에 대한 정렬된 지하철역들을 반환한다.")
    @Test
    void getStations() {
        //given
        Station 강남 = new Station("강남");
        Station 판교 = new Station("판교");
        Station 정자 = new Station("정자");
        Line 신분당선= new Line("신분당선", "bg-redd-100", 판교, 정자, 10);
        lineRepository.save(신분당선);
        stationRepository.save(강남);
        SectionRequest request = new SectionRequest(강남.getId(),판교.getId(),10);
        lineService.addLineStation(신분당선.getId(), request);

        //when
        List<Station> stations = lineService.getStations(신분당선);

        //then
        assertThat(stations).containsExactly(강남,판교,정자);
    }
}
