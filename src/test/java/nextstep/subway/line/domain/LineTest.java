package nextstep.subway.line.domain;

import nextstep.subway.exception.IllegalSectionStateException;
import nextstep.subway.exception.InvalidDistanceException;
import nextstep.subway.exception.InvalidStationException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.domain.Station.stationStaticFactoryForTestCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

  private Station 강남역 = stationStaticFactoryForTestCode(1L, "강남역");

  private Station 양재역 = stationStaticFactoryForTestCode(2L, "양재역");

  private Station 청계산입구역 = stationStaticFactoryForTestCode(3L, "청계산입구역");

  private Station 판교역 = stationStaticFactoryForTestCode(4L, "판교역");

  private Station 수지구청역 = stationStaticFactoryForTestCode(5L, "수지구청역");

  private Station 광교역 = stationStaticFactoryForTestCode(6L, "광교역");

  private Line 신분당선 = new Line("신분당선", "red", 강남역, 광교역, Distance.from(12));

  @DisplayName("노선의 역을 상행역 종점 -> 하행 종점 순으로 반환한다.")
  @Test
  void getEndToEndStationsTest() {
    Section first = new Section(신분당선, 강남역, 양재역, Distance.from(2));
    Section second = new Section(신분당선, 양재역, 청계산입구역, Distance.from(2));
    Section third = new Section(신분당선, 청계산입구역, 판교역, Distance.from(2));
    Section fourth = new Section(신분당선, 판교역, 수지구청역, Distance.from(2));
    신분당선.addSection(first);
    신분당선.addSection(second);
    신분당선.addSection(third);
    신분당선.addSection(fourth);
    assertThat(신분당선.getEndToEndStations()).containsExactly(강남역, 양재역, 청계산입구역, 판교역, 수지구청역, 광교역);
  }

  @DisplayName("구간 사이에 새로운 구간이 추가될 수 있다.")
  @Test
  void addSectionTest() {
    //given
    Station 양재시민의숲역 = stationStaticFactoryForTestCode(7L, "양재시민의숲역");
    Section newSection = new Section(신분당선, 강남역, 양재시민의숲역, Distance.from(1));

    //when
    신분당선.addSection(newSection);

    assertThat(신분당선.getEndToEndStations()).containsExactly(강남역, 양재시민의숲역, 광교역);
  }

  @DisplayName("구간 상행 종점에 새로운 구간이 추가될 수 있다.")
  @Test
  void addSectionUpStationEdgeTest() {
    //given
    Station 서울역 = stationStaticFactoryForTestCode(8L, "서울역");
    Section newSection = new Section(신분당선, 서울역, 강남역, Distance.from(1));

    //when
    신분당선.addSection(newSection);

    assertThat(신분당선.getEndToEndStations()).containsExactly(서울역, 강남역, 광교역);
  }

  @DisplayName("구간 하행 종점에 새로운 구간이 추가될 수 있다.")
  @Test
  void addSectionDownStationEdgeTest() {
    //given
    Station 오산역 = stationStaticFactoryForTestCode(9L, "오산역");
    Section newSection = new Section(신분당선, 광교역, 오산역, Distance.from(1));

    //when
    신분당선.addSection(newSection);

    assertThat(신분당선.getEndToEndStations()).containsExactly(강남역, 광교역, 오산역);
  }

  @DisplayName("이미 등록된 역 구간을 등록할 수 없다.")
  @Test
  void addFailTest() {
    //given
    Section first = new Section(신분당선, 강남역, 양재역, Distance.from(2));
    Section second = new Section(신분당선, 양재역, 청계산입구역, Distance.from(2));
    신분당선.addSection(first);
    신분당선.addSection(second);

    Section alreadyExistEdgesSection = new Section(신분당선, 강남역, 양재역, Distance.from(8));
    Section alreadyContainsEachStationsSection = new Section(신분당선, 강남역, 청계산입구역, Distance.from(8));

    //when & then
    assertAll(
        () -> assertThatThrownBy(() -> 신분당선.addSection(alreadyExistEdgesSection)).isInstanceOf(InvalidStationException.class),
        () -> assertThatThrownBy(() -> 신분당선.addSection(alreadyContainsEachStationsSection)).isInstanceOf(InvalidStationException.class)
    );
  }

  @DisplayName("역 사이에 새로운 역을 등록할 때 기존의 역 간격보다 큰 간격을 등록할 수 없다.")
  @Test
  void registerNewSectionFailTest() {
    //given
    Station 양재시민의숲역 = stationStaticFactoryForTestCode(7L, "양재시민의숲역");
    Section given = new Section(신분당선, 강남역, 양재시민의숲역, Distance.from(15));
    //when
    assertThatThrownBy(() -> 신분당선.addSection(given)).isInstanceOf(InvalidDistanceException.class);
  }

  @DisplayName("구간에서 역을 제거한다.")
  @Test
  void removeStationTest() {
    //given
    Section newSection = new Section(신분당선, 강남역, 양재역, Distance.from(1));
    신분당선.addSection(newSection);

    //when
    신분당선.removeStation(양재역.getId());

    //then
    assertThat(신분당선.getEndToEndStations()).containsExactly(강남역, 광교역);
  }

  @DisplayName("구간에 포함되지 않은 역은 제거할 수 없다.")
  @Test
  void removeFailWhenStationNotContainedTest() {
    //given
    Station 양재시민의숲역 = stationStaticFactoryForTestCode(7L, "양재시민의숲역");

    //when & then
    assertThatThrownBy(() -> 신분당선.removeStation(양재시민의숲역.getId())).isInstanceOf(IllegalSectionStateException.class);
  }

  @DisplayName("단일 구간일 때는 역을 제거할 수 없다.")
  @Test
  void removeFailWhenSingleSectionTest() {
    assertThatThrownBy(() -> 신분당선.removeStation(강남역.getId())).isInstanceOf(IllegalSectionStateException.class);
  }

}
