package com.mysite.shop.member;

import com.mysite.shop.constant.Role;
import com.mysite.shop.member.MemberFormDto;
import com.mysite.shop.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)	//DB 에 저장될때 숫자로 저장이되지 않고 String 형식으로 저장  
    private Role role;

    
    // Entity 클래스에서 메소드 선언 
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
//        member.setRole(Role.ADMIN);	// 회원가입시 기본 Role은 Admin
        member.setRole(Role.USER);      // 회원 가입시 기본 Role 은 User
        return member;
    }

}
