package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/stationInitData.sql")
public class LineTest {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Test
    @DisplayName("라인에 포함된 역목록 조회")
    void getStations() {
        //given
        Station 서울역 = stationRepository.findById(1L).get();
        Station 용산역 = stationRepository.findById(2L).get();
        Line line = Line.of("1호선", "blue", 서울역, 용산역, 10);

        //when
        List<Station> stations = line.getStations();

        //then
        assertThat(stations).extracting(Station::getName).containsExactly("서울역", "용산역");
    }

    @Test
    @DisplayName("라인에 구간 추가")
    void addLineStation() {
        //given
        Station 서울역 = stationRepository.findById(1L).get();
        Station 용산역 = stationRepository.findById(2L).get();
        Line line = Line.of("1호선", "blue", 서울역, 용산역, 10);

        lineRepository.save(line);

        Station 남영역 = stationRepository.findById(3L).get();

        //when
        line.addLineStation(서울역, 남영역, 5);

        //then
        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(line.getStations()).extracting(Station::getName).containsExactly("서울역", "남영역", "용산역");

    }

    @Test
    @DisplayName("구간이 없는 라인에 구간 추가")
    void noSectionLineAddStation() {
        Station 서울역 = stationRepository.findById(1L).get();
        Station 용산역 = stationRepository.findById(2L).get();

        Line line = Line.of("1호선", "blue");

        line.addLineStation(서울역, 용산역, 10);

        //then
        assertThat(line.getSections().size()).isEqualTo(1);
        assertThat(line.getStations()).extracting(Station::getName).containsExactly("서울역", "용산역");
    }

    @Test
    @DisplayName("이미 포함된 구간 추가시 실패")
    void addLineStationExistsFail() {
        //given
        Station 서울역 = stationRepository.findById(1L).get();
        Station 용산역 = stationRepository.findById(2L).get();
        Line line = Line.of("1호선", "blue", 서울역, 용산역, 10);

        assertThatThrownBy(
            () -> line.addLineStation(서울역, 용산역, 12))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("연결된 역이 없는 구간 추가시 실패")
    void addLineStationNoIncludeFail() {
        //given
        Station 서울역 = stationRepository.findById(1L).get();
        Station 용산역 = stationRepository.findById(2L).get();
        Line line = Line.of("1호선", "blue", 서울역, 용산역, 10);

        Station 강남역 = stationRepository.findById(4L).get();
        Station 역삼역 = stationRepository.findById(5L).get();

        assertThatThrownBy(
            () -> line.addLineStation(강남역, 역삼역, 10))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("등록할 수 없는 구간 입니다.");
    }
}
