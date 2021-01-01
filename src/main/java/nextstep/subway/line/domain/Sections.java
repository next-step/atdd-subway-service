package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
    }


    public void remove(Line line, Station station) {
    }
}
