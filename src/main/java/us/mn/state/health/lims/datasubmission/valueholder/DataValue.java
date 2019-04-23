package us.mn.state.health.lims.datasubmission.valueholder;

import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.SafeHtml;

import spring.mine.datasubmission.form.DataSubmissionForm;
import us.mn.state.health.lims.common.valueholder.BaseObject;

public class DataValue extends BaseObject {
	private static final long serialVersionUID = -2843847589920119564L;

	private String id;

	@Pattern(regexp = "^[a-z0-9_]*$", groups = { Default.class, DataSubmissionForm.DataSubmission.class })
	private String columnName;

	@SafeHtml(groups = { Default.class, DataSubmissionForm.DataSubmission.class })
	private String value;
	private String displayKey;
	private boolean visible;

	public DataValue() {
		columnName = "";
		value = "";
		visible = true;
	}

	public DataValue(String columnName, String displayKey) {
		this.columnName = columnName;
		value = "";
		this.displayKey = displayKey;
		visible = true;
	}

	public DataValue(boolean visible) {
		columnName = "";
		value = "";
		this.visible = visible;
	}

	public DataValue(String columnName, String value, boolean visible) {
		this.columnName = columnName;
		this.value = value;
		this.visible = visible;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayKey() {
		return displayKey;
	}

	public void setDisplayKey(String displayKey) {
		this.displayKey = displayKey;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
