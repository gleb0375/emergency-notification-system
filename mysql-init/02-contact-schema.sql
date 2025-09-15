USE contact_db;

CREATE TABLE IF NOT EXISTS user_profiles (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_profile_uuid CHAR(36) NOT NULL UNIQUE,         
  auth_account_uuid CHAR(36) NOT NULL UNIQUE,         
  surname VARCHAR(100) NULL,
  name VARCHAR(50) NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  telegram_username VARCHAR(32) NULL UNIQUE,
  phone_number VARCHAR(20) NULL UNIQUE,
  city VARCHAR(100) NULL,
  country_code CHAR(2) NULL,
  preferred_channel ENUM('email','sms','telegram') NOT NULL DEFAULT 'email',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_up_telegram (telegram_username),
  INDEX idx_up_phone (phone_number),
  INDEX idx_up_city (city),
  INDEX idx_up_country_code (country_code),
  INDEX idx_up_country_city (country_code, city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ---------- CONTACT GROUPS ----------
CREATE TABLE IF NOT EXISTS contact_groups (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  contact_group_uuid CHAR(36) NOT NULL UNIQUE, 
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  created_by_account_uuid CHAR(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_cg_created_by_uuid (created_by_account_uuid)
);

-- ---------- NOTIFICATION TEMPLATES ----------
CREATE TABLE IF NOT EXISTS notification_templates (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  notification_template_uuid CHAR(36) NOT NULL UNIQUE, 
  title VARCHAR(100) NOT NULL,
  body TEXT NOT NULL,
  channel ENUM('email', 'sms', 'telegram') NOT NULL,
  created_by_account_uuid CHAR(36) NOT NULL,     -- soft-link
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_nt_created_by_uuid (created_by_account_uuid)
);

------------ GROUP MEMBERSHIPS (group ↔ user_profile) ----------
CREATE TABLE IF NOT EXISTS group_memberships (
  group_id INT UNSIGNED NOT NULL,
  profile_id BIGINT UNSIGNED NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (group_id, profile_id),
  FOREIGN KEY (group_id)  REFERENCES contact_groups(id)  ON DELETE CASCADE,
  FOREIGN KEY (profile_id) REFERENCES user_profiles(id)  ON DELETE CASCADE,
  INDEX idx_gm_group (group_id),
  INDEX idx_gm_profile (profile_id)
);

-- ---------- TEMPLATE TARGETS (template ↔ group) ----------
CREATE TABLE IF NOT EXISTS template_targets (
  template_id INT UNSIGNED NOT NULL,
  group_id INT UNSIGNED NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (template_id, group_id),
  FOREIGN KEY (template_id) REFERENCES notification_templates(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id)    REFERENCES contact_groups(id)         ON DELETE CASCADE,
  INDEX idx_tt_template (template_id),
  INDEX idx_tt_group (group_id)
);

-- ---------- MESSAGE LOG (event consumer idempotency) ----------
CREATE TABLE IF NOT EXISTS message_log (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  message_id VARCHAR(100) NOT NULL,              
  event_type VARCHAR(50) NOT NULL,
  occurred_at TIMESTAMP NULL,                    -- when it happened to the producer (from payload)
  received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  payload_hash CHAR(64) NULL,                    -- probably no need: SHA-256 body
  status ENUM('processed','failed') NOT NULL DEFAULT 'processed',
  error_message TEXT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_message_id (message_id),
  INDEX idx_ml_received (received_at)
);
