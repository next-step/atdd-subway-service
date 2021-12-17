package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Embedded
    private MemberPassword password;

    @Embedded
    private MemberAge age;

    public Member() {
    }

    public Member(String email, String password, int age) {
        this.email = email;
        this.password = new MemberPassword(password);
        this.age = new MemberAge(age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password.getPassword();
    }

    public int getAge() {
        return age.getAge();
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        this.password.checkPassword(password);
    }
}
