package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("경로 서비스 관련 기능")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    Station 강남역;
    Station 양재역;
    Station 양재시민의숲;

    LoginMember 어른;
    LoginMember 청소년;
    LoginMember 어린이;
    LoginMember 비회원;


    @BeforeEach
    void setUp() {
        givenStation(1L, "강남역");
        givenStation(2L, "양재역");
        givenStation(3L, "양재시민의숲");

        강남역 = stationService.findStationById(1L);
        양재역 = stationService.findStationById(2L);
        양재시민의숲 = stationService.findStationById(3L);

        어른 = LoginMember.of(1L, "email@gmail.com", 19);
        청소년 = LoginMember.of(1L, "email@gmail.com", 13);
        어린이 = LoginMember.of(1L, "email@gmail.com", 12);
        비회원 = LoginMember.fromGuest();
    }

    @Test
    void 추가요금이_없고_노선이_하나일때_최단_경로_조회() {
        // given
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 7);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(어른, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(9);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(1250));
        });
    }

    @DisplayName("추가요금이_있고_노선이_하나일때_최단_경로_조회 " +
            "- 기본요금 (1250 + 900 = 2150)")
    @Test
    void 추가요금이_있고_노선이_하나일때_최단_경로_조회() {
        // given
        int 신분당선_추가요금_900원 = 900;
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 7, 신분당선_추가요금_900원);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(어른, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(9);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(2150));
        });
    }

    @DisplayName("추가요금이_없고_총_거리가_10_50_사이인_경로의_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)")
    @Test
    void 추가요금이_없고_총_거리가_10_50_사이인_경로의_추가요금_조회() {
        // given
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 48, 0);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(어른, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(50);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(2050));
        });
    }

    @DisplayName("추가요금이_없고_총_거리가_50이상인_경로의_추가요금_조회 " +
            "- 이용거리 178KM " +
            "- 기본요금 (1250 + 2400 = 3,650원)")
    @Test
    void 추가요금이_없고_총_거리가_50이상인_경로의_추가요금_조회() {
        // given
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 176, 0);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(어른, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(178);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(3650));
        });
    }

    @DisplayName("로그인_사용자가_어린이이고_이용거리가_50KM_일때_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)" +
            "- 공제 (2,050 - 350 = 1,700원)" +
            "- 50% 할인 = 850원 ")
    @Test
    void 어린이의_이용거리가_50KM_일때_추가요금_조회() {
        // given
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 48, 0);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(어린이, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(50);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(850));
        });
    }

    @DisplayName("로그인_사용자가_청소년이고_이용거리가_50KM_일때_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)" +
            "- 공제 (2,050 - 350 = 1,700원)" +
            "- 20% 할인 = 1,360원 ")
    @Test
    void 청소년의_이용거리가_50KM_일때_추가요금_조회() {
        // given
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 48, 0);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(청소년, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(50);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(1360));
        });
    }

    @DisplayName("로그인_사용자가_비회원이고_이용거리가_50KM_일때_추가요금_조회 " +
            "- 이용거리 50KM " +
            "- 기본요금 (1250 + 800 = 2,050원)" +
            "- 공제 없음" +
            "- 할인 없음 = 2,050원 ")
    @Test
    void 비회원의_이용거리가_50KM_일때_추가요금_조회() {
        // given
        Line line = Line.of("신분당선", "red", 강남역, 양재역, 48, 0);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(비회원, PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        assertAll(() -> {
            assertThat(path.getStations())
                    .extracting("name")
                    .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
            assertThat(path.getDistance()).isEqualTo(50);
            assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(2050));
        });
    }

    private void givenStation(long id, String name) {
        given(stationService.findStationById(id)).willReturn(Station.of(id, name));
    }
}
