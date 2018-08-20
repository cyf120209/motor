package mapper;

import entity.ShadeGroup;
import entity.ShadeGroupRelation;

import java.util.List;

/**
 * Created by lenovo on 2017/6/22.
 */
public interface GroupMapper {

    List<ShadeGroup> queryAll();

    ShadeGroup selectByGroupId(int id);

    ShadeGroup selectByGroupOther(ShadeGroup shadeGroup);

    int insert(ShadeGroup shadeGroup);

    void insertRelation(List<ShadeGroupRelation> shadeGroupRelationList);

    void update(ShadeGroup shadeGroup);

    void delete(int id);

    void deleteByShadeGroup(ShadeGroup shadeGroup);

    void deleteRelation(List<ShadeGroupRelation> shadeGroupRelationList);

    void deleteRelationById(int id);
}
