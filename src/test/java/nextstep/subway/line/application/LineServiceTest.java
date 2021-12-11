package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("노선 서비스 클래스 테스트")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    LineRepository lineRepository;

    @Mock
    StationService stationService;

    @InjectMocks
    LineService lineService;

    private Station seoulStation;
    private Station yongsanStation;
    private Line line;

    @BeforeEach
    public void setUp() {
        seoulStation = new Station("서울역");
        yongsanStation = new Station("용산역");
        // given 지하철 역 저장되어 있음
        line = Line.of("1호선", "blue", seoulStation, yongsanStation, 10);
    }

    @Test
    @DisplayName("라인 저장")
    void saveLine() {

        when(stationService.findStation(1L))
            .thenReturn(seoulStation);
        when(stationService.findStation(2L))
            .thenReturn(yongsanStation);
        when(lineRepository.save(ArgumentMatchers.any()))
            .thenReturn(line);
        LineRequest lineRequest = new LineRequest("1호선", "blue", 1L, 2L, 3, 900);

        // when 라인 저장 요청
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then 라인 저장되어 목록 조회
        assertNotNull(lineResponse);
        assertAll(() -> {
            assertEquals(lineResponse.getName(), "1호선");
            assertSortedStations(lineResponse.getStations(), "서울역", "용산역");
        });
    }

    @Test
    @DisplayName("라인 목록 조회")
    void findLines() {
        //given 노선, 지하철 역 저장되어 있음

        when(lineRepository.findAll())
            .thenReturn(Arrays.asList(line));

        // 목록 조회
        List<LineResponse> lines = lineService.findLineResponses();

        assertAll(() -> {
            assertThat(lines.size()).isEqualTo(1);
            assertSortedStations(lines.get(0).getStations(), "서울역", "용산역");
        });
    }

    @Test
    @DisplayName("라인 ID로 한건 조회")
    void findLineResponseById() {

        //given 라인 저장되어 있음
        when(lineRepository.findById(1L))
            .thenReturn(Optional.of(line));

        // 한건 조회
        LineResponse lineResponse = lineService.findLineResponseById(1L);

        assertAll(() -> {
            assertEquals(lineResponse.getName(), "1호선");
            assertSortedStations(lineResponse.getStations(), "서울역", "용산역");
        });
    }

    @Test
    @DisplayName("라인 수정")
    void updateLine() {
        //given 라인 저장되어 있음
        when(lineRepository.findById(1L))
            .thenReturn(Optional.of(line));

        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("1호선");
            assertThat(line.getColor()).isEqualTo("blue");
        });

        LineUpdateRequest lineRequest = new LineUpdateRequest("2호선", "green", 900);

        lineService.updateLine(1L, lineRequest);

        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("2호선");
            assertThat(line.getColor()).isEqualTo("green");
        });
    }

    @Test
    @DisplayName("라인에 구간 추가")
    void addLineStation() {
        Station addStation = new Station("남영역");

        when(stationService.findStation(1L))
            .thenReturn(seoulStation);
        when(stationService.findStation(2L))
            .thenReturn(addStation);
        when(lineRepository.findById(ArgumentMatchers.any()))
            .thenReturn(Optional.of(line));

        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 3);

        lineService.addLineStation(1L, sectionRequest);

        assertThat(line.getStations()).extracting(Station::getName).containsExactly("서울역", "남영역", "용산역");
    }

    @Test
    @DisplayName("라인에 포함된 역 삭제")
    void removeLineStation() {
        Station addStation = new Station("남영역");

        when(stationService.findStation(2L))
            .thenReturn(addStation);
        when(lineRepository.findById(ArgumentMatchers.any()))
            .thenReturn(Optional.of(line));

        line.addLineStation(Section.create(seoulStation, addStation, Distance.valueOf(5)));

        lineService.removeLineStation(1L, 2L);

        assertThat(line.getStations()).extracting(Station::getName).containsExactly("서울역", "용산역");
    }

    private void assertSortedStations(List<StationResponse> stations, String... expected) {
        assertThat(stations)
            .extracting(StationResponse::getName)
            .containsExactly(expected);
    }
}