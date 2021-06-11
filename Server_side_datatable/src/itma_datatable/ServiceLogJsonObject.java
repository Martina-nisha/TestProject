package itma_datatable;

import java.util.List;


public class ServiceLogJsonObject {

	
	int iTotalRecords;

    int iTotalDisplayRecords;

    String sEcho;

    String sColumns;

    List<ServiceLogObj> aaData;

    public int getiTotalRecords() {
	return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
	this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
	return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
	this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
	return sEcho;
    }

    public void setsEcho(String sEcho) {
	this.sEcho = sEcho;
    }

    public String getsColumns() {
	return sColumns;
    }

    public void setsColumns(String sColumns) {
	this.sColumns = sColumns;
    }

    public List<ServiceLogObj> getAaData() {
        return aaData;
    }

    public void setAaData(List<ServiceLogObj> aaData) {
        this.aaData = aaData;
    }

}
