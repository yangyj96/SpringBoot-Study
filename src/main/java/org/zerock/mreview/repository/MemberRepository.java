package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m,mi, avg(coalesce(r.grade,0)), count(r) from Movie m " +
    "left outer join MovieImage mi on mi.movie = m group by m" +
    "left outer join Review r on r.movie = m group by m ")
    Page<Object[]> getListPage(Pageable pageable);
}
