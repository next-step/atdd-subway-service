package nextstep.subway.line.unit;

import io.restassured.RestAssured;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineTest {
    @LocalServerPort
    int port;

    private Station 강남역;
    private Station 판교역;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();

        강남역 = new Station(1L, "강남역");
        판교역 = new Station(2L, "판교역");
    }

    @Test
    @DisplayName("라인 저장 단위 테스트")
    void 라인저장_테스트() {
        // when
        Line line = new Line("신분당선", "bg-red-600", 강남역, 판교역, 15);
        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("신분당선"),
                () -> assertThat(line.getSections().size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("라인 수정 단위 테스트")
    void 라인수정_테스트() {
        // given
        Line line = new Line("신분당선", "bg-red-600", 강남역, 판교역, 15);
        // when
        line.update(new Line(line.getName(), "bg-yellow-600"));
        // then
        assertThat(line.getColor()).isEqualTo("bg-yellow-600");
    }

    @Test
    @DisplayName("라인 구간 추가 단위 테스트")
    void 라인구간추가_테스트() {
        // given
        Line line = new Line("신분당선", "bg-red-600", 강남역, 판교역, 15);
        Station 양재역 = new Station(3L, "양재역");

        // when
        line.getSections().addLineStation(line, new SectionRequest().toSection(강남역, 양재역, new Distance(5)));

        // then
        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(2),
                () -> assertThat(line.getSections().get(0).getUpStation().getName()).isEqualTo("양재역"),
                () -> assertThat(line.getSections().get(1).getDownStation().getName()).isEqualTo("양재역")
        );
    }

    @Test
    @DisplayName("중간역 삭제 단위 테스트")
    void 중간역삭제_테스트() {
        // given
        Line line = new Line("신분당선", "bg-red-600", 강남역, 판교역, 15);
        Station 양재역 = new Station(3L, "양재역");
        line.getSections().addLineStation(line, new SectionRequest().toSection(강남역, 양재역, new Distance(5)));

        // when
        line.getSections().removeLineStation(line, 양재역);

        // then
        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(1),
                () -> assertThat(line.getSections().get(0).getDownStation().getName()).isEqualTo("판교역")
        );
    }

    @Test
    @DisplayName("종점역 삭제 단위 테스트")
    void 종점역삭제_테스트() {
        // given
        Line line = new Line("신분당선", "bg-red-600", 강남역, 판교역, 15);
        Station 양재역 = new Station(3L, "양재역");
        line.getSections().addLineStation(line, new SectionRequest().toSection(강남역, 양재역, new Distance(5)));

        // when
        line.getSections().removeLineStation(line, 강남역);

        // then
        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(1),
                () -> assertThat(line.getSections().get(0).getUpStation().getName()).isEqualTo("양재역")
        );
    }
}
