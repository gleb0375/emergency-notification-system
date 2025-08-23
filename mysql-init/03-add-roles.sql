USE auth_db;

INSERT INTO roles (uuid, name, description)
VALUES
  (UUID(), 'USER', 'Basic application user'),
  (UUID(), 'ADMIN', 'Administrator with extended privileges'),
  (UUID(), 'SUPERADMIN', 'Super administrator with full privileges')
ON DUPLICATE KEY UPDATE 
  description = VALUES(description);
   
SET @admin_email := 'hlebhnatsiuk@gmail.com';
SET @admin_pass_hash := '$2a$10$Z5PvJ8Y/dBTSvxXiuO9w7Owkrftwz4DmslEgSg8iCpzPlDylx2k2O';

INSERT INTO auth_accounts (uuid, email, password, is_email_verified, last_login_at)
SELECT UUID(), @admin_email, @admin_pass_hash, TRUE, NULL
WHERE NOT EXISTS (
  SELECT 1 FROM auth_accounts WHERE email = @admin_email
);

INSERT IGNORE INTO account_roles (account_id, role_id)
SELECT a.id, r.id
FROM auth_accounts a
JOIN roles r ON r.name = 'SUPERADMIN'
WHERE a.email = @admin_email;