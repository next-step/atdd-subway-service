package nextstep.subway.line.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PathServiceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private PathService pathService;

    private Station 강남역;
    private Station 삼성역;
    private Station 잠실역;

    private Station 덕소역;
    private Station 구리역;

    @BeforeEach
    void beforeEach() {
        databaseCleanup.execute();

        강남역 = new Station("강남역");
        삼성역 = new Station("삼성역");
        잠실역 = new Station("잠실역");
        덕소역 = new Station("덕소역");
        구리역 = new Station("구리역");

        Section 강남_삼성 = new Section(강남역, 삼성역, 5);
        Section 삼성_잠실 = new Section(삼성역, 잠실역, 10);

        Section 덕소_구리 = new Section(덕소역, 구리역, 3);

        Line 이호선 = new Line("이호선", "빨강색");
        이호선.addSection(강남_삼성);
        이호선.addSection(삼성_잠실);
        lineRepository.save(이호선);

        Line 중앙선 = new Line("중앙선", "파랑색");
        중앙선.addSection(덕소_구리);
        lineRepository.save(중앙선);

        stationRepository.save(강남역);
        stationRepository.save(삼성역);
        stationRepository.save(잠실역);
        stationRepository.save(덕소역);
        stationRepository.save(구리역);
    }

    @DisplayName("출발지와 목적지 사이의 경로를 찾아보자(GuestMode)")
    @Test
    void findPathGuestMode() {
        PathResponse response = pathService.findPath(new LoginMember(), 강남역.getId(), 잠실역.getId());

        List<String> stationNames = response.getStations().stream()
                .map(stationResponse -> stationResponse.getName())
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly(강남역.getName(),
                삼성역.getName(), 잠실역.getName());
        assertThat(response.getDistance()).isEqualTo(15);
        assertThat(response.getFare()).isEqualTo(1350);
    }

    @DisplayName("출발지와 목적지 사이의 경로를 찾아보자(Adult)")
    @Test
    void findPathAdult() {
        PathResponse response = pathService.findPath(new LoginMember(0L, "test@email.com", 20), 강남역.getId(), 잠실역.getId());

        List<String> stationNames = response.getStations().stream()
                .map(stationResponse -> stationResponse.getName())
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly(강남역.getName(),
                삼성역.getName(), 잠실역.getName());
        assertThat(response.getDistance()).isEqualTo(15);
        assertThat(response.getFare()).isEqualTo(1350);
    }

    @DisplayName("출발지와 목적지 사이의 경로를 찾아보자(Teenager)")
    @Test
    void findPathTeenAger() {
        PathResponse response = pathService.findPath(new LoginMember(0L, "test@email.com", 15), 강남역.getId(), 잠실역.getId());

        List<String> stationNames = response.getStations().stream()
                .map(stationResponse -> stationResponse.getName())
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly(강남역.getName(),
                삼성역.getName(), 잠실역.getName());
        assertThat(response.getDistance()).isEqualTo(15);
        assertThat(response.getFare()).isEqualTo(820);
    }

    @DisplayName("출발지와 목적지 사이의 경로를 찾아보자(Child)")
    @Test
    void findPathChild() {
        PathResponse response = pathService.findPath(new LoginMember(0L, "test@email.com", 10), 강남역.getId(), 잠실역.getId());

        List<String> stationNames = response.getStations().stream()
                .map(stationResponse -> stationResponse.getName())
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly(강남역.getName(),
                삼성역.getName(), 잠실역.getName());
        assertThat(response.getDistance()).isEqualTo(15);
        assertThat(response.getFare()).isEqualTo(550);
    }

    @DisplayName("출발지와 목적지가 연결되지않는 경로를 찾으면 에러발생")
    @Test
    void notConnectedPath() {
        assertThatThrownBy(
                () -> pathService.findPath(new LoginMember(), 강남역.getId(), 덕소역.getId())
        ).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("존재하지 않는 역을 지정하면 에러발생")
    @Test
    void notExistStation() {
        assertThatThrownBy(
                () -> pathService.findPath(new LoginMember(), 강남역.getId(), 0L)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("동일한 역을 경로검색시 에러발생")
    @Test
    void findPathSameStation() {
        assertThatThrownBy(
                () -> pathService.findPath(new LoginMember(), 강남역.getId(), 강남역.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
