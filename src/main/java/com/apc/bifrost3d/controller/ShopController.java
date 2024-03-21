package com.apc.bifrost3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private String appName = "Bifrost3D |";

    @GetMapping("/items")
    public String shop(ModelMap model) {
        model.addAttribute("pageTitle", appName + " Items");

        return "shop/items";
    }

}
