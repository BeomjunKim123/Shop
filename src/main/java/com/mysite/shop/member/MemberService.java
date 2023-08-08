package com.mysite.shop.member;

import com.mysite.shop.member.Member;
import com.mysite.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    
    // 인증을 처리하는 메소드  : UserDetailsService의 선언만된 메소드를 구현 해서 생성 
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);
        
        //로그로 변수의 값을 출력 : 배포한 서버에서는 colsole 에 값을 출력 할 수 없다.
        
        log.info("client form :  =====> : " + email);
        log.info("sever DB :=====> : " + member.getEmail()); 
        log.info("=====> : " + member.getRole()); 
        log.info("=====> : " + member.getRole().toString());

        
        // 넘겨받은 ID (email) 이 DB에 존재하지 않을 경우 
        if(member == null){
            throw new UsernameNotFoundException(email);  //예외(오류)를 강제로 발생 시킴 ""
        }

        
        // User 객체에는 3가지 값이 반드시 적용 : 1. ID , 2. Pass, 3. Authorization (Role) 
        // Spring Security 에서 인증이 완료되면  ROLE_USER, ROLE_ADMIN
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}