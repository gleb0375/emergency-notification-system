USE contact_db;

CREATE TABLE IF NOT EXISTS contact_groups (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  created_by_credentials_id INT UNSIGNED NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notification_templates (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  body TEXT NOT NULL,
  channel ENUM('email', 'sms', 'telegram') NOT NULL,
  created_by_credentials_id INT UNSIGNED NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS group_memberships (
  group_id INT UNSIGNED NOT NULL,
  credentials_id INT UNSIGNED NOT NULL,
  PRIMARY KEY (group_id, credentials_id),
  FOREIGN KEY (group_id) REFERENCES contact_groups(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS template_targets (
  template_id INT UNSIGNED NOT NULL,
  group_id INT UNSIGNED NOT NULL,
  PRIMARY KEY (template_id, group_id),
  FOREIGN KEY (template_id) REFERENCES notification_templates(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES contact_groups(id) ON DELETE CASCADE
);
