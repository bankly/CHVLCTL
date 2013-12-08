package stepbystep.careful;

import android.app.Application;
import android.os.Environment;

public class DictApplication extends Application {

	public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getPath();
	public static final String FOLDER = "Dict";
	public static final String FILE_INDEX = "dict.index";
	public static final String FILE_MEANING = "dict.dict";
	public static final String FOLDER_EV = "E-V";
	public static final String FOLDER_VE = "V-E";
	public static final String FOLDER_VV = "V-V";
	public static final String FILE_DB = "dict.db";
	
	private int indexSelect;
	private int topDistance;
	private boolean initialResource = false;
	private String typeSearch;
	private String dictType;
	private String tableName;
	
	public static String[] arrDictType = {"E-V", "V-E", "V-V", "P-E-V", "P-V-E"};
	public static int[] arrWordNumber = {387513, 390160, 30164};
	
	public int getIndexSelect() {
		return indexSelect;
	}
	public void setIndexSelect(int indexSelect) {
		this.indexSelect = indexSelect;
	}
	public int getTopDistance() {
	    return topDistance;
    }
	public void setTopDistance(int topDistance) {
	    this.topDistance = topDistance;
    }
	public boolean isInitialResource() {
	    return initialResource;
    }
	public void setInitialResource(boolean initialResource) {
	    this.initialResource = initialResource;
    }

	public String getTypeSearch() {
	    return typeSearch;
    }
	public void setTypeSearch(String typeSearch) {
	    this.typeSearch = typeSearch;
    }
	public String getDictType() {
	    return dictType;
    }
	public void setDictType(String dictType) {
	    this.dictType = dictType;
    }
	public String getTableName() {
	    return tableName;
    }
	public void setTableName(String tableName) {
	    this.tableName = tableName;
    }

}
