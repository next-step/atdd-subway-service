package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class LineTest {

    @Autowired
    public LineRepository lineRepository;

    @Autowired
    public StationRepository stationRepository;
    Station 강남역;
    Station 정자역;
    Line 신분당선;

    @BeforeEach
    public void init() {
        강남역 = 지하철역_저장하기("강남역");
        정자역 = 지하철역_저장하기("정자역");
        신분당선 = 노선_저장하기("신분당선", "red", 강남역, 정자역, 10);
    }


    @Test
    public void 노선에_포함된_역_순서대로_받기() {
        //when
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("강남역", "정자역"));
    }


    @Test
    public void 노선에_역_추가하기_후방() {
        //given
        Station 양재역 = 지하철역_저장하기("양재역");

        //when
        신분당선.addStation(강남역, 양재역, 3);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("강남역", "양재역", "정자역"));
    }

    @Test
    public void 노선에_역_추가하기_전방() {
        //given
        Station 양재역 = 지하철역_저장하기("양재역");

        //when
        신분당선.addStation(양재역, 정자역, 3);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("강남역", "양재역", "정자역"));
    }


    @Test
    public void 노선에_역_추가하기_노선의_시작() {
        //given
        Station 광교역 = 지하철역_저장하기("광교역");

        //when
        신분당선.addStation(정자역, 광교역, 3);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("강남역", "정자역", "광교역"));
    }


    @Test
    public void 노선에_역_추가하기_노선의_끝() {
        //given
        Station 선정릉역 = 지하철역_저장하기("선정릉역");

        //when
        신분당선.addStation(선정릉역, 강남역, 3);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("선정릉역", "강남역", "정자역"));
    }


    @Test
    public void 노선_가운데_역_삭제하기() {
        //given
        Station 선정릉역 = 지하철역_저장하기("선정릉역");
        신분당선.addStation(선정릉역, 강남역, 3);

        //when
        신분당선.removeStation(강남역);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("선정릉역", "정자역"));
    }


    @Test
    public void 노선_시작역_삭제하기() {
        //given
        Station 선정릉역 = 지하철역_저장하기("선정릉역");
        신분당선.addStation(선정릉역, 강남역, 3);

        //when
        신분당선.removeStation(정자역);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("선정릉역", "강남역"));
    }


    @Test
    public void 노선_마지막역_삭제하기() {
        //given
        Station 선정릉역 = 지하철역_저장하기("선정릉역");
        신분당선.addStation(선정릉역, 강남역, 3);

        //when
        신분당선.removeStation(선정릉역);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("강남역", "정자역"));
    }


    @Test
    public void 노선_없는역_삭제하기_에러발생() {
        //given
        Station 선정릉역 = 지하철역_저장하기("선정릉역");
        Station 동탄역 = 지하철역_저장하기("동탄역");
        신분당선.addStation(선정릉역, 강남역, 3);

        //when
        assertThatThrownBy(() -> 신분당선.removeStation(동탄역)).isInstanceOf(RuntimeException.class);
    }


    @Test
    public void 노선_단일구간_삭제하기_에러발생() {
        assertThatThrownBy(() -> 신분당선.removeStation(강남역)).isInstanceOf(RuntimeException.class);
    }


    private Station 지하철역_저장하기(String 지하철역이름) {
        Station 저장할_지하철역 = new Station(지하철역이름);
        stationRepository.save(저장할_지하철역);
        return 저장할_지하철역;
    }

    private Line 노선_저장하기(String 노선이름, String 노선색깔, Station 상향_종착지_지하철역, Station 하향_종착지_지하철역,
        int 거리) {
        Line 저장할_노선 = new Line(노선이름, 노선색깔, 상향_종착지_지하철역, 하향_종착지_지하철역, 거리);
        lineRepository.save(저장할_노선);
        return 저장할_노선;
    }

    private void 역_목록_순서_체크(List<Station> 실제_역목록_순서, List<String> 기대하는_역목록_이름) {
        Assertions.assertThat(실제_역목록_순서)
            .extracting("name")
            .containsExactlyElementsOf(기대하는_역목록_이름);
    }
}
