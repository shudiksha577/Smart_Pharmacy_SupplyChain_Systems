package com.pharmacy.supplychain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entity representing an authorized pharmaceutical manufacturer or distributor.
 */
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Supplier name is required")
    @Size(min = 2, max = 100, message = "Supplier name must be between 2 and 100 characters")
    @Column(name = "supplier_name", nullable = false, length = 100)
    private String supplierName;

    @NotBlank(message = "Contact person is required")
    @Size(min = 2, max = 100, message = "Contact person must be between 2 and 100 characters")
    @Column(name = "contact_person", nullable = false, length = 100)
    private String contactPerson;

    @NotBlank(message = "Phone number is required")
    @Size(min = 7, max = 20, message = "Phone must be between 7 and 20 digits")
    @Column(nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Supplier() {
    }

    public Supplier(String supplierName, String contactPerson, String phone, String email, String address) {
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
