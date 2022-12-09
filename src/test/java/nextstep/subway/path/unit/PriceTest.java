package nextstep.subway.path.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Price;
import nextstep.subway.path.domain.SectionEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 운임 금액 기능")
public class PriceTest {

    private Line 노선운임_100원 = new Line("노선운임_100원", 100);
    private Line 노선운임_300원 = new Line("노선운임_300원", 300);
    private Line 노선운임_500원 = new Line("노선운임_500원", 500);
    private Line 노선운임_700원 = new Line("노선운임_700원", 700);

    private SectionEdge 운임_100원 = new SectionEdge();
    private SectionEdge 운임_300원 = new SectionEdge();
    private SectionEdge 운임_500원 = new SectionEdge();
    private SectionEdge 운임_700원 = new SectionEdge();

    List<SectionEdge> 최대운임_300원 = new ArrayList<>();
    List<SectionEdge> 최대운임_500원 = new ArrayList<>();
    List<SectionEdge> 최대운임_700원 = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        운임_100원.setSection(new Section(노선운임_100원, null, null, 0));
        운임_300원.setSection(new Section(노선운임_300원, null, null, 0));
        운임_500원.setSection(new Section(노선운임_500원, null, null, 0));
        운임_700원.setSection(new Section(노선운임_700원, null, null, 0));

        최대운임_300원.add(운임_100원);
        최대운임_300원.add(운임_300원);

        최대운임_500원.add(운임_100원);
        최대운임_500원.add(운임_300원);
        최대운임_500원.add(운임_500원);

        최대운임_700원.add(운임_500원);
        최대운임_700원.add(운임_700원);
    }

    @Test
    @DisplayName("지하철 운임 금액 계산 (10km 이하 / 성인 / 최대노선 운임 500원)")
    void calculatePrice1() {
        Price price = new Price();
        price.calculatePrice(8, 최대운임_500원, 28);
        // 1250 + 500 + 0 = 1750

        assertThat(price.value()).isEqualTo(1750);
    }

    @Test
    @DisplayName("지하철 운임 금액 계산 (10km 초과 / 성인 / 최대노선 운임 300원)")
    void calculatePrice2() {
        Price price = new Price();
        price.calculatePrice(30, 최대운임_300원, 28);
        // 1250 + 300 + 400 = 1950

        assertThat(price.value()).isEqualTo(1950);
    }

    @Test
    @DisplayName("지하철 운임 금액 계산 (10km 이하 / 어린이 / 최대노선 운임 700원)")
    void calculatePrice3() {
        Price price = new Price();
        price.calculatePrice(8, 최대운임_700원, 11);
        // 450 + 700 + 0 = 1150

        assertThat(price.value()).isEqualTo(1150);
    }

    @Test
    @DisplayName("지하철 운임 금액 계산 (10km 초과 / 청소년 / 최대노선 운임 500원)")
    void calculatePrice4() {
        Price price = new Price();
        price.calculatePrice(30, 최대운임_500원, 18);
        // 720 + 500 + 400 = 1620

        assertThat(price.value()).isEqualTo(1620);
    }

}
