import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Spinner;

public class MainInterface {

	protected Shell shlMonitorgui;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainInterface window = new MainInterface();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlMonitorgui.open();
		shlMonitorgui.layout();
		while (!shlMonitorgui.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	
	private MonitorThread collectorMon = null;
	private TailerListener listenerCollector;
	private Tailer tailerCollector;
	private Thread tailerCollectorThread;
	private Text txtTest;
	
	/**
	 * Create contents of the window.
	 */
	
	public static void modifyLabel(Label a, String s){
		a.setText(s);
	}
	

	
	protected void createContents() {
		shlMonitorgui = new Shell();
		shlMonitorgui.setSize(598, 277);
		shlMonitorgui.setText("MonitorGUI");
				shlMonitorgui.setLayout(null);
				
				Label lblPathToFile = new Label(shlMonitorgui, SWT.BORDER);
				lblPathToFile.setAlignment(SWT.CENTER);
				lblPathToFile.setBounds(20, 52, 294, 22);
				lblPathToFile.setText("/path/to/file/dir");
				
				Button btnChooseFile = new Button(shlMonitorgui, SWT.NONE);
				btnChooseFile.setBounds(320, 40, 115, 22);
				btnChooseFile.setText("Choose File");
				
				Label lblStatus = new Label(shlMonitorgui, SWT.NONE);
				lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				lblStatus.setBounds(456, 97, 93, 21);
				lblStatus.setText("Status: Stopped");
				
				Label lblNomeMonitoraggio = new Label(shlMonitorgui, SWT.NONE);
				lblNomeMonitoraggio.setBounds(258, 139, 151, 14);
				lblNomeMonitoraggio.setText("--");
				
				Label lblXxyyzzHhmmss = new Label(shlMonitorgui, SWT.NONE);
				lblXxyyzzHhmmss.setBounds(154, 194, 141, 22);
				lblXxyyzzHhmmss.setText("--");
				
				Label lblYyKb = new Label(shlMonitorgui, SWT.NONE);
				lblYyKb.setBounds(441, 166, 108, 22);
				lblYyKb.setText("-- Kb");
				
				Label lblZzKbs = new Label(shlMonitorgui, SWT.NONE);
				lblZzKbs.setBounds(258, 166, 119, 22);
				lblZzKbs.setText("-- Kb/s");
				
				txtTest = new Text(shlMonitorgui, SWT.BORDER);
				txtTest.setText("test");
				txtTest.setBounds(168, 11, 64, 19);
				
				Spinner spinner = new Spinner(shlMonitorgui, SWT.BORDER);
				spinner.setBounds(383, 9, 52, 22);
				
				Button btnRicordaStatisticheDopo = new Button(shlMonitorgui, SWT.CHECK); // checkbox
				btnRicordaStatisticheDopo.setBounds(27, 89, 205, 28);
				btnRicordaStatisticheDopo.setText("Ricorda statistiche dopo Stop");
				
				Button btnChooseDir = new Button(shlMonitorgui, SWT.NONE);
				btnChooseDir.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						System.out.println("browing dir...");
				        DirectoryDialog dlg = new DirectoryDialog(shlMonitorgui);

				        // Set the initial filter path according
				        // to anything they've selected or typed in
				         final Text text = new Text(shlMonitorgui, SWT.BORDER);
				         dlg.setFilterPath(text.getText());

				        // Change the title bar text
				        dlg.setText("Browsing Directory...");

				        // Customizable message displayed in the dialog
				        // dlg.setMessage("Select a directory");

				        // Calling open() will open and run the dialog.
				        // It will return the selected directory, or
				        // null if user cancels
				        String dir = dlg.open();
				        if (dir != null) {
				          // Set the text box to the new selection
				          text.setText(dir);
				          System.out.println(dir);
				          lblPathToFile.setText(dir);
				        }

					}
				});
				btnChooseDir.setBounds(320, 62, 115, 22);
				btnChooseDir.setText("Choose Dir");
				
				Label lblSeparator = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.HORIZONTAL);
				lblSeparator.setText("separator");
				lblSeparator.setBounds(10, 82, 430, 2);
				
				Label label_1 = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.HORIZONTAL);
				label_1.setText("separator");
				label_1.setBounds(10, 34, 430, 2);
				
				Button btnStart = new Button(shlMonitorgui, SWT.NONE);
				btnStart.setBounds(456, 24, 94, 22);
				btnStart.setText("Start");
				
				Button btnStop = new Button(shlMonitorgui, SWT.NONE);
				btnStop.setEnabled(false);
				btnStop.setBounds(455, 52, 94, 22);
				btnStop.setText("Stop");
				
				Label lblXxKbs = new Label(shlMonitorgui, SWT.NONE);
				lblXxKbs.setBounds(85, 166, 109, 22);
				lblXxKbs.setText("-- Kb/s");
				

				
			    
			      
			      // Thread tailerCollectorThread;
				
				// creando l'obj qua fuori mantengo i parziali media e totale anche se stoppo e poi avvio il monitoraggio
				listenerCollector = new TrailerListener(lblXxKbs, lblXxyyzzHhmmss, lblYyKb, lblZzKbs);

				
				btnStart.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String dir = lblPathToFile.getText();
						File path = new File(dir);
						if(!path.exists()){
							MessageBox dialog = new MessageBox(shlMonitorgui, SWT.ICON_ERROR);
							dialog.setText("Errore");
							dialog.setMessage("Il Path/File: " + dir + " non esiste!");
							dialog.open();
							return;
						}
						String logFile = null;
						if(path.isDirectory()){
							logFile = lblPathToFile.getText() + "/" + "monitor_s236.log";
						}
						if(path.isFile()){
							logFile = lblPathToFile.getText() + "-" + "monitor_s236.log";
						}
						
						if (logFile == null){
							MessageBox dialog = new MessageBox(shlMonitorgui, SWT.ICON_ERROR);
							dialog.setText("Errore");
							dialog.setMessage("Il Path/File: " + dir + " non è una directory o file regolare!");
							dialog.open();
							return;
						}
						if (!btnRicordaStatisticheDopo.getSelection()){
							listenerCollector = new TrailerListener(lblXxKbs, lblXxyyzzHhmmss, lblYyKb, lblZzKbs);
							lblYyKb.setText("-- Kb");
							lblXxyyzzHhmmss.setText("--");
							lblXxKbs.setText("-- Kb/s");
							lblZzKbs.setText("-- Kb/s");
							
						}
						int overheadperc;
						


						try{
							PrintWriter writer = new PrintWriter(logFile);
							writer.print("");
							writer.close();
						}
						catch (Exception e){
							e.printStackTrace();
						}
						
						String monitorname = txtTest.getText();
						System.out.println("monitorname " + monitorname);
						overheadperc = Integer.parseInt(spinner.getText());
						System.out.println("overheadperc " + overheadperc);
						collectorMon = new MonitorThread(dir, overheadperc, logFile, monitorname);
						collectorMon.start();
						lblNomeMonitoraggio.setText(monitorname);
						File test = new File(logFile);
						tailerCollector = new Tailer(test,listenerCollector);
						tailerCollectorThread = new Thread(tailerCollector);
					    tailerCollectorThread.setDaemon(true); // optional
					    tailerCollectorThread.start();
					    lblStatus.setText("Status: Running");
					    lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					    btnStart.setEnabled(false);
					    btnStop.setEnabled(true);
					    txtTest.setEnabled(false);
					    spinner.setEnabled(false);
					    btnChooseFile.setEnabled(false);
					    btnChooseDir.setEnabled(false);
					    btnRicordaStatisticheDopo.setEnabled(false);
					}
				});

				


			      
				btnStop.addSelectionListener(new SelectionAdapter() {
					@SuppressWarnings("deprecation")
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW));
						lblStatus.setText("Status: Stopping..");
						btnStop.setEnabled(false);
						btnStop.setText("Wait..");
						collectorMon.stopMonitor();
						tailerCollectorThread.stop();
						try{
							collectorMon.join();
							tailerCollectorThread.join();
						}
						catch (Exception e){
							e.printStackTrace();
						}
						System.out.println("stopped");
						lblStatus.setText("Status: Stopped");
						lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
						btnStart.setEnabled(true);
						btnStop.setEnabled(false);
						btnStop.setText("Stop");
					    txtTest.setEnabled(true);
					    spinner.setEnabled(true);
					    btnChooseFile.setEnabled(true);
					    btnChooseDir.setEnabled(true);
					    btnRicordaStatisticheDopo.setEnabled(true);
					}
				});

				
				Label lblMonitor = new Label(shlMonitorgui, SWT.NONE);
				lblMonitor.setBounds(200, 139, 59, 14);
				lblMonitor.setText("Monitor:");
				
				Label lblSep = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.HORIZONTAL);
				lblSep.setText("sep2");
				lblSep.setBounds(9, 123, 540, 2);
				
				Label label_3 = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.HORIZONTAL);
				label_3.setBounds(9, 131, 540, 2);
				
				Label lblIstantaneo = new Label(shlMonitorgui, SWT.NONE);
				lblIstantaneo.setBounds(20, 166, 64, 22);
				lblIstantaneo.setText("Istantaneo:");
				
				Label lblTotale = new Label(shlMonitorgui, SWT.NONE);
				lblTotale.setBounds(390, 166, 45, 22);
				lblTotale.setText("Totale:");
				

				
				Label lblUltimoAggiornamento = new Label(shlMonitorgui, SWT.NONE);
				lblUltimoAggiornamento.setBounds(20, 194, 129, 22);
				lblUltimoAggiornamento.setText("Ultimo aggiornamento:");
				
				Label lblNomeMonitoraggio_1 = new Label(shlMonitorgui, SWT.NONE);
				lblNomeMonitoraggio_1.setBounds(47, 14, 115, 17);
				lblNomeMonitoraggio_1.setText("Nome monitoraggio:");
				

				
				Label lblOverhead = new Label(shlMonitorgui, SWT.NONE);
				lblOverhead.setBounds(292, 14, 85, 17);
				lblOverhead.setText("Overhead %:");
				
				Label label_4 = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.VERTICAL);
				label_4.setBounds(10, 5, 2, 117);
				
				Label label_5 = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.HORIZONTAL);
				label_5.setBounds(10, 5, 430, 2);
				
				Label label_6 = new Label(shlMonitorgui, SWT.SEPARATOR | SWT.VERTICAL);
				label_6.setBounds(441, 5, 2, 117);
				
				Label lblMedio = new Label(shlMonitorgui, SWT.NONE);
				lblMedio.setBounds(200, 166, 52, 14);
				lblMedio.setText("Medio:");
				


				
				

				

				

				

				

				btnChooseFile.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						System.out.println("browing file...");
						
			
				        FileDialog dlg = new FileDialog(shlMonitorgui);

				        // Set the initial filter path according
				        // to anything they've selected or typed in
				         final Text text = new Text(shlMonitorgui, SWT.BORDER);
				         dlg.setFilterPath(text.getText());

				        // Change the title bar text
				        dlg.setText("Browsing Files...");

				        // Customizable message displayed in the dialog
				        // dlg.setMessage("Select a directory");

				        // Calling open() will open and run the dialog.
				        // It will return the selected directory, or
				        // null if user cancels
				        String dir = dlg.open();
				        if (dir != null) {
				          // Set the text box to the new selection
				          text.setText(dir);
				          System.out.println(dir);
				          lblPathToFile.setText(dir);
				        }

					}
					
				
				});

	}
}
