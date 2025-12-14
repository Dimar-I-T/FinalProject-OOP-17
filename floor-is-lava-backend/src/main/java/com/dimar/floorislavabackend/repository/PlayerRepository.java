package com.dimar.floorislavabackend.repository;

import com.dimar.floorislavabackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Optional<Player> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(value = "select * from players order by high_score desc LIMIT :limit", nativeQuery = true)
    List<Player> findTopPlayersByHighScore(@Param("limit") int limit);

    List<Player> findByHighScoreGreaterThan(Integer minScore);
    List<Player> findAllByOrderByTotalCoinsDesc();

    List<Player> findAllByOrderByTotalDistanceDesc();

    @Query(value = "select * from players", nativeQuery = true)
    List<Player> getAllPlayers();
}
