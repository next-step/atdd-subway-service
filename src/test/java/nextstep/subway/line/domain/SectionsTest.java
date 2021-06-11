package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

  private Station 강남역 = new Station("강남역");

  private Station 양재역 = new Station("양재역");

  private Station 청계산입구역 = new Station("청계산입구역");

  private Station 판교역 = new Station("판교역");

  private Station 수지구청역 = new Station("수지구청역");

  private Station 광교역 = new Station("광교역");

  private Line 신분당선 = new Line("신분당선", "red", 강남역, 광교역, Distance.from(12));

  private Sections sections;
  @BeforeEach
  void setUp() {
    Section first = new Section(신분당선, 강남역, 양재역, Distance.from(2));
    Section second = new Section(신분당선, 양재역, 청계산입구역, Distance.from(2));
    Section third = new Section(신분당선, 청계산입구역, 판교역, Distance.from(2));
    Section fourth = new Section(신분당선, 판교역, 수지구청역, Distance.from(2));
    Section fifth = new Section(신분당선, 수지구청역, 광교역, Distance.from(2));
    Sections prepared = new Sections();
    prepared.registerNewSection(first);
    prepared.registerNewSection(second);
    prepared.registerNewSection(third);
    prepared.registerNewSection(fourth);
    prepared.registerNewSection(fifth);
    this.sections = prepared;
  }


  @DisplayName("상행 -> 하행 순서에 맞게 역을 반환한다.")
  @Test
  void sortedStationsTest() {
    assertThat(sections.getDistinctStations()).containsExactly(강남역, 양재역, 청계산입구역, 판교역, 수지구청역, 광교역);
  }

  @DisplayName("이미 등록된 역 구간을 등록할 수 없다.")
  @Test
  void addFailTest() {
    Section alreadyExistEdgesSection = new Section(신분당선, 청계산입구역, 판교역, Distance.from(8));
    Section alreadyContainsEachStationsSection = new Section(신분당선, 강남역, 광교역, Distance.from(8));
    assertAll(
        () -> assertThatThrownBy(() -> sections.registerNewSection(alreadyExistEdgesSection)).isInstanceOf(IllegalArgumentException.class),
        () -> assertThatThrownBy(() -> sections.registerNewSection(alreadyContainsEachStationsSection)).isInstanceOf(IllegalArgumentException.class)
    );
  }

  @DisplayName("역 사이에 새로운 역을 등록한다.")
  @Test
  void registerNewSectionTest() {
    //given
    Station 양재시민의숲역 = new Station("양재시민의숲역");
    Section given = new Section(신분당선, 양재역, 양재시민의숲역, Distance.from(1));
    //when
    sections.registerNewSection(given);
    //then
    assertThat(sections.getDistinctStations()).containsExactly(강남역, 양재역, 양재시민의숲역, 청계산입구역, 판교역, 수지구청역, 광교역);
  }

  @DisplayName("역 사이에 새로운 역을 등록할 때 기존의 역 간격보다 큰 간격을 등록할 수 없다.")
  @Test
  void registerNewSectionFailTest() {
    //given
    Station 양재시민의숲역 = new Station("양재시민의숲역");
    Section given = new Section(신분당선, 양재역, 양재시민의숲역, Distance.from(15));
    //when
    assertThatThrownBy(() -> sections.registerNewSection(given)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("구간에서 역을 제거한다.")
  @Test
  void removeStationTest() {
    //when
    sections.removeStation(양재역);

    //then
    assertThat(sections.getDistinctStations()).containsExactly(강남역, 청계산입구역, 판교역, 수지구청역, 광교역);
  }

  @DisplayName("구간에 포함되지 않은 역은 제거할 수 없다.")
  @Test
  void removeFailWhenStationNotContainedTest() {
    //given
    Station 양재시민의숲역 = new Station("양재시민의숲역");

    //when & then
    assertThatThrownBy(() -> sections.removeStation(양재시민의숲역)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("단일 구간일 때는 역을 제거할 수 없다.")
  @Test
  void removeFailWhenSingleSectionTest() {
    //given
    Section singleSection = new Section(신분당선, 강남역, 양재역, Distance.from(2));
    Sections givenSections = new Sections();
    givenSections.registerNewSection(singleSection);

    //when & then
    assertThatThrownBy(() -> givenSections.removeStation(강남역)).isInstanceOf(IllegalArgumentException.class);
  }

}
