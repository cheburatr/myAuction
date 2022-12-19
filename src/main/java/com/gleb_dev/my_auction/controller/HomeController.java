package com.gleb_dev.my_auction.controller;

import com.gleb_dev.my_auction.entity.Image;
import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.exceptions.ImageNotFoundException;
import com.gleb_dev.my_auction.response.LotWithImageResponse;
import com.gleb_dev.my_auction.services.ImageService;
import com.gleb_dev.my_auction.services.LotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

// Controller that is responsible for home page
@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(getClass());

    ImageService imageService;
    LotService lotService;

    @Autowired
    public HomeController(ImageService imageService, LotService lotService) {
        this.imageService = imageService;
        this.lotService = lotService;
    }

    @GetMapping({"/", "/index", "/home"})
    public String showHomePage(Model model){

        List<Lot> firstLots = lotService.getFirst10Lots();

        List<LotWithImageResponse> lotsWithImages = imageService.getLotsWithImages(firstLots);

        model.addAttribute("lotsWithImages", lotsWithImages);

        return "index";
    }
}
