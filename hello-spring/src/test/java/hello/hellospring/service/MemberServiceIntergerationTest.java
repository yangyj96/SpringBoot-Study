package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceIntergerationTest {
    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    @Test
    @Commit
    void 회원가입() {
        Member member = new Member();
        member.setName("spring100");
    }

}
