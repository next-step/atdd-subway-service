package nextstep.subway.line.domain.section;

import nextstep.subway.exception.SectionException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        validation(section);
        sections.add(section);
    }

    private void validation(Section section) {
        validateDuplicateSection(section);
    }

    private void validateDuplicateSection(Section section) {
        boolean isDuplicate = sections.stream().anyMatch(it -> it.isSame(section));
        if (isDuplicate) {
            throw new SectionException("노선이 중복됩니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
