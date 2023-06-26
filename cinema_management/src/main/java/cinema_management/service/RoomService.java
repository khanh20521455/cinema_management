package cinema_management.service;

import cinema_management.entities.Room;
import cinema_management.entities.Showtimes;
import cinema_management.helper.Message;
import cinema_management.repository.BookingRepository;
import cinema_management.repository.RoomRepository;
import cinema_management.repository.SeatRepository;
import cinema_management.repository.ShowtimesRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ShowtimesRepository showtimesRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private BookingRepository bookingRepository;
    public String getRoomManagement(Integer page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Room> roomList = roomRepository.findAllOrderByRoomNameAsc(pageable);
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
            session.setAttribute("message", new Message("Thêm phòng mới thành công!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Đã xảy ra lỗi, vui lòng thử lại ! ", "danger"));
        }
        return "redirect:/admin/room_management/0";

    }

    public String updateRoom(Integer id, Model m) {

        Optional<Room> roomOptional = this.roomRepository.findById(id);
        Room room = roomOptional.get();
        m.addAttribute("Title", "Update Room");
        m.addAttribute("room", room);
        return "adminuser/room/update_room";
    }

    public String roomUpdateProcess(
            Integer id,
            Room room,
            Model m, HttpSession session) {


        Optional<Room> roomOptional = roomRepository.findById(id);
        Room oldRoomDetail = roomOptional.get();

        try {
            room.setId(oldRoomDetail.getId());
            room.setName(room.getName());
            room.setTypeScreen(room.getTypeScreen());
            room.setStatus(room.getStatus());
            this.roomRepository.save(room);

            session.setAttribute("message", new Message("Cập nhập phòng thành công!", "success"));
        } catch (Exception e) {
            e.printStackTrace();

            m.addAttribute("Title", "Update Room");
            m.addAttribute("room", oldRoomDetail);
            session.setAttribute("message", new Message("Đã xảy ra lỗi " + e.getMessage(), "danger"));
        }

        m.addAttribute("Title", "Update Room");
        m.addAttribute("room", room);
        return "redirect:/admin/room_management/0";
    }
    public String deleteRoom(Integer id){
        List<Showtimes> showtimesList= this.showtimesRepository.findByRoomId(id);
        for(Showtimes showtimes: showtimesList){
            this.seatRepository.deleteSeatBaseShowtimes(showtimes.getId());
            this.bookingRepository.deleteBookingBaseShowtimes(showtimes.getId());
        }
        showtimesRepository.deleteBaseOnRoom(id);
        roomRepository.deleteById(id);
        return "redirect:/admin/room_management/0";
    }
}
