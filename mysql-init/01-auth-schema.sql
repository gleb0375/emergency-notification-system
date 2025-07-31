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

-- ---------- USER PROFILES ----------
CREATE TABLE IF NOT EXISTS user_profiles (
  id BIGINT  UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid CHAR(36) NOT NULL UNIQUE,
  account_id BIGINT UNSIGNED NOT NULL UNIQUE,
  telegram_username VARCHAR(32),
  phone_number VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (account_id) REFERENCES auth_accounts(id) ON DELETE CASCADE,
  INDEX idx_tel_username (telegram_username)
);

-- ---------- SESSIONS (refresh‑tokens) ----------
CREATE TABLE IF NOT EXISTS sessions (
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
