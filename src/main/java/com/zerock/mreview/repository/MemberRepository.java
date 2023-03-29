package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.Member;

// 프록시 서버는 클라이언트가 자신을 통해서 다른 네트워크 서비스에 간접적으로 접속할 수 있게 해 주는 컴퓨터 시스템이나 응용 프로그램

public interface MemberRepository extends JpaRepository<Member, Long> {

}
