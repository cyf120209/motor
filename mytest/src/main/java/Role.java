
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/18.
 */
//@Table
public class Role {

    private Integer id;
    private Integer roleid;
    private String rolename;
    private Integer ordernum;
    private String description;

//    @ManyToMany(mappedBy = "roles",targetEntity = User.class,fetch = FetchType.LAZY)
    private Set users = new HashSet();

    public Role() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set getUsers() {
        return users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }
}
