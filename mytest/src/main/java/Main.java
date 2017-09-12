import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/18.
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.addUserToRole();
//        main.getGroup(3);
    }

    public void addUserToRole() {
        Session session = HibernateSessionManager1.currentSession();
        session.beginTransaction();
//        Query query = session.createQuery("from User u where u.id=101");
//        List<User> list = query.list();
        SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM Users WHERE userid=101");
        List<User> list =sqlQuery.list();
        User user = list.get(0);
        user.getPassword();
//        User xhg = (User) session.get(User.class,new Integer(101));
//        Set roles = xhg.getRoles();
//        Role sys = (Role) session.get("cn.qeli.ums.entity.Role",
//                "402881e4393a6f3a01393a6f3c1a0000");
//        xhg.getRoles().add(sys);
//        session.getTransaction().commit();
    }

    public void getGroup(int id) {
        Session session = HibernateSessionManager1.currentSession();
        System.out.println("=====Query test=====");

        ShadeGroup group = (ShadeGroup) session.get(ShadeGroup.class, new Integer(id));
        Hibernate.initialize(group);
        if (group != null) {
            Set<Shade> shades = group.getShades();
            Iterator iterator = shades.iterator();
//            while (iterator.hasNext()){
//                Object next = iterator.next();
//                Class<?> aClass = next.getClass();
//            }
//            System.out.println("");
        }
        HibernateSessionManager1.closeSession();
    }
}
