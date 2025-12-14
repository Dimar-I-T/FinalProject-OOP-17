package com.dimar.floorislavabackend.repository;

import com.dimar.floorislavabackend.model.Player;
import com.dimar.floorislavabackend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {
    @Query(value = "select * from scores s where s.player_id = :playerId", nativeQuery = true)
    List<Score> findByPlayerId(UUID playerId);
    boolean existsByPlayerId(UUID playerId);

    List<Score> findByPlayerIdOrderByValueDesc(UUID playerId);
    List<Score> findByValueGreaterThan(Integer minValue);
    List<Score> findAllByOrderByCreatedAtDesc();

    @Query(value = "select * from scores s order by s.score desc limit :limit", nativeQuery = true)
    List<Score> findTopScores(@Param("limit") int limit);

    @Query(value = "select * from scores s where s.player_id = :playerId order by s.score desc", nativeQuery = true)
    List<Score> findHighestScoreByPlayerId(@Param("playerId") UUID playerId);

    @Query(value = "select sum(s.coins_collected) from scores s where s.player_id = :playerId", nativeQuery = true)
    Integer getTotalCoinsByPlayerId(@Param("playerId") UUID playerId);

    @Query(value = "select sum(s.distance_travelled) from scores s where s.player_id = :playerId", nativeQuery = true)
    Integer getTotalDistanceByPlayerId(UUID playerId);

    @Query(value = "select * from scores s where s.player_id = :playerId order by s.score desc limit 1", nativeQuery = true)
    Optional<Score> getHighestScoreByPlayerId1(@Param("playerId") UUID playerId);
}