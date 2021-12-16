package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 리스트 기능")
public class LinesTest {
  private Station 교대역;
  private Station 강남역;
  private Station 선릉역;
  private Station 선정릉역;
  private Station 고속터미널역;
  private Station 양재역;

  private Line 이호선;
  private Line 삼호선;
  private Line 구호선;
  private Line 수인분당선;

  private static int 이호선_요금 = 5000;
  private static int 삼호선_요금 = 3000;
  private static int 구호선_요금 = 4000;
  private static int 수인분당선_요금 = 1000;

  @BeforeEach
  void setUp() {
    교대역 = new Station(1L, "교대역");
    강남역 = new Station(2L, "강남역");
    선릉역 = new Station(3L, "선릉역");
    선정릉역 = new Station(4L, "선정릉역");
    고속터미널역 = new Station(5L, "고속터미널역");
    양재역 = new Station(6L, "양재역");

    이호선 = new Line(1L, "이호선", "green", Section.of(1L, 교대역, 선릉역, Distance.of(50)));
    삼호선 = new Line(2L, "삼호선", "orange", Section.of(1L, 교대역, 고속터미널역, Distance.of(10)));
    구호선 = new Line(3L, "구호선", "brown", Section.of(1L, 고속터미널역, 선정릉역, Distance.of(50)));
    수인분당선 = new Line(4L, "수인분당선", "yellow", Section.of(1L, 선릉역, 선정릉역, Distance.of(10)));
  }

  @DisplayName("노선 목록에 속한 지하철역들 기준으로 가장 높은 추가 요금을 가져온다.")
  @Test
  void 노선_중_가장_높은_추가_요금() {
    // given
    이호선.addSurcharge(이호선_요금);
    삼호선.addSurcharge(삼호선_요금);
    구호선.addSurcharge(구호선_요금);
    수인분당선.addSurcharge(수인분당선_요금);

    // when
    Lines lines = Lines.of(Arrays.asList(이호선, 삼호선, 구호선, 수인분당선));

    // then
    assertThat(lines.getMaxSurcharge(Arrays.asList(교대역, 선릉역, 선정릉역, 고속터미널역))).isEqualTo(이호선_요금);
  }
}
