package cinema_management.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cinema_management.repository.MovieticketRepository;
import cinema_management.repository.PurchaseRepository;
import cinema_management.repository.UserRepository;
import cinema_management.entities.Movieticket;
import cinema_management.entities.Purchase;
import cinema_management.entities.User;
import cinema_management.helper.Message;




@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieticketRepository movieticketRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

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


    // movie upload form

    @GetMapping("/upload-movie")
    public String uploadMovie(Model model) {
        model.addAttribute("title", "Upload Movie");
        model.addAttribute("movieticket", new Movieticket());
        return "adminuser/upload_movie_form";
    }


    // insert movie to database

    @PostMapping("/process-uploaded-movie")
    public String processMovie(
            @ModelAttribute Movieticket movieticket,
            @RequestParam("movieImageUrl") MultipartFile file,
            HttpSession session) {

        try {

            if (file.isEmpty()) {
                movieticket.setMovieImage("Movie Poster Default Photo.png");
            } else {

                movieticket.setMovieImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("message", new Message("New Movie has been successfully uploaded", "success"));

            movieticket.setSeatRemaining(movieticket.getTotalSeat());
            movieticketRepository.save(movieticket);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong, try again ! ", "danger"));
        }

        return "adminuser/upload_movie_form";

    }


    // show upcoming movies Admin view

    @GetMapping("/show-upcoming-movie/{page}")
    public String showMovies(@PathVariable("page") Integer page, Model m) {

        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);

        Pageable pageable = PageRequest.of(page, 5);
        Page<Movieticket> movietickets = this.movieticketRepository.findByDateGreaterThanEqualOrderByDateAsc(date, pageable);

        m.addAttribute("title", "Movie list Admin view");
        m.addAttribute("movietickets", movietickets);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", movietickets.getTotalPages());

        return "adminuser/show_upcoming_movie";
    }


    // update movie form


    @GetMapping("/update-movie-form/{id}")
    public String updateMovieForm(@PathVariable("id") Integer id, Model m) {

        Optional<Movieticket> movieTiketOptional = this.movieticketRepository.findById(id);
        Movieticket movieticket = movieTiketOptional.get();

        m.addAttribute("Title", "Update Movie");
        m.addAttribute("movieticket", movieticket);
        return "adminuser/update_movie_form";
    }


    // update movie process

    @PostMapping("/process-movie-update/{id}")
    public String movieUpdateProcess(
            @PathVariable("id") Integer id,
            @ModelAttribute Movieticket movieticket,
            @RequestParam("movieImageUrl") MultipartFile file,
            Model m, HttpSession session) {


        Optional<Movieticket> movieTiketOptional = movieticketRepository.findById(id);
        Movieticket oldMovieTicketDetails = movieTiketOptional.get();

        try {

            if (oldMovieTicketDetails.getTotalSeat() > movieticket.getTotalSeat()) {
                throw new Exception("You can not decrease total seat.");
            } else {

                int extra_seat = movieticket.getTotalSeat() - oldMovieTicketDetails.getTotalSeat();
                movieticket.setSeatRemaining(oldMovieTicketDetails.getSeatRemaining() + extra_seat);
            }

            if (!file.isEmpty()) {

                //delete
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldMovieTicketDetails.getMovieImage());
                file1.delete();


                //update
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                movieticket.setMovieImage(file.getOriginalFilename());
            } else {
                movieticket.setMovieImage(oldMovieTicketDetails.getMovieImage());
            }

            movieticket.setMovieId(oldMovieTicketDetails.getMovieId());
            movieticket.setStartTime(oldMovieTicketDetails.getStartTime());
            this.movieticketRepository.save(movieticket);

            session.setAttribute("message", new Message("Movie Updated Successfully.", "success"));
        } catch (Exception e) {
            e.printStackTrace();

            m.addAttribute("Title", "Update Movie");
            m.addAttribute("movieticket", oldMovieTicketDetails);
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));
        }


        m.addAttribute("Title", "Update Movie");
        m.addAttribute("movieticket", movieticket);
        return "adminuser/update_movie_form";
    }


    //Show and update Payment Transaction

    @GetMapping("/show-payment-transaction/{page}")
    public String showPaymentTransaction(@PathVariable("page") Integer page, Model m) {

        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        int status = 0;

        Pageable pageable = PageRequest.of(page, 5);
        Page<Purchase> purchaseList = purchaseRepository.getPurchaseByPaymentStatusAndMovieDate(status, date, pageable);

        m.addAttribute("title", "Payment Transaction");
        m.addAttribute("purchaseList", purchaseList);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", purchaseList.getTotalPages());

        return "adminuser/show_payment_transaction";
    }


    // process update payment status of the user

    @GetMapping("/update-payment-status/{id}")
    public String updatePaymentStatus(
            @PathVariable("id") Integer id,
            @RequestParam(value = "actionValue") Integer actionValue,
            Model m, HttpSession session) {

        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        Purchase purchase = purchaseOptional.get();

        purchase.setPaymentStatus(actionValue);
        this.purchaseRepository.save(purchase);

        return "redirect:/admin/show-payment-transaction/0";
    }


    //Show All Payment Transaction

    @GetMapping("/show-all-payment-transaction/{page}")
    public String showAllPaymentTransaction(@PathVariable("page") Integer page, Model m) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Purchase> purchaseList = purchaseRepository.findAllOrderByMovieticketDateAsc(pageable);

        m.addAttribute("title", "Payment Transaction");
        m.addAttribute("purchaseList", purchaseList);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", purchaseList.getTotalPages());

        return "adminuser/show_all_payment_transaction";
    }


    // Delete Movie

    @GetMapping("/delete-movie/{id}")
    public String deleteMovie(
            @PathVariable("id") Integer id,
            Model m, HttpSession session) {

        Optional<Movieticket> movieTiketOptional = movieticketRepository.findById(id);
        Movieticket movieTicket = movieTiketOptional.get();

        java.util.List<Purchase> purchaseList = purchaseRepository.findByMovieticketMovieId(movieTicket.getMovieId());

        for (int i = 0; i < purchaseList.size(); i++) {
            int purchaseId = purchaseList.get(i).getPurchaseId();
            int userId = purchaseList.get(i).getUser().getUserId();
            Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
            Purchase purchase = purchaseOptional.get();
            if (purchase.getPaymentStatus() == 1) {
                User user = userRepository.getById(userId);
                user.setRefundAmount(purchase.getQuantity() * purchase.getMovieticket().getTicketPrize() + user.getRefundAmount());
                userRepository.save(user);
            }
            purchaseRepository.deleteById(purchase.getPurchaseId());
        }

        session.setAttribute("message", new Message("Movie has been Deleted Successfully.", "success"));
        movieticketRepository.deleteById(movieTicket.getMovieId());
        return "redirect:/admin/show-upcoming-movie/0";
    }
}


