package spring.mine.dataexchange.order.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import spring.mine.common.form.BaseForm;
import us.mn.state.health.lims.dataexchange.order.valueholder.ElectronicOrder;
import us.mn.state.health.lims.dataexchange.order.valueholder.ElectronicOrder.SortOrder;

public class ElectronicOrderViewForm extends BaseForm {
	@NotNull
	private ElectronicOrder.SortOrder sortOrder = ElectronicOrder.SortOrder.LAST_UPDATED;

	@Min(1)
	private int page = 1;

	// for display
	private List<ElectronicOrder> eOrders;

	// for display
	private SortOrder[] sortOrderOptions = ElectronicOrder.SortOrder.values();

	public ElectronicOrderViewForm() {
		setFormName("ElectronicOrderViewForm");
	}

	public ElectronicOrder.SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(ElectronicOrder.SortOrder sortBy) {
		sortOrder = sortBy;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<ElectronicOrder> getEOrders() {
		return eOrders;
	}

	public void setEOrders(List<ElectronicOrder> eOrders) {
		this.eOrders = eOrders;
	}

	public SortOrder[] getSortOrderOptions() {
		return sortOrderOptions;
	}

	public void setSortOrderOptions(SortOrder[] sortOrderOptions) {
		this.sortOrderOptions = sortOrderOptions;
	}
}
