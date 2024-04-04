package com.apc.bifrost3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @GetMapping("/items")
    public String shop(ModelMap model) {
        model.addAttribute("pageTitle", " Items");

        return "shop/items";
    }

}
