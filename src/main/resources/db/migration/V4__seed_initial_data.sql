-- Insert roles
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN');

-- Insert users (plain text passwords for now, will hash later)
INSERT INTO users (username, password) VALUES
  ('admin', 'admin123'),
  ('user1', 'user123');

-- Link users to roles
-- admin has ROLE_USER and ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES
  (1, 1),
  (1, 2);

-- user1 has ROLE_USER only
INSERT INTO user_roles (user_id, role_id) VALUES
  (2, 1);