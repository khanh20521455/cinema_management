package cinema_management.controller;

import java.security.Principal;
import javax.servlet.http.HttpSession;
import cinema_management.entities.*;
import cinema_management.repository.*;
import cinema_management.service.MovieService;
import cinema_management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private MovieRepository movieRepository;

    // adding common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }

    // showing dashboard
    @RequestMapping("/dashboard")
    public String userDashboard(Model model, Principal principal) {
        model.addAttribute("title", "Admin Dashboard");
        return "adminuser/admin_dashboard";
    }
    //Room management
    @GetMapping("/room_management/{page}")
    public String getRoom(@PathVariable("page") Integer page, Model model){
        return roomService.getRoomManagement(page, model);
    }
    @GetMapping("/add_room")
    public String addRoom(Model model){
        return roomService.addRoom(model);
    }
    @PostMapping("/add_room_process")
    public String addRoomProcess(@ModelAttribute Room room, HttpSession session){
        return roomService.addRoomProcess(room,session);

    }
    @GetMapping("/update_room/{id}")
    public String updateRoom(@PathVariable("id") Integer id, Model model) {
        return roomService.updateRoom(id,model);
    }
    @PostMapping("update_room_process/{id}")
    public String movieUpdateProcess(
            @PathVariable("id") Integer id,
            @ModelAttribute Room room,
            Model model, HttpSession session) {
        return roomService.movieUpdateProcess(id,room, model,session);
    }
    @GetMapping("/delete_room/{id}")
    public String deleteRoom(@PathVariable("id") Integer id){
        return roomService.deleteRoom(id);
    }

    //Movie management
    @GetMapping("/movie_management/{page}")
    public String getMovie(@PathVariable("page") Integer page, Model model){
        return movieService.getMovieManagement(page,model);
    }
    @GetMapping("/add_movie")
    public String uploadMovie(Model model) {
        return movieService.addMovie(model);
    }
    @PostMapping("/add_movie_process")
    public 	String processMovie(
            @ModelAttribute Movie movie,
            @RequestParam("movieImageUrl") MultipartFile file,
            HttpSession session) {
        return movieService.addMovieProcess(movie, file,session);
    }
    @GetMapping("/update_movie/{id}")
    public String updateMovie(@PathVariable("id") Integer id, Model model){
        return movieService.updateMovie(id, model);
    }
    @PostMapping("/update_movie_process/{id}")
    public String updateMovieProcess(@PathVariable("id") Integer id,
                                     @ModelAttribute Movie movie,
                                     @RequestParam("movieImageUrl") MultipartFile file,
                                     Model model, HttpSession session){
        return movieService.movieUpdateProcess(id, movie,file,model,session);
    }
    @GetMapping("delete_movie/{id}")
    public String deleteMovie(@PathVariable("id") Integer id){
        return movieService.deleteMovie(id);
    }
}


