use meditrak;

insert into hospitals(name, zone, contact_person, email, phone, address) values
('City Heart Hospital', 'North', 'Dr. Meera Singh', 'meera@cityheart.org', '9876543210', 'North Avenue, Bengaluru'),
('Riverfront Clinic', 'South', 'Arun Nair', 'arun@riverfront.org', '9123456780', 'MG Road, Kochi'),
('North Diagnostic Lab', 'North', 'Priya Menon', 'priya@ndl.org', '9988776655', 'Lab Street, Chennai'),
('Metro Blood Bank', 'East', 'Rahul Das', 'rahul@metroblood.org', '9090909090', 'Ring Road, Hyderabad'),
('Sunrise Medical Center', 'West', 'Neha Kapoor', 'neha@sunrisemedical.org', '9012345678', 'Outer Ring Road, Pune'),
('Green Valley Hospital', 'South', 'Joseph Mathew', 'joseph@greenvalley.org', '9345678123', 'Civil Lines, Coimbatore'),
('CarePoint Specialty Clinic', 'East', 'Lavanya Iyer', 'lavanya@carepoint.org', '9456123780', 'Lake View Road, Visakhapatnam'),
('Unity Trauma Centre', 'North', 'Vikram Bhat', 'vikram@unitytrauma.org', '9567812340', 'Airport Corridor, Mysuru');

insert into waste_entries(hospital_id, waste_type, quantity_kg, bin_color, collection_status, pickup_date) values
(1, 'Infectious Waste', 45.50, 'Yellow', 'Scheduled', '2026-03-28'),
(2, 'Sharps Waste', 18.25, 'White', 'Collected', '2026-03-27'),
(3, 'Pathological Waste', 28.75, 'Yellow', 'Scheduled', '2026-03-29'),
(4, 'Glass Waste', 12.00, 'Blue', 'Collected', '2026-03-27'),
(5, 'Expired Medicines', 16.80, 'Blue', 'Scheduled', '2026-03-30'),
(6, 'Microbiology Waste', 22.40, 'Yellow', 'In Transit', '2026-03-29'),
(7, 'Plastic Disposable Waste', 14.60, 'Red', 'Scheduled', '2026-03-30'),
(8, 'Sharps Waste', 19.35, 'White', 'Scheduled', '2026-03-31'),
(1, 'Soiled Waste', 11.25, 'Yellow', 'Collected', '2026-03-26'),
(3, 'Chemical Waste', 9.75, 'Red', 'In Transit', '2026-03-30');

insert into routes(zone, vehicle_number, driver_name, estimated_waste_kg, optimized_distance_km) values
('North', 'KA-01-MT-101', 'Suresh Kumar', 74.25, 21.50),
('South', 'KL-07-MT-212', 'Anil Joseph', 18.25, 15.00),
('East', 'TS-09-MT-325', 'Ravi Teja', 12.00, 12.40),
('North', 'TN-11-MT-410', 'Karthik Raj', 28.75, 18.20),
('West', 'MH-12-MT-518', 'Pooja Salunke', 16.80, 17.60),
('South', 'TN-09-MT-287', 'Hari Prasath', 22.40, 19.10),
('East', 'AP-31-MT-644', 'Sandeep Varma', 14.60, 16.75),
('North', 'KA-09-MT-590', 'Naveen Gowda', 19.35, 13.90);
