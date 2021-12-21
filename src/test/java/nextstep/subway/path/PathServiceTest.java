package nextstep.subway.path;

import com.google.common.collect.Lists;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |리
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용하여 로그인한 사옹자의 경로조회 테스트한다.")
    @Test
    void findShortestPath() {
        // given
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 20);
        LineRepository lineRepository = mock(LineRepository.class);
        StationService stationService = mock(StationService.class);

        final Station 교대역 = Station.from(1L, "교대역");
        final Station 양재역 = Station.from(2L, "양재역");
        final Station 강남역 = Station.from(3L, "강남역");
        final Station 남부터미널역 = Station.from(4L, "남부터미널역");
        final Section 삼호선구간1 = new Section(1L, null, 교대역, 남부터미널역, Distance.of(10));
        final Section 삼호선구간2 = new Section(2L, null, 남부터미널역, 양재역, Distance.of(10));
        final Line 이호선 = new Line(1L, "이호선", "blue",
                Sections.from(Section.of(교대역, 강남역, Distance.of(20))), Fare.from(400));
        final Line 삼호선 = new Line(2L, "삼호선", "blue", Sections.from(Lists.newArrayList(삼호선구간1, 삼호선구간2)), Fare.from(200));
        삼호선구간1.addLine(삼호선);
        삼호선구간2.addLine(삼호선);
        final Line 신분당선 = new Line(3L, "신분당선", "blue", Sections.from(
                Section.of(강남역, 양재역, Distance.of(20))), Fare.from(300));
        final List<Section> sections = Lists.newArrayList(
                new Section(1L, 삼호선, 교대역, 남부터미널역, Distance.of(10)),
                new Section(2L, 삼호선, 남부터미널역, 양재역, Distance.of(10)));
        final List<Station> stations = Lists.newArrayList(
                교대역, 남부터미널역, 양재역);

        when(stationService.findStationById(1L)).thenReturn(교대역);
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(lineRepository.findAll())
                .thenReturn(Lists.newArrayList(이호선, 삼호선, 신분당선));

        PathService pathService = new PathService(lineRepository, stationService);

        // when
        PathResponse responses = pathService.findShortestPath(loginMember, 교대역.getId(), 양재역.getId());

        // then
        assertAll(
                () -> assertThat(responses.getStations()).hasSize(3),
                () -> assertThat(responses.getDistance()).isEqualTo(20),
                () -> assertThat(responses.getFare()).isEqualTo(new BigDecimal("1650")),
                () -> assertThat(responses.getStations()).containsExactly(
                        StationResponse.of(교대역), StationResponse.of(남부터미널역),
                        StationResponse.of(양재역))
        );
    }
}
