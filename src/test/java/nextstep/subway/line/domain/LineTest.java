package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
class LineTest {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    private Station 인천역;
    private Station 용산역;
    private Line 일호선;

    @AfterEach
    void tearDown() {
        stationRepository.deleteAll();
        lineRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        인천역 = stationRepository.save(new Station("인천역"));
        용산역 = stationRepository.save(new Station("용산역"));
        일호선 = lineRepository.save(new Line("1호선", "indigo", 인천역, 용산역, 10));
    }

    @Test
    void addSection() {
        // given
        final Station 송내역 = stationRepository.save(new Station("송내역"));

        // when
        일호선.addSection(new Section(일호선, 인천역, 송내역, 5));

        // then
        assertThat(일호선.computeSortedStations()).containsExactly(인천역, 송내역, 용산역);
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 예외 발생")
    @Test
    void addSectionWithLongDistance() {
        // given
        final Station 의정부역 = stationRepository.save(new Station("의정부역"));

        // when, then
        assertThatThrownBy(() -> 일호선.addSection(new Section(일호선, 인천역, 의정부역, 10)))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 예외 발생")
    @Test
    void addSectionThatAlreadyExists() {
        // when, then
        assertThatThrownBy(() -> 일호선.addSection(new Section(일호선, 인천역, 용산역, 5)))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("상행역과 하행역이 이미 노선에 모두 등록되어 있습니다.");
    }

    @DisplayName("서로 연결이 불가능한 구간이 추가되려고 할 때 예외 발생")
    @Test
    void addSectionNotConnected() {
        // given
        final Station 사당역 = stationRepository.save(new Station("사당역"));
        final Station 강남역 = stationRepository.save(new Station("강남역"));

        // when, then
        assertThatThrownBy(() -> 일호선.addSection(new Section(일호선, 사당역, 강남역, 5)))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("추가되는 구간은 기존의 구간과 연결 가능하여야 합니다.");
    }

    @Test
    void removeStation() {
        // given
        final Station 송내역 = stationRepository.save(new Station("송내역"));
        일호선.addSection(new Section(일호선, 인천역, 송내역, 5));

        // when
        일호선.removeStation(용산역);

        // then
        assertThat(일호선.computeSortedStations()).containsExactly(인천역, 송내역);
    }

    @DisplayName("연결이 되어있지 않은 역을 삭제하려고 할 때 예외 발생")
    @Test
    void removeStationNotConnected() {
        // given
        final Station 강남역 = stationRepository.save(new Station("강남역"));
        final Station 송내역 = stationRepository.save(new Station("송내역"));
        일호선.addSection(new Section(일호선, 인천역, 송내역, 5));

        // when, then
        assertThatThrownBy(() -> 일호선.removeStation(강남역))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("노선에 등록되어 있지 않은 역은 삭제할 수 없습니다.");
    }

    @DisplayName("노선의 구간이 하나밖에 남지 않았을 때(역이 2개만 남아있을 때) 삭제를 하려고 하면 예외 발생")
    @Test
    void removeLastSection() {
        // when, then
        assertThatThrownBy(() -> 일호선.removeStation(인천역))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("노선의 마지막 구간은 삭제할 수 없습니다.");
    }

    @Test
    void computeSortedStations() {
        // given
        final Station 송내역 = stationRepository.save(new Station("송내역"));
        final Station 신도림역 = stationRepository.save(new Station("신도림역"));

        // when
        일호선.addSection(new Section(일호선, 인천역, 신도림역, 5));
        일호선.addSection(new Section(일호선, 송내역, 신도림역, 2));

        // then
        assertThat(일호선.computeSortedStations()).containsExactly(인천역, 송내역, 신도림역, 용산역);
    }
}