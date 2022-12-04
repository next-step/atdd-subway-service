package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static nextstep.subway.utils.Message.INVALID_EMPTY_STRING;

@Embeddable
public class Name {
    @Column(unique = true, nullable = false)
    private String name;

    protected Name() {
    }

    private Name(String name) {
        this.name = name;
    }

    public static Name from(String name) {
        checkNotNull(name);
        return new Name(name);
    }

    private static void checkNotNull(String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException(INVALID_EMPTY_STRING);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
