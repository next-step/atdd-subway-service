package nextstep.subway;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DomainTest {

    @Autowired
    public LineRepository lineRepository;

    @Autowired
    public StationRepository stationRepository;

    public Station 지하철역_저장하기(String 지하철역이름) {
        Station 저장할_지하철역 = new Station(지하철역이름);
        stationRepository.save(저장할_지하철역);
        return 저장할_지하철역;
    }

    public Line 노선_저장하기(String 노선이름, String 노선색깔, Station 상향_종착지_지하철역, Station 하향_종착지_지하철역,
        int 거리) {
        Line 저장할_노선 = new Line(노선이름, 노선색깔, 상향_종착지_지하철역, 하향_종착지_지하철역, 거리);
        lineRepository.save(저장할_노선);
        return 저장할_노선;
    }

    public void 역_목록_순서_체크(List<Station> 실제_역목록_순서, List<String> 기대하는_역목록_이름) {
        Assertions.assertThat(실제_역목록_순서)
            .extracting("name")
            .containsExactlyElementsOf(기대하는_역목록_이름);
    }

}
