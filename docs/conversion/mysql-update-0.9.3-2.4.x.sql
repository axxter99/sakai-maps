-- MAPS-3, Ability to set view type for maps
ALTER TABLE edia_maps_map_config ADD COLUMN mc_type VARCHAR(255) AFTER mc_center_zoom;
INSERT INTO edia_maps_map_config(mc_type) VALUES ('G_NORMAL_MAP');

-- MAPS-2, Ability to specify API keys in sakai.properties
CREATE TABLE  edia_maps_key (
  gmk_id bigint(20) NOT NULL auto_increment,
  gmk_url varchar(255) default NULL,
  gmk_key varchar(255) default NULL,
  gmk_context varchar(255) NOT NULL,
  gmk_index int(11) default NULL,
  PRIMARY KEY  (gmk_id),
  KEY FKCDD939FF37EDD2CD (gmk_context),
  CONSTRAINT FKCDD939FF37EDD2CD FOREIGN KEY (gmk_context) REFERENCES edia_maps_map_tool_config (conf_context)
) DEFAULT CHARSET=utf8;

-- MAPS-2, Ability to specify API keys in sakai.properties
-- This copies currently configured keys to the new table. If you intend to put google api keys in the
-- sakai.properties file, you should NOT run this, as explicitely configured keys override the ones
-- in sakai.properties
INSERT INTO edia_maps_key (gmk_context, gmk_key) SELECT conf_context, conf_google_map_key from edia_maps_map_tool_config;

-- Set index to 0, there is only on key present.
UPDATE edia_maps_key set gmk_index = 0;

-- MAPS-2, Ability to specify API keys in sakai.properties
ALTER TABLE edia_maps_map_tool_config DROP COLUMN conf_google_map_key;

-- MAPS-4, Ability to set default map view
ALTER TABLE edia_maps_map_tool_config ADD COLUMN conf_defaultmap bigint(20) default NULL AFTER conf_context;