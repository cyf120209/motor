package entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/18.
 */
public class User {
    private Integer id;
    public Integer userid;
    private String username;
    private String password;

//    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
//    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set roles = new HashSet();

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }
}
