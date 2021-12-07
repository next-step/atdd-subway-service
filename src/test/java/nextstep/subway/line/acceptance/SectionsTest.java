package nextstep.subway.line.acceptance;

import nextstep.subway.line.application.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class SectionsTest {
  private Station 역삼역;
  private Station 강남역;
  private Station 교대역;
  private Station 서초역;
  private Station 방배역;
  private Station 사당역;
  private Line 지하철_2호선;

  @BeforeEach
  void setUp() {
    역삼역 = new Station(6L, "역삼역");
    강남역 = new Station(1L, "강남역");
    교대역 = new Station(2L, "교대역");
    서초역 = new Station(3L, "서초역");
    방배역 = new Station(4L, "방배역");
    사당역 = new Station(5L, "사당역");
    지하철_2호선 = new Line(1L, "2호선", "green", Section.of(강남역, 서초역, Distance.of(10)));
  }

  @DisplayName("지하철 노선 구간의 상행 종점에 역을 추가한다.")
  @Test
  void 지하철_노선_구간_상행_종점_추가() {
    // given
    Section 신규_상행_종점 = Section.of(역삼역, 강남역, Distance.of(10));

    // when
    지하철_2호선.addSection(신규_상행_종점);

    // then
    assertThat(지하철_2호선.getStations()).containsExactly(역삼역, 강남역, 서초역);
  }

  @DisplayName("지하철 노선 구간의 하행 종점에 역을 추가한다.")
  @Test
  void 지하철_노선_구간_하행_종점_추가() {
    // given
    Section 신규_하행_종점 = Section.of(서초역, 방배역, Distance.of(10));

    // when
    지하철_2호선.addSection(신규_하행_종점);

    // then
    assertThat(지하철_2호선.getStations()).containsExactly(강남역, 서초역, 방배역);
  }

  @DisplayName("지하철 노선 구간을 중간에 추가한다.")
  @Test
  void 지하철_노선_구간_중간_추가() {
    // given
    Section 신규_구간 = Section.of(교대역, 서초역, Distance.of(5));

    // when
    지하철_2호선.addSection(신규_구간);

    // then
    assertThat(지하철_2호선.getStations()).containsExactly(강남역, 교대역, 서초역);
  }

  @DisplayName("구간에 등록된 역들과 이어질 수 없는 구간을 추가할 경우 예외를 던진다.")
  @Test
  void 지하철_노선_구간_추가_연결된_역이_없을_경우_예외() {
    // given
    Section 신규_구간 = Section.of(방배역, 사당역, Distance.of(10));

    // when
    Throwable thrown = catchThrowable(() -> 지하철_2호선.addSection(신규_구간));

    // then
    assertThat(thrown)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("등록할 수 없는 구간 입니다.");
  }

  @DisplayName("지하철 노선에 이미 등록된 구간을 추가할 경우 예외를 던진다.")
  @Test
  void 지하철_노선_구간_추가_이미_등록된_구간_추가_예외() {
    // given
    Section 신규_구간 = Section.of(강남역, 서초역, Distance.of(10));

    // when
    Throwable thrown = catchThrowable(() -> 지하철_2호선.addSection(신규_구간));

    // then
    assertThat(thrown)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("이미 등록된 구간 입니다.");
  }

  @DisplayName("신규 구간의 길이가 기존 역과 역 사이 길이보다 같거나 클 경우 추가할 수 없다.")
  @Test
  void 지하철_노선_구간_중간_추가_길이_예외() {
    // given
    Section 신규_구간 = Section.of(교대역, 서초역, Distance.of(10));

    // when
    Throwable thrown = catchThrowable(() -> 지하철_2호선.addSection(신규_구간));

    // then
    assertThat(thrown)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
  }

  @DisplayName("지하철 노선에 상행 종점역을 제거한다.")
  @Test
  void 지하철_노선_상행_종점_역_제거() {
    // given
    Section 신규_구간 = Section.of(서초역, 방배역, Distance.of(10));
    지하철_2호선.addSection(신규_구간);

    // when
    지하철_2호선.removeStation(강남역);

    // then
    assertThat(지하철_2호선.getStations()).containsExactly(서초역, 방배역);
  }

  @DisplayName("지하철 노선에 하행 종점역을 제거한다.")
  @Test
  void 지하철_노선_하행_종점_역_제거() {
    // given
    Section 신규_구간 = Section.of(강남역, 교대역, Distance.of(5));
    지하철_2호선.addSection(신규_구간);

    // when
    지하철_2호선.removeStation(서초역);

    // then
    assertThat(지하철_2호선.getStations()).containsExactly(강남역, 교대역);
  }

  @DisplayName("지하철 노선에 중간 역을 제거한다.")
  @Test
  void 지하철_노선_중간_역_제거() {
    // given
    Section 신규_구간 = Section.of(강남역, 교대역, Distance.of(5));
    Section 신규_구간2 = Section.of(서초역, 방배역, Distance.of(5));
    지하철_2호선.addSection(신규_구간);
    지하철_2호선.addSection(신규_구간2);

    // when
    지하철_2호선.removeStation(교대역);

    // then
    assertThat(지하철_2호선.getStations()).containsExactly(강남역, 서초역, 방배역);
  }

  @DisplayName("지하철 노선에 등록되지 않은 역을 제거하면 예외가 발생한다.")
  @Test
  void 지하철_노선_미등록_역_제거_예외() {
    // given
    Section 신규_구간 = Section.of(강남역, 교대역, Distance.of(5));
    지하철_2호선.addSection(신규_구간);

    // when
    Throwable thrown = catchThrowable(() -> 지하철_2호선.removeStation(방배역));

    // then
    assertThat(thrown)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("노선에 등록되지 않은 역은 제거할 수 없습니다.");
  }

  @DisplayName("지하철 노선 구간이 한개일 때 역을 제거할 경우 예외가 발생한다.")
  @Test
  void 지하철_노선_구간_한개_역_제거_예외() {
    // when
    Throwable thrown = catchThrowable(() -> 지하철_2호선.removeStation(강남역));

    // then
    assertThat(thrown)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("구간이 하나일 경우, 지하철 역을 제거할 수 없습니다.");
  }
}
