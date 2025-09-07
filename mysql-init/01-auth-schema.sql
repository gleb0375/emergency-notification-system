USE auth_db;

-- ---------- ACCOUNTS ----------
CREATE TABLE IF NOT EXISTS auth_accounts (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid CHAR(36) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  is_email_verified BOOLEAN DEFAULT FALSE,
  last_login_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_auth_email (email)
);

-- ---------- ROLES ----------
CREATE TABLE IF NOT EXISTS roles (
  id BIGINT  UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid CHAR(36) NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(255),
  PRIMARY KEY (id)
);

-- ---------- ACCOUNT ↔ ROLE (many‑to‑many) ----------
CREATE TABLE IF NOT EXISTS account_roles (
  account_id BIGINT UNSIGNED NOT NULL,
  role_id    BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (account_id, role_id),
  FOREIGN KEY (account_id) REFERENCES auth_accounts(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id)    REFERENCES roles(id)         ON DELETE CASCADE,
  INDEX idx_ar_account (account_id),
  INDEX idx_ar_role   (role_id)
);

-- ---------- EMAIL VERIFICATIONS ----------
CREATE TABLE IF NOT EXISTS email_verifications (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid CHAR(36) NOT NULL UNIQUE,
  account_id BIGINT UNSIGNED NOT NULL,
  token VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (account_id) REFERENCES auth_accounts(id) ON DELETE CASCADE,
  INDEX idx_ev_expires (expires_at)
);

-- ---------- SESSIONS (refresh‑tokens) ----------
CREATE TABLE IF NOT EXISTS tokens (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid CHAR(36) NOT NULL UNIQUE,
  account_id BIGINT UNSIGNED NOT NULL,
  refresh_token_hash VARCHAR(255) NOT NULL UNIQUE,
  user_agent VARCHAR(255),
  ip_address VARCHAR(45),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (account_id) REFERENCES auth_accounts(id) ON DELETE CASCADE,
  INDEX idx_rt_hash (refresh_token_hash),
  INDEX idx_sessions_account (account_id)
);

CREATE TABLE IF NOT EXISTS outbox_events (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  event_id CHAR(36) NOT NULL,                
  aggregate_type VARCHAR(50) NOT NULL,       
  aggregate_id CHAR(36) NOT NULL,            
  event_type VARCHAR(50) NOT NULL,           
  payload JSON NOT NULL,                     
  status ENUM('NEW','PUBLISHED','FAILED') NOT NULL DEFAULT 'NEW',
  occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  published_at TIMESTAMP NULL,
  publish_attempts INT NOT NULL DEFAULT 0,
  last_error TEXT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_outbox_event_id (event_id),
  INDEX idx_outbox_pick (status, published_at, publish_attempts),
  INDEX idx_outbox_type_time (event_type, occurred_at)
);

