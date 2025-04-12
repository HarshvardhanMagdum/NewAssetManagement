package com.hexa.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.hexa.entity.Asset;
import com.hexa.entity.Employee;
import com.hexa.exception.AssetNotFoundException;
import com.hexa.exception.AssetNotMaintainException;
import com.hexa.exception.DbConnectionException;
import com.hexa.util.DBConnection;

public class AssetManagementServiceImpl implements AssetManagementService {

	// Add Asset
    @Override
    public boolean addAsset(Asset asset) {
        String query = "INSERT INTO assets (name, type, serial_number, purchase_date, location, status, owner_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, asset.getName());
            stmt.setString(2, asset.getType());
            stmt.setString(3, asset.getSerialNumber());
            stmt.setDate(4, Date.valueOf(asset.getPurchaseDate()));
            stmt.setString(5, asset.getLocation());
            stmt.setString(6, asset.getStatus());
            stmt.setInt(7, asset.getOwner().getEmployeeId());

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Asset
    @Override
    public boolean updateAsset(Asset asset) throws AssetNotFoundException {
        String query = "UPDATE assets SET name = ?, type = ?, serial_number = ?, purchase_date = ?, location = ?, status = ? " +
                "WHERE asset_id = ?";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, asset.getName());
            stmt.setString(2, asset.getType());
            stmt.setString(3, asset.getSerialNumber());
            stmt.setDate(4, Date.valueOf(asset.getPurchaseDate()));
            stmt.setString(5, asset.getLocation());
            stmt.setString(6, asset.getStatus());
            stmt.setInt(7, asset.getAssetId());

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Asset with ID " + asset.getAssetId() + " not found.");
            }
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Asset
    @Override
    public boolean deleteAsset(int assetId) throws AssetNotFoundException {
        String query = "DELETE FROM assets WHERE asset_id = ?";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assetId);

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Asset with ID " + assetId + " not found.");
            }
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Allocate Asset
    @Override
    public boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws AssetNotFoundException {
        String query = "INSERT INTO asset_allocations (asset_id, employee_id, allocation_date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assetId);
            stmt.setInt(2, employeeId);
            stmt.setDate(3, Date.valueOf(allocationDate));

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Asset with ID " + assetId + " not found.");
            }
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deallocate Asset
    @Override
    public boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws AssetNotFoundException {
        String query = "UPDATE asset_allocations SET return_date = ? WHERE asset_id = ? AND employee_id = ?";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setInt(2, assetId);
            stmt.setInt(3, employeeId);

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Asset with ID " + assetId + " not found or not allocated.");
            }
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Perform Maintenance
    @Override
    public boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) throws AssetNotFoundException {
        String query = "INSERT INTO maintenance_records (asset_id, maintenance_date, description, cost) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assetId);
            stmt.setDate(2, Date.valueOf(maintenanceDate));
            stmt.setString(3, description);
            stmt.setDouble(4, cost);

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Asset with ID " + assetId + " not found.");
            }
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Reserve Asset
    @Override
    public boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) throws AssetNotFoundException, AssetNotMaintainException {
        String query = "SELECT purchase_date FROM assets WHERE asset_id = ?";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, assetId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LocalDate purchaseDate = rs.getDate("purchase_date").toLocalDate();
                if (purchaseDate.isBefore(LocalDate.now().minusYears(2))) {
                    throw new AssetNotMaintainException("Asset with ID " + assetId + " has not been maintained for 2 years.");
                }
            } else {
                throw new AssetNotFoundException("Asset with ID " + assetId + " not found.");
            }

            // If the asset is maintained, proceed with reservation
            String insertQuery = "INSERT INTO reservations (asset_id, employee_id, reservation_date, start_date, end_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, assetId);
                insertStmt.setInt(2, employeeId);
                insertStmt.setDate(3, Date.valueOf(reservationDate));
                insertStmt.setDate(4, Date.valueOf(startDate));
                insertStmt.setDate(5, Date.valueOf(endDate));
                insertStmt.setString(6, "pending");

                int result = insertStmt.executeUpdate();
                return result > 0;
            }
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Withdraw Reservation
    @Override
    public boolean withdrawReservation(int reservationId) throws AssetNotFoundException {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Reservation with ID " + reservationId + " not found.");
            }
            return result > 0;
        } catch (SQLException | DbConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Asset getAssetById(int assetId) throws AssetNotFoundException{
        Asset asset = null;
        String query = "SELECT * FROM assets WHERE asset_id = ?";
        
        try (Connection conn = DBConnection.getDbConnection();
        		PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the assetId parameter
            statement.setInt(1, assetId);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if an asset with the given ID exists
            if (resultSet.next()) {
                // Retrieve asset details from the result set
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                String serialNumber = resultSet.getString("serial_number");
                LocalDate purchaseDate = resultSet.getDate("purchase_date").toLocalDate();
                String location = resultSet.getString("location");
                String status = resultSet.getString("status");
                int ownerId = resultSet.getInt("owner_id");
                
                Employee owner = getEmployeeById(ownerId); // fetch full employee from DB
                asset = new Asset(assetId, name, type, serialNumber, purchaseDate, location, status, owner);

            }else {
                throw new AssetNotFoundException("Asset with ID " + assetId + " not found.");
            }
        } catch (SQLException | DbConnectionException e) {
            System.out.println("Error fetching asset: " + e.getMessage());
        }

        return asset;
    }
    
    @Override
    public Employee getEmployeeById(int ownerId) {
        // SQL query to fetch the employee by ID
        String query = "SELECT * FROM employees WHERE employee_id = ?";

        try (Connection conn = DBConnection.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter in the SQL query (ownerId)
            stmt.setInt(1, ownerId);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Check if the employee exists
            if (rs.next()) {
                // Retrieve employee details from the result set
                int employeeId = rs.getInt("employee_id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // Return the Employee object
                return new Employee(employeeId, name, department, email, password);
            } else {
                // If no employee found, return null
                System.out.println("Employee with ID " + ownerId + " not found.");
                return null;
            }

        } catch (SQLException | DbConnectionException e) {
            // Handle SQL exceptions
            System.out.println("Error fetching employee: " + e.getMessage());
            return null;
        }
    }

}
