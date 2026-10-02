-- Baseline: the schema Hibernate generated for PostgreSQL from the entities (ADR 0005).
-- Change the schema with a new V<n>__<what>.sql, never by editing this file.

create table configurations (created_at timestamp(6) not null, updated_at timestamp(6) not null, updated_by uuid, config_value varchar(255) not null, name varchar(255) not null, primary key (name));
create table event_group_maximums (maximum integer not null, event_id uuid not null, group_id uuid not null, primary key (event_id, group_id));
create table event_registrations (waiting boolean not null, created_at timestamp(6) not null, signed_up_at timestamp(6) not null, updated_at timestamp(6) not null, demande_group_id uuid, event_id uuid not null, group_id uuid, person_id uuid not null, pilote_registration_id uuid, registration_id uuid not null, updated_by uuid, demande_status varchar(255) check ((demande_status in ('EN_ATTENTE','ACCEPTEE','REFUSEE'))), mode varchar(255) not null check ((mode in ('PILOTE','PASSAGER'))), primary key (registration_id), unique (event_id, person_id));
create table events (max_participants integer, created_at timestamp(6) not null, endDateTime timestamp(6), startDateTime timestamp(6) not null, updated_at timestamp(6) not null, event_id uuid not null, sector_id uuid, updated_by uuid, address varchar(255), address_complement varchar(255), city varchar(255), country varchar(255), description varchar(255), image_url varchar(255), latitude varchar(255), longitude varchar(255), name varchar(255) not null, postal_code varchar(255), status varchar(255) not null check ((status in ('PLANIFICATION','OUVERT','COMPLET','EN_COURS','ARCHIVE','ANNULE'))), primary key (event_id));
create table features (is_active boolean not null, description varchar(255) not null, name varchar(255) not null, primary key (name));
create table groups (created_at timestamp(6) not null, updated_at timestamp(6) not null, chef_id uuid unique, group_id uuid not null, sector_id uuid, updated_by uuid, area varchar(255), description varchar(255), name varchar(255) not null, primary key (group_id));
create table medias (created_at timestamp(6) not null, file_size bigint not null, updated_at timestamp(6) not null, media_id uuid not null, updated_by uuid, content_type varchar(31) not null check ((content_type in ('VIDEO','PHOTO'))), file_key varchar(255) not null, original_filename varchar(255) not null, primary key (media_id));
create table post_attachments (media_id uuid not null, post_id uuid not null);
create table posts (created_at timestamp(6) not null, updated_at timestamp(6) not null, author_id uuid, media_id uuid unique, post_id uuid not null, updated_by uuid, content varchar(255) not null, status varchar(255) default 'PUBLIE' not null check ((status in ('BROUILLON','PUBLIE'))), title varchar(255) not null, primary key (post_id));
create table sectors (closed boolean default false not null, created_at timestamp(6) not null, updated_at timestamp(6) not null, sector_id uuid not null, updated_by uuid, description varchar(255), name varchar(255) not null, primary key (sector_id));
create table users (president boolean default false not null, created_at timestamp(6) not null, updated_at timestamp(6) not null, sector_id uuid, updated_by uuid, user_id uuid not null, first_name varchar(255) not null, last_name varchar(255) not null, phone varchar(255), role varchar(255) default 'BENEVOLE' not null check ((role in ('VISITEUR','BENEVOLE','MEMBRE','CHEF_DE_GROUPE','BUREAU','ADMIN','SUPER_ADMIN'))), primary key (user_id));
alter table if exists event_group_maximums add constraint FKcr73c5882bxqijg4kf58l0lpw foreign key (event_id) references events;
alter table if exists event_registrations add constraint FKpvtoa435iplty2lcwt4culbug foreign key (demande_group_id) references groups;
alter table if exists event_registrations add constraint FK6eykq6wu4n23qhn5vwb8kyut5 foreign key (event_id) references events;
alter table if exists event_registrations add constraint FKrmhxyo3p2yeeoels1hmehg9w7 foreign key (group_id) references groups;
alter table if exists event_registrations add constraint FK54hmrysuc23lb00htvckdm9q2 foreign key (person_id) references users;
alter table if exists event_registrations add constraint FKr4ouurjl9u16ajoku6m5t6umh foreign key (pilote_registration_id) references event_registrations;
alter table if exists events add constraint FKkg6e1lfsxt9bddivlk49ujmn7 foreign key (sector_id) references sectors;
alter table if exists groups add constraint FKp9jmji7c58caigt48tuby2jjc foreign key (chef_id) references users;
alter table if exists groups add constraint FK9ke893rcb22ryvak756qfph1c foreign key (sector_id) references sectors;
alter table if exists post_attachments add constraint FKqve9uyvq7qq6ocmof048skslf foreign key (media_id) references medias;
alter table if exists post_attachments add constraint FKdwocy2l1nlf11ebpfrax6sto1 foreign key (post_id) references posts;
alter table if exists posts add constraint FK6xvn0811tkyo3nfjk2xvqx6ns foreign key (author_id) references users;
alter table if exists posts add constraint FKln9rfnpm7f302sske1uh52ybc foreign key (media_id) references medias;
alter table if exists users add constraint FKsm6sv7j8yj27ns2josjloy4pl foreign key (sector_id) references sectors;
