package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.DomainTest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LineTest extends DomainTest {

    Station 강남역;
    Station 정자역;
    Line 신분당선;

    @BeforeEach
    public void init() {
        강남역 = 지하철역_저장하기("강남역");
        정자역 = 지하철역_저장하기("정자역");
        신분당선 = 노선_저장하기("신분당선", "red", 강남역, 정자역, 10);
    }

    /**
     * background given Line에 정자역 ~ 강남역 을 저장하고 when station순서를 출력할때 then 순서대로 출력이된다.
     */
    @Test
    public void 노선에_포함된_역_순서대로_받기() {
        //when
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("강남역", "정자역"));
    }

    /**
     * background
        * given Line에 정자역 ~ 강남역 을 저장하고
     * when 양재역 ~ 강남역을 추가하고 목록 조회시
     * then 순서대로 출력이된다.
     */
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

    /**
     * background
        * given Line에 정자역 ~ 강남역 을 저장하고
     * when 정자역 ~ 양재역을 추가하고 목록 조회시
     * then 순서대로 출력이된다.
     */
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

    /**
     * background
        * given Line에 정자역 ~ 강남역 을 저장하고
     * when 광교역 ~ 정자역을 추가하고 목록 조회시
     * then 순서대로 출력이된다.
     */
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

    /**
     * background
        * given Line에 정자역 ~ 강남역 을 저장하고
     * when 강남역 ~ 선정릉역을 추가하고 목록 조회시
     * then 순서대로 출력이된다.
     */
    @Test
    public void 노선에_역_추가하기_노선의_끝() {
        //given
        Station 선정릉역 = 지하철역_저장하기("선정릉역");

        //when
        신분당선.addStation(선정릉역, 강남역, 3);
        List<Station> 역_목록 = 신분당선.getStations();

        //then
        역_목록_순서_체크(역_목록, Arrays.asList("선정릉역","강남역", "정자역"));
    }

    /**
     * background
        * given Line에 정자역 ~ 강남역 을 저장하고
     * given 강남역 ~ 선정릉역을 추가 line에 추가한 뒤
     * when 강남역 삭제시
     * then 정상 삭제가 되고 순서가 정상적으로 출력된다.
     */
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

    /**
     * background
        * given Line에 정자역 ~ 강남역 을 저장하고
     * given 강남역 ~ 선정릉역을 추가 line에 추가한 뒤
     * when 정자역 삭제시
     * then 정상 삭제가 되고 순서가 정상적으로 출력된다.
     */
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

}
