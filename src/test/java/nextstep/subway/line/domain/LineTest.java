package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철 노선을 관리한다.")
class LineTest {

  private Line 신분당선;
  private Station 강남역;
  private Station 광교역;
  private Station 양재역;

  @BeforeEach
  void setUp() {
    신분당선 = new Line("신분당선", "pink");
    강남역 = new Station("강남역");
    광교역 = new Station("광교역");
    양재역 = new Station("양재역");
  }

  @DisplayName("지하철 노선을 수정한다.")
  @Test
  void update() {
    // when
    신분당선.update(new Line("신분당선2", "pink2"));

    // then
    assertThat(신분당선.getName()).isEqualTo("신분당선2");
    assertThat(신분당선.getColor()).isEqualTo("pink2");
  }

  @DisplayName("지하철 노선에 구간을 추가한다")
  @Test
  void addSection() {
    // when
    신분당선.addSection(new Section(신분당선, 강남역, 광교역, 5));

    // then
    assertThat(신분당선.getStations())
        .extracting("name")
        .containsExactly("강남역", "광교역");
  }

  @DisplayName("지하철 노선에 구간을 삭제한다")
  @Test
  void removeStation() {
    // given
    신분당선.addSection(new Section(신분당선, 강남역, 광교역, 10));
    신분당선.addSection(new Section(신분당선, 양재역, 광교역, 5));

    // when
    신분당선.removeStation(광교역);

    // then
    assertThat(신분당선.getStations())
        .extracting("name")
        .containsExactly("강남역", "양재역");

  }
}