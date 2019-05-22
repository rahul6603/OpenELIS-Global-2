package spring.service.localization;

import spring.service.common.BaseObjectService;
import us.mn.state.health.lims.localization.valueholder.Localization;

public interface LocalizationService extends BaseObjectService<Localization> {

	boolean languageChanged(Localization localization, String english, String french);
}