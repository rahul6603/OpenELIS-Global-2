package spring.generated.sample.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spring.generated.sample.form.SampleEntryByProjectForm;
import spring.mine.sample.controller.BaseSampleEntryController;
import us.mn.state.health.lims.common.services.DisplayListService;
import us.mn.state.health.lims.common.services.DisplayListService.ListType;
import us.mn.state.health.lims.common.services.StatusService;
import us.mn.state.health.lims.common.services.StatusService.SampleStatus;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.dictionary.ObservationHistoryList;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.organization.util.OrganizationTypeList;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.patient.saving.Accessioner;
import us.mn.state.health.lims.patient.saving.SampleEntry;
import us.mn.state.health.lims.patient.saving.SampleEntryAfterPatientEntry;
import us.mn.state.health.lims.patient.saving.SampleSecondEntry;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

@Controller
public class SampleEntryByProjectController extends BaseSampleEntryController {

	@RequestMapping(value = "/SampleEntryByProject", method = RequestMethod.GET)
	public ModelAndView showSampleEntryByProject(HttpServletRequest request) {
		SampleEntryByProjectForm form = new SampleEntryByProjectForm();

		Date today = Calendar.getInstance().getTime();
		String dateAsText = DateUtil.formatDateAsText(today);
		form.setReceivedDateForDisplay(dateAsText);
		form.setInterviewDate(dateAsText);

		setDisplayLists(form);
		addFlashMsgsToRequest(request);

		return findForward(FWD_SUCCESS, form);
	}

	@RequestMapping(value = "/SampleEntryByProject", method = RequestMethod.POST)
	public ModelAndView postSampleEntryByProject(HttpServletRequest request,
			@ModelAttribute("form") @Valid SampleEntryByProjectForm form, BindingResult result,
			RedirectAttributes redirectAttributes) throws Exception {
		if (result.hasErrors()) {
			saveErrors(result);
			setDisplayLists(form);
			return findForward(FWD_FAIL_INSERT, form);
		}

		String forward;

		Accessioner accessioner;
		accessioner = new SampleSecondEntry(form, getSysUserId(request), request);
		if (accessioner.canAccession()) {
			forward = handleSave(request, accessioner, form);
			if (forward != null) {
				if (FWD_SUCCESS_INSERT.equals(forward)) {
					redirectAttributes.addFlashAttribute(FWD_SUCCESS, true);
				} else {
					setDisplayLists(form);
				}
				return findForward(forward, form);
			}
		}
		accessioner = new SampleEntry(form, getSysUserId(request), request);
		if (accessioner.canAccession()) {
			forward = handleSave(request, accessioner, form);
			if (forward != null) {
				if (FWD_SUCCESS_INSERT.equals(forward)) {
					redirectAttributes.addFlashAttribute(FWD_SUCCESS, true);
				} else {
					setDisplayLists(form);
				}
				return findForward(forward, form);
			}
		}
		accessioner = new SampleEntryAfterPatientEntry(form, getSysUserId(request), request);
		if (accessioner.canAccession()) {
			forward = handleSave(request, accessioner, form);
			if (forward != null) {
				if (FWD_SUCCESS_INSERT.equals(forward)) {
					redirectAttributes.addFlashAttribute(FWD_SUCCESS, true);
				} else {
					setDisplayLists(form);
				}
				return findForward(forward, form);
			}
		}
		logAndAddMessage(request, "postSampleEntryByProject", "errors.UpdateException");

		setDisplayLists(form);
		return findForward(FWD_FAIL_INSERT, form);
	}

	public SampleItem getSampleItem(Sample sample, TypeOfSample typeofsample) {
		SampleItem item = new SampleItem();
		item.setSample(sample);
		item.setTypeOfSample(typeofsample);
		item.setSortOrder(Integer.toString(1));
		item.setStatusId(StatusService.getInstance().getStatusID(SampleStatus.Entered));

		return item;
	}

