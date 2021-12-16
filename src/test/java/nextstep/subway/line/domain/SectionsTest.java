package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.station.domain.Station;

public class SectionsTest {
    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private Station 광교역;

    private Section 강남_판교_구간;
    private Section 강남_양재_구간;
    private Section 양재_판교_구간;
    private Section 퍈교_광교_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        광교역 = new Station("광교약");

        강남_판교_구간 = new Section(신분당선, 강남역, 판교역, 10);
        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 8);
        양재_판교_구간 = new Section(신분당선, 양재역, 판교역, 2);
        퍈교_광교_구간 = new Section(신분당선, 판교역, 광교역, 2);
    }

    @DisplayName("구간에서 역 목록을 가져온다")
    @Test
    void getStations() {
        Sections sections = Sections.from(
            Arrays.asList(강남_양재_구간, 양재_판교_구간));

        List<Station> stations = sections.getStations();

        assertThat(stations)
            .isEqualTo(Arrays.asList(강남역, 양재역, 판교역));
    }

    @DisplayName("구간에서 역 목록을 가져온다")
    @Test
    void getStations2() {
        Sections sections = Sections.from(
            Arrays.asList(강남_양재_구간, 양재_판교_구간));

        List<Station> stations = sections.getStations();

        assertThat(stations)
            .isEqualTo(Arrays.asList(강남역, 양재역, 판교역));
    }

    @DisplayName("하행역을 포함하는 구간을 추가했을 때 업데이트 성공")
    @Test
    void addStation_containsDownStation() {
        Sections sections = Sections.from(
            Arrays.asList(강남_판교_구간));

        sections.addStation(양재_판교_구간);

        assertThat(sections.getStations()).isEqualTo(Arrays.asList(강남역, 양재역, 판교역));
    }

    @DisplayName("상행역을 포함하는 구간을 추가했을 때 업데이트 성공")
    @Test
    void addStation_containsUpStation() {
        Sections sections = Sections.from(
            Arrays.asList(강남_판교_구간));

        sections.addStation(강남_양재_구간);

        assertThat(sections.getStations()).isEqualTo(Arrays.asList(강남역, 양재역, 판교역));
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가 실패")
    @Test
    void addStation_errorWhenSectionsContainsAll() {
        Sections sections = Sections.from(
            Arrays.asList(강남_판교_구간));

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> sections.addStation(강남_판교_구간))
            .withMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("상행역과 하행역이 모두 노선에 없다면 추가 실패")
    @Test
    void addStation_errorWhenSectionsNotInLine() {
        Sections sections = Sections.from(
            Arrays.asList(강남_양재_구간));

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> sections.addStation(퍈교_광교_구간))
            .withMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("지하철 종점 삭제")
    @Test
    void removeLastStation() {
        Sections sections = Sections.from(
            Arrays.asList(강남_양재_구간, 양재_판교_구간));

        sections.removeStation(판교역);

        assertThat(sections.getStations()).isEqualTo(Arrays.asList(강남역, 양재역));
    }

    @DisplayName("중간역이 제거될 경우 재배치를 함")
    @Test
    void removeBetweenStation() {
        Sections sections = Sections.from(
            Arrays.asList(강남_양재_구간, 양재_판교_구간));

        sections.removeStation(양재역);

        assertThat(sections.getStations()).isEqualTo(Arrays.asList(강남역, 판교역));
    }

    @DisplayName("구간이 하나일 때 삭제 에러")
    @Test
    void removeStation_errorWhenLineHasOnlyOneStation() {
        Sections sections = Sections.from(Arrays.asList(강남_양재_구간));

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> sections.removeStation(강남역))
            .withMessage("구간이 하나인 노선에서 마지막 구간을 제거할 수 없습니다.");
    }
}
