package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("구간을 관리한다.")
class SectionsTest {
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

  @DisplayName("구간을 추가한다.")
  @Test
  void add() {
    // when
    Section section1 = new Section(신분당선, 강남역, 광교역, 10);
    Section section2 = new Section(신분당선, 양재역, 광교역, 5);
    Sections sections = new Sections();
    sections.add(section1);
    sections.add(section2);

    // then
    assertThat(sections.getOrderedStations())
        .extracting("name")
        .containsExactly("강남역", "양재역", "광교역");
  }


  @DisplayName("구간을 제거한다.")
  @Test
  void remove() {
    // given
    Section section1 = new Section(신분당선, 강남역, 광교역, 10);
    Section section2 = new Section(신분당선, 양재역, 광교역, 5);
    Sections sections = new Sections();
    sections.add(section1);
    sections.add(section2);

    // when
    sections.remove(양재역);

    // then
    assertThat(sections.getOrderedStations())
        .extracting("name")
        .containsExactly("강남역", "광교역");
  }

}