	private void setDisplayLists(SampleEntryByProjectForm form) {
		Map<String, List<Dictionary>> formListsMapOfLists = new HashMap<>();
		List<Dictionary> listOfDictionary = new ArrayList<>();
		List<IdValuePair> genders = DisplayListService.getList(ListType.GENDERS);

		for (IdValuePair i : genders) {
			Dictionary dictionary = new Dictionary();
			dictionary.setId(i.getId());
			dictionary.setDictEntry(i.getValue());
			listOfDictionary.add(dictionary);
		}

		formListsMapOfLists.put("GENDERS", listOfDictionary);
		form.setFormLists(formListsMapOfLists);

		// Get Lists
		Map<String, List<Dictionary>> observationHistoryMapOfLists = new HashMap<>();
		observationHistoryMapOfLists.put("EID_WHICH_PCR", ObservationHistoryList.EID_WHICH_PCR.getList());
		observationHistoryMapOfLists.put("EID_SECOND_PCR_REASON",
				ObservationHistoryList.EID_SECOND_PCR_REASON.getList());
		observationHistoryMapOfLists.put("EID_TYPE_OF_CLINIC", ObservationHistoryList.EID_TYPE_OF_CLINIC.getList());
		observationHistoryMapOfLists.put("EID_HOW_CHILD_FED", ObservationHistoryList.EID_HOW_CHILD_FED.getList());
		observationHistoryMapOfLists.put("EID_STOPPED_BREASTFEEDING",
				ObservationHistoryList.EID_STOPPED_BREASTFEEDING.getList());
		observationHistoryMapOfLists.put("YES_NO", ObservationHistoryList.YES_NO.getList());
		observationHistoryMapOfLists.put("EID_INFANT_PROPHYLAXIS_ARV",
				ObservationHistoryList.EID_INFANT_PROPHYLAXIS_ARV.getList());
		observationHistoryMapOfLists.put("YES_NO_UNKNOWN", ObservationHistoryList.YES_NO_UNKNOWN.getList());
		observationHistoryMapOfLists.put("EID_MOTHERS_HIV_STATUS",
				ObservationHistoryList.EID_MOTHERS_HIV_STATUS.getList());
		observationHistoryMapOfLists.put("EID_MOTHERS_ARV_TREATMENT",
				ObservationHistoryList.EID_MOTHERS_ARV_TREATMENT.getList());
		observationHistoryMapOfLists.put("HIV_STATUSES", ObservationHistoryList.HIV_STATUSES.getList());
		observationHistoryMapOfLists.put("SPECIAL_REQUEST_REASONS",
				ObservationHistoryList.SPECIAL_REQUEST_REASONS.getList());
		observationHistoryMapOfLists.put("ARV_REGIME", ObservationHistoryList.ARV_REGIME.getList());
		observationHistoryMapOfLists.put("ARV_REASON_FOR_VL_DEMAND",
				ObservationHistoryList.ARV_REASON_FOR_VL_DEMAND.getList());

		form.setDictionaryLists(observationHistoryMapOfLists);

		// Get EID Sites
		Map<String, List<Organization>> organizationTypeMapOfLists = new HashMap<>();
		organizationTypeMapOfLists.put("ARV_ORGS", OrganizationTypeList.ARV_ORGS.getList());
		organizationTypeMapOfLists.put("ARV_ORGS_BY_NAME", OrganizationTypeList.ARV_ORGS_BY_NAME.getList());
		organizationTypeMapOfLists.put("EID_ORGS_BY_NAME", OrganizationTypeList.EID_ORGS_BY_NAME.getList());
		organizationTypeMapOfLists.put("EID_ORGS", OrganizationTypeList.EID_ORGS.getList());
		form.setOrganizationTypeLists(organizationTypeMapOfLists);
	}

	@Override
	protected String findLocalForward(String forward) {
		if (FWD_SUCCESS.equals(forward)) {
			return "sampleEntryByProjectDefinition";
		} else if (FWD_FAIL.equals(forward)) {
			return "homePageDefinition";
		} else if (FWD_SUCCESS_INSERT.equals(forward)) {
			return "redirect:/SampleEntryByProject.do?type=" + Encode.forUriComponent(request.getParameter("type"));
		} else if (FWD_FAIL_INSERT.equals(forward)) {
			return "sampleEntryByProjectDefinition";
		} else {
			return "PageNotFound";
		}
	}

	@Override
	protected String getPageTitleKey() {
		return null;
	}

	@Override
	protected String getPageSubtitleKey() {
		return null;
	}
}
