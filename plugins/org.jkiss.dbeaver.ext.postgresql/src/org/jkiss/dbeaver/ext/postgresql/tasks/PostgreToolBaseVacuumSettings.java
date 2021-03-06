package org.jkiss.dbeaver.ext.postgresql.tasks;

import org.jkiss.dbeaver.ext.postgresql.model.PostgreDataSource;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.data.json.JSONUtils;
import org.jkiss.dbeaver.model.meta.IPropertyValueValidator;
import org.jkiss.dbeaver.model.meta.Property;
import org.jkiss.dbeaver.model.runtime.DBRRunnableContext;
import org.jkiss.dbeaver.model.sql.task.SQLToolExecuteSettings;
import org.jkiss.dbeaver.model.struct.DBSObject;

import java.util.Map;

public class PostgreToolBaseVacuumSettings extends SQLToolExecuteSettings<DBSObject> {
    private boolean isFull;
    private boolean isFreeze;
    private boolean isAnalyzed;
    private boolean isDisableSkipping;
    private boolean isSkipLocked;
    private boolean isIndexCleaning;
    private boolean isTruncated;

    @Property(viewable = true, editable = true, updatable = true)
    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    @Property(viewable = true, editable = true, updatable = true)
    public boolean isFreeze() {
        return isFreeze;
    }

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }

    @Property(viewable = true, editable = true, updatable = true)
    public boolean isAnalyzed() {
        return isAnalyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        isAnalyzed = analyzed;
    }

    //Works since PostgreSQL 9.6
    @Property(viewable = true, editable = true, updatable = true)
    public boolean isDisableSkipping() {
        return isDisableSkipping;
    }

    public void setDisableSkipping(boolean disableSkipping) {
        isDisableSkipping = disableSkipping;
    }

    //Works since PostgreSQL 12
    @Property(viewable = true, editable = true, updatable = true)
    public boolean isSkipLocked() {
        return isSkipLocked;
    }

    public void setSkipLocked(boolean skipLocked) {
        isSkipLocked = skipLocked;
    }

    //Works since PostgreSQL 12
    @Property(viewable = true, editable = true, updatable = true)
    public boolean isIndexCleaning() {
        return isIndexCleaning;
    }

    public void setIndexCleaning(boolean indexCleaning) {
        isIndexCleaning = indexCleaning;
    }

    //Works since PostgreSQL 12
    @Property(viewable = true, editable = true, updatable = true, visibleIf = PostgreVersionValidator12.class)
    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    @Override
    public void loadConfiguration(DBRRunnableContext runnableContext, Map<String, Object> config) {
        super.loadConfiguration(runnableContext, config);
        isFull = JSONUtils.getBoolean(config, "full");
        isFreeze = JSONUtils.getBoolean(config, "freeze");
        isAnalyzed = JSONUtils.getBoolean(config, "analyze");
        isDisableSkipping = JSONUtils.getBoolean(config, "disable_page_skipping");
        isSkipLocked = JSONUtils.getBoolean(config, "skip_locked");
        isIndexCleaning = JSONUtils.getBoolean(config, "index_cleanup");
        isTruncated = JSONUtils.getBoolean(config, "truncate");
    }

    @Override
    public void saveConfiguration(Map<String, Object> config) {
        super.saveConfiguration(config);
        config.put("full", isFull);
        config.put("freeze", isFreeze);
        config.put("analyze", isAnalyzed);
        config.put("disable_page_skipping", isDisableSkipping);
        config.put("skip_locked", isSkipLocked);
        config.put("index_cleanup", isIndexCleaning);
        config.put("truncate", isTruncated);
    }

    public static class PostgreVersionValidator12 implements IPropertyValueValidator<PostgreToolBaseVacuumSettings, Object> {

        @Override
        public boolean isValidValue(PostgreToolBaseVacuumSettings object, Object value) throws IllegalArgumentException {
            if (!object.getObjectList().isEmpty()) {
                DBPDataSource dataSource = object.getObjectList().get(0).getDataSource();
                if (dataSource instanceof PostgreDataSource) {
                    return ((PostgreDataSource) dataSource).isServerVersionAtLeast(12, 0);
                }
            }
            return false;
        }
    }
}
