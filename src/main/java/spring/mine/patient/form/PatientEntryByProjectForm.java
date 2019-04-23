package spring.mine.patient.form;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import spring.mine.common.form.BaseForm;
import spring.mine.common.validator.ValidationHelper;
import spring.mine.validation.annotations.ValidAccessionNumber;
import spring.mine.validation.annotations.ValidDate;
import spring.mine.validation.annotations.ValidTime;
import us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory.AccessionFormat;
import us.mn.state.health.lims.common.util.validator.CustomDateValidator.DateRelation;
import us.mn.state.health.lims.dictionary.ObservationHistoryList;
import us.mn.state.health.lims.organization.util.OrganizationTypeList;
import us.mn.state.health.lims.patient.action.IPatientUpdate.PatientUpdateStatus;
import us.mn.state.health.lims.patient.valueholder.ObservationData;
import us.mn.state.health.lims.sample.form.ProjectData;

public class PatientEntryByProjectForm extends BaseForm {
	// for display
	private Map<String, Object> formLists;

	// for display
	private Map<String, ObservationHistoryList> dictionaryLists;

	// for display
	private Map<String, OrganizationTypeList> organizationTypeLists;

	@Pattern(regexp = ValidationHelper.ID_REGEX)
	private String patientPK = "";

	@Pattern(regexp = ValidationHelper.ID_REGEX)
	private String samplePK = "";

	@ValidDate(relative = DateRelation.PAST)
	private String interviewDate = "";

	@ValidTime
	private String interviewTime = "";

	@ValidDate(relative = DateRelation.PAST)
	private String receivedDateForDisplay = "";

	@ValidTime
	private String receivedTimeForDisplay = "";

	@ValidDate(relative = DateRelation.TODAY)
	private String currentDate = "";

	private PatientUpdateStatus patientUpdateStatus = PatientUpdateStatus.ADD;

	@Pattern(regexp = ValidationHelper.PATIENT_ID_REGEX)
	private String subjectNumber = "";

	@Pattern(regexp = ValidationHelper.PATIENT_ID_REGEX)
	private String siteSubjectNumber = "";

	@ValidAccessionNumber(format = AccessionFormat.PROGRAM)
	private String labNo = "";

	@Pattern(regexp = ValidationHelper.ID_REGEX)
	private String centerName = "";

	@Min(0)
	private Integer centerCode;

	@Pattern(regexp = ValidationHelper.NAME_REGEX)
	private String firstName = "";

	@Pattern(regexp = ValidationHelper.NAME_REGEX)
	private String lastName = "";

	@Pattern(regexp = ValidationHelper.GENDER_REGEX)
	private String gender = "";

	@ValidDate(relative = DateRelation.PAST)
	private String birthDateForDisplay = "";

	@Valid
	private ObservationData observations;

	@Valid
	private ProjectData projectData;

	public PatientEntryByProjectForm() {
		setFormName("patientEntryByProjectForm");
	}

	public Map<String, Object> getFormLists() {
		return formLists;
	}

	public void setFormLists(Map<String, Object> formLists) {
		this.formLists = formLists;
	}

	public Map<String, ObservationHistoryList> getDictionaryLists() {
		return dictionaryLists;
	}

	public void setDictionaryLists(Map<String, ObservationHistoryList> dictionaryLists) {
		this.dictionaryLists = dictionaryLists;
	}

	public Map<String, OrganizationTypeList> getOrganizationTypeLists() {
		return organizationTypeLists;
	}

	public void setOrganizationTypeLists(Map<String, OrganizationTypeList> organizationTypeLists) {
		this.organizationTypeLists = organizationTypeLists;
	}

	public String getPatientPK() {
		return patientPK;
	}

	public void setPatientPK(String patientPK) {
		this.patientPK = patientPK;
	}

	public String getSamplePK() {
		return samplePK;
	}

	public void setSamplePK(String samplePK) {
		this.samplePK = samplePK;
	}

	public String getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(String interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getReceivedDateForDisplay() {
		return receivedDateForDisplay;
	}

	public void setReceivedDateForDisplay(String receivedDateForDisplay) {
		this.receivedDateForDisplay = receivedDateForDisplay;
	}

	public String getReceivedTimeForDisplay() {
		return receivedTimeForDisplay;
	}

	public void setReceivedTimeForDisplay(String receivedTimeForDisplay) {
		this.receivedTimeForDisplay = receivedTimeForDisplay;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public PatientUpdateStatus getPatientUpdateStatus() {
		return patientUpdateStatus;
	}

	public void setPatientUpdateStatus(PatientUpdateStatus patientUpdateStatus) {
		this.patientUpdateStatus = patientUpdateStatus;
	}

	public String getSubjectNumber() {
		return subjectNumber;
	}

	public void setSubjectNumber(String subjectNumber) {
		this.subjectNumber = subjectNumber;
	}

	public String getSiteSubjectNumber() {
		return siteSubjectNumber;
	}

	public void setSiteSubjectNumber(String siteSubjectNumber) {
		this.siteSubjectNumber = siteSubjectNumber;
	}

	public String getLabNo() {
		return labNo;
	}

	public void setLabNo(String labNo) {
		this.labNo = labNo;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public Integer getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(Integer centerCode) {
		this.centerCode = centerCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDateForDisplay() {
		return birthDateForDisplay;
	}

	public void setBirthDateForDisplay(String birthDateForDisplay) {
		this.birthDateForDisplay = birthDateForDisplay;
	}

	public ObservationData getObservations() {
		return observations;
	}

	public void setObservations(ObservationData observations) {
		this.observations = observations;
	}

	public ProjectData getProjectData() {
		return projectData;
	}

	public void setProjectData(ProjectData projectData) {
		this.projectData = projectData;
	}
}
