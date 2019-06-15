package at.ac.htlperg.squarebeard.logging;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

public abstract class LoggingWindow {
	
	public abstract java.util.logging.Handler newHandler();
	public abstract void show();
	public abstract void hide();
	public abstract void close();
	
	public static LoggingWindow swingInstance() {
		return new SwingLoggingWindow();
	}
	
	public static LoggingWindow fxInstance() {
		return new FXLoggingWindow();
	}
	
	private static class SwingLoggingWindow extends LoggingWindow {

		private JTable table;
		private JFrame frame;

		public SwingLoggingWindow() {
			super();
			frame = new JFrame("Logs");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			this.table = new JTable(
					new DefaultTableModel(new Object[] { "Time", "Source", "Thread", "Level", "Message" }, 0));
			frame.getContentPane().add(new JScrollPane(table));
		}

		@Override
		public Handler newHandler() {
			return new Handler();
		}
		
		@Override
		public void close() {
			frame.dispose();
		}

		private class Handler extends java.util.logging.Handler {

			@Override
			public void publish(LogRecord record) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(
						new Object[] { Instant.ofEpochMilli(record.getMillis()).toString(), record.getSourceClassName(),
								Thread.currentThread().getName(), record.getLevel(), record.getMessage() });
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() {
			}

		}

		@Override
		public void show() {
			frame.setVisible(true);
		}

		@Override
		public void hide() {
			frame.setVisible(false);
		}
		
	}
	
	private static class FXLoggingWindow extends LoggingWindow {
		
		private Stage stage;
		private TableView<LogRecord> table = new TableView<>();
		
		@SuppressWarnings("unchecked")
		public FXLoggingWindow() {
			this.stage = new Stage();
			stage.setScene(new Scene(table));
			
			TableColumn<LogRecord, Instant> timeColumn = new TableColumn<>("Time");
			timeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LogRecord,Instant>, ObservableValue<Instant>>() {

				@Override
				public ObservableValue<Instant> call(CellDataFeatures<LogRecord, Instant> param) {
					return new SimpleObjectProperty<Instant>(Instant.ofEpochMilli(param.getValue().getMillis()));
				}
				
			});
			
			TableColumn<LogRecord, String> sourceColumn = new TableColumn<>("Source");
			sourceColumn.setCellValueFactory(param -> {
				return new SimpleStringProperty(param.getValue().getSourceClassName());
			});
			
			TableColumn<LogRecord, Number> threadColumn = new TableColumn<>("Thread");
			threadColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getThreadID()));
			
			TableColumn<LogRecord, Level> levelColumn = new TableColumn<>("Level");
			levelColumn.setCellValueFactory(param -> new SimpleObjectProperty<Level>(param.getValue().getLevel()));
			
			TableColumn<LogRecord, String> msgColumn = new TableColumn<>("Message");
			msgColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMessage()));
			
			
			table.getColumns().addAll(timeColumn, sourceColumn, threadColumn, levelColumn, msgColumn);
		}

		@Override
		public java.util.logging.Handler newHandler() {
			return new Handler();
		}

		@Override
		public void show() {
			stage.show();
		}

		@Override
		public void hide() {
			stage.hide();
		}
		
		@Override
		public void close() {
			stage.close();
		}
		
		private class Handler extends java.util.logging.Handler {

			@Override
			public void publish(LogRecord record) {
				table.getItems().add(record);
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() {
			}
			
		}
		
	}

}
