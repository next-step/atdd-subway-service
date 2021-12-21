package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathDtos;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

class PathAssemblerTest {
    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    private Section 강남_양재_구간;
    private Section 양재_판교_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");

        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 8);
        양재_판교_구간 = new Section(신분당선, 양재역, 판교역, 2);
    }

    @DisplayName("response 변환")
    @Test
    void writeResponse() {
        List<Section> sections = Arrays.asList(강남_양재_구간, 양재_판교_구간);
        PathDtos paths = PathDtos.from(sections);
        StationGraph stationGraph = new StationGraph(paths);
        LoginMember loginMember = new LoginMember(1L, "email@email.com", 30);
        PathFinder pathFinder = new PathFinder(stationGraph, 강남역, 판교역);
        List<StationResponse> stationResponses = StationResponses.from(Arrays.asList(강남역, 양재역, 판교역))
            .getResponses();
        PathResponse pathResponse = new PathResponse(stationResponses, 10, 1250);

        PathResponse actual = PathAssembler.writeResponse(loginMember, pathFinder, sections);

        assertThat(actual)
            .isEqualTo(pathResponse);
    }
}