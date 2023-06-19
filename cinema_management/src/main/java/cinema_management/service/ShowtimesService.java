package cinema_management.service;

import cinema_management.entities.Movie;
import cinema_management.entities.Room;
import cinema_management.entities.Seat;
import cinema_management.entities.Showtimes;
import cinema_management.helper.Message;
import cinema_management.repository.MovieRepository;
import cinema_management.repository.RoomRepository;
import cinema_management.repository.SeatRepository;
import cinema_management.repository.ShowtimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimesService {
    @Autowired
    private ShowtimesRepository showtimesRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SeatRepository seatRepository;
    public String showtimesManagement(Integer page, Model model){
        Pageable pageable = PageRequest.of(page, 3);
        Page<Showtimes> showtimesList = showtimesRepository.findAllOrderByDateAsc(pageable);
        model.addAttribute("showtimesList",showtimesList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", showtimesList.getTotalPages());
        return "adminuser/showtimes/showtimes_management";
    }
    public String addShowtimes(Model model){
        model.addAttribute("showtimes", new Showtimes());
        List <Movie> movieList= movieRepository.movieList();
        List <Room> roomList= roomRepository.roomList();
        model.addAttribute("movieList", movieList);
        model.addAttribute("roomList", roomList);
        return "adminuser/showtimes/add_showtimes";
    }
    public String addShowtimesProcess(Showtimes showtimes, HttpSession session){
        try {
           showtimesRepository.save(showtimes);
            session.setAttribute("message", new Message("New showtime has successfully added", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong, try again ! ", "danger"));
        }
        for (int i=0; i<40;i++){
            Seat seat=new Seat();
            seat.setSeat(i+1);
            seat.setStatus(0);
            seat.setShowtimes(showtimes);
            seatRepository.save(seat);
        }
        return "adminuser/showtimes/add_showtimes";
    }
    public String updateShowtimes(Integer id, Model model){
        Optional<Showtimes> showtimesOptional = this.showtimesRepository.findById(id);
        Showtimes showtimes= showtimesOptional.get();
        List <Movie> movieList= movieRepository.movieList();
        List <Room> roomList= roomRepository.roomList();
        model.addAttribute("showtimes", showtimes);
        model.addAttribute("movieList", movieList);
        model.addAttribute("roomList", roomList);
        return "adminuser/showtimes/update_showtimes";
    }
    public String showtimesUpdateProcess(
            @PathVariable("id") Integer id,
            @ModelAttribute Showtimes showtimes,
            Model m, HttpSession session) {


        Optional<Showtimes> showtimesOptional = showtimesRepository.findById(id);
        Showtimes oldShowtimesDetail=showtimesOptional.get();

        try {
            showtimes.setId(oldShowtimesDetail.getId());
            showtimes.setMovie(showtimes.getMovie());
            showtimes.setRoom(showtimes.getRoom());
            showtimes.setTime(showtimes.getTime());
            showtimes.setDate(showtimes.getDate());
            showtimes.setPrice(showtimes.getPrice());
            this.showtimesRepository.save(showtimes);
            session.setAttribute("message", new Message("showtimes Updated Successfully.", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("showtimes", oldShowtimesDetail);
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));
        }

        m.addAttribute("showtimes", showtimes);
        return "adminuser/showtimes/update_showtimes";
    }
    public String deleteShowtimes(Integer id){
        seatRepository.deleteSeatBaseShowtimes(id);
        showtimesRepository.deleteById(id);
        return "redirect:/admin/showtimes_management/0";
    }
}