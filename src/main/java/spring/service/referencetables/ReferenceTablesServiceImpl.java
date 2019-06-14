package spring.service.referencetables;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.service.common.BaseObjectServiceImpl;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.referencetables.dao.ReferenceTablesDAO;
import us.mn.state.health.lims.referencetables.valueholder.ReferenceTables;

@Service
public class ReferenceTablesServiceImpl extends BaseObjectServiceImpl<ReferenceTables, String>
		implements ReferenceTablesService {
	@Autowired
	protected ReferenceTablesDAO baseObjectDAO;

	ReferenceTablesServiceImpl() {
		super(ReferenceTables.class);
	}

	@Override
	protected ReferenceTablesDAO getBaseObjectDAO() {
		return baseObjectDAO;
	}

	@Override
	@Transactional(readOnly = true)
	public ReferenceTables getReferenceTableByName(String tableName) {
		return baseObjectDAO.getReferenceTableByName(tableName);
	}

	@Override
	@Transactional(readOnly = true)
	public void getData(ReferenceTables referenceTables) {
		getBaseObjectDAO().getData(referenceTables);

	}

	@Override
	@Transactional(readOnly = true)
	public List getAllReferenceTablesForHl7Encoding() {
		return getBaseObjectDAO().getAllReferenceTablesForHl7Encoding();
	}

	@Override
	@Transactional(readOnly = true)
	public List getAllReferenceTables() {
		return getBaseObjectDAO().getAllReferenceTables();
	}

	@Override
	@Transactional(readOnly = true)
	public ReferenceTables getReferenceTableByName(ReferenceTables referenceTables) {
		return getBaseObjectDAO().getReferenceTableByName(referenceTables);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getTotalReferenceTableCount() {
		return getBaseObjectDAO().getTotalReferenceTableCount();
	}

	@Override
	@Transactional(readOnly = true)
	public List getPreviousReferenceTablesRecord(String id) {
		return getBaseObjectDAO().getPreviousReferenceTablesRecord(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List getPageOfReferenceTables(int startingRecNo) {
		return getBaseObjectDAO().getPageOfReferenceTables(startingRecNo);
	}

	@Override
	@Transactional(readOnly = true)
	public List getNextReferenceTablesRecord(String id) {
		return getBaseObjectDAO().getNextReferenceTablesRecord(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getTotalReferenceTablesCount() {
		return getBaseObjectDAO().getTotalReferenceTablesCount();
	}

	@Override
	public String insert(ReferenceTables referenceTables) {
		if (duplicateReferenceTablesExists(referenceTables, true)) {
			throw new LIMSDuplicateRecordException("Duplicate record exists for " + referenceTables.getTableName());
		}
		return super.insert(referenceTables);
	}

	@Override
	public ReferenceTables save(ReferenceTables referenceTables) {
		if (duplicateReferenceTablesExists(referenceTables, false)) {
			throw new LIMSDuplicateRecordException("Duplicate record exists for " + referenceTables.getTableName());
		}
		return super.save(referenceTables);
	}

	@Override
	public ReferenceTables update(ReferenceTables referenceTables) {
		if (duplicateReferenceTablesExists(referenceTables, false)) {
			throw new LIMSDuplicateRecordException("Duplicate record exists for " + referenceTables.getTableName());
		}
		return super.update(referenceTables);
	}

	private boolean duplicateReferenceTablesExists(ReferenceTables referenceTables, boolean isNew) {
		return baseObjectDAO.duplicateReferenceTablesExists(referenceTables, isNew);
	}
}
