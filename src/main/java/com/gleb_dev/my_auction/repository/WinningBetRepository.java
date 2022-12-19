package com.gleb_dev.my_auction.repository;

import com.gleb_dev.my_auction.entity.Bet;
import com.gleb_dev.my_auction.entity.WinningBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WinningBetRepository extends JpaRepository<WinningBet, Long> {

    List<WinningBet> findAllByBetUserId(long id);
}
