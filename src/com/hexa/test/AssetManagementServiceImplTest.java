package com.hexa.test;

import com.hexa.dao.AssetManagementServiceImpl;
import com.hexa.entity.Asset;
import com.hexa.entity.Employee;
import com.hexa.exception.AssetNotFoundException;
import com.hexa.exception.AssetNotMaintainException;
import org.junit.*;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class AssetManagementServiceImplTest {

	private AssetManagementServiceImpl service;

    @Before
    public void setUp() {
        service = new AssetManagementServiceImpl();
    }
    
    
    @Test
    public void testAddAsset_Success() {
    	Employee emp = new Employee(101);
        Asset asset = new Asset(101,"HP EliteBook", "Laptop", "HP-EL-1122",LocalDate.of(2023, 10, 1), "Pune Office", "in use", emp);
        boolean result = service.addAsset(asset);
        assertTrue("Asset should be added successfully", result);
    }
    
    @Test
    public void testPerformMaintenance_Success() throws AssetNotFoundException {
        boolean result = service.performMaintenance(2, "2025-04-01", "replaced brake pads", 10000.00);
        assertTrue("Maintenance record should be added successfully", result);
    }
   
    @Test
    public void testReserveAsset_Success() throws AssetNotFoundException, AssetNotMaintainException {
        boolean result = service.reserveAsset(4, 105, "2025-04-10", "2025-04-12", "2025-04-18");
        assertTrue("Asset should be reserved successfully", result);
    }
    
    @Test(expected = AssetNotFoundException.class)
    public void testAssetNotFoundException() throws AssetNotFoundException {
        service.getAssetById(999);  // Assuming 999 does not exist
    }
    
}

