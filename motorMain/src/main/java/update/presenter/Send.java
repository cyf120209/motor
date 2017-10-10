package update.presenter;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import listener.UpdateListener;
import update.view.UpdateView;
import util.MyLocalDevice;

/**
 * Created by lenovo on 2017/9/28.
 */
public class Send {

    UpdateListener listener;
    UpdatePresenter updatePresenter;
    UpdateView updateView;
    boolean isReceived = false;

    public Send(UpdatePresenter updatePresenter,UpdateListener listener,UpdateView updateView) {
        this.updatePresenter=updatePresenter;
        this.updateView=updateView;
        this.listener = listener;
    }

    public void send(final UnconfirmedRequestService serviceRequest) {
        listener.setListener(new UpdateListener.ReceivedListener() {
            @Override
            public void received() {
                isReceived = true;
//                updateView.showUpgradeInformation("-----+  origin isReceived:"+isReceived);
//                System.out.println("*********************************************************************true");
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    if (!isReceived) {
                        try {
                            MyLocalDevice.getInstance().sendGlobalBroadcast(serviceRequest);
//                            updateView.showUpgradeInformation("-----send WhoIsRequest"+isReceived);
//                            System.out.println("*********************************************************************WhoIsRequest");
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BACnetException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
//                updateView.showUpgradeInformation("-----end send whois--"+isReceived);
//                System.out.println("+++++++++++++++++++++++++++++++++++++++"+isReceived);
                if(!isReceived){
                    updatePresenter.cancelUpgrade();
                }
            }
        };
        new Thread(runnable).start();

    }
}
