package mapper;

import entity.Shade;
import entity.ShadeGroup;

import java.util.List;

/**
 * Created by lenovo on 2017/6/22.
 */
public interface GroupMapper {

    List<ShadeGroup> queryAll() throws Exception;

    ShadeGroup selectByGroupId(int id);

    void insert(ShadeGroup shadeGroup);

    void update(ShadeGroup shadeGroup);

    void delete(int id);
}
