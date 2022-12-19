package com.gleb_dev.my_auction.services;


import com.gleb_dev.my_auction.entity.Bet;
import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.entity.User;
import com.gleb_dev.my_auction.entity.WinningBet;
import com.gleb_dev.my_auction.exceptions.LotNotFoundException;
import com.gleb_dev.my_auction.exceptions.WinningBetCreateException;
import com.gleb_dev.my_auction.repository.LotRepository;
import com.gleb_dev.my_auction.repository.UserRepository;
import com.gleb_dev.my_auction.repository.WinningBetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WinningBetService {

    private WinningBetRepository winningBetRepository;
    private UserRepository userRepository;
    private LotRepository lotRepository;

    @Autowired
    public WinningBetService(WinningBetRepository winningBetRepository, UserRepository userRepository, LotRepository lotRepository) {
        this.winningBetRepository = winningBetRepository;
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
    }

    public List<WinningBet> saveWinningBets(List<WinningBet> winningBets) {
        return winningBetRepository.saveAll(winningBets);
    }

    public List<WinningBet> getAllWinningBetsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        return winningBetRepository.findAllByBetUserId(user.getId());
    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
}
