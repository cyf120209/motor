package update.presenter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by lenovo on 2017/6/23.
 */
public class BinFileFilter extends FileFilter {

    /**
     * 仅显示目录或bin结尾的文件
     * @param f
     * @return
     */
    @Override
    public boolean accept(File f) {
        String name = f.getName();
        return f.isDirectory() || name.toLowerCase().endsWith(".bin");
//        return name.toLowerCase().endsWith(".bin");
    }

    @Override
    public String getDescription() {
        return "*.bin";
    }

}
