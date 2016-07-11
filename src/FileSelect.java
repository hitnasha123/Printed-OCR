import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class FileSelect extends JFrame{
	public String selectFile(){try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }catch(Exception ex) {
        ex.printStackTrace();
    }
		
		JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    	int result = fileChooser.showOpenDialog(this);
    	File selectedFile=null;
    	if (result == JFileChooser.APPROVE_OPTION) {
    	    // user selects a file
    		 selectedFile = fileChooser.getSelectedFile();
    		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
    	}
    	
    	return selectedFile.getAbsolutePath();
	}
}