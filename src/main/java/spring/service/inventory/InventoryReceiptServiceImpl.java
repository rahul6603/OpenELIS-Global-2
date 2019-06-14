package spring.service.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.service.common.BaseObjectServiceImpl;
import us.mn.state.health.lims.inventory.dao.InventoryReceiptDAO;
import us.mn.state.health.lims.inventory.valueholder.InventoryReceipt;

@Service
public class InventoryReceiptServiceImpl extends BaseObjectServiceImpl<InventoryReceipt, String>
		implements InventoryReceiptService {
	@Autowired
	protected InventoryReceiptDAO baseObjectDAO;

	InventoryReceiptServiceImpl() {
		super(InventoryReceipt.class);
	}

	@Override
	protected InventoryReceiptDAO getBaseObjectDAO() {
		return baseObjectDAO;
	}

	@Override
	@Transactional(readOnly = true)
	public void getData(InventoryReceipt inventoryReceipt) {
		getBaseObjectDAO().getData(inventoryReceipt);

	}

	@Override
	@Transactional(readOnly = true)
	public InventoryReceipt getInventoryReceiptById(String id) {
		return getBaseObjectDAO().getInventoryReceiptById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<InventoryReceipt> getAllInventoryReceipts() {
		return getBaseObjectDAO().getAllInventoryReceipts();
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryReceipt getInventoryReceiptByInventoryItemId(String id) {
		return getBaseObjectDAO().getInventoryReceiptByInventoryItemId(id);
	}
}
