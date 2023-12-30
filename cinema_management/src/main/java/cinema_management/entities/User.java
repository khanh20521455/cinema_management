package cinema_management.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    @Column(length = 50)
    private String about;
    private String role;
    private boolean enable;
    private String imgUrl;
    @Column(columnDefinition = "int default 0")
    private Integer point;
    @ManyToMany
    @JsonBackReference
    @JoinColumn(name="favorite_list")
    private List<Movie> favorites;
    public User() {
        super();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public List<Movie> getFavorites() {
        return favorites;
    }

    public void setFavorites(List <Movie> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password=" + password
                + ", phone=" + phone + ", about=" + about
                + ", role=" + role + ", enable=" + enable + ", imgUrl=" + imgUrl  + "]";
    }
}
