package http.data;

import java.util.ArrayList;

public class TableList {

	private boolean hasMore;
	private ArrayList<Object> arrayList = new ArrayList<Object>();

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public ArrayList<Object> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<Object> arrayList) {
		this.arrayList = arrayList;
	}

}
