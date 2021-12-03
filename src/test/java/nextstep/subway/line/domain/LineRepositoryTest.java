package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
public class LineRepositoryTest {

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Test
    @DisplayName("라인 저장")
    void saveLine() {
        Station seoulStation = stationRepository.findById(1L).get();
        Station yongsanStation = stationRepository.findById(2L).get();

        Line line = lineRepository.save(Line.of("1호선", "blue", seoulStation, yongsanStation, 10));

        assertAll(() -> {
            assertThat(line.getSections().size()).isEqualTo(1);
            assertThat(line.getStations()).extracting(Station::getName).containsExactly("서울역", "용산역");
        });
    }

    @Test
    @DisplayName("라인 삭제")
    void deleteLine() {
        Line line = savedLineStation();

        lineRepository.delete(line);

        //쿼리 확인
        lineRepository.flush();

        List<Line> lines = lineRepository.findAll();
        assertThat(lines.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("라인 이름,색상 수정")
    void updateLine() {
        Line line = savedLineStation();

        line.update(Line.of("2호선", "green"));

        //쿼리 확인
        lineRepository.flush();

        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("2호선");
            assertThat(line.getColor()).isEqualTo("green");
        });
    }

    private Line savedLineStation() {
        Station seoulStation = stationRepository.findById(1L).get();
        Station yongsanStation = stationRepository.findById(2L).get();

        return lineRepository.save(Line.of("1호선", "blue", seoulStation, yongsanStation, 10));
    }
}
