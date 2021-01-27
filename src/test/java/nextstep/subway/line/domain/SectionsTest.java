package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SectionsTest {

    private Line 일호선;
    private Station 소요산역;
    private Station 서울역;
    private Station 창동역;
    private Station 인천역;
    private Sections 구간들;

    @BeforeEach
    void setUp() {
        구간들 = new Sections();
        소요산역 = new Station("소요산역");
        창동역 = new Station("창동역");
        서울역 = new Station("서울역");
        인천역 = new Station("인천역");
        일호선 = Line.builder()
                .name("1호선")
                .color("파란색")
                .upStation(소요산역)
                .downStation(인천역)
                .distance(50)
                .build();

        구간들 = 일호선.getSections();

        구간들.add(Section.builder()
                .line(일호선)
                .upStation(소요산역)
                .downStation(창동역)
                .distance(10)
                .build());
    }

    @DisplayName("지하철 구간 정렬")
    @Test
    void orderStations() {
        지하철_노선_구간_순서_검증(소요산역, 창동역, 인천역);
    }

    @DisplayName("역과 역사이에 새로운 역을 등록할 경우 : 기존 상행역 - 새로운 하행역 관계")
    @Test
    void addSection() {
        구간들.add(Section.builder()
                .line(일호선)
                .upStation(소요산역)
                .downStation(서울역)
                .distance(5)
                .build());

        지하철_노선_구간_순서_검증(소요산역, 서울역, 창동역, 인천역);
    }

    @DisplayName("역과 역사이에 새로운 역을 등록할 경우 : 기존 하행역 - 새로운 상행역 관계")
    @Test
    void addSection2() {
        구간들.add(Section.builder()
                .line(일호선)
                .upStation(서울역)
                .downStation(인천역)
                .distance(5)
                .build());

        지하철_노선_구간_순서_검증(소요산역, 창동역, 서울역, 인천역);
    }

    @DisplayName("새로운 역을 상행 종점으로 등록할 경우")
    @Test
    void addSection3() {
        구간들.add(Section.builder()
                .line(일호선)
                .upStation(서울역)
                .downStation(소요산역)
                .distance(5)
                .build());

        지하철_노선_구간_순서_검증(서울역, 소요산역, 창동역, 인천역);
    }

    @DisplayName("새로운 역을 하행 종점으로 등록할 경우")
    @Test
    void addSection4() {
        구간들.add(Section.builder()
                .line(일호선)
                .upStation(인천역)
                .downStation(서울역)
                .distance(5)
                .build());

        지하철_노선_구간_순서_검증(소요산역, 창동역, 인천역, 서울역);
    }

    @DisplayName("상행역 아래에 새로운 역을 등록할 경우 기존 길이보다 같거나 크면 등록이 안됨")
    @Test
    void addInvalidDistanceUpStation() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.add(Section.builder()
                    .line(일호선)
                    .upStation(소요산역)
                    .downStation(서울역)
                    .distance(10)
                    .build());
        });
    }

    @DisplayName("하행역 위에 새로운 역을 등록할 경우 기존 길이보다 같거나 크면 등록이 안됨")
    @Test
    void addInvalidDistanceDownStation() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.add(Section.builder()
                    .line(일호선)
                    .upStation(서울역)
                    .downStation(인천역)
                    .distance(50)
                    .build());
        });
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록 되었다면 추가 안됨")
    @Test
    void alreadyExistStations() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.add(Section.builder()
                    .line(일호선)
                    .upStation(소요산역)
                    .downStation(인천역)
                    .distance(50)
                    .build());
        });
    }

    @DisplayName("상하행역 중 하나라도 포함되어있지 않을 경우")
    @Test
    void notExistStations() {
        Station 동대문역 = new Station("동대문역");
        Station 용산역 = new Station("용산역");
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.add(Section.builder()
                    .line(일호선)
                    .upStation(동대문역)
                    .downStation(용산역)
                    .distance(50)
                    .build());
        });
    }

    @DisplayName("역과 역사이 역 삭제")
    @Test
    void deleteSection() {
        구간들.removeSection(일호선, 창동역);
        지하철_노선_구간_순서_검증(소요산역, 인천역);
    }

    @DisplayName("상행역 종점 삭제")
    @Test
    void deleteSection2() {
        구간들.removeSection(일호선, 소요산역);
        지하철_노선_구간_순서_검증(창동역, 인천역);
    }

    @DisplayName("하행역 종점 삭제")
    @Test
    void deleteSection3() {
        구간들.removeSection(일호선, 인천역);
        지하철_노선_구간_순서_검증(소요산역, 창동역);
    }

    @DisplayName("노선에 등록되지 않은 역 제거 예외처리")
    @Test
    void deleteSection4() {
        Station 동대문역 = new Station("동대문역");
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.removeSection(일호선, 동대문역);
        });
    }

    @DisplayName("등록된 구간이 1개일 때 상행역 제거 예외처리")
    @Test
    void deleteSection5() {
        구간들.removeSection(일호선, 창동역);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.removeSection(일호선, 소요산역);
        });
    }

    @DisplayName("등록된 구간이 1개일 때 하행역 제거 예외처리")
    @Test
    void deleteSection6() {
        구간들.removeSection(일호선, 창동역);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            구간들.removeSection(일호선, 인천역);
        });
    }

    private void 지하철_노선_구간_순서_검증(Station... stations) {
        assertThat(구간들.getStations()).containsExactly(stations);
    }
}