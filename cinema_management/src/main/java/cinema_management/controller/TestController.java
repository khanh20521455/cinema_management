package cinema_management.controller;

import cinema_management.entities.Theater;
import cinema_management.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class TestController {
    @Autowired
    private TheaterService theaterService;
    @GetMapping("/add_theater")
    public String addTheater(Model model){
        return theaterService.addTheater(model);
    }
    @PostMapping("/add_theater_process")
    public String addTheaterProcess(@ModelAttribute Theater theater, HttpSession httpSession){
        return theaterService.addTheaterProcess(theater, httpSession);
    }
    @PostMapping("/update_theater_process/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute Theater theater, HttpSession httpSession){
        return theaterService.updateTheaterProcess(id, theater,httpSession);
    }
}
