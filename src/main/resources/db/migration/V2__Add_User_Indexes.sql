CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_waste_collections_status ON waste_collections(status);
CREATE INDEX idx_waste_collections_scheduled_time ON waste_collections(scheduled_time);