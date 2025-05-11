package com.carlospi.crow.controller;

import com.carlospi.crow.model.enumeration.GeneroEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enums")
public class EnumController {

    @GetMapping("/generos")
    public GeneroEnum[] getGenders() {
        return GeneroEnum.values();
    }

    
}
