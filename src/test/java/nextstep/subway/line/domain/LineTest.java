package nextstep.subway.line.domain;

import nextstep.subway.exception.AddLineSectionFail;
import nextstep.subway.line.application.LineService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LineTest {

    @Autowired
    StationRepository stationRepository;

    Station 강남역;
    Station 역삼역;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
    }

    @Test
    void saveLine() {
        // given
        Line line = new Line("2호선", "초록색", 강남역, 역삼역, 15);

        // then
        assertAll(
                () -> assertEquals(line.getName(), "2호선"),
                () -> assertEquals(line.getColor(), "초록색"),
                () -> assertEquals(line.getSections().get(0).getUpStation(), 강남역),
                () -> assertEquals(line.getSections().get(0).getDownStation(), 역삼역),
                () -> assertEquals(line.getSections().get(0).getDistance(), 15)
        );
    }

    @Test
    void update() {
        // given
        Line line = new Line("2호선", "초록색", 강남역, 역삼역, 15);
        Line newLine = new Line("two", "green");

        // when
        line.update(newLine);

        // then
        assertAll(
                () -> assertEquals(line.getName(), "two"),
                () -> assertEquals(line.getColor(), "green")
        );
    }

    @Test
    void addLineStation() {
        // given
        Line line = new Line("2호선", "초록색", 강남역, 역삼역, 15);
        Station 교대역 = stationRepository.save(new Station("교대역"));

        // when
        line.addDownStationExisted(new Section(line, 교대역, 강남역, 11));
        assertAll(
                () -> assertEquals(line.getSections().size(), 2),
                () -> equalsSection(line.getSections().get(0), 강남역, 역삼역, 15),
                () -> equalsSection(line.getSections().get(1), 교대역, 강남역, 11)
        );
    }

    @Test
    void addLineStation2() {
        // given
        Line line = new Line("2호선", "초록색", 강남역, 역삼역, 15);
        Station 선릉역 = stationRepository.save(new Station("선릉역"));

        // when
        line.addUpStationExisted(new Section(line, 역삼역, 선릉역, 11));
        assertAll(
                () -> assertEquals(line.getSections().size(), 2),
                () -> equalsSection(line.getSections().get(0), 강남역, 역삼역, 15),
                () -> equalsSection(line.getSections().get(1), 역삼역, 선릉역, 11)
        );
    }

    @Test
    void addLineStation3() {
        // given
        Line line = new Line("2호선", "초록색", 강남역, 역삼역, 15);
        Station 교대역 = stationRepository.save(new Station("교대역"));
        Station 선릉역 = stationRepository.save(new Station("선릉역"));
        Station 삼성역 = stationRepository.save(new Station("삼성역"));
        Station 종합운동장역 = stationRepository.save(new Station("종합운동장역"));
        Station 신도림역 = stationRepository.save(new Station("신도림역"));
        Station 대림역 = stationRepository.save(new Station("대림역"));

        // when
        // 교대역-11-강남역-15-역삼역-7-선릉역-14-삼성역-9-종합운동장역
        line.addUpStationExisted(new Section(line, 역삼역, 종합운동장역, 30));
        line.addDownStationExisted(new Section(line, 삼성역, 종합운동장역, 9));
        line.addUpStationExisted(new Section(line, 역삼역, 선릉역, 7));
        line.addDownStationExisted(new Section(line, 교대역, 강남역, 11));

        // then
        assertAll(
                () -> assertEquals(line.getSections().size(), 5),
                () -> equalsSection(line.getSections().get(0), 강남역, 역삼역, 15),
                () -> equalsSection(line.getSections().get(1), 선릉역, 삼성역, 14),
                () -> equalsSection(line.getSections().get(2), 삼성역, 종합운동장역, 9),
                () -> equalsSection(line.getSections().get(3), 역삼역, 선릉역, 7),
                () -> equalsSection(line.getSections().get(4), 교대역, 강남역, 11)
        );
    }

    @Test
    void removeLineStation() {
        // given
        Line line = new Line("2호선", "초록색", 강남역, 역삼역, 15);
        Station 교대역 = stationRepository.save(new Station("교대역"));
        Section newSection = new Section(line, 교대역, 강남역, 13);
        // when
        line.addDownStationExisted(newSection);
        line.removeLineStation(newSection);

        // then
        equalsSection(line.getSections().get(0), 강남역, 역삼역, 15);
    }

    private void equalsSection(Section section, Station upStation, Station downStation, int distance) {
        assertAll(
                () -> assertTrue(section.getUpStation().equals(upStation)),
                () -> assertTrue(section.getDownStation().equals(downStation)),
                () -> assertEquals(section.getDistance(), distance)
        );
    }
}
