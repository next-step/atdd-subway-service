package nextstep.subway.line.domain;

import com.google.common.collect.Lists;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Sections 단위 테스트")
class SectionsTest {

    @Test
    @DisplayName("비어있는 구간 목록에 구간을 추가한다.")
    public void addSection_empty() throws Exception {
        // given
        Sections sections = new Sections();
        Section section = new Section(new Station("강남역"), new Station("양재역"));

        // when
        sections.addSection(section);

        // then
        assertThat(sections.getSections().get(0)).isEqualTo(section);
    }

    @Test
    @DisplayName("이미 존재하는 구간을 추가한다.")
    public void addSection_already() throws Exception {
        // given
        Section section = new Section(new Station("강남역"), new Station("양재역"));
        Sections sections = new Sections(Arrays.asList(section));

        // when, then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("현재 구간에 존재하지 않는 역으로 구성된 구간을 추가한다.")
    public void addSection_not_found() throws Exception {
        // given
        Section section = new Section(new Station("강남역"), new Station("양재역"));
        Section newSection = new Section(new Station("판교역"), new Station("금정역"));
        Sections sections = new Sections(Arrays.asList(section));

        // when, then
        assertThatThrownBy(() -> sections.addSection(newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("상행 종점에 구간을 추가한다.")
    public void addSection_상행_종점() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section section = new Section(양재역, 판교역, 3);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section)));

        // when
        sections.addSection(new Section(강남역, 양재역, 7));

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @Test
    @DisplayName("하행 종점에 구간을 추가한다.")
    public void addSection_하행_종점() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section section = new Section(강남역, 양재역, 3);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section)));

        // when
        sections.addSection(new Section(양재역, 판교역, 7));

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @Test
    @DisplayName("상행 기준으로 중간에 구간을 추가한다.")
    public void addSection_중간_상행_기준() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section section = new Section(강남역, 판교역, 10);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section)));

        // when
        sections.addSection(new Section(강남역, 양재역, 3));

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @Test
    @DisplayName("하행 기준으로 중간에 구간을 추가한다.")
    public void addSection_중간_하행_기준() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section section = new Section(강남역, 판교역, 10);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section)));

        // when
        sections.addSection(new Section(양재역, 판교역, 3));

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @Test
    @DisplayName("정렬된 지하철역 리스트를 리턴한다.")
    public void getStations() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");
        Section 양재_판교_구간 = new Section(양재역, 판교역);
        Section 강남_양재_구간 = new Section(강남역, 양재역);
        Sections sections = new Sections(Arrays.asList(양재_판교_구간, 강남_양재_구간));

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @Test
    @DisplayName("구간이 1개인 경우에 지하철역을 제거한다.")
    public void remove_only_one() throws Exception {
        // given
        Line line = new Line("신분당선", "red");

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section section = new Section(line, 강남역, 판교역, 10);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(section)));

        // when, then
        assertThatThrownBy(() -> sections.remove(line, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간이 1개일 경우에는 역을 제거할 수 없습니다.");
    }

    @Test
    @DisplayName("구간에서 상행 종점역을 제거한다.")
    public void remove_상행_종점() throws Exception {
        // given
        Line line = new Line("신분당선", "red");

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section 강남_양재_구간 = new Section(line, 강남역, 양재역, 3);
        Section 양재_판교_구간 = new Section(line, 양재역, 판교역, 7);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(강남_양재_구간, 양재_판교_구간)));

        // when
        sections.remove(line, 강남역);

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(양재역, 판교역);
    }

    @Test
    @DisplayName("구간에서 하행 종점역을 제거한다.")
    public void remove_하행_종점() throws Exception {
        // given
        Line line = new Line("신분당선", "red");

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section 강남_양재_구간 = new Section(line, 강남역, 양재역, 3);
        Section 양재_판교_구간 = new Section(line, 양재역, 판교역, 7);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(강남_양재_구간, 양재_판교_구간)));

        // when
        sections.remove(line, 판교역);

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(강남역, 양재역);
    }

    @Test
    @DisplayName("구간에서 중간역을 제거한다.")
    public void remove_중간역() throws Exception {
        // given
        Line line = new Line("신분당선", "red");

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section 강남_양재_구간 = new Section(line, 강남역, 양재역, 3);
        Section 양재_판교_구간 = new Section(line, 양재역, 판교역, 7);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(강남_양재_구간, 양재_판교_구간)));

        // when
        sections.remove(line, 양재역);

        // then
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(강남역, 판교역);
    }

    @Test
    @DisplayName("지하철역 경로 목록을 순회하며 구간에 포함되는지 여부를 반환한다.")
    public void isContains() throws Exception {
        // given
        Line line = new Line("신분당선", "red");

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Section 강남_양재_구간 = new Section(line, 강남역, 양재역, 3);
        Section 양재_판교_구간 = new Section(line, 양재역, 판교역, 7);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(강남_양재_구간, 양재_판교_구간)));

        List<Station> 양재_판교 = new ArrayList<>(Arrays.asList(양재역, 판교역));
        List<Station> 강남_판교 = new ArrayList<>(Arrays.asList(강남역, 판교역));

        // when
        boolean 양재_판교_결과 = sections.isContains(양재_판교);
        boolean 강남_판교_결과 = sections.isContains(강남_판교);

        // then
        assertThat(양재_판교_결과).isTrue();
        assertThat(강남_판교_결과).isFalse();
    }
}