package com.thxpapa.juneberrydiary.domain.user;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import com.thxpapa.juneberrydiary.domain.blog.BlogUser;
import com.thxpapa.juneberrydiary.domain.geo.Spot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name="juneberry_user", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "juneberryUserUid", callSuper=false)
public class JuneberryUser extends BaseTimeEntity implements UserDetails {
    @Id
    @Column(name="juneberry_user_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long juneberryUserUid;

    @Comment("이름")
    @Column(name="name", length = 45, nullable = false)
    private String name;

    @Comment("이메일")
    @Column(name="email", length = 45, nullable = false)
    private String email;

    @Comment("유저이름")
    @Column(name="username", length = 45, nullable = false)
    private String username;

    @Comment("비밀번호")
    @Column(name="password", length = 255, nullable = true)
    private String password;

    @Lob
    @Comment("자기소개")
    @Column(name="intro", nullable = true)
    private String intro;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "juneberryUser", fetch=FetchType.EAGER)
    private List<Spot> spots = new ArrayList<Spot>();

    @OneToMany(mappedBy = "juneberryUser", fetch=FetchType.EAGER)
    private List<BlogUser> blogUsers = new ArrayList<BlogUser>();

    @Builder
    public JuneberryUser(String name, String email, String username, String password, List<String> roles, String intro) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.intro = intro;
    }
}
