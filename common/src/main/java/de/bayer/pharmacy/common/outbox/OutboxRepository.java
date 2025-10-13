package de.bayer.pharmacy.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select m from OutboxMessage m where m.published = false order by m.id asc")
//    List<OutboxMessage> fetchBatch(Pageable pageable);

    @Query(value = """
      update outbox
         set status='IN_PROGRESS'
       where id in (
         select id from outbox
          where status='PENDING'
            and next_attempt_at <= now()
          order by created_at
          for update skip locked
          limit :lim
       )
      returning *
      """, nativeQuery = true)
    List<OutboxMessage> claimBatch(@Param("lim") int limit);
}