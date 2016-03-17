package input_output;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TextFileFilter extends FileFilter {

    public final String TXT = "txt";

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        return extension != null && extension.equals(TXT);
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
