package mapper;

import entity.State;

import java.util.List;

public interface GeocoderMapper {

    List<State> queryAll();

    State selectById(int stateId);

    void insert(State state);

    void update(State state);

    void delete(int stateId);
}
