package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class LineTest {

    private Station 교대역;
    private Station 강남역;
    private Station 서초역;

    private Station 삼성역;
    private Station 역삼역;
    private Station 신림역;


    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        교대역 = new Station(2L, "교대역");
        서초역 = new Station(3L, "서초역");
        삼성역 = new Station(4L, "삼성역");
        역삼역 = new Station(5L, "역삼역");
        신림역 = new Station(6L, "신림역");
    }

    @DisplayName("생성 테스트")
    @Test
    public void test1() {
        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        assertThat(line.getStations().size()).isEqualTo(2);
        assertThat(line.getSections().getSections().size()).isEqualTo(1);
        assertThat(line.getStations().get(0)).isEqualTo(교대역);
        assertThat(line.getStations().get(1)).isEqualTo(강남역);
    }

    @DisplayName("생성 후 구간 추가 테스트")
    @Test
    public void test2() {
        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        line.addSection(서초역, 교대역, 10);
        assertThat(line.getStations().size()).isEqualTo(3);
        assertThat(line.getSections().getSections().size()).isEqualTo(2);
        assertThat(line.getStations().get(0)).isEqualTo(서초역);
        assertThat(line.getStations().get(1)).isEqualTo(교대역);
        assertThat(line.getStations().get(2)).isEqualTo(강남역);
    }

    @DisplayName("생성 후 구간 추가 테스트2")
    @Test
    public void test3() {

        Station 삼성역 = new Station(4L, "삼성역");
        Station 역삼역 = new Station(5L, "역삼역");
        Station 신림역 = new Station(6L, "신림역");

        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        line.addSection(신림역, 교대역, 10);
        line.addSection(강남역, 삼성역, 10);
        line.addSection(역삼역, 삼성역, 5);
        line.addSection(신림역, 서초역, 5);
        assertThat(line.getStations().size()).isEqualTo(6);

        assertThat(line.getSections().getSections().size()).isEqualTo(5);


        assertThat(line.getStations().get(0)).isEqualTo(신림역);
        assertThat(line.getStations().get(1)).isEqualTo(서초역);
        assertThat(line.getStations().get(2)).isEqualTo(교대역);
        assertThat(line.getStations().get(3)).isEqualTo(강남역);
        assertThat(line.getStations().get(4)).isEqualTo(역삼역);
        assertThat(line.getStations().get(5)).isEqualTo(삼성역);
    }

    @DisplayName("생성 후 구간 추가 불가 케이스 - 관련 없는 역인 경우")
    @Test
    public void test4() {
        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        line.addSection(신림역, 교대역, 10);
        assertThatThrownBy(() -> {
            line.addSection(삼성역, 역삼역, 10);
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("생성 후 구간 추가 불가 케이스 - 이미 등록된 구간인 경우")
    @Test
    public void test5() {
        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        line.addSection(신림역, 교대역, 10);
        assertThatThrownBy(() -> {
            line.addSection(신림역, 교대역, 10);
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("역 삭제 테스트")
    @Test
    public void test6() {
        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        line.addSection(신림역, 교대역, 10);
        line.removeStation(교대역);
        assertThat(line.getStations().size()).isEqualTo(2);
        assertThat(line.getStations().get(0)).isEqualTo(신림역);
        assertThat(line.getStations().get(1)).isEqualTo(강남역);
    }

    @DisplayName("역 삭제 불가 케이스 - 등록 안된 역인경우")
    @Test
    public void test7() {
        Line line = new Line("2호선", "green", 교대역, 강남역, 10);
        line.addSection(신림역, 교대역, 10);
        assertThatThrownBy(() -> {
            line.removeStation(삼성역);
        }).isInstanceOf(RuntimeException.class);
    }
}
