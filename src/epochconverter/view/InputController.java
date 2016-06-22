package epochconverter.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import epochconverter.ConverterGui;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class InputController {

	@FXML
    private Label timeInMs;
    @FXML
    private Label timeInS;
    @FXML
    private Label timeInEpochDays;
    @FXML
    private Label timeInIsoStd;

    @FXML
    private DatePicker mDatePicker;

    @FXML
    private TextField mTextField;

    @FXML
    private ToggleGroup mTimeZoneToggleGroup;

    @FXML
    private RadioButton mLocalTimeZone;

    @FXML
    private RadioButton mUtcTimeZone;

    // Reference to the main application.
    private ConverterGui mEpochGui;
    private LocalDateTime mCachedLTD;


    public InputController() {

    }

    @FXML
    private void initialize() {
    	clearData();
    	showDateData(LocalDateTime.now());
    }

    public void setConverterGui(ConverterGui pGui) {
    	this.mEpochGui = pGui;
    }

    private void showDateData(LocalDateTime pLocalDateTime) {

    	if (null != pLocalDateTime) {

    		ZonedDateTime tZonedTime = ZonedDateTime.of(pLocalDateTime, ZoneId.systemDefault());
    		ZonedDateTime tUTCTime;
    		if (mUtcTimeZone.isSelected()) {
    			tUTCTime = pLocalDateTime.atZone(ZoneOffset.UTC);
    		} else {
    			tUTCTime = tZonedTime.withZoneSameInstant(ZoneOffset.UTC);
    		}
    		timeInEpochDays.setText(String.valueOf(tUTCTime.getLong(ChronoField.EPOCH_DAY)));
    		timeInMs.setText(String.valueOf(tUTCTime.toInstant().toEpochMilli()));
    		timeInS.setText(String.valueOf(tUTCTime.toInstant().toEpochMilli()/1000));
    		timeInIsoStd.setText(tUTCTime.format(DateTimeFormatter.ISO_DATE_TIME));
    		mCachedLTD = pLocalDateTime;
    	} else {
    		clearData();
    	}
    }

    @FXML
    private void onDatePicked() {
    	LocalDate tLD = mDatePicker.getValue();
    	showDateData(tLD.atStartOfDay());
    }

    @FXML
    private void onChangedRadioButton() {
    	//Recalc time
    	showDateData(mCachedLTD);
    }

    @FXML
    private void onTimeStampSupplied() {
    	mDatePicker.setValue(LocalDate.now());
    	String tSuppliedStamp = mTextField.getText().trim();
    	if (null == tSuppliedStamp || tSuppliedStamp.isEmpty()) {
    		setInvalidText();
    		return;
    	}
    	try {
			if (tSuppliedStamp.toLowerCase().endsWith("ms")) {
				long tStamp = Long.parseLong(tSuppliedStamp.substring(0, tSuppliedStamp.length() - 2));
				long tSec = tStamp / 1000;
				int tNano = (int) ((tStamp % 1000) * 1_000_000);
				LocalDateTime tLDT = LocalDateTime.ofEpochSecond(tSec, tNano, ZoneOffset.UTC);
				showDateData(tLDT);
				return;
			} else if (tSuppliedStamp.toLowerCase().endsWith("ns")) {
				long tStamp = Long.parseLong(tSuppliedStamp.substring(0, tSuppliedStamp.length() - 2));
				long tSec = tStamp / 1_000_000_000;
				int tNano = (int) ((tStamp % 1_000_000_000));
				LocalDateTime tLDT = LocalDateTime.ofEpochSecond(tSec, tNano, ZoneOffset.UTC);
				showDateData(tLDT);
				return;
			} else if (tSuppliedStamp.toLowerCase().endsWith("mics")) {
				long tStamp = Long.parseLong(tSuppliedStamp.substring(0, tSuppliedStamp.length() - 4));
				long tSec = tStamp / 1_000_000;
				int tNano = (int) ((tStamp % 1_000_000) * 1000);
				LocalDateTime tLDT = LocalDateTime.ofEpochSecond(tSec, tNano, ZoneOffset.UTC);
				showDateData(tLDT);
				return;
			} else if (tSuppliedStamp.toLowerCase().endsWith("s")) {
				long tStamp = Long.parseLong(tSuppliedStamp.substring(0, tSuppliedStamp.length() - 4));
				long tSec = tStamp;
				int tNano = 0;
				LocalDateTime tLDT = LocalDateTime.ofEpochSecond(tSec, tNano, ZoneOffset.UTC);
				showDateData(tLDT);
				return;
			} else {
				long tStamp = Long.parseLong(tSuppliedStamp);
				LocalDateTime tLDT = LocalDateTime.ofEpochSecond(tStamp, 0, ZoneOffset.UTC);
				showDateData(tLDT);
				return;
			}
    	} catch (NumberFormatException e) {
    		//By design
    	}
    	setInvalidText();
    }

    private void setInvalidText() {
    	mTextField.getParent().requestFocus();
    	mTextField.clear();
    	mTextField.setPromptText("Supplied value was incompatible.");
    }
    private void clearData() {
    	timeInMs.setText("");
    	timeInS.setText("");
    	timeInIsoStd.setText("");
    	timeInEpochDays.setText("");
    }
}
