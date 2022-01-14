package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class MyController implements Initializable {
	@FXML
	private ProgressBar pb;
	@FXML
	private Button btn_start;
	@FXML
	private Button btn_stop;
	@FXML
	private Label lb_start;
	@FXML
	private Label lb_stop;
	
	private TimeService timeservice;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btn_start.setOnAction(e->handleBtnStart(e));
		btn_stop.setOnAction(e->handleBtnStop(e));
		timeservice = new TimeService();
		timeservice.start();
	}

	private void handleBtnStart(ActionEvent e) {
		timeservice.restart();
		lb_stop.setText("");
	}

	private void handleBtnStop(ActionEvent e) {
		timeservice.cancel();
	}
	
	class TimeService extends Service<Integer>{

		@Override
		protected Task<Integer> createTask() {
			Task<Integer> task = new Task<>() {

				@Override
				protected Integer call() throws Exception {
					int result = 0;
					for(int i=0;i<=100;i++) {
						if(isCancelled()) {
							break;
						}
						result+=i;
						updateProgress(i, 100);
						updateMessage(String.valueOf(i));
						try {
							Thread.sleep(100);
						}
						catch(InterruptedException e) {
							if(isCancelled()) {
								break;
							}
						}
					}
					return result;
				}
				
			};
			pb.progressProperty().bind(task.progressProperty());
			lb_start.textProperty().bind(task.messageProperty());
			return task;
		}
		
		@Override
		protected void succeeded() {
			System.out.println(timeservice.getValue());
			lb_stop.setText(String.valueOf(getValue()));
		}
		
		@Override
		protected void cancelled() {
			lb_stop.setText("Ãë¼ÒµÊ");
		}
		
		@Override
		protected void failed() {
			lb_stop.setText("½ÇÆÐ");
		}
	}
}
