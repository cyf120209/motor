package mapper;

import entity.Shade;

import java.util.List;

/**
 * Created by lenovo on 2017/6/18.
 */
public interface ShadeMapper {

    List<Shade> queryAll();

    Shade selectByShadeId(int id);

    void insert(Shade shade);

    void update(Shade shade);

    void delete(int shadeId);
}
