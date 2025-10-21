-- Clean up existing data
DELETE FROM EVENT_PARTICIPANTS;
DELETE FROM EVENT_ORGANIZERS;
DELETE FROM USERS_ROLES;
DELETE FROM POSTS;
DELETE FROM MEDIAS;
DELETE FROM EVENTS;
DELETE FROM FEATURES;
DELETE FROM USERS;
DELETE FROM ROLES;
DELETE FROM GROUPS;
DELETE FROM SECTORS;
DELETE FROM SERIES;
DELETE FROM CONFIGURATIONS;

-- Insert Roles (Using proper UUID format)
INSERT INTO ROLES (roleid, name, description, role_type, created_at, updated_at) VALUES
                                                                                     ('550e8400-e29b-41d4-a716-446655440000', 'ADMIN', 'Administrator role with full access', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                     ('550e8400-e29b-41d4-a716-446655440001', 'USER', 'Standard user role', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                     ('550e8400-e29b-41d4-a716-446655440002', 'MODERATOR', 'Content moderator role', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Sectors
INSERT INTO SECTORS (sectorid, name, description, created_at, updated_at) VALUES
                                                                              ('550e8400-e29b-41d4-a716-446655440003', 'Main Sector', 'Primary sector of activities', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                              ('550e8400-e29b-41d4-a716-446655440004', 'Secondary Sector', 'Secondary sector of activities', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Series
INSERT INTO SERIES (serieid, name, description, created_at, updated_at) VALUES
    ('550e8400-e29b-41d4-a716-446655440005', '2025 Series', 'Events series for 2025', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Users
INSERT INTO USERS (userid, created_at, updated_at) VALUES
                                                                                               ('550e8400-e29b-41d4-a716-446655440006',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                               ('550e8400-e29b-41d4-a716-446655440007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Features
INSERT INTO FEATURES ( name, description, IS_ACTIVE) VALUES
                                                                                            ( 'EVENTS', 'Events management feature', true),
                                                                                            ( 'POSTS', 'Posts management feature', true);

-- Insert Groups
INSERT INTO GROUPS (groupid, name, description, sector_id, created_at, updated_at) VALUES
    ('550e8400-e29b-41d4-a716-446655440010', 'Main Group', 'Primary user group', '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Events
INSERT INTO EVENTS (eventid, name, description, startdatetime, enddatetime, created_at, updated_at) VALUES
    ('550e8400-e29b-41d4-a716-446655440011', 'Test Event', 'Test event description', CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Link Users and Roles
INSERT INTO USERS_ROLES (user_id, role_id) VALUES
                                               ('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440000'), -- Admin user gets admin role
                                               ('550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440001'); -- Standard user gets user role

-- Insert Event Organizers
INSERT INTO EVENT_ORGANIZERS (event_id, user_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440006');

-- Insert Event Participants
INSERT INTO EVENT_PARTICIPANTS (event_id, user_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440007');


INSERT INTO CONFIGURATIONS (created_at, updated_at, updated_by, name, value) VALUES
                                                                                 ('2025-10-21 14:03:30.063', '2025-10-21 16:09:42.0', '550e8400-e29b-41d4-a716-446655440000', 'Test1', 'Value1'),
                                                                                 ('2025-10-21 14:03:30.063', '2025-10-21 14:03:30.063', '550e8400-e29b-41d4-a716-446655440000', 'Test2', 'Value2');
