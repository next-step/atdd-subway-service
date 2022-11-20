package nextstep.subway.line.domain;

import static java.util.Collections.singletonList;
import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.line.domain.SectionsTestFixture.createSections;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간들 관련 도메인 테스트")
public class SectionsTest {

    @DisplayName("구간을 추가한다.")
    @Test
    void addSection() {
        // given
        Station 강남역 = createStation("강남역");
        Station 양재역 = createStation("양재역");
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        Section section = createSection(line, 양재역, createStation("양재시민의숲역"), 15);
        Section newSection = createSection(line, 강남역, 양재역, 10);
        Sections sections = createSections(singletonList(section));

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.findStations()).hasSize(3);
    }

    @DisplayName("구간을 정렬된 형태로 조회하면 상행역부터 하행역까지 정렬되어 조회된다.")
    @Test
    void findInOrderStations() {
        // given
        Station 양재역 = createStation("양재역");
        Station 양재시민의숲역 = createStation("양재시민의숲역");
        Station 청계산입구역 = createStation("청계산입구역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section1 = createSection(line, 양재역, 양재시민의숲역, 15);
        Section section2 = createSection(line, 양재시민의숲역, 청계산입구역, 10);

        // when
        Sections sections = createSections(Arrays.asList(section1, section2));
        List<Station> stations = sections.findInOrderStations();

        // then
        assertThat(stations).containsExactly(양재역, 양재시민의숲역, 청계산입구역);
    }

    @DisplayName("구간들에 이미 존재하는 구간을 추가하려고 하면 에러가 발생한다.")
    @Test
    void addSectionThrowErrorWhenAlreadyExist() {
        // given
        Station 양재역 = createStation("양재역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section = createSection(line, 양재역, createStation("양재시민의숲역"), 15);
        Sections sections = createSections(singletonList(section));

        // when & then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이미_존재하는_구간.getErrorMessage());
    }

    @DisplayName("추가하려는 구간의 상행/하행역 모두 노선에 존재하는 역이 아니면 에러가 발생한다.")
    @Test
    void addSectionThrowErrorWhenAllStationIsNotExist() {
        // given
        Station 양재역 = createStation("양재역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section = createSection(line, 양재역, createStation("양재시민의숲역"), 15);
        Sections sections = createSections(singletonList(section));

        // when & then
        assertThatThrownBy(() -> sections.addSection(createSection(line, createStation("신사역"), createStation("논현역"), 20)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.구간의_상행역과_하행역이_모두_노선에_포함되지_않음.getErrorMessage());
    }

    @DisplayName("추가하려는 구간의 노선이 기존에 존재하는 구간들의 노선과 다를 경우 에러가 발생한다.")
    @Test
    void addSectionThrowErrorWhenLineIsDifferent() {
        // given
        Station 강남역 = createStation("강남역");
        Station 양재역 = createStation("양재역");
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        Line otherLine = createLine("2호선", "bg-green", 강남역, createStation("역삼역"), 5);
        Section section = createSection(line, 양재역, createStation("양재시민의숲역"), 15);
        Sections sections = createSections(singletonList(section));

        // when & then
        assertThatThrownBy(() -> sections.addSection(createSection(otherLine, 강남역, 양재역, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.구간의_노선이_기존_구간들과_상이함.getErrorMessage());
    }

    @DisplayName("기존 구간 사이에 신규 역이 등록되면, 구간이 변경된다. (신규 구간의 상행역이 기존에 노선에 등록되어 있는 경우)")
    @Test
    void addSectionMakeExistedSectionChange_existUpStation() {
        // given
        Station 강남역 = createStation("강남역");
        Station 양재역 = createStation("양재역");
        Station 양재시민의숲역 = createStation("양재시민의숲역");
        Line 신분당선 = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        Section section = createSection(신분당선, 강남역, 양재시민의숲역, 15);
        Section newSection = createSection(신분당선, 강남역, 양재역, 5);

        // when & then
        Sections sections = createSections(singletonList(section));
        assertThat(sections.findInOrderStations()).containsExactly(강남역, 양재시민의숲역);

        // when & then
        sections.addSection(newSection);
        assertThat(sections.findInOrderStations()).containsExactly(강남역, 양재역, 양재시민의숲역);
    }

    @DisplayName("기존 구간 사이에 신규 역이 등록되면, 구간이 변경된다. (신규 구간의 하행역이 기존에 노선에 등록되어 있는 경우)")
    @Test
    void addSectionMakeExistedSectionChange_existDownStation() {
        // given
        Station 강남역 = createStation("강남역");
        Station 양재역 = createStation("양재역");
        Station 양재시민의숲역 = createStation("양재시민의숲역");
        Line 신분당선 = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        Section section = createSection(신분당선, 강남역, 양재시민의숲역, 15);
        Section newSection = createSection(신분당선, 양재역, 양재시민의숲역, 5);

        // when & then
        Sections sections = createSections(singletonList(section));
        assertThat(sections.findInOrderStations()).containsExactly(강남역, 양재시민의숲역);

        // when & then
        sections.addSection(newSection);
        assertThat(sections.findInOrderStations()).containsExactly(강남역, 양재역, 양재시민의숲역);
    }
}
