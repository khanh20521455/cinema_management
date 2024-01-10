package cinema_management.service;

import cinema_management.entities.Movie;
import cinema_management.entities.Snacks;
import cinema_management.helper.Message;
import cinema_management.repository.SnacksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class SnacksService {
    @Autowired
    private SnacksRepository snacksRepository;
    public  String getAll(Integer page, Model model){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Snacks> listSnacks = snacksRepository.getAll(pageable);
        model.addAttribute("listSnacks", listSnacks);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listSnacks.getTotalPages());
        return "adminuser/snacks/snacks_management";
    }
    public String addSnacks(Model model){
        model.addAttribute("snacks", new Snacks());
        return "adminuser/snacks/add_snacks";
    }
    public String addSnackProcess(Snacks snacks, MultipartFile file, HttpSession session){
        try {
            if(file ==null || file.isEmpty()) {
                snacks.setImage("Movie Poster Default Photo.png");
            }
            else {
                snacks.setImage(file.getOriginalFilename());
                File saveFile =  new ClassPathResource("static/img").getFile();
                Path path =   Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
            }
            snacksRepository.save(snacks);
            session.setAttribute("message", new Message("Thêm mới thành công", "success"));

        }
        catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Đã xảy ra lỗi, vui lòng thử lại ! ", "danger"));
        }
        return "redirect:/admin/snacks_management/0";
    }
    public String updateSnacks(Integer id, Model model){
        Snacks snacks = snacksRepository.getById(id);
        model.addAttribute("snacks", snacks);
        return "adminuser/snacks/update_snacks";
    }
    public String updateSnackProcess(Integer id, Snacks snacks, MultipartFile file, HttpSession session){
       Snacks oldSnacks = snacksRepository.getById(id);
        try {
            String price = String.valueOf(snacks.getPrice());
            if (!file.isEmpty()) {
                //delete
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldSnacks.getImage());
                file1.delete();
                //update
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                snacks.setImage(file.getOriginalFilename());

            } else {
               snacks.setImage(oldSnacks.getImage());
            }
            snacks.setId(oldSnacks.getId());
            this.snacksRepository.save(snacks);
            session.setAttribute("message", new Message("Cập nhật thành công", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Đã xảy ra lỗi " + e.getMessage(), "danger"));
        }
        return "redirect:/admin/snacks_management/0";
    }
    public String deleteSnacks(Integer id, Model model){

        return "redirect:/admin/snacks_management/0";
    }
}
