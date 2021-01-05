package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        sections.addSection(yangjaeGyodae);
        sections.addSection(gyodaeHongdae);
    }
    // stations 리스트 올바르게 나오는지 check

    @Test
    void getStations() {
        List<Station> stations = sections.getStations();

        assertThat(stations).containsExactly(gangnam, yangjae, gyodae, hongdae);
    }

    @Test
    void findSectionWithUpStation() {
        Optional<Section> sectionOptional = sections.findSectionWithUpStation(yangjae);

        assertThat(sectionOptional.get()).isEqualTo(yangjaeGyodae);
    }

    @Test
    void findSectionWithDownStation() {
        Optional<Section> sectionOptional = sections.findSectionWithDownStation(hongdae);

        assertThat(sectionOptional.get()).isEqualTo(gyodaeHongdae);
    }



}
