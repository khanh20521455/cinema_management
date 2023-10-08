package cinema_management.service;

import cinema_management.entities.Theater;
import cinema_management.helper.Message;
import cinema_management.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;
    public String theaterManagement(Model model, Integer page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Theater> theaterList = theaterRepository.findAllOrderByNameDesc(pageable);
        model.addAttribute("theaterList",theaterList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", theaterList.getTotalPages());
        return "admiuser/theater/theater_managemnet";
    }
    public String addTheater(Model model) {
        model.addAttribute("theater", new Theater());
        return "adminuser/theater/add_theater";
    }
    public String addTheaterProcess(Theater theater, HttpSession httpSession){
        try{
            theaterRepository.save(theater);
            httpSession.setAttribute("message", new Message("Thêm rạp mới thành công!", "success"));
        }
        catch (Exception e){
            httpSession.setAttribute("message", new Message("Đã có lỗi xảy ra, vui lòng thử lại", "danger"));
        }
        return "redirect:/admin/theater_management/0";
    }
    public String updateTheater(Integer id, Model model){
        Theater theater = theaterRepository.getById(id);
        model.addAttribute("theater", theater );
        return "adminuser/theater/update_theater";
    }
    public String updateTheaterProcess(Integer id, Theater theater, HttpSession httpSession){
        Theater oldTheater = theaterRepository.getById(id);
        try{
            oldTheater.setName(theater.getName());
            oldTheater.setProvince(theater.getProvince());
            oldTheater.setStatus(theater.getStatus());
            theaterRepository.save(oldTheater);
            httpSession.setAttribute("message", new Message("Cập nhật rạp thành công!", "success"));
        }catch (Exception e){
            httpSession.setAttribute("message", new Message("Đã có lỗi xảy ra, vui lòng thử lại", "danger"));
        }
        return "redirect:/admin/theater_management/0";
    }
    public String deleteTheater(Integer id){
        return "redirect:/admin/theater_management/0";
    }

}
