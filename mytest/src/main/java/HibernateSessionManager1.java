
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by lenovo on 2017/6/16.
 */
public class HibernateSessionManager1 {

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hiber.cfg.xml").buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final ThreadLocal tl = new ThreadLocal();

    public static Session currentSession() {
        Session s = (Session) tl.get();
        if (s == null) {
            s = sessionFactory.openSession();
            tl.set(s);
        }

        return s;
    }

    public static void closeSession() {
        Session s = (Session) tl.get();
        tl.set(null);
        if (s != null) {
            s.close();
        }
    }
}
