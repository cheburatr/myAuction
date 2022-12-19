package com.gleb_dev.my_auction.controller;

import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.entity.User;
import com.gleb_dev.my_auction.exceptions.BetCreateException;
import com.gleb_dev.my_auction.request.BetRequest;
import com.gleb_dev.my_auction.services.BetService;
import com.gleb_dev.my_auction.services.LotService;
import com.gleb_dev.my_auction.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

// Controller that is responsible for creating and deletion of lots
@Controller
@RequestMapping("/bet")
public class BetController {

    Logger logger = LoggerFactory.getLogger(getClass());

    LotService lotService;
    BetService betService;
    UserService userService;

    @Autowired
    public BetController(LotService lotService, BetService betService, UserService userService) {
        this.lotService = lotService;
        this.betService = betService;
        this.userService = userService;
    }

    @GetMapping("/create/{lotId}")
    public String showBetCreationForm(@PathVariable("lotId") long lotId,
                                      Model model,
                                      Principal principal){

        Lot lot = lotService.getLotById(lotId);
        User user = userService.getCurrentUser(principal);

        if(lot.getUser().equals(user)){
            throw new BetCreateException("You can't bet on your lot");
        }

        model.addAttribute("betRequest", new BetRequest());
        model.addAttribute("lotId", lotId);

        return "betCreateForm";
    }

    @PostMapping("/create/{lotId}")
    public String processBetCreation(@PathVariable("lotId") long lotId,
                                     @Valid @ModelAttribute BetRequest betRequest,
                                     BindingResult bindingResult,
                                     Model model,
                                     Principal principal){

        // Checking the correctness of the data entered
        if (bindingResult.hasErrors()) {
            return "betCreateForm";
        }

        try {
            betService.createBet(lotId, betRequest, principal);
        }
        catch (BetCreateException ex){
            logger.error(ex.getMessage());
            model.addAttribute("betError", ex.getMessage());
            return "betCreateForm";
        }


        return "redirect:/lot/" + lotId;
    }

    @GetMapping("/delete/{id}")
    public String deleteBet(@PathVariable("id") long id,
                            Principal principal) {
        long lotId = betService.getBetById(id).getLot().getId();
        betService.deleteBet(id, principal);
        return "redirect:/lot/"+lotId;
    }

}
