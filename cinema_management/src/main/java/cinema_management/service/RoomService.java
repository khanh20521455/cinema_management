package cinema_management.service;

import cinema_management.entities.Room;
import cinema_management.helper.Message;
import cinema_management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public String getRoomManagement(Integer page, Model model) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Room> roomList = roomRepository.findAllOrderByRoomNameDateAsc(pageable);
        model.addAttribute("title", "Get room list");
        model.addAttribute("roomList", roomList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roomList.getTotalPages());
        return "adminuser/room/room_management";
    }

    public String addRoom(Model model) {
        model.addAttribute("title", "Add Room");
        model.addAttribute("room", new Room());
        return "adminuser/room/add_room";
    }

    public String addRoomProcess(Room room, HttpSession session) {
        try {
            roomRepository.save(room);
            session.setAttribute("message", new Message("New room has successfully added", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong, try again ! ", "danger"));
        }
        return "adminuser/room/add_room";

    }

    public String updateRoom(Integer id, Model m) {

        Optional<Room> roomOptional = this.roomRepository.findById(id);
        Room room = roomOptional.get();
        m.addAttribute("Title", "Update Room");
        m.addAttribute("room", room);
        return "adminuser/room/update_room";
    }

    public String movieUpdateProcess(
            @PathVariable("id") Integer id,
            @ModelAttribute Room room,
            Model m, HttpSession session) {


        Optional<Room> roomOptional = roomRepository.findById(id);
        Room oldRoomDetail = roomOptional.get();

        try {
            room.setId(oldRoomDetail.getId());
            room.setName(room.getName());
            room.setTypeScreen(room.getTypeScreen());
            room.setStatus(room.getStatus());
            this.roomRepository.save(room);

            session.setAttribute("message", new Message("Room Updated Successfully.", "success"));
        } catch (Exception e) {
            e.printStackTrace();

            m.addAttribute("Title", "Update Room");
            m.addAttribute("room", oldRoomDetail);
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));
        }

        m.addAttribute("Title", "Update Room");
        m.addAttribute("room", room);
        return "adminuser/room/update_room";
    }
    public String deleteRoom(Integer id, Model m, HttpSession session){
        roomRepository.deleteById(id);
        return "redirect:/admin/room_management/0";
    }
}
