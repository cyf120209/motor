package groupOperation.presenter;

import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * Created by lenovo on 2017/1/19.
 */
public interface GroupOperationPresenter {

    void AnnounceSubbutton();
    void addSelGroup();
    void addAllGroup();
    void delSelGroup();
    void delAllGroup();
    void paraseServiceParameter(UnsignedInteger serviceNumber, Sequence serviceParameters);

    String[] getGroups(int index);

    String[] getDrapers(int index);

    void upBt();
    void stopButton();
    void downbt();

    void updateGroups();

    void updateDrapers();

    /**
     * 注销监听
     */
    void cancelListener();

}
