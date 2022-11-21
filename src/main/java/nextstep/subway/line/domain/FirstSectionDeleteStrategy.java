package nextstep.subway.line.domain;

public class FirstSectionDeleteStrategy implements SectionDeleteStrategy {
    @Override
    public void delete(Line line, Section sectionOfUpStation, Section sectionOfDownStation) {
        line.deleteSection(sectionOfUpStation);
    }
}
