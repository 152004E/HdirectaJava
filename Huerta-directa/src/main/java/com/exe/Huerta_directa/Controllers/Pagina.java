package com.exe.Huerta_directa.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Pagina {

    @GetMapping({"/", "/index"})
    public String info() {
        return "index"; // busca templates/index.html
    }
}
