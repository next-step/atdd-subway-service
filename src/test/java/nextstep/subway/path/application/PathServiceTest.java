package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private PathService pathService;

    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    final Station 강남역 = new Station(1L, "강남역");
    final Station 역삼역 = new Station(2L,"역삼역");
    final Station 교대역 = new Station(3L,"교대역");
    final Station 양재시민의숲역 = new Station(4L,"양재시민의숲역");
    final Station 양재역 = new Station(5L,"양재역");
    final Station 남부터미널역 = new Station(6L,"남부터미널역");
    final Station 인천역 = new Station(7L,"인천역");
    final Station 동인천역 = new Station(8L, "동인천역");
    final Station 도원역 = new Station(9L, "도원역");
    final Station 부평역 = new Station(10L, "부평역");
    final Station 신도림역 = new Station(11L, "신도림역");
    final Station 서울역 = new Station(12L, "서울역");
    final Station 동두천역 = new Station(13L, "동두천역");
    final Station 판교역 = new Station(14L, "판교역");
    final Station 정자역 = new Station(15L, "정자역");
    final Station 경기광주역 = new Station(16L, "경기광주역");

    final Map<Long, Station> stationMap = new HashMap();

    final Line 일호선 = new Line ("일호선", "bg-blue-600");
    final Line 이호선 = new Line ("이호선", "bg-green-600");
    final Line 삼호선 = new Line ("이호선", "bg-yellow-600");
    final Line 신분당선 = new Line("신분당선", "bg-red-600", new ExtraFare(100));
    final Line 경강선 = new Line("경강선", "bg-blue-400", new ExtraFare(200));

    final LoginMember 어린이 = new LoginMember(1L,"kid@test.com", 10);
    final LoginMember 청소년 = new LoginMember(2L,"teenager@test.com", 15);
    final LoginMember 어른 = new LoginMember(3L,"adult@test.com", 30);

    /**
     * 교대역    --- *2호선* ---   강남역    --- *2호선* ---   역삼역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역    --- *신분당선* ---   양재시민의숲역
     *                          |
     *                          *신분당선*
     *                          |
     *                          판교역    --- *경강선* ---   경기광주역
     *                          |
     *                          *신분당선*
     *                          |
     *                          정자역
     *
     * 인천역    --- *1호선* ---   동인천역     --- *1호선* ---   도원역     --- *1호선* ---   신도림역     --- *1호선* ---   서울역     --- *1호선* ---   동두천역
     */
    @BeforeEach
    void setUpBeforeEach() {
        일호선.addSection(new Section(일호선, 동두천역, 서울역, new Distance(40)));
        일호선.addSection(new Section(일호선, 서울역, 신도림역, new Distance(10)));
        일호선.addSection(new Section(일호선, 신도림역, 부평역, new Distance(27)));
        일호선.addSection(new Section(일호선, 부평역, 도원역, new Distance(14)));
        일호선.addSection(new Section(일호선, 도원역, 동인천역, new Distance(6)));
        일호선.addSection(new Section(일호선, 동인천역, 인천역, new Distance(5)));

        이호선.addSection(new Section(이호선, 역삼역, 강남역, new Distance(10)));
        이호선.addSection(new Section(이호선, 강남역, 교대역, new Distance(5)));

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, new Distance(3)));
        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, new Distance(2)));

        신분당선.addSection(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        신분당선.addSection(new Section(신분당선, 양재역, 양재시민의숲역, new Distance(10)));
        신분당선.addSection(new Section(신분당선, 양재시민의숲역, 판교역, new Distance(35)));
        신분당선.addSection(new Section(신분당선, 판교역, 정자역, new Distance(25)));

        경강선.addSection(new Section(경강선, 판교역, 경기광주역, new Distance(25)));

        lineService = new LineService(lineRepository, stationService);
        pathService = new PathService(stationService, lineService);

        stationMap.clear();
        stationMap.put(1L, 강남역);
        stationMap.put(2L, 역삼역);
        stationMap.put(3L, 교대역);
        stationMap.put(4L, 양재시민의숲역);
        stationMap.put(5L, 양재역);
        stationMap.put(6L, 남부터미널역);
        stationMap.put(7L, 인천역);
        stationMap.put(8L, 동인천역);
        stationMap.put(9L, 도원역);
        stationMap.put(10L, 부평역);
        stationMap.put(11L, 신도림역);
        stationMap.put(12L, 서울역);
        stationMap.put(13L, 동두천역);
        stationMap.put(14L, 판교역);
        stationMap.put(15L, 정자역);
        stationMap.put(16L, 경기광주역);
    }

    @Test
    void findShortestRoute() {
        //given
        when(stationService.findStationById(1L)).thenReturn(교대역);
        when(stationService.findStationById(5L)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        //when
        PathResponse pathResponse = pathService.findShortestRoute(1L, 5L, LoginMember.GUEST);

        //then
        assertThat(pathResponse.getStations().stream().map(station -> station.getName()).collect(Collectors.toList()))
                .containsExactly("교대역","남부터미널역","양재역");
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @ParameterizedTest
    @DisplayName("노선 추가요금 테스트")
    @CsvSource(value = {"5:1350", "4:1550", "14:2250", "15:2550", "16:2650"}, delimiter = ':')
    void lineExtraFeeTest(Long targetId, int expectedFare) {
        //given
        Long sourceId = 1L;
        when(stationService.findStationById(sourceId)).thenReturn(stationMap.get(sourceId));
        when(stationService.findStationById(targetId)).thenReturn(stationMap.get(targetId));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 삼호선, 신분당선, 경강선));

        //when
        int fare = pathService.findShortestRoute(sourceId, targetId, LoginMember.GUEST).getFare();

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @DisplayName("비회원 요금 테스트")
    @CsvSource(value = {"8:1250", "9:1350", "10:1550", "11:2150", "12:2250", "13:2750"}, delimiter = ':')
    void noLoginFeeTest(Long targetId, int expectedFare) {
        //given
        Long sourceId = 7L;
        when(stationService.findStationById(sourceId)).thenReturn(stationMap.get(sourceId));
        when(stationService.findStationById(targetId)).thenReturn(stationMap.get(targetId));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 삼호선, 신분당선, 경강선));

        //when
        int fare = pathService.findShortestRoute(sourceId, targetId, LoginMember.GUEST).getFare();

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @DisplayName("비회원 요금 테스트")
    @CsvSource(value = {"8:1250", "9:1350", "10:1550", "11:2150", "12:2250", "13:2750"}, delimiter = ':')
    void adultLoginFeeTest(Long targetId, int expectedFare) {
        //given
        Long sourceId = 7L;
        when(stationService.findStationById(sourceId)).thenReturn(stationMap.get(sourceId));
        when(stationService.findStationById(targetId)).thenReturn(stationMap.get(targetId));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 삼호선, 신분당선, 경강선));

        //when
        int fare = pathService.findShortestRoute(sourceId, targetId, 어른).getFare();

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @DisplayName("어린이 요금 테스트")
    @CsvSource(value = {"8:450", "9:500", "10:600", "11:900", "12:950", "13:1200"}, delimiter = ':')
    void kidLoginFeeTest(Long targetId, int expectedFare) {
        //given
        Long sourceId = 7L;
        when(stationService.findStationById(sourceId)).thenReturn(stationMap.get(sourceId));
        when(stationService.findStationById(targetId)).thenReturn(stationMap.get(targetId));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 삼호선, 신분당선, 경강선));

        //when
        int fare = pathService.findShortestRoute(sourceId, targetId, 어린이).getFare();

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @DisplayName("청소년 요금 테스트")
    @CsvSource(value = {"8:720", "9:800", "10:960", "11:1440", "12:1520", "13:1920"}, delimiter = ':')
    void teenagerLoginFeeTest(Long targetId, int expectedFare) {
        //given
        Long sourceId = 7L;
        when(stationService.findStationById(sourceId)).thenReturn(stationMap.get(sourceId));
        when(stationService.findStationById(targetId)).thenReturn(stationMap.get(targetId));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선, 삼호선, 신분당선, 경강선));

        //when
        int fare = pathService.findShortestRoute(sourceId, targetId, 청소년).getFare();

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

}
