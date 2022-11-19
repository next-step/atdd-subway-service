package nextstep.subway.line.domain;

public class LastSectionDeleteStrategy implements SectionDeleteStrategy {
    @Override
    public void delete(Line line, Section sectionOfUpStation, Section sectionOfDownStation) {
        line.deleteSection(sectionOfDownStation);
    }
}
