import org.apache.commons.io.input.TailerListenerAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class TrailerListener extends TailerListenerAdapter{
	
	private Label kbs;
	private Label lastUpd;
	private float thrAcc;
	private float thrMedia;
	private int nmisurazioni;
	private Label kbsTot;
	private Label kbsMed;
	
	public TrailerListener(Label kbs, Label lastUpd, Label kbsTot, Label kbsMed){
		this.kbs = kbs;
		this.lastUpd = lastUpd;
		this.kbsTot = kbsTot;
		this.kbsMed = kbsMed;
		thrAcc = 0;
		thrMedia = 0;
		nmisurazioni = 0;
	}
	
    public void handle(String line) {
    	nmisurazioni++;
        String[] lineArr = line.split("\t");
        

        int i = lineArr[2].length();
        String thr = lineArr[2];
        
        char[] thrch = thr.toCharArray();
        for (int j = 0; j < i; j++){
        	if (thrch[j] == ','){
        		thrch[j] = '.';
        	}
        }
        thr = String.valueOf(thrch);
//        System.out.println("after " + thr);
        
        try{
            thrAcc += Float.parseFloat(thr);
            thrMedia = thrAcc / (float)nmisurazioni;
            
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        
        try{
        	Display.getDefault().asyncExec(new Runnable() {
        		public void run() {
                	MainInterface.modifyLabel(kbs,lineArr[2] + " " + lineArr[3]);
                	MainInterface.modifyLabel(lastUpd, lineArr[0]);
                	MainInterface.modifyLabel(kbsTot, Float.toString(thrAcc*5) + " " + "Kb");
                	MainInterface.modifyLabel(kbsMed, Float.toString(thrMedia) + " " + lineArr[3]);
        		}
        		
        	});
        }
        catch (Exception e){
        	e.printStackTrace();
        }
    }
}
