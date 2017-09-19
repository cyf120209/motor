package limitsAndStops.presenter;

/**
 * Created by lenovo on 2017/9/18.
 */
public class CfgResult {

    private int success=0;

    private int fail=0;

    public int getSuccess() {
        return success;
    }

    public void success() {
        success++;
    }

    public int getFail() {
        return fail;
    }

    public void fail() {
        fail++;
    }

    public void clear(){
        success=0;
        fail=0;
    }


    @Override
    public String toString() {
        return "成功 "+success+"次，失败 "+fail+"次\r\n";
    }
}
