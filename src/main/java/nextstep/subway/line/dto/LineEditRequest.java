package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineEditRequest {
    private String name;
    private String color;
    private int money;

    public LineEditRequest() {
    }

    public LineEditRequest(String name, String color, int money) {
        this.name = name;
        this.color = color;
        this.money = money;
    }

    public static LineEditRequest of(String name, String color, int money) {
        return new LineEditRequest(name, color, money);
    }

    public Line toLine() {
        return new Line(name, color, money);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getMoney() {
        return money;
    }
}

