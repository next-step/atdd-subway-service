package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    Line 신분당선;
    Station 강남역;
    Station 판교역;
    Station 정자역;
    Section 판교_정자;

    SectionRequest 강남_판교_요청;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-100");
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("졍자역");
        판교_정자 = new Section(신분당선, 판교역, 정자역, 3);

        //초기 구간 등록
        Section 강남_판교 = new Section(신분당선, 강남역, 판교역, 10);
        Sections sections = new Sections();
        sections.add(강남_판교);
        강남_판교_요청 = new SectionRequest(1L, 2L, 10);
        신분당선.addLineStation(강남_판교_요청, 강남역, 판교역);
    }


    @Test
    @DisplayName("이미 등록된 구간 등록 못함")
    void addLineStationExists() {
        assertThatThrownBy(() -> 신분당선.addLineStation(강남_판교_요청, 강남역, 판교역))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구간에 일치하는 역이 없어 구간등록 못함")
    void addLineStationNotMatch() {
        Station 미금역 = new Station("미금역");
        SectionRequest sectionRequest = new SectionRequest(3L, 4L, 2);

        assertThatThrownBy(() -> 신분당선.addLineStation(sectionRequest, 정자역, 미금역))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구간 정상 등록")
    void addLineStationSuccess() {
        SectionRequest 판교_정자_요청 = new SectionRequest(2L, 3L, 2);
        신분당선.addLineStation(판교_정자_요청, 판교역, 정자역);

        assertThat(신분당선.getStations()).contains(강남역, 판교역, 정자역);
    }
}