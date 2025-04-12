package com.hexa.main;

import com.hexa.dao.AssetManagementService;
import com.hexa.dao.AssetManagementServiceImpl;
import com.hexa.entity.Asset;
import com.hexa.entity.Employee;
import com.hexa.exception.AssetNotFoundException;
import com.hexa.exception.AssetNotMaintainException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AssetManagementApp {
	private static Scanner scanner = new Scanner(System.in);
	private static AssetManagementService assetService =new AssetManagementServiceImpl();

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    handleAddAsset();
                    break;
                case 2:
                    handleUpdateAsset();
                    break;
                case 3:
                    handleDeleteAsset();
                    break;
                case 4:
                    handleAllocateAsset();
                    break;
                case 5:
                    handleDeallocateAsset();
                    break;
                case 6:
                    handlePerformMaintenance();
                    break;
                case 7:
                    handleReserveAsset();
                    break;
                case 8:
                    System.out.println("Exiting the Asset Management System...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("===== Asset Management System =====");
        System.out.println("1. Add Asset");
        System.out.println("2. Update Asset");
        System.out.println("3. Delete Asset");
        System.out.println("4. Allocate Asset");
        System.out.println("5. Deallocate Asset");
        System.out.println("6. Perform Maintenance");
        System.out.println("7. Reserve Asset");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleAddAsset() {
    	try {
            System.out.print("Enter Asset Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Asset Type (e.g., Laptop, Vehicle, Equipment): ");
            String type = scanner.nextLine();

            System.out.print("Enter Serial Number: ");
            String serialNumber = scanner.nextLine();

            System.out.print("Enter Purchase Date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            LocalDate purchaseDate = LocalDate.parse(dateStr);

            System.out.print("Enter Location: ");
            String location = scanner.nextLine();

            System.out.print("Enter Status (e.g., in use, decommissioned, under maintenance): ");
            String status = scanner.nextLine();

            System.out.print("Enter Owner Employee ID: ");
            int ownerId = Integer.parseInt(scanner.nextLine());

            Employee owner = assetService.getEmployeeById(ownerId);

            // Create asset using default constructor and setters
            Asset asset = new Asset();
            asset.setName(name);
            asset.setType(type);
            asset.setSerialNumber(serialNumber);
            asset.setPurchaseDate(purchaseDate);
            asset.setLocation(location);
            asset.setStatus(status);
            asset.setOwner(owner);

            boolean added = assetService.addAsset(asset);

            if (added) {
                System.out.println("✅ Asset added successfully.");
            } else {
                System.out.println("❌ Failed to add asset.");
            }

        } catch (Exception e) {
            System.out.println("❗ Error: " + e.getMessage());
        }
    }

    private static void handleUpdateAsset() {
    	System.out.print("Enter Asset ID to update: ");
        int updateAssetId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Check if the asset exists in the system
        Asset existingAsset;
		try {
			existingAsset = assetService.getAssetById(updateAssetId);
		
        if (existingAsset == null) {
            System.out.println("Error: Asset with ID " + updateAssetId + " not found.");
            return;
        }

        System.out.print("Enter New Asset Name (Leave empty to keep unchanged): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            existingAsset.setName(newName);
        }

        System.out.print("Enter New Asset Type (Leave empty to keep unchanged): ");
        String newType = scanner.nextLine();
        if (!newType.isEmpty()) {
            existingAsset.setType(newType);
        }

        System.out.print("Enter New Asset Serial Number (Leave empty to keep unchanged): ");
        String newSerialNumber = scanner.nextLine();
        if (!newSerialNumber.isEmpty()) {
            existingAsset.setSerialNumber(newSerialNumber);
        }
        System.out.print("Enter New Purchase Date (YYYY-MM-DD) (Leave empty to keep unchanged): ");
        String newPurchaseDate = scanner.nextLine();
        if (!newPurchaseDate.isEmpty()) {
        	try {
                // Define the expected date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                // Convert String to LocalDate
                LocalDate localDate = LocalDate.parse(newPurchaseDate, formatter);
                existingAsset.setPurchaseDate(localDate);  // Set the LocalDate
            } catch (Exception e) {
                System.out.println("Error: Invalid date format.");
                return;
            }
        }
        System.out.print("Enter New Location (Leave empty to keep unchanged): ");
        String newLocation = scanner.nextLine();
        if (!newLocation.isEmpty()) {
            existingAsset.setLocation(newLocation);
        }

        System.out.print("Enter New Asset Status (Leave empty to keep unchanged): ");
        String newStatus = scanner.nextLine();
        if (!newStatus.isEmpty()) {
            existingAsset.setStatus(newStatus);
        }

        // Update the asset
        try {
            boolean assetUpdated = assetService.updateAsset(existingAsset);
            if (assetUpdated) {
                System.out.println("Asset updated successfully.");
            } else {
                System.out.println("Failed to update asset.");
            }
        } catch (AssetNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
		} catch (AssetNotFoundException e) {
			System.out.println("Asset not found");
		}
    }

    private static void handleDeleteAsset() {
        System.out.print("Enter Asset ID to delete: ");
        int deleteAssetId = scanner.nextInt();
        try {
            boolean assetDeleted = assetService.deleteAsset(deleteAssetId);
            if (assetDeleted) {
                System.out.println("Asset deleted successfully.");
            } else {
                System.out.println("Failed to delete asset.");
            }
        } catch (AssetNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleAllocateAsset() {
        System.out.print("Enter Asset ID to allocate: ");
        int allocateAssetId = scanner.nextInt();
        System.out.print("Enter Employee ID for allocation: ");
        int allocateEmployeeId = scanner.nextInt();
        System.out.print("Enter Allocation Date (YYYY-MM-DD): ");
        String allocationDate = scanner.next();

        try {
            boolean assetAllocated = assetService.allocateAsset(allocateAssetId, allocateEmployeeId, allocationDate);
            if (assetAllocated) {
                System.out.println("Asset allocated successfully.");
            } else {
                System.out.println("Failed to allocate asset.");
            }
        } catch (AssetNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleDeallocateAsset() {
        System.out.print("Enter Asset ID to deallocate: ");
        int deallocateAssetId = scanner.nextInt();
        System.out.print("Enter Employee ID for deallocation: ");
        int deallocateEmployeeId = scanner.nextInt();
        System.out.print("Enter Return Date (YYYY-MM-DD): ");
        String returnDate = scanner.next();

        try {
            boolean assetDeallocated = assetService.deallocateAsset(deallocateAssetId, deallocateEmployeeId, returnDate);
            if (assetDeallocated) {
                System.out.println("Asset deallocated successfully.");
            } else {
                System.out.println("Failed to deallocate asset.");
            }
        } catch (AssetNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handlePerformMaintenance() {
        System.out.print("Enter Asset ID for maintenance: ");
        int maintenanceAssetId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter Maintenance Date (YYYY-MM-DD): ");
        String maintenanceDate = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Cost: ");
        double cost = scanner.nextDouble();

        try {
            boolean maintenancePerformed = assetService.performMaintenance(maintenanceAssetId, maintenanceDate, description, cost);
            if (maintenancePerformed) {
                System.out.println("Maintenance performed successfully.");
            } else {
                System.out.println("Failed to perform maintenance.");
            }
        } catch (AssetNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleReserveAsset() {
        System.out.print("Enter Asset ID to reserve: ");
        int reserveAssetId = scanner.nextInt();
        System.out.print("Enter Employee ID for reservation: ");
        int reserveEmployeeId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Reservation Date (YYYY-MM-DD): ");
        String reservationDate = scanner.nextLine();
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        try {
            boolean assetReserved = assetService.reserveAsset(reserveAssetId, reserveEmployeeId, reservationDate, startDate, endDate);
            if (assetReserved) {
                System.out.println("Asset reserved successfully.");
            } else {
                System.out.println("Failed to reserve asset.");
            }
        } catch (AssetNotFoundException | AssetNotMaintainException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
