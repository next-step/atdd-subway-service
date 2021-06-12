package nextstep.subway.line.domain;

import java.util.Objects;

public class LineName {
    private String name;

    public LineName(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("이름은 공백이거나 공백이면 안됩니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineName lineName = (LineName) o;
        return Objects.equals(name, lineName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
