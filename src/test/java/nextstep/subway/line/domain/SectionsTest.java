package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DuplicateSectionException;
import nextstep.subway.line.exception.MinimumSectionException;
import nextstep.subway.line.exception.NotFoundSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    private Station gangnam;
    private Station yangjae;
    private Station gyodae;
    private Station hongdae;
    private Section gangnamYangjae;
    private Section yangjaeGyodae;
    private Section gyodaeHongdae;
    private Line line;
    private Sections sections = new Sections();

    @BeforeEach
    public void setUp() {
        gangnam = new Station("강남역");
        yangjae = new Station("양재역");
        gyodae = new Station("교대역");
        hongdae = new Station("홍대역");
        line = new Line("2호선", "green", gangnam, yangjae, 10);
        gangnamYangjae = new Section(line, gangnam, yangjae, 10);
        yangjaeGyodae = new Section(line, yangjae, gyodae, 5);
        gyodaeHongdae = new Section(line, gyodae, hongdae, 2);
        sections.addSection(gangnamYangjae);
    }

    @DisplayName("구간 등록 테스트")
    @Test
    void addSection() {
        sections.addSection(yangjaeGyodae);

        List<Station> stations = sections.getStations();
        assertThat(stations).isNotNull();
        assertThat(stations).containsExactly(gangnam, yangjae, gyodae);
    }

    @DisplayName("예외처리 테스트: 이미 등록된 구간")
    @Test
    void DuplicateSectionException() {
        assertThatThrownBy(() -> {
            sections.addSection(gangnamYangjae);
        }).isInstanceOf(DuplicateSectionException.class);
    }

    @DisplayName("예외처리 테스트: 등록되지않은 구간")
    @Test
    void NotFoundSectionException() {
        assertThatThrownBy(() -> {
            sections.addSection(gyodaeHongdae);
        }).isInstanceOf(NotFoundSectionException.class);
    }

    @DisplayName("구간 삭제 테스트")
    @Test
    void removeSection() {
        sections.addSection(yangjaeGyodae);

        sections.removeStation(yangjae, line);

        List<Station> stations = sections.getStations();
        assertThat(stations).isNotNull();
        assertThat(stations).containsExactly(gangnam, gyodae);
    }

    @DisplayName("예외처리 테스트: 최소 구간인 경우")
    @Test
    void removeSection2() {
        assertThatThrownBy(() -> {
            sections.removeStation(yangjae, line);
        }).isInstanceOf(MinimumSectionException.class);
    }

    @Test
    void getStations() {
        List<Station> stations = sections.getStations();

        assertThat(stations).containsExactly(gangnam, yangjae);
    }

    @Test
    void findSectionWithUpStation() {
        sections.addSection(yangjaeGyodae);

        Optional<Section> sectionOptional = sections.findSectionWithUpStation(yangjae);

        assertThat(sectionOptional.get()).isEqualTo(yangjaeGyodae);
    }

    @Test
    void findSectionWithDownStation() {
        sections.addSection(yangjaeGyodae);
        sections.addSection(gyodaeHongdae);

        Optional<Section> sectionOptional = sections.findSectionWithDownStation(hongdae);

        assertThat(sectionOptional.get()).isEqualTo(gyodaeHongdae);
    }



}
