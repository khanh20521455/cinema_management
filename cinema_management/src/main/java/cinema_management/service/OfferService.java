package cinema_management.service;

import cinema_management.entities.Movie;
import cinema_management.entities.Offer;
import cinema_management.entities.Room;
import cinema_management.helper.Message;
import cinema_management.repository.MovieRepository;
import cinema_management.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    @Autowired
    OfferRepository offerRepository;
    @Autowired
    MovieRepository movieRepository;
    public String getAll(Integer page, Model model){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Offer> offerList = offerRepository.getAll(pageable);
        model.addAttribute("offerList", offerList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", offerList.getTotalPages());
        return "adminuser/offer/offer_management";
    }
    public String addOffer(Model model){
        List<Movie> movieList= movieRepository.movieUpcomingAndPlaying(new Date(System.currentTimeMillis()));
        model.addAttribute("movieList", movieList);
        model.addAttribute("offer", new Offer());
        return "adminuser/offer/add_offer";
    }
    public String addOfferProcess(Offer offer, HttpSession session) throws Exception{
        offerRepository.save(offer);
        session.setAttribute("message", new Message("Thêm phòng mới thành công!", "success"));
        return "redirect:/admin/offer_management/0";
    }
    public String updateOffer(Integer id, Model model){
        Offer oldOffer = offerRepository.getById(id);
        List<Movie> movieList= movieRepository.movieUpcomingAndPlaying(new Date(System.currentTimeMillis()));
        model.addAttribute("movieList", movieList);
        model.addAttribute("offer", oldOffer);
        return "adminuser/offer/update_offer";
    }
    public String updateOfferProcess(Integer id, Offer offer, HttpSession session){
        Optional<Offer> optional = this.offerRepository.findById(id);
        Offer oldOffer = optional.get();
        try{
            offer.setId(oldOffer.getId());
            this.offerRepository.save(offer);
            session.setAttribute("message", new Message("Cập nhập phòng thành công!", "success"));
        }catch(Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Đã xảy ra lỗi " + e.getMessage(), "danger"));
        }
        return  "redirect:/admin/offer_management/0";
    }
}
