package epochconverter.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import epochconverter.Calculators;
import epochconverter.ConverterGui;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;

public class InputController {

	private static final String TooltipText = "Supply time stamp in any of the following format:"
			+ "\n\tdefault (no suffix) [s]"
			+ "\n\t <stamp>ns [ns]"
			+ "\n\t <stamp>mics [micro seconds]"
			+ "\n\t <stamp>s [s]"
			+ "\n\t <stamp>ms [milli seconds]";
	private static final String ms = "ms";
	private static final String ns = "ns";
	private static final String mics = "mics";
	private static final String s = "s";

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
    private ToggleGroup mInputTimeZoneToggleGroup;
    @FXML
    private RadioButton mInputLocalTimeZone;
    @FXML
    private RadioButton mInputUtcTimeZone;

    @FXML
    private ToggleGroup mDisplayTimeZoneToggleGroup;
    @FXML
    private RadioButton mDisplayLocalTimeZone;
    @FXML
    private RadioButton mDisplayUtcTimeZone;

    // Reference to the main application.
    private ConverterGui mEpochGui;
    private LocalDateTime mCachedLTD;


    public InputController() {

    }

    @FXML
    private void initialize() {
    	clearData();
		ZonedDateTime tZonedTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
    	displayTimeFieldsInGui(tZonedTime.withZoneSameInstant(ZoneOffset.UTC));
    	mCachedLTD = tZonedTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    	mTextField.setTooltip(new Tooltip(TooltipText));
    }

    public void setConverterGui(ConverterGui pGui) {
    	this.mEpochGui = pGui;
    }

    private void convertAndShowDateData(LocalDateTime pLocalDateTime) {

    	if (null != pLocalDateTime) {
    		LocalDateTime tConvertedTime;
    		ZoneId tFromZone = mInputUtcTimeZone.isSelected() ? ZoneOffset.UTC : ZoneId.systemDefault();
    		ZoneId tToZone = mDisplayUtcTimeZone.isSelected() ? ZoneOffset.UTC : ZoneId.systemDefault();

    		tConvertedTime = Calculators.convertToTimeZone(pLocalDateTime, tFromZone, tToZone);
    		ZonedDateTime tDisplayTime = tConvertedTime.atZone(tToZone);
    		displayTimeFieldsInGui(tDisplayTime);
    		mCachedLTD = pLocalDateTime;
    		mDatePicker.setValue(pLocalDateTime.toLocalDate());
    	} else {
    		clearData();
    	}
    }

    private void displayTimeFieldsInGui(ZonedDateTime pUTCTime) {
    	timeInEpochDays.setText(String.valueOf(pUTCTime.getLong(ChronoField.EPOCH_DAY)));
		timeInMs.setText(String.valueOf(pUTCTime.toInstant().toEpochMilli()));
		timeInS.setText(String.valueOf(pUTCTime.toInstant().toEpochMilli()/1000));
		timeInIsoStd.setText(pUTCTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @FXML
    private void onDatePicked() {
    	LocalDate tLD = mDatePicker.getValue();
    	convertAndShowDateData(tLD.atStartOfDay());
    }

    @FXML
    private void onChangedRadioButton() {
    	//Recalc time
    	convertAndShowDateData(mCachedLTD);
    }

    @FXML
    private void onTimeStampSupplied() {
    	String tSuppliedStamp = mTextField.getText().trim();
    	if (null == tSuppliedStamp || tSuppliedStamp.isEmpty()) {
    		setInvalidText();
    		return;
    	}
    	try {
			if (tSuppliedStamp.toLowerCase().endsWith(ms)) {
				LocalDateTime tLDT = getLDT(tSuppliedStamp, ms);
				convertAndShowDateData(tLDT);
				return;
			} else if (tSuppliedStamp.toLowerCase().endsWith(ns)) {
				LocalDateTime tLDT = getLDT(tSuppliedStamp, ns);
				convertAndShowDateData(tLDT);
				return;
			} else if (tSuppliedStamp.toLowerCase().endsWith(mics)) {
				LocalDateTime tLDT = getLDT(tSuppliedStamp, mics);
				convertAndShowDateData(tLDT);
				return;
			} else if (tSuppliedStamp.toLowerCase().endsWith(s)) {
				LocalDateTime tLDT = getLDT(tSuppliedStamp, s);
				convertAndShowDateData(tLDT);
				return;
			} else {
				LocalDateTime tLDT = getLDT(tSuppliedStamp, null);
				convertAndShowDateData(tLDT);
				return;
			}
    	} catch (NumberFormatException e) {
    		//By design
    	}
    	setInvalidText();
    }

    private LocalDateTime getLDT(String pTimeStamp, String pSuffix) {
    	if (null == pSuffix) {
    		pSuffix = "";
    	}
    	return Calculators.getLocalDateTimeFromStamp(pTimeStamp.substring(0, pTimeStamp.length() - pSuffix.length()),
    			pSuffix);
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
