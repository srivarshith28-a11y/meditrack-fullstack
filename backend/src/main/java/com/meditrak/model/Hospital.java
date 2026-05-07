package com.meditrak.model;

public class Hospital {
    private Long id;
    private String name;
    private String zone;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;

    public Hospital() {
    }

    public Hospital(Long id, String name, String zone, String contactPerson, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.zone = zone;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
