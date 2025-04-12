package com.hexa.dao;

import com.hexa.entity.Asset;
import com.hexa.entity.Employee;
import com.hexa.exception.AssetNotFoundException;
import com.hexa.exception.AssetNotMaintainException;

public interface AssetManagementService {

    boolean addAsset(Asset asset);

    boolean updateAsset(Asset asset) throws AssetNotFoundException;

    boolean deleteAsset(int assetId) throws AssetNotFoundException;

    boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws AssetNotFoundException;

    boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws AssetNotFoundException;

    boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) throws AssetNotFoundException;

    boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) throws AssetNotFoundException, AssetNotMaintainException;

    boolean withdrawReservation(int reservationId) throws AssetNotFoundException;

	Asset getAssetById(int assetId) throws AssetNotFoundException;

	Employee getEmployeeById(int ownerId);
	
	static AssetManagementService getDaoInstance() {
		AssetManagementService dao=new AssetManagementServiceImpl();
		return dao;
	}
}
